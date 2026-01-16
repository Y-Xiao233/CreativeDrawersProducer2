
package net.yxiao233.cdp2.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import org.jetbrains.annotations.NotNull;

public class CDPItemModelProvider extends ItemModelProvider {
    public CDPItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CreativeDrawersProducer2.MODID, existingFileHelper);
    }

    @Override
    public @NotNull String getName() {
        return CreativeDrawersProducer2.MODID + " - ItemModel";
    }

    @Override
    protected void registerModels() {
    }
}
