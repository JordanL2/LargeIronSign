package com.largesign;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

@Environment(EnvType.CLIENT)
public class LargeSignModel implements UnbakedModel, BakedModel, FabricBakedModel {
	
	private final Sprite[] sprites = new Sprite[LargeSignCharacter.values().length];
	
	private final static DirectionUtil directionUtil = new DirectionUtil();
	

	// UnbakedModel methods

	@Override
	public Collection<Identifier> getModelDependencies() {
		return List.of();
	}

	@Override
	public void setParents(Function<Identifier, UnbakedModel> var1) {
	}

	@Override
	public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, 
						   ModelBakeSettings rotationContainer, Identifier modelId) {

		for (LargeSignCharacter character : LargeSignCharacter.values()) {
			SpriteIdentifier spriteId = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
						character.getBlockIdentifier());
			sprites[character.ordinal()] = textureGetter.apply(spriteId);
		}
		
		return this;
	}


	// BakedModel methods
	
	@Override
	public List<BakedQuad> getQuads(BlockState var1, Direction var2, Random var3) {
		return List.of();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean hasDepth() {
		return false;
	}

	@Override
	public boolean isSideLit() {
		return false;
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public Sprite getParticleSprite() {
		return null;
	}

	@Override
	public ModelTransformation getTransformation() {
		return null;
	}

	@Override
	public ModelOverrideList getOverrides() {
		return null;
	}
		
	
	// FabricBakedModel methods
		
	@Override
    public boolean isVanillaAdapter() {
        return false; // False to trigger FabricBakedModel rendering
    }
	
	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		QuadEmitter emitter = context.getEmitter();
		
		Direction direction = state.get(LargeSignBlock.FACING);
		LargeSignCharacter character = state.get(LargeSignBlock.CHAR);
		
		// Front
		emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
		emitter.spriteBake(sprites[character.ordinal()], MutableQuadView.BAKE_LOCK_UV);
		emitter.color(-1, -1, -1, -1);
		emitter.emit();

		// Back
		emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R180), 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
		emitter.spriteBake(sprites[LargeSignCharacter.SPACE.ordinal()], MutableQuadView.BAKE_LOCK_UV);
		emitter.color(-1, -1, -1, -1);
		emitter.emit();
	}
	 
    @Override
    public void emitItemQuads(ItemStack itemStack, Supplier<Random> supplier, RenderContext renderContext) {
    }
    
}