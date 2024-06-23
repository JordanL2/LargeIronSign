package com.largesign;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import org.joml.Vector3f;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockview.v2.FabricBlockView;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
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
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
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
	private Sprite spriteBackground;
	private Sprite spriteEdge;
	
	private final static DirectionUtil directionUtil = new DirectionUtil();
	
	private ModelTransformation transformation;
	private RenderMaterial cutoutMaterial;
	

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
			SpriteIdentifier spriteId = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
						character.getBlockIdentifier());
			//spriteId.getRenderLayer(RenderLayer.getCutout());
			sprites[character.ordinal()] = textureGetter.apply(spriteId);
		}
		spriteBackground = textureGetter.apply(
				new SpriteIdentifier(
						PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, 
						new Identifier(LargeSign.MOD_ID, "block/large_sign_back")));
		spriteEdge = textureGetter.apply(
				new SpriteIdentifier(
						PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, 
						new Identifier(LargeSign.MOD_ID, "block/large_sign_edge")));

		// Find cutout material
		MaterialFinder finder = Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).materialFinder();
		cutoutMaterial = finder.blendMode(BlendMode.CUTOUT).find();

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
		Mesh mesh = buildMesh(direction, entityState.character, entityState.foreground, entityState.background);
		mesh.outputTo(context.getEmitter());
	}
	
	private Mesh buildMesh(Direction direction, LargeSignCharacter character, int foreground, int background) {
		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		MeshBuilder builder = renderer.meshBuilder();
		QuadEmitter emitter = builder.getEmitter();
		
		float depth = 0.001f;
		
		// Front - Background
		emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.9375f);
		emitter.spriteBake(spriteBackground, MutableQuadView.BAKE_LOCK_UV);
		emitter.color(background, background, background, background);
		emitter.emit();
		
		// Front - Text
		emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.9375f - depth);
		emitter.spriteBake(sprites[character.ordinal()], MutableQuadView.BAKE_LOCK_UV);
		emitter.material(cutoutMaterial);
		emitter.color(foreground, foreground, foreground, foreground);
		emitter.emit();

		// Back
		emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R180), 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
		emitter.spriteBake(spriteBackground, MutableQuadView.BAKE_LOCK_UV);
		emitter.color(background, background, background, background);
		emitter.emit();

		// Left
		emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R90), 0.0f, 0.0f, 0.0625f, 1.0f, 0.0f);
		emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV);
		emitter.color(background, background, background, background);
		emitter.emit();

		// Right
		emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R270), 0.9375f, 0.0f, 1f, 1.0f, 0.0f);
		emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV);
		emitter.color(background, background, background, background);
		emitter.emit();
		
		VoxelShape shape = LargeSignBlock.getOutlineShape(direction);
		
		// Up
		emitter.square(Direction.UP, 
				(float)shape.getMin(Axis.X), 1f - (float)shape.getMax(Axis.Z), 
				(float)shape.getMax(Axis.X), 1f - (float)shape.getMin(Axis.Z), 0.0f);
		emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV);
		emitter.color(background, background, background, background);
		emitter.emit();
		
		// Down
		emitter.square(Direction.DOWN, 
				(float)shape.getMin(Axis.X), (float)shape.getMin(Axis.Z), 
				(float)shape.getMax(Axis.X), (float)shape.getMax(Axis.Z), 0.0f);
		emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV);
		emitter.color(background, background, background, background);
		emitter.emit();	
		
		return builder.build();
	}
	 
    @Override
    public void emitItemQuads(ItemStack itemStack, Supplier<Random> randomSupplier, RenderContext context) {
		Mesh mesh = buildMesh(Direction.NORTH, LargeSignCharacter.KEY_A, 0xff000000, 0xffffffff);
		mesh.outputTo(context.getEmitter());
    }
    
}