package com.smokeythebandicoot.deeperdark.mixins;

import com.rwtema.extrautils2.dimensions.DimensionEntry;
import com.rwtema.extrautils2.dimensions.XUWorldProvider;
import com.rwtema.extrautils2.dimensions.deep_dark.ChunkProviderDeepDark;
import com.rwtema.extrautils2.dimensions.deep_dark.WorldProviderDeepDark;
import com.smokeythebandicoot.deeperdark.DeeperDark;
import com.smokeythebandicoot.deeperdark.config.ModConfig;
import com.smokeythebandicoot.deeperdark.replacements.ChunkProviderDeeperDark;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = WorldProviderDeepDark.class, remap = false)
public abstract class WorldProviderDeepDarkMixin extends XUWorldProvider {

    @Shadow(remap = false)
    ChunkProviderDeepDark chunkProviderDeepDark;

    public WorldProviderDeepDarkMixin(DimensionEntry entry) {
        super(entry);
    }

    @Shadow
    public abstract long getSeed();

    @Inject(method = "createChunkGenerator()Lcom/rwtema/extrautils2/dimensions/deep_dark/ChunkProviderDeepDark;",
            at = @At("HEAD"),
            cancellable = true)
    private void createChunkGeneratorMixin(CallbackInfoReturnable<ChunkProviderDeepDark> cir) {
        if (ModConfig.ModConfiguration.masterSwitch) {
            chunkProviderDeepDark = new ChunkProviderDeeperDark(world, getSeed());
            cir.setReturnValue(chunkProviderDeepDark);
        }
    }

}
