package net.yxiao233.cdp2.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.integration.botany_pot.BotanyPotJei;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class CDPJeiPlugin implements IModPlugin {
    private static IJeiRuntime runtime;
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return CreativeDrawersProducer2.makeId("jei");
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        IModPlugin.super.onRuntimeAvailable(jeiRuntime);
        runtime = jeiRuntime;
    }

    public static IJeiRuntime getRuntime(){
        return runtime;
    }


    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        IModPlugin.super.registerRecipeCatalysts(registration);
        BotanyPotJei.registerRecipeCatalysts(registration);
    }
}
