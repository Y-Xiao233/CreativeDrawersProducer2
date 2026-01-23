package net.yxiao233.cdp2.common.block.entity;

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
import mekanism.common.util.WorldUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.block.entity.CDPMachineBlockEntity;
import net.yxiao233.cdp2.api.capabilities.BlockCapabilityMap;
import net.yxiao233.cdp2.client.gui.CDPSprites;
import net.yxiao233.cdp2.common.item.CreativeShardItem;
import net.yxiao233.cdp2.common.item.CreativeShardTier;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPItem;
import net.yxiao233.cdp2.common.registry.CDPTag;
import net.yxiao233.cdp2.misc.UpgradableTypes;
import net.yxiao233.cdp2.misc.UpgradePointManager;
import net.yxiao233.cdp2.util.LDLibUtil;
import org.appliedenergistics.yoga.YogaEdge;
import org.appliedenergistics.yoga.YogaFlexDirection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class UpgradeStationBlockEntity extends CDPMachineBlockEntity implements INBTSerializable<CompoundTag>{
    private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);
    @DescSynced
    @Persisted(key = "owner")
    private UUID ownerUUID = null;
    @DescSynced
    @Persisted(key = "show_information")
    private boolean showInformation = true;
    @DescSynced
    @Persisted(key = "show_range")
    private boolean showRange = true;
    @DescSynced
    @Persisted(key = "progress")
    public int progress;
    public int maxProgress = 100;
    private Player player;
    @DescSynced
    private boolean shouldApplyMek = true;
    public static final HashMap<BlockPos,UpgradeStationBlockEntity> entries = new HashMap<>();
    @DescSynced
    @Persisted(key = "mekanism")
    private final UpgradePointManager mekanism = UpgradePointManager.of(UpgradableTypes.MEKANISM);
    @DescSynced
    @Persisted(key = "basic")
    private final UpgradePointManager basic = UpgradePointManager.of(UpgradableTypes.BASIC);

    private final BlockCapabilityMap capabilityMap = BlockCapabilityMap.create()
            .addItemHandler(new ItemStackHandler(1){
                @Override
                public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                    return stack.is(CDPTag.Items.CREATIVE_SHARDS);
                }
            });
    public UpgradeStationBlockEntity(BlockPos pos, BlockState blockState) {
        super(CDPBlock.UPGRADE_STATION.asBlockEntityType(), pos, blockState);
        entries.put(pos,this);
        shouldApplyMek = true;
    }

    @Override
    public BlockCapabilityMap getCapabilityMap() {
        return capabilityMap;
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if(player == null){
            player = level.getPlayerByUUID(ownerUUID);
        }
        if(!level.isClientSide()){
            if(shouldApplyMek){
                applyMekanismUpgrade(level);
                shouldApplyMek = false;
            }
            handle();
        }
    }

    private void handle(){
        ItemStack stack = capabilityMap.getItemHandler().getStackInSlot(0);
        if(stack.isEmpty()){
            progress = 0;
            return;
        }
        if(progress >= maxProgress && stack.getItem() instanceof CreativeShardItem shardItem && player != null){
            progress = 0;
            stack.setCount(stack.getCount() - 1);
            addTotalPoint(player,shardItem.getTier());
            return;
        }
        progress = Math.min(++ progress, maxProgress);
    }

    private void applyMekanismUpgrade(Level level){
        if(level != null && !level.isClientSide() && getRange() > 0){
            AABB aabb = getBoundary();
            BlockPos.betweenClosed((int) aabb.minX, (int) aabb.minY, (int) aabb.minZ, (int) aabb.maxX, (int) aabb.maxY, (int) aabb.maxZ).forEach(pos -> {
                BlockEntity blockEntity = WorldUtils.getTileEntity(level, pos);
                if(blockEntity instanceof IUpgradeTile tile){
                    mekanism.getMap().forEach((k,p) ->{
                        if(tile.supportsUpgrades()){
                            TileComponentUpgrade component = tile.getComponent();
                            ItemUpgrade itemUpgrade = getItemUpgrade(k);
                            Upgrade type = itemUpgrade.getUpgradeType(new ItemStack(itemUpgrade,p.getPoint()));
                            if(tile.supportsUpgrade(type)){
                                component.addUpgrades(type,p.getPoint());
                            }
                        }
                    });
                }
            });
        }
    }

    public void removeMekanismUpgrade(Level level, int range){
        range = range == -1 ? getRange() : range;
        if(level != null && !level.isClientSide() && range > 0){
            AABB aabb = getBoundary(range);
            BlockPos.betweenClosed((int) aabb.minX, (int) aabb.minY, (int) aabb.minZ, (int) aabb.maxX, (int) aabb.maxY, (int) aabb.maxZ).forEach(pos -> {
                BlockEntity blockEntity = WorldUtils.getTileEntity(level, pos);
                if(blockEntity instanceof IUpgradeTile tile){
                    mekanism.getMap().forEach((key,pointPair) ->{
                        if(tile.supportsUpgrades()){
                            TileComponentUpgrade component = tile.getComponent();
                            ItemUpgrade itemUpgrade = getItemUpgrade(key);
                            Upgrade type = itemUpgrade.getUpgradeType(itemUpgrade.getDefaultInstance());
                            if(tile.supportsUpgrade(type)){
                                component.removeUpgrade(type,true);
                            }
                        }
                    });
                }
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
            default -> MekanismItems.ENERGY_UPGRADE.get();
        };
    }

    public void markToUpdate(Level level){
        if(level != null){
            removeMekanismUpgrade(level,getRange());
            applyMekanismUpgrade(level);
        }
    }

    //UI
    @Override
    public ModularUI createUI(BlockUIMenuType.BlockUIHolder holder){
        if(this.getOwner() == null){
            this.setOwner(holder.player.getUUID());
        }
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

    private void mainTab(BlockUIMenuType.BlockUIHolder holder, TabView tabView){
        var tabMain = LDLibUtil.createBaseRoot();
        Label point = pointDisplay(holder.player);
        UIElement information = LDLibUtil.createBaseRoot();

        for (int i = 1; i <= CreativeShardTier.values().length; i++) {
            UIElement element = LDLibUtil.rowElement();
            UIElement item = LDLibUtil.fakeItemElement(CDPItem.SHARDS.get(CreativeDrawersProducer2.makeId("creative_shard_" + i)).asItem());
            UIElement description = LDLibUtil.text(Component.translatable("gui.cdp.points",i,i > 1 ? "s" : "")).layout(layoutStyle -> layoutStyle.setPosition(YogaEdge.TOP,4));
            element.addChildren(item,description);
            information.addChildren(element,LDLibUtil.flex(1));
        }
        UIElement inputDesc = LDLibUtil.text(Component.translatable("gui.cdp2.input")).layout(layoutStyle -> layoutStyle.horizontal(71));
        UIElement input = LDLibUtil.inputSlot(capabilityMap).layout(layoutStyle -> layoutStyle.horizontal(72));
        UIElement playerInventory = LDLibUtil.playerInventory();
        ProgressBar progressBar = new ProgressBar().setMaxValue(maxProgress).setProgress(0).bindDataSource(SupplierDataSource.of(() -> (float) progress));
        progressBar.label.bindDataSource(SupplierDataSource.of(() -> Component.literal(progressBar.getValue().intValue() + "/" + ((Float) progressBar.getMaxValue()).intValue())));
        progressBar.label.setText(Component.literal(progressBar.getValue().intValue() + "/" + ((Float) progressBar.getMaxValue()).intValue()));
        Button showRangeButton = showRangeButton();

        tabMain.addChildren(point,information,inputDesc,input,progressBar,playerInventory,LDLibUtil.rowElement().addChildren(LDLibUtil.flex(1),showRangeButton));
        creativeDebugButton(holder,tabMain);

        tabView.addTab(new Tab().setText(Component.translatable("gui.cdp2.tab_main")),tabMain);
    }

    private void upgradeTab(String id, UpgradePointManager manager, BlockUIMenuType.BlockUIHolder holder, TabView tabView, HoverTipCallBack descriptionHoverTip, @Nullable HoverTipCallBack upgradeTierTip){
        Player player = holder.player;
        var upgradeTab = LDLibUtil.createBaseRoot();
        Label pointDisplay = pointDisplay(player);
        UIElement information = LDLibUtil.createBaseRoot();

        int baseGap = 15;
        AtomicInteger firstElementWidth = new AtomicInteger(0);
        manager.getMap().forEach((key, pointPair) ->{
            UIElement element = LDLibUtil.rowElement().layout(layoutStyle -> layoutStyle.width(100));

            Label type = (Label) LDLibUtil.text(Component.literal(key))
                    .layout(layoutStyle -> layoutStyle.width(key.length() * 6 - 1))
                    .addEventListener(UIEvents.HOVER_TOOLTIPS, event -> {
                        if(showInformation){
                            descriptionHoverTip.accept(manager, key, event);
                        }else{
                            event.hoverTooltips = HoverTooltips.empty();
                        }
                    });

            if(firstElementWidth.get() == 0){
                firstElementWidth.set(key.length() * 6 - 1);
            }

            Button decrease = (Button) new Button().setText("-").setOnServerClick(event -> {
                int point = manager.getPoint(key);
                if(id.equals("basic")){
                    removeMekanismUpgrade(level,point);
                }
                int delta = 1;
                manager.updatePoint(key,Math.max(0,point - delta));

                addTotalPoint(player,point - manager.getPoint(key));

                if(id.equals("mekanism")){
                    removeMekanismUpgrade(level,point);
                    applyMekanismUpgrade(level);
                }
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

                int maxAdd = Math.min(delta,getTotalPoint(player));
                manager.updatePoint(key,Math.min(manager.getMax(key),point + maxAdd));

                addTotalPoint(player,-(manager.getPoint(key) - point));

                if(id.equals("mekanism") || id.equals("basic")){
                    removeMekanismUpgrade(level,getRange());
                    applyMekanismUpgrade(level);
                }
            }).layout(layoutStyle -> layoutStyle.setPosition(YogaEdge.TOP,-0.8f)).addEventListener(UIEvents.HOVER_TOOLTIPS,event -> {
                if(showInformation){
                    event.hoverTooltips = HoverTooltips.empty().append(Component.translatable("gui.cdp2.increase"));
                }else{
                    event.hoverTooltips = HoverTooltips.empty();
                }
            });

            Label how = (Label) LDLibUtil.text(Component.literal(String.valueOf(manager.getPoint(key))))
                    .bind(DataBindingBuilder.componentS2C(() -> Component.literal(String.valueOf(manager.getPoint(key)))).build())
                    .layout(layoutStyle -> layoutStyle.setPosition(YogaEdge.TOP,1.2f).width(String.valueOf(manager.getPoint(key)).length() * 5 - 1))
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


            float prefix = (key.length() * 6 - 1) - firstElementWidth.get();
            element.addChildren(type,LDLibUtil.widthFlex(baseGap-prefix),decrease,LDLibUtil.flex(2),how,LDLibUtil.flex(3.5f),increase);
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
            if(id.equals("basic")){
                int range = basic.getPoint("range");
                removeMekanismUpgrade(level,range);
            }
            addTotalPoint(player,manager.resetAll());

            if(id.equals("mekanism")){
                removeMekanismUpgrade(level,getRange());
            }
        }).addEventListener(UIEvents.HOVER_TOOLTIPS,event -> {
            event.hoverTooltips = HoverTooltips.empty().append(Component.translatable("gui.cdp2.reset"));
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

    private void creativeDebugButton(BlockUIMenuType.BlockUIHolder holder, UIElement root){
        if(holder.player.isCreative()){
            UIElement testTotalPoint = new UIElement().layout(layoutStyle -> layoutStyle
                    .flexDirection(YogaFlexDirection.ROW)
            );
            testTotalPoint.addChildren(new Button().setText("- point").setOnServerClick(event -> addTotalPoint(holder.player,-1)),LDLibUtil.flex(2),new Button().setText("+ point").setOnServerClick(event -> addTotalPoint(holder.player,1)));
            root.addChild(testTotalPoint);
        }
    }

    private Label pointDisplay(Player player){
        Label pointDisplay = (Label) new Label()
                .textStyle(textStyle -> textStyle.textAlignHorizontal(Horizontal.CENTER))
                .setText(Component.translatable("gui.cdp2.total_points",getTotalPoint(player)))
                .layout(layoutStyle -> layoutStyle.height(22).paddingAll(7).gapAll(5))
                .style(basicStyle -> basicStyle.background(Sprites.BORDER));
        pointDisplay.bind(DataBindingBuilder.componentS2C(() -> Component.translatable("gui.cdp2.total_points",getTotalPoint(player))).build());
        return pointDisplay;
    }

    private Button showRangeButton(){
        Button showRangeButton = (Button) new Button().setText(" ").setOnServerClick(event -> {
            showRange = !showRange;
        }).addEventListener(UIEvents.HOVER_TOOLTIPS,event -> {
            event.hoverTooltips = HoverTooltips.empty().append(Component.translatable("gui.cdp2." + (showRange ? "preview" : "hide")));
        });
        showRangeButton.layout(layoutStyle -> layoutStyle.width(13f));
        showRangeButton.buttonStyle(buttonStyle -> {
            buttonStyle.setDefault(PropertyRegistry.BASE_BACKGROUND, CDPSprites.RANGE_BUTTON);
            buttonStyle.setDefault(PropertyRegistry.HOVER_BACKGROUND, CDPSprites.RANGE_BUTTON_LIGHT);
            buttonStyle.setDefault(PropertyRegistry.PRESSED_BACKGROUND, CDPSprites.RANGE_BUTTON_DARK);
        });
        return showRangeButton;
    }

    //getter
    public boolean isShowRange() {
        return showRange && getRange() > 0;
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

    public void addTotalPoint(Player player, int delta){
        CompoundTag data = player.getPersistentData();
        if(data.contains("total_point")){
            int old = data.getInt("total_point");
            data.putInt("total_point",old + delta);
        }else{
            data.putInt("total_point",delta);
        }
    }

    public int getTotalPoint(Player player){
        CompoundTag data = player.getPersistentData();
        if(data.contains("total_point")){
            return data.getInt("total_point");
        }else{
            data.putInt("total_point",0);
            return 0;
        }
    }

    @Override
    public IManagedStorage getSyncStorage() {
        return syncStorage;
    }

    public void setOwner(UUID ownerUUID){
        this.ownerUUID = ownerUUID;
    }

    public UUID getOwner() {
        return ownerUUID;
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

    //interface
    @FunctionalInterface
    public interface HoverTipCallBack{
        void accept(UpgradePointManager manager,  String key, UIEvent event);
    }
}
