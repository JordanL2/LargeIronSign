package com.largesign;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;

@Environment(EnvType.CLIENT)
public class LargeSignModelLoadingPlugin implements ModelLoadingPlugin {
	
    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        pluginContext.modifyModelOnLoad().register((original, context) -> {
            if (context.id().getNamespace().equals(LargeSignBlock.ID.getNamespace())
            		&& context.id().getPath().equals(LargeSignBlock.ID.getPath())) {
                return new LargeSignModel();
            } else {
            	return original;
            }
        });
    }
}