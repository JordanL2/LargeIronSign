package com.jordanl2.largeironsign;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import org.jetbrains.annotations.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.render.model.UnbakedModel;

import static net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier.OVERRIDE_PHASE;

@Environment(EnvType.CLIENT)
public class LargeIronSignModelLoadingPlugin implements ModelLoadingPlugin {
    
    public static final LargeIronSignUnbakedModel LARGE_IRON_SIGN_MODEL = new LargeIronSignUnbakedModel();
    
    @Override
    public void initialize(final Context pluginContext) {
        pluginContext.modifyModelOnLoad().register(OVERRIDE_PHASE, new ModelModifier.OnLoad() {
            @Override
            public @Nullable UnbakedModel modifyModelOnLoad(@Nullable UnbakedModel unbakedModel, Context context) {
                if (context.id().getNamespace().equals(LargeIronSign.MOD_ID)
                        && (context.id().getPath().equals(LargeIronSignBlock.BLOCK_PATH)
                        || context.id().getPath().equals(LargeIronSignBlock.ITEM_PATH))) {
                    return LARGE_IRON_SIGN_MODEL;
                }
                return unbakedModel;
            }
        });
    }
}