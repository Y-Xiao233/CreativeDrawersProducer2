package net.yxiao233.cdp2.common.integration.ae2ltpp;

import appeng.api.config.Actionable;
import appeng.api.crafting.IPatternDetails;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import com.moakiee.ae2lt.packaged.logic.multiblock.DispatchPlan;
import com.moakiee.ae2lt.packaged.logic.multiblock.InsertionStrategy;
import com.moakiee.ae2lt.packaged.logic.multiblock.MultiblockAdapter;
import com.moakiee.ae2lt.packaged.logic.multiblock.TargetSlot;
import com.moakiee.ae2lt.packaged.logic.multiblock.binding.BindingMode;
import com.moakiee.ae2lt.packaged.logic.multiblock.binding.BindingResult;
import com.moakiee.ae2lt.packaged.patternprovider.AllowedOutputFilter;
import com.stal111.forbidden_arcanus.common.block.HephaestusForgeBlock;
import com.stal111.forbidden_arcanus.common.block.entity.PedestalBlockEntity;
import com.stal111.forbidden_arcanus.common.block.entity.forge.HephaestusForgeBlockEntity;
import com.stal111.forbidden_arcanus.common.block.entity.forge.HephaestusForgeLevel;
import com.stal111.forbidden_arcanus.common.block.entity.forge.essence.EssenceType;
import com.stal111.forbidden_arcanus.common.block.entity.forge.ritual.Ritual;
import com.stal111.forbidden_arcanus.common.block.entity.forge.ritual.RitualInput;
import com.stal111.forbidden_arcanus.common.block.entity.forge.ritual.result.CreateItemResult;
import com.stal111.forbidden_arcanus.common.block.entity.forge.ritual.result.RitualResult;
import com.stal111.forbidden_arcanus.common.block.entity.forge.ritual.result.TransmuteInputResult;
import com.stal111.forbidden_arcanus.common.block.entity.forge.ritual.result.UpgradeTierResult;
import com.stal111.forbidden_arcanus.common.item.enhancer.EnhancerDefinition;
import com.stal111.forbidden_arcanus.common.item.enhancer.EnhancerHelper;
import com.stal111.forbidden_arcanus.core.registry.FARegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.integration.ae2.key.ForbiddenEssenceKey;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * Packaged core adapter for the Forbidden Arcanus Hephaestus Forge.
 *
 * <ul>
 *     <li>The forge tier must satisfy the ritual's tier requirement.</li>
 *     <li>Blood / Souls / Aureal / Experience are <b>not</b> part of the pattern. The adapter derives the essence
 *     cost from the matched ritual and pulls the corresponding {@link ForbiddenEssenceKey} from the connected
 *     network, consuming it when the ritual starts.</li>
 *     <li>Tier-upgrade rituals are supported. Once the forge block has been upgraded in place, the crafting job is
 *     completed by feeding the expected output straight to the crafting CPU (it is not added to the network), so no
 *     duplicate item is created.</li>
 *     <li>When the provider is in auto-return mode, a crafted item is pulled from the forge's main slot and returned
 *     to the network. Otherwise it stays in the forge.</li>
 * </ul>
 */
public final class HephaestusForgeAdapter implements MultiblockAdapter {
    public static final ResourceLocation ADAPTER_ID = CreativeDrawersProducer2.makeId("hephaestus_forge_packaged_core");

    private static final int PRIORITY = 100;
    private static final int PEDESTAL_HORIZONTAL_RANGE = 4;
    private static final int PEDESTAL_VERTICAL_RANGE = 2;
    private static final ResourceLocation RITUAL_RECIPE_ID = ResourceLocation.fromNamespaceAndPath("forbidden_arcanus", "ritual");
    private static final Map<GlobalPos, PendingUpgrade> PENDING_UPGRADES = new ConcurrentHashMap<>();

    private enum Handle {
        INSTANCE
    }

    @Override
    public int priority() {
        return PRIORITY;
    }

    @Override
    public boolean recognizesMain(ServerLevel level, BlockPos pos, @Nullable BlockEntity be) {
        return be instanceof HephaestusForgeBlockEntity;
    }

    @Override
    public ResourceLocation requiredAdapterId(ServerLevel level, BlockPos pos) {
        return ADAPTER_ID;
    }

