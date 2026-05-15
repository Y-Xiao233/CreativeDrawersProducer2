package net.yxiao233.cdp2.mixin.arsnouveau;

import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.yxiao233.cdp2.common.integration.arsnouveau.StructureRitualDefinition;
import net.yxiao233.cdp2.common.integration.kubejs.event.CDPRegistryEvent;
import net.yxiao233.cdp2.common.integration.kubejs.event.RegistryDefinitionEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemsRegistry.class)
public class ItemRegistryMixin {

    @Inject(method = "onItemRegistry", at = @At("HEAD"))
    private static void cdp2$onItemRegistry(RegisterEvent.RegisterHelper<Item> helper, CallbackInfo ci){
        var event = new RegistryDefinitionEvent();
        CDPRegistryEvent.REGISTRY.post(ScriptType.STARTUP, event);
        StructureRitualDefinition.registryAll();
    }
}
