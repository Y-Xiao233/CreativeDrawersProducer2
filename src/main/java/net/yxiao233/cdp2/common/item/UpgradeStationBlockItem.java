package net.yxiao233.cdp2.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.yxiao233.cdp2.common.registry.CDPDataComponentTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class UpgradeStationBlockItem extends BlockItem {
    public UpgradeStationBlockItem(Supplier<Block> blockSupplier, Properties properties) {
        super(blockSupplier.get(), properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltips, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltips, tooltipFlag);
        if(stack.has(CDPDataComponentTypes.COMPOUND_TAG)){
            CompoundTag tag = stack.getOrDefault(CDPDataComponentTypes.COMPOUND_TAG, new CompoundTag());
            if(tag.contains("entity_data")){
                CompoundTag entityData = tag.getCompound("entity_data");
                if(entityData.contains("managed")){
                    CompoundTag managed = entityData.getCompound("managed");
                    managed.getAllKeys().forEach(key ->{
                        if(managed.contains(key,10)){
                            tooltips.add(Component.literal(key + ":").withStyle(ChatFormatting.GREEN));
                            CompoundTag typed = managed.getCompound(key);
                            typed.getAllKeys().forEach(content ->{
                                tooltips.add(Component.literal("   ").append(Component.literal(content + ": ").withStyle(ChatFormatting.AQUA)).append(Component.literal(String.valueOf(typed.getCompound(content).getInt("point"))).withStyle(ChatFormatting.WHITE)));
                            });
                        }else if(managed.contains(key,99)){
                            int number = managed.getInt(key);
                        }
                    });
                }
            }
        }
    }
}
