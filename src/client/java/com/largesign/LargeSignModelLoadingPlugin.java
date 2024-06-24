package com.largesign;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelResolver;
import net.minecraft.client.render.model.UnbakedModel;

@Environment(EnvType.CLIENT)
public class LargeSignModelLoadingPlugin implements ModelLoadingPlugin {
	
	public static final LargeSignModel LARGE_SIGN_MODEL = new LargeSignModel();
	
    @Override
    public void onInitializeModelLoader(Context pluginContext) {
    	pluginContext.resolveModel().register(new ModelResolver() {
			@Override
			public @Nullable UnbakedModel resolveModel(Context context) {
	            if (context.id().getNamespace().equals(LargeSign.MOD_ID)
	            		&& (context.id().getPath().equals(LargeSignBlock.BLOCK_PATH)
	            		    || context.id().getPath().equals(LargeSignBlock.ITEM_PATH))) {
	                return LARGE_SIGN_MODEL;
	            }
	            return null;
			}
    	});
    }
}