package net.yxiao233.cdp2.common.registry;

import com.blakebr0.cucumber.item.BaseBlockItem;
import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.api.crop.CropTier;
import com.blakebr0.mysticalagriculture.block.InferiumCropBlock;
import com.blakebr0.mysticalagriculture.block.InfusedFarmlandBlock;
import com.blakebr0.mysticalagriculture.init.ModBlocks;
import com.buuz135.industrial.block.IndustrialBlock;
import com.hrznstudio.titanium.module.BlockWithTile;
import net.darkhax.botanypots.common.impl.block.PotType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.block.FullCopyBlock;
import net.yxiao233.cdp2.api.block.SimpleBlock;
import net.yxiao233.cdp2.api.registry.CDPBlockDeferredRegister;
import net.yxiao233.cdp2.api.registry.CDPBlockEntityDeferredRegister;
import net.yxiao233.cdp2.common.block.UpgradeStationBlock;
import net.yxiao233.cdp2.common.block.VoidCraftingTableBlock;
import net.yxiao233.cdp2.common.block.entity.CreativeDrawerBlockEntity;
import net.yxiao233.cdp2.common.block.entity.UpgradeStationBlockEntity;
import net.yxiao233.cdp2.common.integration.mysticalagriculture.CDPCrops;
import net.yxiao233.cdp2.common.integration.mysticalagriculture.CDPEssenceCropBlock;
import net.yxiao233.cdp2.common.item.UpgradeStationBlockItem;
import net.yxiao233.cdp2.common.integration.botanypot.block.entity.CDPBotanyPotBlockEntity;
import net.yxiao233.cdp2.common.integration.botanypot.CDPPotTier;
import net.yxiao233.cdp2.common.integration.industrialforegoing.block.entity.VoidSieveBlockEntity;
import net.yxiao233.cdp2.common.integration.industrialforegoing.block.VoidSieveEntityBlock;
import net.yxiao233.cdp2.common.integration.mysticalagriculture.CDPCropTier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Supplier;