    @Nullable
    @Override
    public BindingResult bind(ServerLevel level, BlockPos mainPos, IPatternDetails pattern) {
        BlockEntity be = level.getBlockEntity(mainPos);
        if (!(be instanceof HephaestusForgeBlockEntity forge) || !isIdle(forge, level, mainPos)) {
            return null;
        }
        for (Ritual ritual : rituals(level)) {
            if (resultMatches(pattern, ritual)) {
                return new BindingResult(Handle.INSTANCE, BindingMode.REAL);
            }
        }
        return null;
    }

    @Override
    public boolean canDispatch(ServerLevel level, BlockPos mainPos, Object handle) {
        if (handle != Handle.INSTANCE) {
            return false;
        }
        BlockEntity be = level.getBlockEntity(mainPos);
        return be instanceof HephaestusForgeBlockEntity forge && isIdle(forge, level, mainPos);
    }

    @Nullable
    @Override
    public DispatchPlan planWithBinding(ServerLevel level, BlockPos mainPos, IPatternDetails pattern, KeyCounter[] inputs,
                                        Object handle, IActionSource source) {
        if (handle != Handle.INSTANCE) {
            return null;
        }
        BlockEntity be = level.getBlockEntity(mainPos);
        if (!(be instanceof HephaestusForgeBlockEntity forge) || !isIdle(forge, level, mainPos)) {
            return null;
        }
        IGrid grid = gridFrom(source);
        if (grid == null) {
            return null;
        }

        Map<AEItemKey, Long> itemCounts = new LinkedHashMap<>();
        for (KeyCounter counter : inputs) {
            for (var entry : counter) {
                AEKey key = entry.getKey();
                long amount = entry.getLongValue();
                if (amount > 0 && key instanceof AEItemKey itemKey) {
                    itemCounts.merge(itemKey, amount, Long::sum);
                }
            }
        }

        int forgeTier = forgeTier(level, mainPos);
        HephaestusForgeLevel forgeLevel = forgeLevel(level, mainPos);
        MEStorage storage = grid.getStorageService().getInventory();
        for (Ritual ritual : rituals(level)) {
            if (!resultMatches(pattern, ritual)) {
                continue;
            }
            Match match = matchRitual(ritual, forgeTier, itemCounts, forge, level);
            if (match != null && essencesAvailable(forgeLevel, storage, ritual, source)) {
                GenericStack upgradeOutput = upgradeOutput(pattern, ritual);
                return buildPlan(level, mainPos, forge, ritual, match, storage, source, upgradeOutput);
            }
        }
        return null;
    }

    @Override
    public List<GenericStack> extractOutputs(ServerLevel level, BlockPos mainPos, AllowedOutputFilter filter, IActionSource source) {
        BlockEntity be = level.getBlockEntity(mainPos);
        if (!(be instanceof HephaestusForgeBlockEntity forge)) {
            return List.of();
        }
        if (forge.getRitualManager().isRitualActive()) {
            return List.of();
        }

        ItemStack output = forge.getStack(HephaestusForgeBlockEntity.MAIN_SLOT);
        if (!output.isEmpty()) {
            AEItemKey key = AEItemKey.of(output);
            if (key != null && filter.matches(key)) {
                int count = output.getCount();
                forge.setStack(HephaestusForgeBlockEntity.MAIN_SLOT, ItemStack.EMPTY);
                return List.of(new GenericStack(key, count));
            }
            return List.of();
        }

        GlobalPos key = GlobalPos.of(level.dimension(), mainPos);
        PendingUpgrade pending = PENDING_UPGRADES.get(key);
        if (pending != null && forgeLevel(level, mainPos).getAsInt() >= pending.targetTier()) {
            PENDING_UPGRADES.remove(key);
            completeUpgradeWithoutItem(gridFrom(source), pending.output(), source);
        }
        return List.of();
    }

    /**
     * Feeds the expected output to the crafting CPU so the job finishes, then removes the item again so the network
     * does not gain anything.
     */
    private static void completeUpgradeWithoutItem(@Nullable IGrid grid, GenericStack output, IActionSource source) {
        if (grid == null || output == null || output.amount() <= 0) {
            return;
        }
        MEStorage storage = grid.getStorageService().getInventory();
        long stored = storage.insert(output.what(), output.amount(), Actionable.MODULATE, source);
        if (stored > 0) {
            storage.extract(output.what(), stored, Actionable.MODULATE, source);
        }
    }

