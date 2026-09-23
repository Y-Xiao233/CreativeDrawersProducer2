package net.yxiao233.cdp2.common.integration.ae2ltpp;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class CDPPackagedCoreBakedModel implements BakedModel {
    private final BakedModel delegate;

    public CDPPackagedCoreBakedModel(BakedModel delegate) {
        this.delegate = delegate;
    }

    @SuppressWarnings("deprecation")
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource random) {
        return this.delegate.getQuads(state, side, random);
    }

    public boolean useAmbientOcclusion() {
        return this.delegate.useAmbientOcclusion();
    }

    public boolean isGui3d() {
        return false;
    }

    public boolean usesBlockLight() {
        return this.delegate.usesBlockLight();
    }

    public boolean isCustomRenderer() {
        return true;
    }

    @SuppressWarnings("deprecation")
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return this.delegate.getParticleIcon();
    }

    @SuppressWarnings("deprecation")
    public @NotNull ItemTransforms getTransforms() {
        return this.delegate.getTransforms();
    }

    public @NotNull ItemOverrides getOverrides() {
        return this.delegate.getOverrides();
    }
}
