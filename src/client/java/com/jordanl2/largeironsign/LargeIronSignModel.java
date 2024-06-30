package com.jordanl2.largeironsign;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
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
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockRenderView;

@Environment(EnvType.CLIENT)
public class LargeIronSignModel implements UnbakedModel, BakedModel, FabricBakedModel {

	public static final float TEXT_DEPTH = 0.001f;
	public static final float THICKNESS = 1f / 16f;
	public static final float FRONT_DEPTH = 1f - THICKNESS;
	public static final float TRIM_WIDTH = 2f / 16f;

	private final Sprite[] sprites = new Sprite[LargeIronSignCharacter.values().length];
	private Sprite spriteFront;
	private Sprite spriteBack;
	private Sprite spriteEdge;
	private Sprite spriteTrimFront;
	private Sprite spriteTrimBack;
	private Sprite spriteTrimEdge;
	private Sprite spriteTrimCornerEdge;
	private Sprite spriteTrimCornerFront;

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
		for (LargeIronSignCharacter character : LargeIronSignCharacter.values()) {
			SpriteIdentifier spriteId = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
						character.getBlockTextureIdentifier());
			sprites[character.ordinal()] = textureGetter.apply(spriteId);
		}
		spriteFront = textureGetter.apply(
				new SpriteIdentifier(
						PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, 
						LargeIronSignBlock.FRONT_TEXTURE));
		spriteBack = textureGetter.apply(
				new SpriteIdentifier(
						PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, 
						LargeIronSignBlock.BACK_TEXTURE));
		spriteEdge = textureGetter.apply(
				new SpriteIdentifier(
						PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, 
						LargeIronSignBlock.EDGE_TEXTURE));

		spriteTrimFront = textureGetter.apply(
				new SpriteIdentifier(
						PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
						LargeIronSignBlock.TRIM_FRONT_TEXTURE));
		spriteTrimBack = textureGetter.apply(
				new SpriteIdentifier(
						PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
						LargeIronSignBlock.TRIM_BACK_TEXTURE));
		spriteTrimEdge = textureGetter.apply(
				new SpriteIdentifier(
						PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
						LargeIronSignBlock.TRIM_EDGE_TEXTURE));
		spriteTrimCornerFront = textureGetter.apply(
				new SpriteIdentifier(
						PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
						LargeIronSignBlock.TRIM_CORNER_FRONT_TEXTURE));
		spriteTrimCornerEdge = textureGetter.apply(
				new SpriteIdentifier(
						PlayerScreenHandler.BLOCK_ATLAS_TEXTURE,
						LargeIronSignBlock.TRIM_CORNER_EDGE_TEXTURE));

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
		return sprites[LargeIronSignCharacter.SPACE.ordinal()];
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
		Direction direction = state.get(LargeIronSignBlock.FACING);
		LargeIronSignBlockEntity entityState = (LargeIronSignBlockEntity) 
				((FabricBlockView)blockView).getBlockEntityRenderData(pos);
		Mesh mesh = buildMesh(direction, entityState.character, entityState.foreground, entityState.background, entityState.trim);
		mesh.outputTo(context.getEmitter());
	}
	
	private Mesh buildMesh(Direction direction, LargeIronSignCharacter character, int foreground, int background, int trim) {
		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		MeshBuilder builder = renderer.meshBuilder();
		QuadEmitter emitter = builder.getEmitter();

		boolean topTrim = (trim & LargeIronSignBlockEntity.TOP_EDGE) > 0;
		boolean rightTrim = (trim & LargeIronSignBlockEntity.RIGHT_EDGE) > 0;
		boolean bottomTrim = (trim & LargeIronSignBlockEntity.BOTTOM_EDGE) > 0;
		boolean leftTrim = (trim & LargeIronSignBlockEntity.LEFT_EDGE) > 0;

		// Front - Background
		emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, FRONT_DEPTH);
		emitter.spriteBake(spriteFront, MutableQuadView.BAKE_LOCK_UV);
		emitter.color(background, background, background, background);
		emitter.emit();
		
		// Front - Text
		emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, FRONT_DEPTH - TEXT_DEPTH);
		emitter.spriteBake(sprites[character.ordinal()], MutableQuadView.BAKE_LOCK_UV);
		emitter.material(cutoutMaterial);
		emitter.color(foreground, foreground, foreground, foreground);
		emitter.emit();

		// Front - Trim
		if (topTrim) {
			emitter.square(direction, 0.0f, 1.0f, 1.0f, 1.0f + TRIM_WIDTH, FRONT_DEPTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimFront, MutableQuadView.BAKE_NORMALIZED);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}
		if (rightTrim) {
			emitter.square(direction, 1.0f, 0.0f, 1.0f + TRIM_WIDTH, 1.0f, FRONT_DEPTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}
		if (bottomTrim) {
			emitter.square(direction, 0.0f, 0.0f - TRIM_WIDTH, 1.0f, 0.0f, FRONT_DEPTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_180);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}
		if (leftTrim) {
			emitter.square(direction, 0.0f - TRIM_WIDTH, 0.0f, 0.0f, 1.0f, FRONT_DEPTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}

		// Back
		Direction backDirection = directionUtil.rotate(direction, VariantSettings.Rotation.R180);
		emitter.square(backDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
		emitter.spriteBake(spriteBack, MutableQuadView.BAKE_LOCK_UV);
		emitter.color(-1, -1, -1, -1);
		emitter.emit();

		// Back - Trim
		if (topTrim) {
			emitter.square(backDirection,0.0f, 1.0f, 1.0f, 1.0f + TRIM_WIDTH, 0.0f);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimBack, MutableQuadView.BAKE_NORMALIZED);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}
		if (leftTrim) {
			emitter.square(backDirection,1.0f, 0.0f, 1.0f + TRIM_WIDTH, 1.0f, 0.0f);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimBack, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}
		if (bottomTrim) {
			emitter.square(backDirection, 0.0f, 0.0f - TRIM_WIDTH, 1.0f, 0.0f, 0.0f);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimBack, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_180);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}
		if (rightTrim) {
			emitter.square(backDirection, 0.0f - TRIM_WIDTH, 0.0f, 0.0f, 1.0f, 0.0f);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimBack, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}

		// Left
		if (!leftTrim) {
			emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R90), 0.0f, 0.0f, 0.0625f, 1.0f, 0.0f);
			emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		} else {
			emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R90), 0.0f, 0.0f, 0.0625f, 1.0f, 0.0f - TRIM_WIDTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimEdge, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}

		// Right
		if (!rightTrim) {
			emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R270), 0.9375f, 0.0f, 1f, 1.0f, 0.0f);
			emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		} else {
			emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R270), 0.9375f, 0.0f, 1f, 1.0f, 0.0f - TRIM_WIDTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimEdge, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}
		
		VoxelShape shape = LargeIronSignBlock.getOutlineShape(direction);
		
		int upRotateFlag = 0;
		int upOppositeRotateFlag = 0;
		int downRotateFlag = 0;
		int downOppositeRotateFlag = 0;
		switch (direction) {
			case NORTH:
				upRotateFlag |= MutableQuadView.BAKE_ROTATE_180;
				downOppositeRotateFlag |= MutableQuadView.BAKE_ROTATE_180;
				break;
			case EAST:
				upRotateFlag |= MutableQuadView.BAKE_ROTATE_270;
				upOppositeRotateFlag |= MutableQuadView.BAKE_ROTATE_90;
				downRotateFlag |= MutableQuadView.BAKE_ROTATE_270;
				downOppositeRotateFlag |= MutableQuadView.BAKE_ROTATE_90;
				break;
			case SOUTH:
				upOppositeRotateFlag |= MutableQuadView.BAKE_ROTATE_180;
				downRotateFlag |= MutableQuadView.BAKE_ROTATE_180;
				break;
			case WEST:
				upRotateFlag |= MutableQuadView.BAKE_ROTATE_90;
				upOppositeRotateFlag |= MutableQuadView.BAKE_ROTATE_270;
				downRotateFlag |= MutableQuadView.BAKE_ROTATE_90;
				downOppositeRotateFlag |= MutableQuadView.BAKE_ROTATE_270;
				break;
			default:
				break;
		}
		
		// Up
		if (!topTrim) {
			emitter.square(Direction.UP,
					(float) shape.getMin(Axis.X), 1f - (float) shape.getMax(Axis.Z),
					(float) shape.getMax(Axis.X), 1f - (float) shape.getMin(Axis.Z), 0.0f);
			emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV | upRotateFlag);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		} else {
			emitter.square(Direction.UP,
					(float) shape.getMin(Axis.X), 1f - (float) shape.getMax(Axis.Z),
					(float) shape.getMax(Axis.X), 1f - (float) shape.getMin(Axis.Z), 0.0f - TRIM_WIDTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimEdge, MutableQuadView.BAKE_NORMALIZED | upRotateFlag);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}
		
		// Down
		if (!bottomTrim) {
			emitter.square(Direction.DOWN,
					(float) shape.getMin(Axis.X), (float) shape.getMin(Axis.Z),
					(float) shape.getMax(Axis.X), (float) shape.getMax(Axis.Z), 0.0f);
			emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV | downRotateFlag);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		} else {
			emitter.square(Direction.DOWN,
					(float) shape.getMin(Axis.X), (float) shape.getMin(Axis.Z),
					(float) shape.getMax(Axis.X), (float) shape.getMax(Axis.Z), 0.0f - TRIM_WIDTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimEdge, MutableQuadView.BAKE_NORMALIZED | downRotateFlag);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}

		// Top Left Corner
		if (topTrim && leftTrim) {
			// Front
			emitter.square(direction, 0.0f - TRIM_WIDTH, 1.0f, 0.0f, 1.0f + TRIM_WIDTH, FRONT_DEPTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
			// Back
			emitter.square(backDirection, 1.0f, 1.0f, 1.0f + TRIM_WIDTH, 1.0f + TRIM_WIDTH, 0.0f);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
			// Left
			emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R90), 0.0f, 1.0f, 0.0625f, 1.0f + TRIM_WIDTH, 0.0f - TRIM_WIDTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
			// Top
			Quad top = new Quad(1.0f, 0.0f, 1.0f + TRIM_WIDTH, 0.0f + THICKNESS);
			top.rotate(directionUtil.getRotation(Direction.NORTH, direction));
			emitter.square(Direction.UP, top.left, top.bottom, top.right, top.top, 0.0f - TRIM_WIDTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | upRotateFlag);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}

		// Top Right Corner
		if (topTrim && rightTrim) {
			// Front
			emitter.square(direction, 1.0f, 1.0f, 1.0f + TRIM_WIDTH, 1.0f + TRIM_WIDTH, FRONT_DEPTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
			// Back
			emitter.square(backDirection, 0.0f - TRIM_WIDTH, 1.0f, 0.0f, 1.0f + TRIM_WIDTH, 0.0f);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
			// Right
			emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R270), 0.9375f, 1.0f, 1f, 1.0f + TRIM_WIDTH, 0.0f - TRIM_WIDTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
			// Top
			Quad top = new Quad(0.0f - TRIM_WIDTH, 0.0f, 0.0f, 0.0f + THICKNESS);
			top.rotate(directionUtil.getRotation(Direction.NORTH, direction));
			emitter.square(Direction.UP, top.left, top.bottom, top.right, top.top, 0.0f - TRIM_WIDTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | upOppositeRotateFlag);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}

		// Bottom Left Corner
		if (bottomTrim && leftTrim) {
			// Front
			emitter.square(direction, 0.0f - TRIM_WIDTH, 0.0f - TRIM_WIDTH, 0.0f, 0.0f, FRONT_DEPTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
			// Back
			emitter.square(backDirection, 1.0f, 0.0f - TRIM_WIDTH, 1.0f + TRIM_WIDTH, 0.0f, 0.0f);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_180);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
			// Left
			emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R90), 0.0f, 0.0f - TRIM_WIDTH, 0.0625f, 0.0f, 0.0f - TRIM_WIDTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
			// Bottom
			Quad top = new Quad(1.0f, 1.0f - THICKNESS, 1.0f + TRIM_WIDTH, 1.0f);
			top.rotate(directionUtil.getRotation(direction, Direction.NORTH));
			emitter.square(Direction.DOWN, top.left, top.bottom, top.right, top.top, 0.0f - TRIM_WIDTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | downOppositeRotateFlag);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}

		// Bottom Right Corner
		if (bottomTrim && rightTrim) {
			// Front
			emitter.square(direction, 1.0f, 0.0f - TRIM_WIDTH, 1.0f + TRIM_WIDTH, 0.0f, FRONT_DEPTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_180);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
			// Back
			emitter.square(backDirection, 0.0f - TRIM_WIDTH, 0.0f - TRIM_WIDTH, 0.0f, 0.0f, 0.0f);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
			// Right
			emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R270), 0.9375f, 0.0f - TRIM_WIDTH, 1f, 0.0f, 0.0f - TRIM_WIDTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
			// Bottom
			Quad top = new Quad(0.0f - TRIM_WIDTH, 1.0f - THICKNESS, 0.0f, 1.0f);
			top.rotate(directionUtil.getRotation(direction, Direction.NORTH));
			emitter.square(Direction.DOWN, top.left, top.bottom, top.right, top.top, 0.0f - TRIM_WIDTH);
			emitter.uvUnitSquare();
			emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | downRotateFlag);
			emitter.color(-1, -1, -1, -1);
			emitter.emit();
		}

		return builder.build();
	}

    @Override
    public void emitItemQuads(ItemStack itemStack, Supplier<Random> randomSupplier, RenderContext context) {
		Mesh mesh = buildMesh(
				Direction.NORTH, 
				LargeIronSignCharacter.KEY_A, 
				LargeIronSignBlock.DEFAULT_COLOUR_FOREGROUND, 
				LargeIronSignBlock.DEFAULT_COLOUR_BACKGROUND,
				0);
		mesh.outputTo(context.getEmitter());
    }
    
}