    @Nullable
    private static GenericStack upgradeOutput(IPatternDetails pattern, Ritual ritual) {
        if (!(ritual.result() instanceof UpgradeTierResult)) {
            return null;
        }
        List<GenericStack> outputs = pattern.getOutputs();
        return outputs.isEmpty() ? null : outputs.get(0);
    }

    @Nullable
    private static DispatchPlan buildPlan(ServerLevel level, BlockPos mainPos, HephaestusForgeBlockEntity forge,
                                          Ritual ritual, Match match, MEStorage storage, IActionSource source,
                                          @Nullable GenericStack upgradeOutput) {
        List<PedestalBlockEntity> emptyPedestals = new ArrayList<>();
        for (PedestalBlockEntity pedestal : pedestals(level, mainPos)) {
            if (pedestal.getStack().isEmpty()) {
                emptyPedestals.add(pedestal);
            }
        }
        if (emptyPedestals.size() < match.pedestals().size()) {
            return null;
        }

        List<TargetSlot> targets = new ArrayList<>(match.pedestals().size() + 1);
        List<PedestalPlacement> placements = new ArrayList<>(match.pedestals().size());
        for (int i = 0; i < match.pedestals().size(); i++) {
            ItemStack planned = match.pedestals().get(i);
            BlockPos pedestalPos = emptyPedestals.get(i).getBlockPos();
            placements.add(new PedestalPlacement(pedestalPos, planned));
            targets.add(new TargetSlot(
                    level,
                    pedestalPos,
                    null,
                    List.of(new GenericStack(AEItemKey.of(planned), 1)),
                    InsertionStrategy.CUSTOM,
                    pedestalInserter(level, pedestalPos, planned)
            ));
        }

        ItemStack mainStack = match.main().toStack(1);
        targets.add(new TargetSlot(
                level,
                mainPos,
                null,
                List.of(new GenericStack(match.main(), 1)),
                InsertionStrategy.CUSTOM,
                forgeInserter(level, mainPos, mainStack)
        ));

        int targetTier = ritual.result() instanceof UpgradeTierResult upgrade ? upgrade.resultTier() : 0;
        return new DispatchPlan(List.copyOf(targets), () -> {
            for (PedestalPlacement placement : placements) {
                forge.updatePedestalStack(placement.pos(), placement.stack());
            }
            startRitual(level, mainPos, forge, ritual, storage, source, upgradeOutput, targetTier);
        });
    }

    private static void startRitual(ServerLevel level, BlockPos mainPos, HephaestusForgeBlockEntity forge, Ritual ritual,
                                    MEStorage storage, IActionSource source, @Nullable GenericStack upgradeOutput, int targetTier) {
        for (EssenceType type : EssenceType.values()) {
            int need = ritual.requirements().essences().get(type);
            if (need <= 0) {
                continue;
            }
            long extracted = storage.extract(ForbiddenEssenceKey.of(type), need, Actionable.MODULATE, source);
            if (extracted > 0) {
                forge.getEssenceManager().setEssence(type, (int) Math.min(Integer.MAX_VALUE, extracted));
            }
        }

        FakePlayer player = FakePlayerFactory.getMinecraft(level);
        if (forge.getRitualManager().startRitual(player, forge.getEssenceManager().getStorage())) {
            level.sendBlockUpdated(mainPos, forge.getBlockState(), forge.getBlockState(), Block.UPDATE_ALL);
            if (upgradeOutput != null && targetTier > 0) {
                PENDING_UPGRADES.put(GlobalPos.of(level.dimension(), mainPos), new PendingUpgrade(upgradeOutput, targetTier));
            }
        }
    }

