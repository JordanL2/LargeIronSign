package com.jordanl2.largeironsign;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

import static net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier.OVERRIDE_PHASE;

@Environment(EnvType.CLIENT)
public class LargeIronSignModelLoadingPlugin implements ModelLoadingPlugin {
    
    public LargeIronSignBlockStateModel LARGE_IRON_SIGN_MODEL;
    
    @Override
    public void initialize(final Context pluginContext) {
        pluginContext.modifyBlockModelAfterBake().register(OVERRIDE_PHASE, (blockStateModel, context) -> {
            if (context.state().getBlock() instanceof LargeIronSignBlock) {
                if (LARGE_IRON_SIGN_MODEL == null) {
                    LARGE_IRON_SIGN_MODEL = new LargeIronSignBlockStateModel(context.baker());
                }
                return new LargeIronSignBlockStateModel(context.baker());
            }
            return blockStateModel;
        });
    }
}