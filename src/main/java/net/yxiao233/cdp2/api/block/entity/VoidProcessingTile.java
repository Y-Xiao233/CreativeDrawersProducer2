package net.yxiao233.cdp2.api.block.entity;

import com.buuz135.industrial.item.addon.ProcessingAddonItem;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.augment.AugmentTypes;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.block.redstone.RedstoneAction;
import com.hrznstudio.titanium.client.screen.addon.ProgressBarScreenAddon;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.item.AugmentWrapper;
import com.hrznstudio.titanium.module.BlockWithTile;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.ISyncPersistRPCBlockEntity;
import com.lowdragmc.lowdraglib2.syncdata.storage.FieldManagedStorage;
import com.lowdragmc.lowdraglib2.syncdata.storage.IManagedStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.yxiao233.cdp2.common.registry.CDPItem;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public abstract class VoidProcessingTile<T extends VoidProcessingTile<T>> extends RecipeTile<T>  implements ISyncPersistRPCBlockEntity {
    private final FieldManagedStorage storage = new FieldManagedStorage(this);
    @Save
    private ProgressBarComponent<T> progressBar;
    @Save
    private SidedInventoryComponent<T> voidInput;
    @Save
    private ProgressBarComponent<T> voidBar;
    @DescSynced
    public int progress;
    @DescSynced
    public int voidMatterCount;

    public VoidProcessingTile(BlockWithTile basicTileBlock, final int x, final int y, BlockPos blockPos, BlockState blockState) {
        super(basicTileBlock, blockPos, blockState);

        super.addInventory(this.voidInput = (SidedInventoryComponent<T>) new SidedInventoryComponent<T>("void_input",10,64,1,0).setColor(DyeColor.YELLOW)
                .setInputFilter((stack, integer) -> {
                    return stack.is(CDPItem.VOID_MATTER.asItem());
                })
                .setOutputFilter((stack, integer) -> {
                    return false;
                }).setComponentHarness(this.getSelf())
        );

        this.addProgressBar(this.voidBar = (new ProgressBarComponent<T>(13, 4, 1000) {
            @OnlyIn(Dist.CLIENT)
            public @NotNull List<IFactory<? extends IScreenAddon>> getScreenAddons() {
                return Collections.singletonList(() -> {
                    return new ProgressBarScreenAddon<>(VoidProcessingTile.this.voidBar.getPosX(), VoidProcessingTile.this.voidBar.getPosY(), this) {
                        public List<Component> getTooltipLines() {
                            Component[] components = new Component[1];
                            ChatFormatting formatting = ChatFormatting.GOLD;
                            components[0] = Component.literal(formatting + "Matter: " + ChatFormatting.WHITE + NumberFormat.getNumberInstance(Locale.ROOT).format((long) VoidProcessingTile.this.voidBar.getProgress()) + ChatFormatting.GOLD + "/" + ChatFormatting.WHITE + NumberFormat.getNumberInstance(Locale.ROOT).format((long) VoidProcessingTile.this.voidBar.getMaxProgress()));
                            return Arrays.asList(components);
                        }
                    };
                });
            }
        }).setCanIncrease((tileEntity) -> {
            if(this.voidInput.getStackInSlot(0).isEmpty()){
                this.voidMatterCount = this.voidBar.getProgress();
                return false;
            }
            return true;
        }).setCanReset((tileEntity) -> {
            return false;
        }).setOnTickWork(() ->{
            increaseBar();
            this.voidMatterCount = this.voidBar.getProgress();
        }).setColor(DyeColor.PURPLE));

        this.addProgressBar(this.progressBar = (new ProgressBarComponent<T>(x, y, this.getMaxProgress()) {
            @OnlyIn(Dist.CLIENT)
            public @NotNull List<IFactory<? extends IScreenAddon>> getScreenAddons() {
                return Collections.singletonList(() -> {
                    return new ProgressBarScreenAddon<>(x, y, VoidProcessingTile.this.progressBar) {
                        public List<Component> getTooltipLines() {
                            List<Component> tooltip = new ArrayList<>();
                            String gold = String.valueOf(ChatFormatting.GOLD);
                            tooltip.add(Component.literal(gold + Component.translatable("tooltip.titanium.progressbar.progress").getString() + String.valueOf(ChatFormatting.WHITE) + (new DecimalFormat()).format((long)VoidProcessingTile.this.progressBar.getProgress()) + String.valueOf(ChatFormatting.GOLD) + "/" + String.valueOf(ChatFormatting.WHITE) + (new DecimalFormat()).format((long)VoidProcessingTile.this.progressBar.getMaxProgress())));
                            int progress = VoidProcessingTile.this.progressBar.getMaxProgress() - VoidProcessingTile.this.progressBar.getProgress();
                            if (!VoidProcessingTile.this.progressBar.getIncreaseType()) {
                                progress = VoidProcessingTile.this.progressBar.getMaxProgress() - progress;
                            }

                            gold = String.valueOf(ChatFormatting.GOLD);
                            tooltip.add(Component.literal(gold + Component.translatable("tooltip.industrialforegoing.eta").getString() + String.valueOf(ChatFormatting.WHITE) + (new DecimalFormat()).format(Math.ceil((double)(progress * VoidProcessingTile.this.progressBar.getTickingTime()) / 20.0 / (double)VoidProcessingTile.this.progressBar.getProgressIncrease())) + String.valueOf(ChatFormatting.DARK_AQUA) + Component.translatable("tooltip.industrialforegoing.sec_short").getString()));
                            gold = String.valueOf(ChatFormatting.GOLD);
                            tooltip.add(Component.literal(gold + Component.translatable("tooltip.industrialforegoing.usage").getString() + String.valueOf(ChatFormatting.WHITE) + VoidProcessingTile.this.getTickMatter() + String.valueOf(ChatFormatting.DARK_AQUA) + " Matter" + String.valueOf(ChatFormatting.GOLD) + "/" + String.valueOf(ChatFormatting.WHITE) + String.valueOf(ChatFormatting.DARK_AQUA) + "t"));
                            return tooltip;
                        }
                    };
                });
            }
        }).setComponentHarness(this.getSelf()).setBarDirection(this.getBarDirection()).setCanReset((tileEntity) -> {
            return true;
        }).setOnStart(() -> {
            int maxProgress = (int)Math.floor((double)((float)this.getMaxProgress() * (this.hasAugmentInstalled(AugmentTypes.EFFICIENCY) ? AugmentWrapper.getType((ItemStack)this.getInstalledAugments(AugmentTypes.EFFICIENCY).get(0), AugmentTypes.EFFICIENCY) : 1.0F)));
            this.progressBar.setMaxProgress(maxProgress);
        }).setCanIncrease((tileEntity) -> {
            return this.getVoidBar().getProgress() >= this.getTickMatter() && this.canIncrease() && ((RedstoneAction)this.getRedstoneManager().getAction()).canRun(tileEntity.getEnvironmentValue(false, (Direction)null)) && this.getRedstoneManager().shouldWork();
        }).setOnTickWork(() -> {
            this.getVoidBar().setProgress(this.getVoidBar().getProgress() - this.getTickMatter());
            this.progressBar.setProgressIncrease(this.hasAugmentInstalled(AugmentTypes.SPEED) ? (int)AugmentWrapper.getType((ItemStack)this.getInstalledAugments(AugmentTypes.SPEED).get(0), AugmentTypes.SPEED) : 1);
            this.progress = this.progressBar.getProgress();
        }).setOnFinishWork(() -> {
            int operations = (int)(this.hasAugmentInstalled(ProcessingAddonItem.PROCESSING) ? AugmentWrapper.getType((ItemStack)this.getInstalledAugments(ProcessingAddonItem.PROCESSING).get(0), ProcessingAddonItem.PROCESSING) : 1.0F);

            for(int i = 0; i < operations; ++i) {
                if (this.canIncrease()) {
                    this.onFinish().run();
                }
            }

            this.getRedstoneManager().finish();
        }));

        this.setShowEnergy(false);
    }

    public ProgressBarComponent<T> getProgressBar() {
        return this.progressBar;
    }
    public ProgressBarComponent<T> getVoidBar(){
        return this.voidBar;
    }

    public SidedInventoryComponent<T> getVoidInput(){
        return this.voidInput;
    }

    public ItemInteractionResult onActivated(@NotNull Player playerIn, @NotNull InteractionHand hand, @NotNull Direction facing, double hitX, double hitY, double hitZ) {
        if (super.onActivated(playerIn, hand, facing, hitX, hitY, hitZ) == ItemInteractionResult.SUCCESS) {
            return ItemInteractionResult.SUCCESS;
        } else {
            this.openGui(playerIn);
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
    }

//    @Override
//    public void serverTick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull T blockEntity) {
//        super.serverTick(level, pos, state, blockEntity);
//        increaseBar();
//        this.voidMatterCount = this.voidBar.getProgress();
//    }

    public int getMaxProgress() {
        return 100;
    }

    public abstract boolean canIncrease();

    public abstract Runnable onFinish();

    protected abstract int getTickMatter();
    public void increaseBar(){
        if(this.voidBar.getProgress() >= this.voidBar.getMaxProgress() || this.voidInput.getStackInSlot(0).isEmpty()){
            return;
        }
        int remain = this.voidBar.getMaxProgress() - this.voidBar.getProgress();
        int times = Math.min(getVoidMatterPerOperationMaxValue(),remain / getEachVoidMatterValue());
        this.voidBar.setProgress(this.voidBar.getProgress() + times * getEachVoidMatterValue());
        this.voidInput.getStackInSlot(0).shrink(times);
    }
    public int getVoidMatterPerOperationMaxValue(){
        return 8;
    }

    public int getEachVoidMatterValue(){
        return 1;
    }

    public ProgressBarComponent.BarDirection getBarDirection() {
        return ProgressBarComponent.BarDirection.ARROW_RIGHT;
    }

    @NotNull
    @Override
    protected EnergyStorageComponent<T> createEnergyStorage() {
        return new EnergyStorageComponent<>(0, 0, 0);
    }

    @Override
    public IManagedStorage getSyncStorage() {
        return storage;
    }
}
