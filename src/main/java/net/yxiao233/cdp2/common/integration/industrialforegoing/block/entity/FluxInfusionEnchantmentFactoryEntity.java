package net.yxiao233.cdp2.common.integration.industrialforegoing.block.entity;

import com.buuz135.industrial.block.tile.IndustrialProcessingTile;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.client.screen.addon.BasicScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.ISyncPersistRPCBlockEntity;
import com.lowdragmc.lowdraglib2.syncdata.storage.FieldManagedStorage;
import com.lowdragmc.lowdraglib2.syncdata.storage.IManagedStorage;
import dev.shadowsoffire.apothic_enchanting.table.EnchantmentTableStats;
import dev.shadowsoffire.apothic_enchanting.table.infusion.InfusionRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.yxiao233.cdp2.client.gui.AllGuiTextures;
import net.yxiao233.cdp2.client.gui.ScreenElement;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import org.jetbrains.annotations.NotNull;

public class FluxInfusionEnchantmentFactoryEntity extends IndustrialProcessingTile<FluxInfusionEnchantmentFactoryEntity> implements ISyncPersistRPCBlockEntity {
    private final FieldManagedStorage fieldManagedStorage = new FieldManagedStorage(this);
    @Save
    private SidedInventoryComponent<FluxInfusionEnchantmentFactoryEntity> input;
    @Save
    private SidedInventoryComponent<FluxInfusionEnchantmentFactoryEntity> output;
    private EnchantmentTableStats enchantmentTableStats;
    @DescSynced
    private float eterna;
    @DescSynced
    private float quanta;
    @DescSynced
    private float arcana;
    private InfusionRecipe recipe;
    public FluxInfusionEnchantmentFactoryEntity(BlockPos blockPos, BlockState blockState) {
        super(CDPBlock.FLUX_INFUSION_ENCHANTMENT_FACTORY,80,34,blockPos, blockState);
        this.addInventory(this.input = (SidedInventoryComponent<FluxInfusionEnchantmentFactoryEntity>) new SidedInventoryComponent<FluxInfusionEnchantmentFactoryEntity>("input", 50, 34, 1, 0)
                .setColor(DyeColor.BLUE)
                .setOnSlotChanged((stack, integer) -> checkForRecipe())
                .setInputFilter((stack, integer) -> canEnchantItem(stack))
                .setOutputFilter((stack, integer) -> false)
                .setComponentHarness(this)
        );
        this.addInventory(this.output = (SidedInventoryComponent<FluxInfusionEnchantmentFactoryEntity>) new SidedInventoryComponent<FluxInfusionEnchantmentFactoryEntity>("output", 115, 34, 1, 1)
                .setColor(DyeColor.ORANGE)
                .setInputFilter((stack, integer) -> false)
                .setComponentHarness(this)
        );
    }

    public boolean canEnchantItem(ItemStack stack) {
        return InfusionRecipe.findItemMatch(this.getLevel(),stack) != null;
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void initClient() {
        super.initClient();
        this.addGuiAddonFactory(() ->{
            return new InfusionEnchantmentProgressComponent(36,56, Component.translatable("gui.cdp2.eterna"), AllGuiTextures.ETERNA, AllGuiTextures.ETERNA_BORDER){
                @Override
                public float getLevel() {
                    return FluxInfusionEnchantmentFactoryEntity.this.eterna;
                }
            };
        });

        this.addGuiAddonFactory(() ->{
            return new InfusionEnchantmentProgressComponent(36,65, Component.translatable("gui.cdp2.quanta"), AllGuiTextures.QUANTA, AllGuiTextures.QUANTA_BORDER){
                @Override
                public float getLevel() {
                    return FluxInfusionEnchantmentFactoryEntity.this.quanta;
                }
            };
        });

        this.addGuiAddonFactory(() ->{
            return new InfusionEnchantmentProgressComponent(36,74, Component.translatable("gui.cdp2.arcana"), AllGuiTextures.ARCANA, AllGuiTextures.ARCANA_BORDER){
                @Override
                public float getLevel() {
                    return FluxInfusionEnchantmentFactoryEntity.this.arcana;
                }
            };
        });
    }

    @Override
    @SuppressWarnings("deprecation")
    public void serverTick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull FluxInfusionEnchantmentFactoryEntity blockEntity) {
        super.serverTick(level, pos, state, blockEntity);
        ItemStack input = this.input.getStackInSlot(0);
        this.enchantmentTableStats = EnchantmentTableStats.gatherStats(this.getLevel(), this.getBlockPos(), input.isEmpty() ? 0 : input.getEnchantmentValue());
        this.eterna = this.enchantmentTableStats.eterna();
        this.quanta = this.enchantmentTableStats.quanta();
        this.arcana = this.enchantmentTableStats.arcana();
        checkForRecipe();
    }