public class CDPBlock {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, CreativeDrawersProducer2.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreativeDrawersProducer2.MODID);
    public static final HashMap<ResourceLocation,CDPBlockEntityDeferredRegister<?>> CREATIVE_DRAWERS_MAP = new HashMap<>();
    public static final HashMap<ResourceLocation,CDPBlockEntityDeferredRegister<CDPBotanyPotBlockEntity>> POTS_MAP = registerAllPots(false);
    public static final CDPBlockEntityDeferredRegister<CreativeDrawerBlockEntity> DIAMOND_CREATIVE_DRAWER = registerCreativeDrawer("diamond_creative_drawer", Items.DIAMOND::getDefaultInstance);
    public static final CDPBlockEntityDeferredRegister<CreativeDrawerBlockEntity> OAK_LOG_CREATIVE_DRAWER = registerCreativeDrawer("oak_log_creative_drawer", Items.OAK_LOG::getDefaultInstance);
    public static final CDPBlockEntityDeferredRegister<CreativeDrawerBlockEntity> VOID_MATTER_CREATIVE_DRAWER = registerCreativeDrawer("void_matter_creative_drawer", CDPItem.VOID_MATTER::asStack);
    public static final CDPBlockEntityDeferredRegister<CreativeDrawerBlockEntity> APPLE_CREATIVE_DRAWER = registerCreativeDrawer("apple_creative_drawer", Items.APPLE::getDefaultInstance);
    public static final CDPBlockDeferredRegister VOID_BLOCK = CDPBlockDeferredRegister.registrySimple("void_block", () -> new FullCopyBlock(Blocks.AMETHYST_BLOCK),null).addToTab(CDPTab.CONTENT_TAB);
    public static final CDPBlockDeferredRegister VOID_CRAFTING_TABLE = CDPBlockDeferredRegister.registrySimple("void_crafting_table", VoidCraftingTableBlock::new,null).addToTab(CDPTab.CONTENT_TAB);
    public static final CDPBlockEntityDeferredRegister<UpgradeStationBlockEntity> UPGRADE_STATION = CDPBlockEntityDeferredRegister.register(
            "upgrade_station",
            () -> new UpgradeStationBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noLootTable()),
            blockSupplier -> () -> new UpgradeStationBlockItem(blockSupplier,new Item.Properties()),
            UpgradeStationBlockEntity::new
    ).addToTab(CDPTab.MACHINE_TAB).addToTab(CDPTab.CONTENT_TAB);
    public static final CDPBlockDeferredRegister ABSOLUTE_FARMLAND = registerFarmland(CDPCropTier.SEVEN);
    public static final CDPBlockDeferredRegister SUPREME_FARMLAND = registerFarmland(CDPCropTier.EIGHT);
    public static final CDPBlockDeferredRegister COSMIC_FARMLAND = registerFarmland(CDPCropTier.NINE);
    public static final CDPBlockDeferredRegister INFINITE_FARMLAND = registerFarmland(CDPCropTier.TEN);
    public static final BlockWithTile VOID_SIEVE = registerIF("void_sieve", VoidSieveEntityBlock::new, VoidSieveBlockEntity::new);
    public static final CDPBlockDeferredRegister FRAME = CDPBlockDeferredRegister.registrySimple("frame",SimpleBlock::new,null).addToTab(CDPTab.CONTENT_TAB);
    public static final CDPBlockDeferredRegister PRUDENTIUM_CROP = registerCropBlock("prudentium_crop", CDPCrops.PRUDENTIUM);
    public static final CDPBlockDeferredRegister TERTIUM_CROP = registerCropBlock("tertium_crop", CDPCrops.TERTIUM);
    public static final CDPBlockDeferredRegister IMPERIUM_CROP = registerCropBlock("imperium_crop", CDPCrops.IMPERIUM);
    public static final CDPBlockDeferredRegister SUPREMIUM_CROP = registerCropBlock("supremium_crop", CDPCrops.SUPREMIUM);
    public static final CDPBlockDeferredRegister INSANIUM_CROP = registerCropBlock("insanium_crop", CDPCrops.INSANIUM);

    static <T extends BlockEntity> CDPBlockEntityDeferredRegister<T> registrySimple(String name, BlockSupplier<?> blockSupplier, BlockEntityType.BlockEntitySupplier<T> blockEntitySupplier){
        return CDPBlockEntityDeferredRegister.registrySimple(name,() -> blockSupplier.create(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)),blockEntitySupplier,new Item.Properties());
    }

    static CDPBlockEntityDeferredRegister<CreativeDrawerBlockEntity> registerCreativeDrawer(String name, Supplier<ItemStack> infinityItem){
        CDPBlockEntityDeferredRegister<CreativeDrawerBlockEntity> register = CDPBlockEntityDeferredRegister.registerCreativeDrawer(name, infinityItem).addToTab(CDPTab.DRAWER_TAB).addToTab(CDPTab.CONTENT_TAB);
        CREATIVE_DRAWERS_MAP.put(CreativeDrawersProducer2.makeId(name),register);
        return register;
    }

    static CDPBlockDeferredRegister registerFarmland(CropTier cropTier){
        return CDPBlockDeferredRegister.register(cropTier.getName() + "_farmland",() -> new InfusedFarmlandBlock(cropTier), blockSupplier -> () -> new BaseBlockItem(blockSupplier.get())).addToTab(CDPTab.CONTENT_TAB).addToTab(CDPTab.MYSTICAL_AGRICULTURE_TAB);
    }

    static BlockWithTile registerIF(String name, Supplier<? extends IndustrialBlock<?>> block, BlockEntityType.BlockEntitySupplier<? extends BlockEntity> entity){
        CDPBlockDeferredRegister blockWithItem = CDPBlockDeferredRegister.register(name,block,blockSupplier -> () -> new BlockItem(blockSupplier.get(),new Item.Properties()));
        DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> blockEntityType = CDPBlock.BLOCK_ENTITIES.register(name,() -> BlockEntityType.Builder.of(entity,blockWithItem.asBlock()).build(null));
        blockWithItem.addToTab(CDPTab.CONTENT_TAB).addToTab(CDPTab.MACHINE_TAB);
        return new BlockWithTile(blockWithItem.getBlock(),blockEntityType);
    }

    @SuppressWarnings("all")
    static HashMap<ResourceLocation,CDPBlockEntityDeferredRegister<CDPBotanyPotBlockEntity>> registerAllPots(boolean hasWaxed){
        HashMap<ResourceLocation,CDPBlockEntityDeferredRegister<CDPBotanyPotBlockEntity>> map = new HashMap<>();
        Arrays.stream(CDPPotTier.values()).toList().forEach(potTier -> {
            final String basic = potTier.getName() + "_botany_pot";
            final String hopper = potTier.getName() + "_hopper_botany_pot";
            map.put(CreativeDrawersProducer2.makeId(basic),CDPBlockEntityDeferredRegister.registerPot(basic, PotType.BASIC,potTier));
            map.put(CreativeDrawersProducer2.makeId(hopper),CDPBlockEntityDeferredRegister.registerPot(hopper, PotType.HOPPER,potTier));
            if(hasWaxed){
                final String waxed = potTier.getName() + "_waxed_botany_pot";
                map.put(CreativeDrawersProducer2.makeId(waxed),CDPBlockEntityDeferredRegister.registerPot(waxed, PotType.WAXED,potTier));
            }
        });
        map.values().forEach(pot -> pot.addToTab(CDPTab.CONTENT_TAB).addToTab(CDPTab.BOTANY_POT_TAB));

        return map;
    }

    private static CDPBlockDeferredRegister registerCropBlock(String name, Crop crop){
        return CDPBlockDeferredRegister.registerNoItem(name,() -> new CDPEssenceCropBlock(crop));
    }

    public static void init(IEventBus eventBus){
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }

    @FunctionalInterface
    interface BlockSupplier<T extends Block> {
        T create(BlockBehaviour.Properties properties);
    }
}