    private static BiFunction<GenericStack, Actionable, Long> pedestalInserter(ServerLevel level, BlockPos pedestalPos, ItemStack planned) {
        return (stack, mode) -> {
            if (!(stack.what() instanceof AEItemKey key)) {
                return 0L;
            }
            ItemStack unit = key.toStack(1);
            if (!ItemStack.isSameItemSameComponents(unit, planned)) {
                return 0L;
            }
            BlockEntity be = level.getBlockEntity(pedestalPos);
            if (!(be instanceof PedestalBlockEntity pedestal) || !pedestal.getStack().isEmpty()) {
                return 0L;
            }
            if (mode == Actionable.MODULATE) {
                pedestal.setStack(unit, null, com.stal111.forbidden_arcanus.common.block.pedestal.effect.PedestalEffectTrigger.PLAYER_PLACE_ITEM);
            }
            return 1L;
        };
    }

    private static BiFunction<GenericStack, Actionable, Long> forgeInserter(ServerLevel level, BlockPos forgePos, ItemStack planned) {
        return (stack, mode) -> {
            if (!(stack.what() instanceof AEItemKey key)) {
                return 0L;
            }
            ItemStack unit = key.toStack(1);
            if (!ItemStack.isSameItemSameComponents(unit, planned)) {
                return 0L;
            }
            BlockEntity be = level.getBlockEntity(forgePos);
            if (!(be instanceof HephaestusForgeBlockEntity forge)
                    || !forge.getStack(HephaestusForgeBlockEntity.MAIN_SLOT).isEmpty()) {
                return 0L;
            }
            if (mode == Actionable.MODULATE) {
                forge.setStack(HephaestusForgeBlockEntity.MAIN_SLOT, unit);
            }
            return 1L;
        };
    }

    @Nullable
    private static Match matchRitual(Ritual ritual, int forgeTier, Map<AEItemKey, Long> itemCounts,
                                     HephaestusForgeBlockEntity forge, ServerLevel level) {
        if (!ritual.requirements().tier().test(forgeTier)) {
            return null;
        }
        if (!enhancersPresent(forge, level, ritual.requirements().enhancers())) {
            return null;
        }

        Map<AEItemKey, Long> remaining = new LinkedHashMap<>(itemCounts);
        AEItemKey main = null;
        for (Map.Entry<AEItemKey, Long> entry : remaining.entrySet()) {
            if (entry.getValue() >= 1 && ritual.mainIngredient().test(entry.getKey().toStack(1))) {
                main = entry.getKey();
                entry.setValue(entry.getValue() - 1);
                break;
            }
        }
        if (main == null) {
            return null;
        }

        List<ItemStack> pedestalStacks = new ArrayList<>();
        for (RitualInput input : ritual.inputs()) {
            for (int i = 0; i < input.amount(); i++) {
                AEItemKey found = null;
                for (Map.Entry<AEItemKey, Long> entry : remaining.entrySet()) {
                    if (entry.getValue() >= 1 && input.ingredient().test(entry.getKey().toStack(1))) {
                        found = entry.getKey();
                        break;
                    }
                }
                if (found == null) {
                    return null;
                }
                remaining.merge(found, -1L, Long::sum);
                pedestalStacks.add(found.toStack(1));
            }
        }
        for (long leftover : remaining.values()) {
            if (leftover != 0) {
                return null;
            }
        }
        return new Match(main, pedestalStacks);
    }

    private static boolean essencesAvailable(HephaestusForgeLevel forgeLevel, MEStorage storage, Ritual ritual, IActionSource source) {
        for (EssenceType type : EssenceType.values()) {
            int need = ritual.requirements().essences().get(type);
            if (need <= 0) {
                continue;
            }
            if (need > forgeLevel.getMaxAmount(type)) {
                return false;
            }
            long available = storage.extract(ForbiddenEssenceKey.of(type), need, Actionable.SIMULATE, source);
            if (available < need) {
                return false;
            }
        }
        return true;
    }

    private static boolean resultMatches(IPatternDetails pattern, Ritual ritual) {
        RitualResult result = ritual.result();
        List<GenericStack> outputs = pattern.getOutputs();
        if (result instanceof CreateItemResult create) {
            return outputs.size() == 1
                    && outputs.getFirst().what() instanceof AEItemKey key
                    && ItemStack.isSameItemSameComponents(key.toStack(1), create.result());
        }
        if (result instanceof TransmuteInputResult transmute) {
            // The main input is transmuted into the result, keeping its components (damage, enchantments...), so only
            // the item type can be compared here.
            return outputs.size() == 1
                    && outputs.getFirst().what() instanceof AEItemKey key
                    && key.getItem() == transmute.result().value();
        }
        if (result instanceof UpgradeTierResult upgrade) {
            if (outputs.isEmpty()) {
                return true;
            }
            if (outputs.size() == 1 && outputs.getFirst().what() instanceof AEItemKey key) {
                Block target = HephaestusForgeLevel.getFromIndex(upgrade.resultTier()).getBlock();
                return key.toStack(1).is(target.asItem());
            }
            return false;
        }
        return false;
    }

