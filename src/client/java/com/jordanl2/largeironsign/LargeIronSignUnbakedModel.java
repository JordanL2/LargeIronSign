package com.jordanl2.largeironsign;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import org.jetbrains.annotations.Nullable;


@Environment(EnvType.CLIENT)
public class LargeIronSignUnbakedModel implements UnbakedModel {
    
    // UnbakedModel methods
    
    @Override
    public void resolve(Resolver resolver) {
    }
    
    @Override
    public BakedModel bake(final ModelTextures textures, final Baker baker, final ModelBakeSettings settings,
                           final boolean ambientOcclusion, final boolean isSideLit, final ModelTransformation transformation) {
        return new LargeIronSignBakedModel(baker);
    }
    
    @Override
    public @Nullable Boolean getAmbientOcclusion() {
        return UnbakedModel.super.getAmbientOcclusion();
    }
    
    @Override
    public @Nullable GuiLight getGuiLight() {
        return UnbakedModel.super.getGuiLight();
    }
    
    @Override
    public ModelTransformation getTransformation() {
        return null;
    }
    
    @Override
    public ModelTextures.Textures getTextures() {
        return UnbakedModel.super.getTextures();
    }
    
    @Override
    public @Nullable UnbakedModel getParent() {
        return UnbakedModel.super.getParent();
    }
    
}