package net.yxiao233.cdp2.common.block.entity;

import com.jerry.mekextras.api.ExtraUpgrade;
import com.jerry.mekextras.common.registries.ExtraItems;
import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.SupplierDataSource;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.Horizontal;
import com.lowdragmc.lowdraglib2.gui.ui.elements.*;
import com.lowdragmc.lowdraglib2.gui.ui.event.HoverTooltips;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvent;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents;
import com.lowdragmc.lowdraglib2.gui.ui.style.PropertyRegistry;
import com.lowdragmc.lowdraglib2.gui.ui.styletemplate.Sprites;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.storage.FieldManagedStorage;
import com.lowdragmc.lowdraglib2.syncdata.storage.IManagedStorage;
import mekanism.api.Upgrade;
import mekanism.common.item.ItemUpgrade;
import mekanism.common.registries.MekanismItems;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.tile.interfaces.IUpgradeTile;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.block.entity.CDPMachineBlockEntity;
import net.yxiao233.cdp2.api.capabilities.BlockCapabilityMap;
import net.yxiao233.cdp2.api.capabilities.ItemCapability;
import net.yxiao233.cdp2.client.gui.CDPSprites;
import net.yxiao233.cdp2.common.item.CreativeShardItem;
import net.yxiao233.cdp2.common.item.CreativeShardTier;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPItem;
import net.yxiao233.cdp2.common.registry.CDPTag;
import net.yxiao233.cdp2.misc.UpgradableTypes;
import net.yxiao233.cdp2.misc.UpgradePointManager;
import net.yxiao233.cdp2.mixin.mekanism.TileComponentUpgradeAccessor;
import net.yxiao233.cdp2.util.LDLibUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.appliedenergistics.yoga.YogaEdge;
import org.appliedenergistics.yoga.YogaFlexDirection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UpgradeStationBlockEntity extends CDPMachineBlockEntity implements INBTSerializable<CompoundTag>{
    private final FieldManagedStorage fieldManagedStorage = new FieldManagedStorage(this);
    private final BlockCapabilityMap capabilityMap = BlockCapabilityMap.create().add(new ItemCapability<>(new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(getLevel() != null && !getLevel().isClientSide()){
                getLevel().sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.is(CDPTag.Items.CREATIVE_SHARDS);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return isItemValid(slot,stack) ? ItemStack.EMPTY : stack;
        }
    }));
    public static final HashMap<BlockPos,UpgradeStationBlockEntity> entries = new HashMap<>();
    @DescSynced
    @Persisted(key = "mekanism")
    private final UpgradePointManager mekanism = UpgradePointManager.of(UpgradableTypes.MEKANISM);
    @DescSynced
    @Persisted(key = "basic")
    private final UpgradePointManager basic = UpgradePointManager.of(UpgradableTypes.BASIC);
    private final Map<BlockPos, IUpgradeTile> TILES = new ConcurrentHashMap<>();
    @DescSynced
    @Persisted(key = "progress")
    public int progress;
    public int maxProgress = 10;
    @DescSynced
    @Persisted(key = "show_range")
    private boolean showRange = true;
    @DescSynced
    @Persisted(key = "show_information")
    private boolean showInformation = true;
    @DescSynced
    @Persisted(key = "total_point")
    private int totalPoint = 0;

    public UpgradeStationBlockEntity(BlockPos pos, BlockState blockState) {
        super(CDPBlock.UPGRADE_STATION.asBlockEntityType(), pos, blockState);
        entries.put(pos,this);
    }

    @Override
    public ModularUI createUI(BlockUIMenuType.BlockUIHolder holder) {
        var root = LDLibUtil.createTabWithBackground();

        mainTab(holder,root);

        upgradeTab("basic",basic,holder,root,(manager, key, event) -> {
            event.hoverTooltips = HoverTooltips.empty().append(Component.translatable("gui.cdp2.range"));
        },(manager, key, event) -> {
            event.hoverTooltips = HoverTooltips.empty().append(Component.literal("max: ").withStyle(ChatFormatting.WHITE).append(Component.literal(String.valueOf(manager.getMax(key))).withStyle(ChatFormatting.RED)));
        });


        upgradeTab("mekanism",mekanism,holder,root,(manager, key, event) -> {
            Item item = getItemUpgrade(key);
            event.hoverTooltips = LDLibUtil.itemHoverTipWithTexture(item);
        },(manager, key, event) -> {
            event.hoverTooltips = HoverTooltips.empty().append(Component.literal("max: ").withStyle(ChatFormatting.WHITE).append(Component.literal(String.valueOf(manager.getMax(key))).withStyle(ChatFormatting.RED)));
        });

        return ModularUI.of(UI.of(root),holder.player);
    }

    @SuppressWarnings("deprecation")
    private void mainTab(BlockUIMenuType.BlockUIHolder holder, TabView tabView){
        var tabMain = LDLibUtil.createBaseRoot();
        Label point = pointDisplay();
        UIElement information = LDLibUtil.createBaseRoot();

        for (int i = 1; i <= CreativeShardTier.values().length; i++) {
            UIElement element = LDLibUtil.rowElement();
            UIElement item = LDLibUtil.fakeItemElement(CDPItem.SHARDS.get(CreativeDrawersProducer2.makeId("creative_shard_" + i)).asItem());
            UIElement description = LDLibUtil.text(Component.translatable("gui.cdp.points",i,i > 1 ? "s" : "")).layout(layoutStyle -> layoutStyle.setPosition(YogaEdge.TOP,4));
            element.addChildren(item,description);
            information.addChildren(element,LDLibUtil.flex(1));
        }
        UIElement inputDesc = LDLibUtil.text(Component.translatable("gui.cdp2.input")).layout(layoutStyle -> layoutStyle.marginHorizontal(71));
        UIElement input = LDLibUtil.inputSlot(capabilityMap).layout(layoutStyle -> layoutStyle.marginHorizontal(72));
        UIElement playerInventory = LDLibUtil.playerInventory();
        ProgressBar progressBar = new ProgressBar().setMaxValue(maxProgress).setProgress(0).bindDataSource(SupplierDataSource.of(() -> (float) progress));
        progressBar.label.bindDataSource(SupplierDataSource.of(() -> Component.literal(progressBar.getValue().intValue() + "/" + ((Float) progressBar.getMaxValue()).intValue())));
        progressBar.label.setText(Component.literal(progressBar.getValue().intValue() + "/" + ((Float) progressBar.getMaxValue()).intValue()));
        Button showRangeButton = showRangeButton();

        tabMain.addChildren(point,information,inputDesc,input,progressBar,playerInventory,LDLibUtil.rowElement().addChildren(LDLibUtil.flex(1),showRangeButton));
        creativeDebugButton(holder,tabMain);

        tabView.addTab(new Tab().setText(Component.translatable("gui.cdp2.tab_main")),tabMain);
    }

    @SuppressWarnings("deprecation")
    private void upgradeTab(String id, UpgradePointManager manager, BlockUIMenuType.BlockUIHolder holder, TabView tabView, HoverTipCallBack descriptionHoverTip, @Nullable HoverTipCallBack upgradeTierTip){
        var upgradeTab = LDLibUtil.createBaseRoot();
        Label pointDisplay = pointDisplay();
        UIElement information = LDLibUtil.createBaseRoot();

        int baseGap = 25;
        AtomicInteger firstElementWidth = new AtomicInteger(0);
        manager.getMap().forEach((key, pointPair) ->{
            UIElement element = LDLibUtil.rowElement().layout(layoutStyle -> layoutStyle.width(100));

            Component component = Component.translatable("upgrade.type.cdp2." + key);
            Label type = (Label) LDLibUtil.text(component)
                    .layout(layoutStyle -> layoutStyle.width(component.getString().length() * 6 - 1))
                    .addEventListener(UIEvents.HOVER_TOOLTIPS, event -> {
                        if(showInformation){
                            descriptionHoverTip.accept(manager, key, event);
                        }else{
                            event.hoverTooltips = HoverTooltips.empty();
                        }
                    });

            if(firstElementWidth.get() == 0){
                firstElementWidth.set(component.getString().length() * 6 - 1);
            }

            Button decrease = (Button) new Button().setText("-").setOnServerClick(event -> {
                int point = manager.getPoint(key);
                int delta = 1;
                manager.updatePoint(key,Math.max(0,point - delta));
                addTotalPoint(point - manager.getPoint(key));
            }).layout(layoutStyle -> layoutStyle.setPosition(YogaEdge.TOP,-0.8f)).addEventListener(UIEvents.HOVER_TOOLTIPS,event -> {
                if(showInformation){
                    event.hoverTooltips = HoverTooltips.empty().append(Component.translatable("gui.cdp2.decrease"));
                }else{
                    event.hoverTooltips = HoverTooltips.empty();
                }
            });
            Button increase = (Button) new Button().setText("+").setOnServerClick(event -> {
                int point = manager.getPoint(key);
                int delta = 1;
                int maxAdd = Math.min(delta,getTotalPoint());
                manager.updatePoint(key,Math.min(manager.getMax(key),point + maxAdd));
                addTotalPoint(-(manager.getPoint(key) - point));
            }).layout(layoutStyle -> layoutStyle.setPosition(YogaEdge.TOP,-0.8f)).addEventListener(UIEvents.HOVER_TOOLTIPS,event -> {
                if(showInformation){
                    event.hoverTooltips = HoverTooltips.empty().append(Component.translatable("gui.cdp2.increase"));
                }else{
                    event.hoverTooltips = HoverTooltips.empty();
                }
            });

            Label how = (Label) LDLibUtil.text(Component.literal(String.valueOf(manager.getPoint(key))))
                    .bind(DataBindingBuilder.componentS2C(() -> Component.literal(String.valueOf(manager.getPoint(key)))).build())
                    .layout(layoutStyle -> layoutStyle.setPosition(YogaEdge.TOP,2f).width(String.valueOf(manager.getPoint(key)).length() * 5 - 1))
                    .addEventListener(UIEvents.HOVER_TOOLTIPS,event -> {
                        if(upgradeTierTip != null){
                            if(showInformation){
                                upgradeTierTip.accept(manager, key, event);
                            }else{
                                event.hoverTooltips = HoverTooltips.empty();
                            }
                        }
                    });
            how.textStyle(textStyle -> textStyle.textAlignHorizontal(Horizontal.CENTER));


            float prefix = (component.getString().length() * 6 - 1) - firstElementWidth.get();
            element.addChildren(type,LDLibUtil.widthFlex(baseGap-prefix),decrease,LDLibUtil.flex(3f),how,LDLibUtil.flex(3.5f),increase);
            information.addChildren(element,LDLibUtil.flex(1));
        });


        UIElement settings = LDLibUtil.rowElement();
        Button showInformationButton = (Button) new Button().setText(Component.literal(" ").withStyle(ChatFormatting.AQUA)).setOnServerClick(event -> {
            showInformation = !showInformation;
        }).addEventListener(UIEvents.HOVER_TOOLTIPS, event -> {
            event.hoverTooltips = HoverTooltips.empty().append(Component.translatable("gui.cdp2." + (showInformation ? "show" : "hide") + "_information"));
        });
        showInformationButton.layout(layoutStyle -> layoutStyle.width(13f));
        showInformationButton.buttonStyle(buttonStyle -> {
            buttonStyle.setDefault(PropertyRegistry.BASE_BACKGROUND, CDPSprites.INFORMATION_BUTTON);
            buttonStyle.setDefault(PropertyRegistry.HOVER_BACKGROUND, CDPSprites.INFORMATION_BUTTON_LIGHT);
            buttonStyle.setDefault(PropertyRegistry.PRESSED_BACKGROUND, CDPSprites.INFORMATION_BUTTON_DARK);
        });

        Button reset = (Button) new Button().setText(" ").setOnServerClick(event -> {
            addTotalPoint(manager.resetAll());
        }).addEventListener(UIEvents.HOVER_TOOLTIPS,event -> {
            if(showInformation){
                event.hoverTooltips = HoverTooltips.empty().append(Component.translatable("gui.cdp2.reset"));
            }else{
                event.hoverTooltips = HoverTooltips.empty();
            }
        });
        reset.layout(layoutStyle -> layoutStyle.width(13f));
        reset.buttonStyle(buttonStyle -> {
            buttonStyle.setDefault(PropertyRegistry.BASE_BACKGROUND, CDPSprites.RESET_BUTTON);
            buttonStyle.setDefault(PropertyRegistry.HOVER_BACKGROUND, CDPSprites.RESET_BUTTON_LIGHT);
            buttonStyle.setDefault(PropertyRegistry.PRESSED_BACKGROUND, CDPSprites.RESET_BUTTON_DARK);
        });
        settings.addChildren(showInformationButton,LDLibUtil.flex(2));
        if(id.equals("basic")){
            Button showRangeButton = showRangeButton();
            settings.addChildren(showRangeButton,LDLibUtil.widthFlex(1f));
        }
        settings.addChildren(reset);

        upgradeTab.addChildren(pointDisplay,information,settings);
        creativeDebugButton(holder,upgradeTab);
        tabView.addTab(new Tab().setText(Component.translatable("gui.cdp2.tab_" + id)),upgradeTab);
    }

    @SuppressWarnings("deprecation")
    private void creativeDebugButton(BlockUIMenuType.BlockUIHolder holder, UIElement root){
        if(holder.player.isCreative()){
            UIElement testTotalPoint = new UIElement().layout(layoutStyle -> layoutStyle.flexDirection(YogaFlexDirection.ROW));
            testTotalPoint.addChildren(new Button().setText("- point").setOnServerClick(event -> addTotalPoint(-1)),LDLibUtil.flex(2),new Button().setText("+ point").setOnServerClick(event -> addTotalPoint(1)));
            root.addChild(testTotalPoint);
        }
    }

    private Button showRangeButton(){
        Button showRangeButton = (Button) new Button().setText(" ").setOnServerClick(event -> {
            showRange = !showRange;
        }).addEventListener(UIEvents.HOVER_TOOLTIPS,event -> {
            if(showInformation){
                event.hoverTooltips = HoverTooltips.empty().append(Component.translatable("gui.cdp2." + (showRange ? "preview" : "hide")));
            }else{
                event.hoverTooltips = HoverTooltips.empty();
            }
        });
        showRangeButton.layout(layoutStyle -> layoutStyle.width(13f));
        showRangeButton.buttonStyle(buttonStyle -> {
            buttonStyle.setDefault(PropertyRegistry.BASE_BACKGROUND, CDPSprites.RANGE_BUTTON);
            buttonStyle.setDefault(PropertyRegistry.HOVER_BACKGROUND, CDPSprites.RANGE_BUTTON_LIGHT);
            buttonStyle.setDefault(PropertyRegistry.PRESSED_BACKGROUND, CDPSprites.RANGE_BUTTON_DARK);
        });
        return showRangeButton;
    }

    private Label pointDisplay(){
        Label pointDisplay = (Label) new Label()
                .textStyle(textStyle -> textStyle.textAlignHorizontal(Horizontal.CENTER))
                .setText(Component.translatable("gui.cdp2.total_points",getTotalPoint()))
                .layout(layoutStyle -> layoutStyle.height(22).paddingAll(7).gapAll(5))
                .style(basicStyle -> basicStyle.background(Sprites.BORDER));
        pointDisplay.bind(DataBindingBuilder.componentS2C(() -> Component.translatable("gui.cdp2.total_points",getTotalPoint())).build());
        return pointDisplay;
    }

    public void addTotalPoint(int delta){
        totalPoint += delta;
        setChanged();
    }

    public int getTotalPoint(){
        return totalPoint;
    }

    public boolean isShowRange() {
        return showRange && getRange() > 0;
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if(!level.isClientSide()){
            if(level.getGameTime() % 20 == 0){
                updateMekanismUpgrade();
            }
            handle();
        }
    }

    private void handle(){
        ItemStack stack = capabilityMap.getHandler(Capabilities.ItemHandler.BLOCK,ItemCapability.class).getHandler().getStackInSlot(0);
        if(stack.isEmpty()){
            progress = 0;
            return;
        }
        if(progress >= maxProgress && stack.getItem() instanceof CreativeShardItem shardItem){
            progress = 0;
            stack.setCount(stack.getCount() - 1);
            addTotalPoint(shardItem.getTier());
            return;
        }
        progress = Math.min(++ progress, maxProgress);
    }

    public void updateMekanismUpgrade(){
        if(level == null){
            return;
        }
        int range = getRange();
        int radius = (range / 16) + 1;
        List<Pair<BlockPos, BlockEntity>> blockEntities = getBlockEntities(getChunksInRadius(radius));
        blockEntities.forEach(entry ->{
            if(entry.getRight() instanceof IUpgradeTile upgradeTile){
                BlockPos pos = entry.getLeft();
                if(getBoundary().contains(pos.getX(), pos.getY(), pos.getZ())){
                    TILES.put(entry.getLeft(),upgradeTile);
                }
            }else{
                TILES.remove(entry.getLeft());
            }
        });

        TILES.forEach((pos, tile) -> {
            if(getBoundary().contains(pos.getX(), pos.getY(), pos.getZ())){
                applyMekanismUpgrade(tile);
            }else{
                removeMekanismUpgrade(tile);
                TILES.remove(pos);
            }
        });
    }

    private List<Pair<BlockPos, BlockEntity>> getBlockEntities(List<LevelChunk> chunks){
        List<Pair<BlockPos, BlockEntity>> entities = new ArrayList<>();
        chunks.forEach(chunk -> {
            chunk.getBlockEntities().forEach((pos, entity) -> {
                entities.add(Pair.of(pos,entity));
            });
        });
        return entities;
    }

    private List<LevelChunk> getChunksInRadius(int radius){
        List<LevelChunk> chunks = new ArrayList<>();
        ChunkPos centerPos = level.getChunkAt(this.worldPosition).getPos();
        for (int x = centerPos.x - radius; x <= centerPos.x + radius; x++) {
            for (int z = centerPos.z - radius; z <= centerPos.z + radius; z++) {
                chunks.add(level.getChunk(x,z));
            }
        }
        return chunks;
    }

    public void applyMekanismUpgrade(IUpgradeTile tile){
        mekanism.getMap().forEach((name, pointPair) ->{
            Upgrade upgrade = Upgrade.valueOf(name.toUpperCase());
            int point = pointPair.getPoint();
            if(tile.supportsUpgrade(upgrade) && point >= 0){
                TileComponentUpgrade component = tile.getComponent();
                int installed = component.getUpgrades(upgrade);

                if(installed < point){
                    component.addUpgrades(upgrade, point - installed);
                }else{
                    removeUpgrades(component,upgrade,installed - point);
                }
            }
        });
    }

    public void applyMekanismUpgrade(BlockPos pos){
        if(level != null && pos != null && level.getBlockEntity(pos) instanceof IUpgradeTile tile){
          applyMekanismUpgrade(tile);
        }
    }

    private void removeUpgrades(TileComponentUpgrade tile, Upgrade upgrade, int toRemove) {
        if(toRemove > 0){
            if(tile instanceof TileComponentUpgradeAccessor accessor){
                Map<Upgrade, Integer> upgradeMap = accessor.getUpgradeMap();
                upgradeMap.put(upgrade, Math.max(tile.getUpgrades(upgrade) - toRemove,0));
                accessor.getTileEntity().recalculateUpgrades(upgrade);
                if(upgrade == Upgrade.MUFFLING){
                    accessor.getTileEntity().sendUpdatePacket();
                }
                accessor.getTileEntity().markForSave();
            }
        }
    }

    public void removeMekanismUpgrade(IUpgradeTile tile){
        for(Upgrade upgrade : Upgrade.values()){
            if(tile.supportsUpgrade(upgrade) && !upgrade.equals(ExtraUpgrade.CREATIVE)){
                TileComponentUpgrade component = tile.getComponent();
                component.removeUpgrade(upgrade, true);
            }
        }
    }

    public void removeSingleMekanismUpgrade(BlockPos pos){
        if(level != null && pos != null && level.getBlockEntity(pos) instanceof IUpgradeTile tile){
            removeMekanismUpgrade(tile);
        }
    }

    public void removeAllMekanismUpgrade(BlockPos pos){
        if(level != null && pos != null){
            TILES.forEach((tilePos, tile) ->{
                removeMekanismUpgrade(tile);
            });
        }
    }

    private ItemUpgrade getItemUpgrade(String type){
        return switch (type){
            case "speed" -> MekanismItems.SPEED_UPGRADE.get();
            case "filter" -> MekanismItems.FILTER_UPGRADE.get();
            case "muffling" -> MekanismItems.MUFFLING_UPGRADE.get();
            case "anchor" -> MekanismItems.ANCHOR_UPGRADE.get();
            case "chemical" -> MekanismItems.CHEMICAL_UPGRADE.get();
            case "STACK" -> ExtraItems.STACK.get();
            case "IONIC_MEMBRANE" -> ExtraItems.IONIC_MEMBRANE.get();
            default -> MekanismItems.ENERGY_UPGRADE.get();
        };
    }

    public int getRange(){
        return basic.getPoint("range");
    }


    public AABB getBoundary(){
        BlockPos pos = getBlockPos();
        int range = getRange();
        return new AABB(
                pos.getX() - range,
                pos.getY() - range,
                pos.getZ() - range,
                pos.getX() + 1 + range,
                pos.getY() + 1 + range,
                pos.getZ() + 1 + range
        );
    }

    public AABB getBoundary(int range){
        BlockPos pos = getBlockPos();
        return new AABB(
                pos.getX() - range,
                pos.getY() - range,
                pos.getZ() - range,
                pos.getX() + 1 + range,
                pos.getY() + 1 + range,
                pos.getZ() + 1 + range
        );
    }

    @Override
    public IManagedStorage getSyncStorage() {
        return fieldManagedStorage;
    }

    @Override
    public BlockCapabilityMap getCapabilityMap() {
        return capabilityMap;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag entityData = new CompoundTag();
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag,provider);
        entityData.put("entity_data",entityData);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag tag) {
        loadAdditional(tag,provider);
    }

    @FunctionalInterface
    public interface HoverTipCallBack{
        void accept(UpgradePointManager manager, String key, UIEvent event);
    }
}
