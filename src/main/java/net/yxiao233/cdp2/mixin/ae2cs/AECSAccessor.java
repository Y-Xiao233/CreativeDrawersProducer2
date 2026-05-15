package net.yxiao233.cdp2.mixin.ae2cs;

import io.github.lounode.ae2cs.common.init.AECSItems;
import io.github.lounode.ae2cs.common.item.CrystalSeedItem;
import net.neoforged.neoforge.registries.DeferredItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(AECSItems.class)
public interface AECSAccessor {
    @Invoker("registerCrystalSeedItem")
    static DeferredItem<CrystalSeedItem> callRegistryCrystalSeedItem(String name, Supplier<CrystalSeedItem> sup) {
        throw new AssertionError();
    }
}
