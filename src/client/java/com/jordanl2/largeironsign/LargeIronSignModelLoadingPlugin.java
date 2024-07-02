package com.jordanl2.largeironsign;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelResolver;
import net.minecraft.client.render.model.UnbakedModel;

@Environment(EnvType.CLIENT)
public class LargeIronSignModelLoadingPlugin implements ModelLoadingPlugin {
    
    public static final LargeIronSignModel LARGE_IRON_SIGN_MODEL = new LargeIronSignModel();
    
    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        pluginContext.resolveModel().register(new ModelResolver() {
            @Override
            public @Nullable UnbakedModel resolveModel(Context context) {
                if (context.id().getNamespace().equals(LargeIronSign.MOD_ID)
                        && (context.id().getPath().equals(LargeIronSignBlock.BLOCK_PATH)
                        || context.id().getPath().equals(LargeIronSignBlock.ITEM_PATH))) {
                    return LARGE_IRON_SIGN_MODEL;
                }
                return null;
            }
        });
    }
}