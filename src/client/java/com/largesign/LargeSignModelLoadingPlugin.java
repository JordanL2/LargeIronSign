package com.largesign;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;

@Environment(EnvType.CLIENT)
public class LargeSignModelLoadingPlugin implements ModelLoadingPlugin {
	
    public static final ModelIdentifier LARGE_SIGN_MODEL = new ModelIdentifier(
    		LargeSign.MOD_ID, "large_sign", "");
 
    @Override
    public void onInitializeModelLoader(Context pluginContext) {
    	System.out.println("JORDAN pluginContext register");
        // We want to add our model when the models are loaded
        pluginContext.modifyModelOnLoad().register((original, context) -> {
            // This is called for every model that is loaded, so make sure we only target ours
            if (context.id().getNamespace().equals(LargeSign.MOD_ID)
            		&& context.id().getPath().equals("large_sign")) {
            	//System.out.println("JORDAN return new LargeSignModel");
                return new LargeSignModel();
            } else {
                // If we don't modify the model we just return the original as-is
            	//System.out.println("JORDAN return original");
            	return original;
            }
        });
    }
}