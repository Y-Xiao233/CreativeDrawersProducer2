package net.yxiao233.cdp2.common.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Unbreakable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VoidOmniToolItem extends TieredItem {
    public VoidOmniToolItem(Tier tier) {
        super(tier,new Properties()
                .stacksTo(1)
                .durability(-1)
                .component(DataComponents.UNBREAKABLE,new Unbreakable(true))
                .component(DataComponents.TOOL,createToolProperties(tier))
        );
    }

    private static Tool createToolProperties(Tier tier){
        return new Tool(List.of(
                Tool.Rule.deniesDrops(tier.getIncorrectBlocksForDrops()),
                Tool.Rule.minesAndDrops(BlockTags.MINEABLE_WITH_PICKAXE,tier.getSpeed()),
                Tool.Rule.minesAndDrops(BlockTags.MINEABLE_WITH_AXE,tier.getSpeed()),
                Tool.Rule.minesAndDrops(BlockTags.MINEABLE_WITH_SHOVEL,tier.getSpeed()),
                Tool.Rule.minesAndDrops(BlockTags.MINEABLE_WITH_HOE,tier.getSpeed())
        ),1.0F,1);
    }

    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        return true;
    }

    public void postHurtEnemy(ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        stack.hurtAndBreak(2, attacker, EquipmentSlot.MAINHAND);
    }
}
