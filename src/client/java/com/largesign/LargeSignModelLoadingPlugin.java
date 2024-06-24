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
				//System.out.println("JORDAN resolving: " + context.id());
	            if (context.id().getNamespace().equals(LargeSignBlock.ID.getNamespace())
	            		&& context.id().getPath().equals("item/" + LargeSignBlock.ID.getPath())) {
	            	//System.out.println("JORDAN resolve: " + context.id());
	                return LARGE_SIGN_MODEL;
	            }
	            return null;
			}
    	});
        
    	pluginContext.modifyModelOnLoad().register((original, context) -> {
            if (context.id().getNamespace().equals(LargeSignBlock.ID.getNamespace())
            		&& (context.id().getPath().equals(LargeSignBlock.ID.getPath())
            				)) {
            		 //|| context.id().getPath().equals(LargeSignBlock.ID.getPath()))) {
            	//System.out.println("JORDAN onload: " + context.id());
                return LARGE_SIGN_MODEL;
            } else {
            	return original;
            }
        });
    }
}