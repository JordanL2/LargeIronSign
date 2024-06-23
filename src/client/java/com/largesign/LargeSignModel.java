package com.largesign;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.joml.Vector3f;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockview.v2.FabricBlockView;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockRenderView;

@Environment(EnvType.CLIENT)
public class LargeSignModel implements UnbakedModel, BakedModel, FabricBakedModel {
	
	private final Sprite[] sprites = new Sprite[LargeSignCharacter.values().length];
	private Sprite spriteEdge;
	
	private final static DirectionUtil directionUtil = new DirectionUtil();
	
	ModelTransformation transformation;
	

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
		// Make model transformation
		Transformation gui = new Transformation(
				new Vector3f(ModelHelper.MODEL_TRANSFORM_BLOCK.gui.rotation),
				new Vector3f(ModelHelper.MODEL_TRANSFORM_BLOCK.gui.translation),
				new Vector3f(ModelHelper.MODEL_TRANSFORM_BLOCK.gui.scale));
		transformation = new ModelTransformation(
				ModelHelper.MODEL_TRANSFORM_BLOCK.thirdPersonLeftHand,
				ModelHelper.MODEL_TRANSFORM_BLOCK.thirdPersonRightHand,
				ModelHelper.MODEL_TRANSFORM_BLOCK.firstPersonLeftHand,
				ModelHelper.MODEL_TRANSFORM_BLOCK.firstPersonRightHand,
				ModelHelper.MODEL_TRANSFORM_BLOCK.head,
				gui,
				ModelHelper.MODEL_TRANSFORM_BLOCK.ground,
				ModelHelper.MODEL_TRANSFORM_BLOCK.fixed);
		transformation.gui.translation.add(0.2f, -0.1f, 0);

		// Load sprites
		for (LargeSignCharacter character : LargeSignCharacter.values()) {
			SpriteIdentifier spriteId = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
						character.getBlockIdentifier());
			sprites[character.ordinal()] = textureGetter.apply(spriteId);
		}
		spriteEdge = textureGetter.apply(
				new SpriteIdentifier(
						SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, 
						new Identifier(LargeSign.MOD_ID, "block/large_sign_edge")));
		
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
		return true;
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public Sprite getParticleSprite() {
		return sprites[LargeSignCharacter.SPACE.ordinal()];
	}

	@Override
	public ModelTransformation getTransformation() {
		return transformation;
	}

	@Override
	public ModelOverrideList getOverrides() {
		return ModelOverrideList.EMPTY;
	}
		
	
	// FabricBakedModel methods
		
	@Override
    public boolean isVanillaAdapter() {
        return false; // False to trigger FabricBakedModel rendering
    }
	
	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		Direction direction = state.get(LargeSignBlock.FACING);
		LargeSignBlockEntity entityState = (LargeSignBlockEntity) 
				((FabricBlockView)blockView).getBlockEntityRenderData(pos);
		LargeSignCharacter character = entityState.character;
		System.out.println("JORDAN rendering char " + character.getDescription() + " at " + pos.toShortString());
		Mesh mesh = buildMesh(direction, character);
		mesh.outputTo(context.getEmitter());
	}
	
	private Mesh buildMesh(Direction direction, LargeSignCharacter character) {
		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		MeshBuilder builder = renderer.meshBuilder();
		QuadEmitter emitter = builder.getEmitter();
		
		// Front
		emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.9375f);
		emitter.spriteBake(sprites[character.ordinal()], MutableQuadView.BAKE_LOCK_UV);
		emitter.color(-1, -1, -1, -1);
		emitter.emit();

		// Back
		emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R180), 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
		emitter.spriteBake(sprites[LargeSignCharacter.SPACE.ordinal()], MutableQuadView.BAKE_LOCK_UV);
		emitter.color(-1, -1, -1, -1);
		emitter.emit();

		// Left
		emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R90), 0.0f, 0.0f, 0.0625f, 1.0f, 0.0f);
		emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV);
		emitter.color(-1, -1, -1, -1);
		emitter.emit();

		// Right
		emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R270), 0.9375f, 0.0f, 1f, 1.0f, 0.0f);
		emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV);
		emitter.color(-1, -1, -1, -1);
		emitter.emit();
		
		VoxelShape shape = LargeSignBlock.getOutlineShape(direction);
		
		// Up
		emitter.square(Direction.UP, 
				(float)shape.getMin(Axis.X), 1f - (float)shape.getMax(Axis.Z), 
				(float)shape.getMax(Axis.X), 1f - (float)shape.getMin(Axis.Z), 0.0f);
		emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV);
		emitter.color(-1, -1, -1, -1);
		emitter.emit();
		
		// Down
		emitter.square(Direction.DOWN, 
				(float)shape.getMin(Axis.X), (float)shape.getMin(Axis.Z), 
				(float)shape.getMax(Axis.X), (float)shape.getMax(Axis.Z), 0.0f);
		emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV);
		emitter.color(-1, -1, -1, -1);
		emitter.emit();	
		
		return builder.build();
	}
	 
    @Override
    public void emitItemQuads(ItemStack itemStack, Supplier<Random> randomSupplier, RenderContext context) {
		Mesh mesh = buildMesh(Direction.NORTH, LargeSignCharacter.KEY_A);
		mesh.outputTo(context.getEmitter());
    }
    
}