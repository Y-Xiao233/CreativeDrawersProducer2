package net.yxiao233.cdp2.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.renderer.tooltip.CataclysmItemTooltipComponent;
import net.yxiao233.cdp2.common.integration.cataclysm.CDPCataclysmItems;
import net.yxiao233.cdp2.util.ReflectionUtil;

import java.util.List;
import java.util.Objects;

@SuppressWarnings({"removal","unused"})
@EventBusSubscriber(modid = CreativeDrawersProducer2.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CataclysmRenderGuiEvent {
    @SubscribeEvent
    public static void renderHUD(RenderGuiEvent.Post event){
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.options.hideGui || Objects.requireNonNull(minecraft.gameMode).getPlayerMode() == GameType.SPECTATOR){
            return;
        }
        ClientLevel level = minecraft.level;
        LocalPlayer player = minecraft.player;
        if(level == null || player == null){
            return;
        }
        if(minecraft.hitResult instanceof EntityHitResult entityHitResult){
            Entity entity = entityHitResult.getEntity();
            String key = CDPCataclysmItems.keyOf(entity.getType());
            if(key != null){
                renderHUDTooltip(minecraft,key,guiGraphics);
            }
        }else if(minecraft.hitResult instanceof BlockHitResult blockHitResult){
            BlockPos blockPos = blockHitResult.getBlockPos();
            Block block = level.getBlockState(blockPos).getBlock();
            String key = CDPCataclysmItems.DOOR_OF_SEAL;
            if(BuiltInRegistries.BLOCK.getKey(block).toString().equals(key)){
                renderHUDTooltip(minecraft,key,guiGraphics);
            }
        }
    }

    private static void renderHUDTooltip(Minecraft minecraft, String key, GuiGraphics guiGraphics){
        if(minecraft.screen != null){
            return;
        }
        int x = minecraft.getWindow().getGuiScaledWidth() / 2;
        int y = minecraft.getWindow().getGuiScaledHeight() / 2;
        Item item = CDPCataclysmItems.get(key);
        if(item != null){
            List<ClientTooltipComponent> lines = List.of(
                    ClientTooltipComponent.create(new CataclysmItemTooltipComponent(item.getDefaultInstance())),
                    ClientTooltipComponent.create(Component.translatable("tooltip.cdp2.need_item_activate",item.getDescription().getString()).getVisualOrderText())
            );
            ReflectionUtil.runPrivateMethod("renderTooltipInternal",
                    new Class[]{Font.class,List.class,int.class,int.class,ClientTooltipPositioner.class},
                    guiGraphics, GuiGraphics.class,
                    new Object[]{minecraft.font,lines,x + 10,y, DefaultTooltipPositioner.INSTANCE},
                    null
            );
        }
    }
}