    private static boolean enhancersPresent(HephaestusForgeBlockEntity forge, ServerLevel level, HolderSet<EnhancerDefinition> required) {
        if (required.size() == 0) {
            return true;
        }
        List<Holder<EnhancerDefinition>> present = new ArrayList<>();
        for (int slot = 0; slot <= 3; slot++) {
            EnhancerHelper.getEnhancerHolder(level.registryAccess(), forge.getStack(slot)).ifPresent(present::add);
        }
        return required.stream().allMatch(present::contains);
    }

    private static boolean isIdle(HephaestusForgeBlockEntity forge, ServerLevel level, BlockPos pos) {
        if (forge.getRitualManager().isRitualActive()) {
            return false;
        }
        for (int slot = HephaestusForgeBlockEntity.MAIN_SLOT; slot <= 8; slot++) {
            if (!forge.getStack(slot).isEmpty()) {
                return false;
            }
        }
        for (PedestalBlockEntity pedestal : pedestals(level, pos)) {
            if (!pedestal.getStack().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static List<PedestalBlockEntity> pedestals(ServerLevel level, BlockPos origin) {
        List<PedestalBlockEntity> list = new ArrayList<>();
        BlockPos from = origin.offset(-PEDESTAL_HORIZONTAL_RANGE, -PEDESTAL_VERTICAL_RANGE, -PEDESTAL_HORIZONTAL_RANGE);
        BlockPos to = origin.offset(PEDESTAL_HORIZONTAL_RANGE, PEDESTAL_VERTICAL_RANGE, PEDESTAL_HORIZONTAL_RANGE);
        for (BlockPos pos : BlockPos.betweenClosed(from, to)) {
            if (pos.equals(origin) || !level.isLoaded(pos)) {
                continue;
            }
            if (level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {
                list.add(pedestal);
            }
        }
        return list;
    }

    private static int forgeTier(ServerLevel level, BlockPos pos) {
        return forgeLevel(level, pos).getAsInt();
    }

    private static HephaestusForgeLevel forgeLevel(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() instanceof HephaestusForgeBlock block
                ? block.getLevel()
                : HephaestusForgeLevel.ONE;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static List<Ritual> rituals(ServerLevel level) {
        // Forbidden Arcanus JS (and KubeJS) turn rituals into recipes of type forbidden_arcanus:ritual and make the
        // forge resolve them through the RecipeManager. Prefer those so scripts/datapack-added rituals are seen too.
        RecipeType<?> recipeType = BuiltInRegistries.RECIPE_TYPE.get(RITUAL_RECIPE_ID);
        if (recipeType != null) {
            List<Ritual> fromRecipes = new ArrayList<>();
            List<RecipeHolder<?>> holders = (List<RecipeHolder<?>>) (List) level.getRecipeManager().getAllRecipesFor((RecipeType) recipeType);
            for (RecipeHolder<?> holder : holders) {
                Object value = holder.value();
                if (value instanceof Ritual ritual) {
                    fromRecipes.add(ritual);
                }
            }
            if (!fromRecipes.isEmpty()) {
                return fromRecipes;
            }
        }
        return level.registryAccess().lookupOrThrow(FARegistries.RITUAL).listElements().map(Holder.Reference::value).toList();
    }

    @Nullable
    private static IGrid gridFrom(IActionSource source) {
        return source.machine()
                .map(IActionHost::getActionableNode)
                .filter(Objects::nonNull)
                .map(IGridNode::getGrid)
                .orElse(null);
    }

    private record Match(AEItemKey main, List<ItemStack> pedestals) {
    }

    private record PedestalPlacement(BlockPos pos, ItemStack stack) {
    }

    private record PendingUpgrade(GenericStack output, int targetTier) {
    }
}
