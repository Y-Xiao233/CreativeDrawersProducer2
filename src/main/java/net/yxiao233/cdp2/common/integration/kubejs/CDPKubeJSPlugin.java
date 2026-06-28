package net.yxiao233.cdp2.common.integration.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.minecraft.core.registries.Registries;
import net.yxiao233.cdp2.api.recipe.ChanceIngredient;
import net.yxiao233.cdp2.api.recipe.ChanceItemStack;
import net.yxiao233.cdp2.common.integration.kubejs.event.CDPModifyEvent;
import net.yxiao233.cdp2.common.integration.kubejs.event.CDPRegistryEvent;
import net.yxiao233.cdp2.common.integration.kubejs.item.FortuneAddonItemBuilder;
import net.yxiao233.cdp2.common.integration.kubejs.schema.ChemicalFromCellInfoSchema;
import net.yxiao233.cdp2.common.integration.kubejs.schema.VoidSieveSchema;

public class CDPKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.namespace("cdp2")
                .register("void_sieve", VoidSieveSchema.SCHEMA)
                .register("chemical_from_cell_info", ChemicalFromCellInfoSchema.SCHEMA);
    }

    @Override
    @SuppressWarnings("removal")
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.ITEM, reg ->{
            reg.add("ifs:fortune_addon", FortuneAddonItemBuilder.class,FortuneAddonItemBuilder::new);
        });
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("ChanceIngredient", ChanceIngredient.class);
        bindings.add("ChanceItemStack", ChanceItemStack.class);
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(CDPRegistryEvent.GROUP);
        registry.register(CDPModifyEvent.GROUP);
    }
}