    public void checkForRecipe(){
        if(this.level != null && isServer()){
            ItemStack input = this.input.getStackInSlot(0);
            if(recipe != null && recipe.matches(input,eterna,quanta,arcana)){
                return;
            }

            recipe = InfusionRecipe.findMatch(this.getLevel(),input,eterna,quanta,arcana);
        }
    }

    @Override
    public boolean canIncrease() {
        if(recipe != null){
            ItemStack stackInSlot = this.output.getStackInSlot(0);
            ItemStack output = recipe.getOutput();
            return output.isEmpty() || stackInSlot.isEmpty() || (ItemStack.isSameItemSameComponents(stackInSlot,output) && stackInSlot.getCount() + output.getCount() <= stackInSlot.getMaxStackSize());
        }
        return false;
    }

    @Override
    public Runnable onFinish() {
        return () ->{
            InfusionRecipe infusionRecipe = recipe;
            ItemHandlerHelper.insertItem(this.output,infusionRecipe.getOutput().copy(),false);
            this.input.getStackInSlot(0).shrink(1);
            this.checkForRecipe();
        };
    }

    @Override
    public void setChanged() {
        super.setChanged();
        checkForRecipe();
    }

    @Override
    protected int getTickPower() {
        if(recipe != null){
            return (int) (recipe.getRequirements().eterna() * 10000);
        }
        return 10000;
    }

    @Override
    public @NotNull FluxInfusionEnchantmentFactoryEntity getSelf() {
        return this;
    }

    @Override
    protected @NotNull EnergyStorageComponent<FluxInfusionEnchantmentFactoryEntity> createEnergyStorage() {
        return new EnergyStorageComponent<>(10000000, 4, 14);
    }

    @Override
    public void saveSettings(Player player, CompoundTag tag) {
        super.saveSettings(player, tag);
        tag.putFloat("eterna",this.eterna);
        tag.putFloat("quanta",this.quanta);
        tag.putFloat("arcana",this.arcana);
    }

    @Override
    public void loadSettings(Player player, CompoundTag tag) {
        super.loadSettings(player, tag);
        this.eterna = tag.getFloat("eterna");
        this.quanta = tag.getFloat("quanta");
        this.arcana = tag.getFloat("arcana");
    }

    @Override
    public IManagedStorage getSyncStorage() {
        return fieldManagedStorage;
    }


    public static abstract class InfusionEnchantmentProgressComponent extends BasicScreenAddon{
        private final AllGuiTextures texture;
        private final AllGuiTextures borderTexture;
        private final Component desc;
        protected InfusionEnchantmentProgressComponent(int posX, int posY, Component desc, AllGuiTextures texture, AllGuiTextures borderTexture) {
            super(posX, posY);
            this.texture = texture;
            this.borderTexture = borderTexture;
            this.desc = desc;
        }

        @Override
        public int getXSize() {
            return texture.getWidth();
        }

        @Override
        public int getYSize() {
            return texture.getHeight();
        }

        @Override
        public void drawBackgroundLayer(GuiGraphics guiGraphics, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
            texture.render(guiGraphics,guiX+getPosX(),guiY+getPosY());
            int width = (int) Math.round(borderTexture.getWidth() * (getLevel() / 100.0));
            borderTexture.render(guiGraphics,guiX+getPosX(),guiY+getPosY(),width, ScreenElement.ExtraType.WIDTH);
            renderTooltip(guiGraphics,guiX,guiY,mouseX,mouseY);
        }

        @Override
        public void drawForegroundLayer(GuiGraphics guiGraphics, Screen screen, IAssetProvider iAssetProvider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        }

        public void renderTooltip(GuiGraphics guiGraphics, int guiX, int guiY, int mouseX, int mouseY){
            if(isMouseOver(guiX,guiY,mouseX,mouseY)){
                guiGraphics.renderTooltip(Minecraft.getInstance().font,Component.literal(desc.getString() + ": " + getLevel()),mouseX,mouseY);
            }
        }
        public boolean isMouseOver(int guiX, int guiY, double mouseX, double mouseY) {
            return mouseX >= (double)this.getPosX() + guiX && mouseX <= (double)(this.getPosX() + guiX + this.getXSize()) && mouseY >= (double)this.getPosY() + guiY && mouseY <= (double)(this.getPosY() + guiY + this.getYSize());
        }
        public abstract float getLevel();
    }
}
