package net.yxiao233.cdp2.common.integration.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.yxiao233.cdp2.api.recipe.ChanceIngredient;
import net.yxiao233.cdp2.api.recipe.ChanceItemStack;
import net.yxiao233.cdp2.common.integration.kubejs.event.CDPRegistryEvent;
import net.yxiao233.cdp2.common.integration.kubejs.schema.VoidSieveSchema;

public class CDPKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.namespace("cdp2")
                .register("void_sieve", VoidSieveSchema.SCHEMA);
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("ChanceIngredient", ChanceIngredient.class);
        bindings.add("ChanceItemStack", ChanceItemStack.class);
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(CDPRegistryEvent.GROUP);
    }
}
