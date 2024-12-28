package com.jordanl2.largeironsign;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.data.VariantSettings;
import net.minecraft.client.model.SpriteGetter;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.jordanl2.largeironsign.LargeIronSignBlock.THICKNESS;
import static com.jordanl2.largeironsign.LargeIronSignBlock.TRIM_WIDTH;

@Environment(EnvType.CLIENT)
public class LargeIronSignBakedModel implements BakedModel, FabricBakedModel {
    
    private static final Identifier BLOCK_ATLAS_TEXTURE = Identifier.ofVanilla("textures/atlas/blocks.png");
    
    public static final float TEXT_DEPTH = 0.001f;

    private final static DirectionUtil directionUtil = new DirectionUtil();
    
    private final Sprite[] sprites = new Sprite[LargeIronSignCharacter.values().length];
    private final Sprite spriteFront;
    private final Sprite spriteBack;
    private final Sprite spriteEdge;
    private final Sprite spriteTrimFront;
    private final Sprite spriteTrimBack;
    private final Sprite spriteTrimEdge;
    private final Sprite spriteTrimInside;
    private final Sprite spriteTrimCornerEdge;
    private final Sprite spriteTrimCornerFront;
    private final Sprite spriteTrimInnerCornerFront;
    private final Sprite spriteTrimInnerCornerBack;
    
    private final ModelTransformation modelTransformation;
    private final RenderMaterial cutoutMaterial;
    
    public LargeIronSignBakedModel(final Baker baker) {
        // Make model transformation
        modelTransformation = new ModelTransformation(
                ModelHelper.TRANSFORM_BLOCK_3RD_PERSON_RIGHT,
                ModelHelper.TRANSFORM_BLOCK_3RD_PERSON_RIGHT,
                ModelHelper.TRANSFORM_BLOCK_1ST_PERSON_LEFT,
                ModelHelper.TRANSFORM_BLOCK_1ST_PERSON_RIGHT,
                Transformation.IDENTITY,
                ModelHelper.TRANSFORM_BLOCK_GUI,
                ModelHelper.TRANSFORM_BLOCK_GROUND,
                ModelHelper.TRANSFORM_BLOCK_FIXED);
        modelTransformation.gui().translation.add(0.2f, -0.1f, 0f);
        
        // Load sprites
        SpriteGetter textureGetter = baker.getSpriteGetter();
        for (LargeIronSignCharacter character : LargeIronSignCharacter.values()) {
            SpriteIdentifier spriteId = new SpriteIdentifier(BLOCK_ATLAS_TEXTURE,
                    character.getBlockTextureIdentifier());
            sprites[character.ordinal()] = textureGetter.get(spriteId);
        }
        spriteFront = textureGetter.get(
                new SpriteIdentifier(
                        BLOCK_ATLAS_TEXTURE,
                        LargeIronSignBlock.FRONT_TEXTURE));
        spriteBack = textureGetter.get(
                new SpriteIdentifier(
                        BLOCK_ATLAS_TEXTURE,
                        LargeIronSignBlock.BACK_TEXTURE));
        spriteEdge = textureGetter.get(
                new SpriteIdentifier(
                        BLOCK_ATLAS_TEXTURE,
                        LargeIronSignBlock.EDGE_TEXTURE));
        
        spriteTrimFront = textureGetter.get(
                new SpriteIdentifier(
                        BLOCK_ATLAS_TEXTURE,
                        LargeIronSignBlock.TRIM_FRONT_TEXTURE));
        spriteTrimBack = textureGetter.get(
                new SpriteIdentifier(
                        BLOCK_ATLAS_TEXTURE,
                        LargeIronSignBlock.TRIM_BACK_TEXTURE));
        spriteTrimEdge = textureGetter.get(
                new SpriteIdentifier(
                        BLOCK_ATLAS_TEXTURE,
                        LargeIronSignBlock.TRIM_EDGE_TEXTURE));
        spriteTrimInside = textureGetter.get(
                new SpriteIdentifier(
                        BLOCK_ATLAS_TEXTURE,
                        LargeIronSignBlock.TRIM_INSIDE_TEXTURE));
        spriteTrimCornerFront = textureGetter.get(
                new SpriteIdentifier(
                        BLOCK_ATLAS_TEXTURE,
                        LargeIronSignBlock.TRIM_CORNER_FRONT_TEXTURE));
        spriteTrimCornerEdge = textureGetter.get(
                new SpriteIdentifier(
                        BLOCK_ATLAS_TEXTURE,
                        LargeIronSignBlock.TRIM_CORNER_EDGE_TEXTURE));
        spriteTrimInnerCornerFront = textureGetter.get(
                new SpriteIdentifier(
                        BLOCK_ATLAS_TEXTURE,
                        LargeIronSignBlock.TRIM_INNER_CORNER_FRONT_TEXTURE));
        spriteTrimInnerCornerBack = textureGetter.get(
                new SpriteIdentifier(
                        BLOCK_ATLAS_TEXTURE,
                        LargeIronSignBlock.TRIM_INNER_CORNER_BACK_TEXTURE));
        
        // Find cutout material
        MaterialFinder finder = Renderer.get().materialFinder();
        cutoutMaterial = finder.blendMode(BlendMode.CUTOUT).find();
    }
    
    
    // BakedModel methods

    @Override
    public List<BakedQuad> getQuads(final BlockState var1, final Direction var2, final Random var3) {
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
    public Sprite getParticleSprite() {
        return sprites[LargeIronSignCharacter.SPACE.ordinal()];
    }
    
    @Override
    public ModelTransformation getTransformation() {
        return modelTransformation;
    }
    
    
    // FabricBakedModel methods
    
    @Override
    public boolean isVanillaAdapter() {
        return false; // False to trigger FabricBakedModel rendering
    }
    
    @Override
    public void emitBlockQuads(final QuadEmitter emitter, final BlockRenderView blockView, final BlockState state,
                               final BlockPos pos, final Supplier<Random> randomSupplier,
                               final Predicate<@Nullable Direction> cullTest) {
        Direction direction = state.get(LargeIronSignBlock.FACING);
        Object entityStateObject = blockView.getBlockEntityRenderData(pos);
        if (!(entityStateObject instanceof LargeIronSignBlockEntity entityState)) {
            return;
        }
        
        LargeIronSignBlockNeighbourState neighbourState = new LargeIronSignBlockNeighbourState(blockView, state, pos);
        
        buildMesh(
                emitter,
                direction,
                entityState.character,
                entityState.foreground,
                entityState.background,
                state.get(LargeIronSignBlock.TRIM),
                neighbourState);
    }
    
    @Override
    public void emitItemQuads(QuadEmitter emitter, Supplier<Random> randomSupplier) {
        buildMesh(
                emitter,
                Direction.NORTH,
                LargeIronSignCharacter.KEY_A,
                LargeIronSignBlock.DEFAULT_COLOUR_FOREGROUND,
                LargeIronSignBlock.DEFAULT_COLOUR_BACKGROUND,
                false,
                new LargeIronSignBlockNeighbourState());
    }
    
    private void buildMesh(final QuadEmitter emitter, final Direction direction, final LargeIronSignCharacter character,
                           final int foreground, final int background, final boolean trim,
                           final LargeIronSignBlockNeighbourState neighbourState) {
        
        final boolean innerCornerTopLeft = neighbourState.innerCornerTopLeft();
        final boolean innerCornerTopRight = neighbourState.innerCornerTopRight();
        final boolean innerCornerRightTop = neighbourState.innerCornerRightTop();
        final boolean innerCornerRightBottom = neighbourState.innerCornerRightBottom();
        final boolean innerCornerBottomRight = neighbourState.innerCornerBottomRight();
        final boolean innerCornerBottomLeft = neighbourState.innerCornerBottomLeft();
        final boolean innerCornerLeftBottom = neighbourState.innerCornerLeftBottom();
        final boolean innerCornerLeftTop = neighbourState.innerCornerLeftTop();
        final boolean topLeftIsClear = neighbourState.topLeftIsClear();
        final boolean topRightIsClear = neighbourState.topRightIsClear();
        final boolean bottomRightIsClear = neighbourState.bottomRightIsClear();
        final boolean bottomLeftIsClear = neighbourState.bottomLeftIsClear();
        
        final boolean topTrim = trim && neighbourState.topIsClear();
        final boolean rightTrim = trim && neighbourState.rightIsClear();
        final boolean bottomTrim = trim && neighbourState.bottomIsClear();
        final boolean leftTrim = trim && neighbourState.leftIsClear();
        
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
        
        // Front - Background
        emitter.square(direction, 0f, 0f, 1f, 1f, 1f - THICKNESS);
        emitter.spriteBake(spriteFront, MutableQuadView.BAKE_LOCK_UV);
        emitter.color(background, background, background, background);
        emitter.emit();
        
        // Front - Text
        emitter.square(direction, 0f, 0f, 1f, 1f, 1f - THICKNESS - TEXT_DEPTH);
        emitter.spriteBake(sprites[character.ordinal()], MutableQuadView.BAKE_LOCK_UV);
        emitter.material(cutoutMaterial);
        emitter.color(foreground, foreground, foreground, foreground);
        emitter.emit();
        
        // Front - Trim
        if (topTrim) {
            emitter.square(direction,
                    innerCornerTopLeft ? TRIM_WIDTH : 0f,
                    1f,
                    1f - (innerCornerTopRight ? TRIM_WIDTH : 0f),
                    1f + TRIM_WIDTH,
                    1f - THICKNESS);
            setUV(emitter,
                    innerCornerTopLeft ? TRIM_WIDTH : 0f,
                    0f,
                    1f - (innerCornerTopRight ? TRIM_WIDTH : 0f),
                    1f);
            emitter.spriteBake(spriteTrimFront, MutableQuadView.BAKE_NORMALIZED);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        if (rightTrim) {
            emitter.square(direction,
                    1f,
                    innerCornerRightBottom ? TRIM_WIDTH : 0f,
                    1f + TRIM_WIDTH,
                    1f - (innerCornerRightTop ? TRIM_WIDTH : 0f),
                    1f - THICKNESS);
            setUV(emitter,
                    0f,
                    innerCornerRightTop ? TRIM_WIDTH : 0f,
                    1f,
                    1f - (innerCornerRightBottom ? TRIM_WIDTH : 0f));
            emitter.spriteBake(spriteTrimFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        if (bottomTrim) {
            emitter.square(direction,
                    innerCornerBottomLeft ? TRIM_WIDTH : 0f,
                    0f - TRIM_WIDTH,
                    1f - (innerCornerBottomRight ? TRIM_WIDTH : 0f),
                    0f,
                    1f - THICKNESS);
            setUV(emitter,
                    innerCornerBottomLeft ? TRIM_WIDTH : 0f,
                    0f,
                    1f - (innerCornerBottomRight ? TRIM_WIDTH : 0f),
                    1f);
            emitter.spriteBake(spriteTrimFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_180);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        if (leftTrim) {
            emitter.square(direction,
                    0f - TRIM_WIDTH,
                    innerCornerLeftBottom ? TRIM_WIDTH : 0f,
                    0f,
                    1f - (innerCornerLeftTop ? TRIM_WIDTH : 0f),
                    1f - THICKNESS);
            setUV(emitter,
                    0f,
                    innerCornerLeftTop ? TRIM_WIDTH : 0f,
                    1f,
                    1f - (innerCornerLeftBottom ? TRIM_WIDTH : 0f));
            emitter.spriteBake(spriteTrimFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        
        // Back
        Direction backDirection = directionUtil.rotate(direction, VariantSettings.Rotation.R180);
        emitter.square(backDirection, 0f, 0f, 1f, 1f, 0f);
        emitter.spriteBake(spriteBack, MutableQuadView.BAKE_LOCK_UV);
        emitter.color(-1, -1, -1, -1);
        emitter.emit();
        
        // Back - Trim
        if (topTrim) {
            emitter.square(backDirection,
                    innerCornerTopRight ? TRIM_WIDTH : 0f,
                    1f,
                    1f - (innerCornerTopLeft ? TRIM_WIDTH : 0f),
                    1f + TRIM_WIDTH,
                    0f);
            setUV(emitter,
                    innerCornerTopRight ? TRIM_WIDTH : 0f,
                    0f,
                    1f - (innerCornerTopLeft ? TRIM_WIDTH : 0f),
                    1f);
            emitter.spriteBake(spriteTrimBack, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_FLIP_U);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        if (rightTrim) {
            emitter.square(backDirection,
                    0f - TRIM_WIDTH,
                    innerCornerRightBottom ? TRIM_WIDTH : 0f,
                    0f,
                    1f - (innerCornerRightTop ? TRIM_WIDTH : 0f),
                    0f);
            setUV(emitter,
                    0f,
                    innerCornerRightTop ? TRIM_WIDTH : 0f,
                    1f,
                    1f - (innerCornerRightBottom ? TRIM_WIDTH : 0f));
            emitter.spriteBake(spriteTrimBack, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270 | MutableQuadView.BAKE_FLIP_U);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        if (bottomTrim) {
            emitter.square(backDirection,
                    innerCornerBottomRight ? TRIM_WIDTH : 0f,
                    0f - TRIM_WIDTH,
                    1f - (innerCornerBottomLeft ? TRIM_WIDTH : 0f),
                    0f,
                    0f);
            setUV(emitter,
                    innerCornerBottomRight ? TRIM_WIDTH : 0f,
                    0f,
                    1f - (innerCornerBottomLeft ? TRIM_WIDTH : 0f),
                    1f);
            emitter.spriteBake(spriteTrimBack, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_180 | MutableQuadView.BAKE_FLIP_U);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        if (leftTrim) {
            emitter.square(backDirection,
                    1f,
                    innerCornerLeftBottom ? TRIM_WIDTH : 0f,
                    1f + TRIM_WIDTH,
                    1f - (innerCornerLeftTop ? TRIM_WIDTH : 0f),
                    0f);
            setUV(emitter,
                    0f,
                    innerCornerLeftTop ? TRIM_WIDTH : 0f,
                    1f,
                    1f - (innerCornerLeftBottom ? TRIM_WIDTH : 0f));
            emitter.spriteBake(spriteTrimBack, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90 | MutableQuadView.BAKE_FLIP_U);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        
        // Left
        if (!leftTrim) {
            emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R90), 0f, 0f, THICKNESS, 1f, 0f);
            emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        } else {
            emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R90),
                    0f,
                    innerCornerLeftBottom ? TRIM_WIDTH : 0f,
                    THICKNESS,
                    1f - (innerCornerLeftTop ? TRIM_WIDTH : 0f),
                    0f - TRIM_WIDTH);
            setUV(emitter,
                    0f,
                    innerCornerLeftTop ? TRIM_WIDTH : 0f,
                    1f,
                    1f - (innerCornerLeftBottom ? TRIM_WIDTH : 0f));
            emitter.spriteBake(spriteTrimEdge, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            if (!topTrim && !innerCornerLeftTop) {
                // No top trim, add top face
                Quad top = new Quad(1f, 0f, 1f + TRIM_WIDTH, THICKNESS);
                top.rotate(directionUtil.getRotation(Direction.NORTH, direction));
                emitter.square(Direction.UP, top.left, top.bottom, top.right, top.top, 0f);
                emitter.uvUnitSquare();
                emitter.spriteBake(spriteTrimInside, MutableQuadView.BAKE_NORMALIZED | upRotateFlag);
                emitter.color(-1, -1, -1, -1);
                emitter.emit();
            }
            if (!bottomTrim && !innerCornerLeftBottom) {
                // No bottom trim, add bottom face
                Quad top = new Quad(1f, 1f - THICKNESS, 1f + TRIM_WIDTH, 1f);
                top.rotate(directionUtil.getRotation(direction, Direction.NORTH));
                emitter.square(Direction.DOWN, top.left, top.bottom, top.right, top.top, 0f);
                emitter.uvUnitSquare();
                emitter.spriteBake(spriteTrimInside, MutableQuadView.BAKE_NORMALIZED | downOppositeRotateFlag);
                emitter.color(-1, -1, -1, -1);
                emitter.emit();
            }
        }
        
        // Right
        if (!rightTrim) {
            emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R270), 1f - THICKNESS, 0f, 1f, 1f, 0f);
            emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        } else {
            emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R270),
                    1f - THICKNESS,
                    innerCornerRightBottom ? TRIM_WIDTH : 0f,
                    1f,
                    1f - (innerCornerRightTop ? TRIM_WIDTH : 0f),
                    0f - TRIM_WIDTH);
            setUV(emitter,
                    0f,
                    innerCornerRightTop ? TRIM_WIDTH : 0f,
                    1f,
                    1f - (innerCornerRightBottom ? TRIM_WIDTH : 0f));
            emitter.spriteBake(spriteTrimEdge, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            if (!topTrim && !innerCornerRightTop) {
                // No top trim, add top face
                Quad top = new Quad(0f - TRIM_WIDTH, 0f, 0f, THICKNESS);
                top.rotate(directionUtil.getRotation(Direction.NORTH, direction));
                emitter.square(Direction.UP, top.left, top.bottom, top.right, top.top, 0f);
                emitter.uvUnitSquare();
                emitter.spriteBake(spriteTrimInside, MutableQuadView.BAKE_NORMALIZED | upOppositeRotateFlag);
                emitter.color(-1, -1, -1, -1);
                emitter.emit();
            }
            if (!bottomTrim && !innerCornerRightBottom) {
                // No bottom trim, add bottom face
                Quad top = new Quad(0f - TRIM_WIDTH, 1f - THICKNESS, 0f, 1f);
                top.rotate(directionUtil.getRotation(direction, Direction.NORTH));
                emitter.square(Direction.DOWN, top.left, top.bottom, top.right, top.top, 0f);
                emitter.uvUnitSquare();
                emitter.spriteBake(spriteTrimInside, MutableQuadView.BAKE_NORMALIZED | downRotateFlag);
                emitter.color(-1, -1, -1, -1);
                emitter.emit();
            }
        }
        
        // Top
        if (!topTrim) {
            Quad top = new Quad(0f, 0f, 1f, THICKNESS);
            top.rotate(directionUtil.getRotation(Direction.NORTH, direction));
            emitter.square(Direction.UP, top.left, top.bottom, top.right, top.top, 0f);
            emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV | upRotateFlag);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        } else {
            Quad top = new Quad(
                    innerCornerTopRight ? TRIM_WIDTH : 0f,
                    0f,
                    1f - (innerCornerTopLeft ? TRIM_WIDTH : 0f),
                    THICKNESS);
            top.rotate(directionUtil.getRotation(Direction.NORTH, direction));
            emitter.square(Direction.UP, top.left, top.bottom, top.right, top.top, 0f - TRIM_WIDTH);
            Quad topUV = new Quad(
                    innerCornerTopRight ? TRIM_WIDTH : 0f,
                    0f,
                    1f - (innerCornerTopLeft ? TRIM_WIDTH : 0f),
                    1f);
            topUV.rotate(directionUtil.getRotation(direction, Direction.NORTH));
            setUV(emitter, topUV.left, topUV.bottom, topUV.right, topUV.top);
            emitter.spriteBake(spriteTrimEdge, MutableQuadView.BAKE_NORMALIZED | upRotateFlag);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            if (!leftTrim && !innerCornerTopLeft) {
                // No left trim, add left face
                emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R90), 0f, 1f, THICKNESS, 1f + TRIM_WIDTH, 0f);
                emitter.uvUnitSquare();
                emitter.spriteBake(spriteTrimInside, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
                emitter.color(-1, -1, -1, -1);
                emitter.emit();
            }
            if (!rightTrim && !innerCornerTopRight) {
                // No right trim, add right face
                emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R270), 1f - THICKNESS, 1f, 1f, 1f + TRIM_WIDTH, 0f);
                emitter.uvUnitSquare();
                emitter.spriteBake(spriteTrimInside, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
                emitter.color(-1, -1, -1, -1);
                emitter.emit();
            }
        }
        
        // Bottom
        if (!bottomTrim) {
            Quad top = new Quad(0f, 1f - THICKNESS, 1f, 1f);
            top.rotate(directionUtil.getRotation(direction, Direction.NORTH));
            emitter.square(Direction.DOWN, top.left, top.bottom, top.right, top.top, 0f);
            emitter.spriteBake(spriteEdge, MutableQuadView.BAKE_LOCK_UV | downRotateFlag);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        } else {
            Quad bottom = new Quad(
                    innerCornerBottomRight ? TRIM_WIDTH : 0f,
                    1f - THICKNESS,
                    1f - (innerCornerBottomLeft ? TRIM_WIDTH : 0f),
                    1f);
            bottom.rotate(directionUtil.getRotation(direction, Direction.NORTH));
            emitter.square(Direction.DOWN, bottom.left, bottom.bottom, bottom.right, bottom.top, 0f - TRIM_WIDTH);
            Quad bottomUV = new Quad(
                    innerCornerBottomRight ? TRIM_WIDTH : 0f,
                    0f,
                    1f - (innerCornerBottomLeft ? TRIM_WIDTH : 0f),
                    1f);
            bottomUV.rotate(directionUtil.getRotation(Direction.NORTH, direction));
            setUV(emitter, bottomUV.left, bottomUV.bottom, bottomUV.right, bottomUV.top);
            emitter.spriteBake(spriteTrimEdge, MutableQuadView.BAKE_NORMALIZED | downRotateFlag);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            if (!leftTrim && !innerCornerBottomLeft) {
                // No left trim, add left face
                emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R90), 0f, 0f - TRIM_WIDTH, THICKNESS, 0f, 0f);
                emitter.uvUnitSquare();
                emitter.spriteBake(spriteTrimInside, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
                emitter.color(-1, -1, -1, -1);
                emitter.emit();
            }
            if (!rightTrim && !innerCornerBottomRight) {
                // No right trim, add right face
                emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R270), 1 - THICKNESS, 0f - TRIM_WIDTH, 1f, 0f, 0f);
                emitter.uvUnitSquare();
                emitter.spriteBake(spriteTrimInside, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
                emitter.color(-1, -1, -1, -1);
                emitter.emit();
            }
        }
        
        // Top Left Corner
        if (topTrim && leftTrim && topLeftIsClear) {
            // Front
            emitter.square(direction, 0f - TRIM_WIDTH, 1f, 0f, 1f + TRIM_WIDTH, 1f - THICKNESS);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Back
            emitter.square(backDirection, 1f, 1f, 1f + TRIM_WIDTH, 1f + TRIM_WIDTH, 0f);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Left
            emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R90), 0f, 1f, THICKNESS, 1f + TRIM_WIDTH, 0f - TRIM_WIDTH);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Top
            Quad top = new Quad(1f, 0f, 1f + TRIM_WIDTH, THICKNESS);
            top.rotate(directionUtil.getRotation(Direction.NORTH, direction));
            emitter.square(Direction.UP, top.left, top.bottom, top.right, top.top, 0f - TRIM_WIDTH);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | upRotateFlag);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        
        // Top Right Corner
        if (topTrim && rightTrim && topRightIsClear) {
            // Front
            emitter.square(direction, 1f, 1f, 1f + TRIM_WIDTH, 1f + TRIM_WIDTH, 1f - THICKNESS);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Back
            emitter.square(backDirection, 0f - TRIM_WIDTH, 1f, 0f, 1f + TRIM_WIDTH, 0f);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Right
            emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R270), 1 - THICKNESS, 1f, 1f, 1f + TRIM_WIDTH, 0f - TRIM_WIDTH);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Top
            Quad top = new Quad(0f - TRIM_WIDTH, 0f, 0f, THICKNESS);
            top.rotate(directionUtil.getRotation(Direction.NORTH, direction));
            emitter.square(Direction.UP, top.left, top.bottom, top.right, top.top, 0f - TRIM_WIDTH);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | upOppositeRotateFlag);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        
        // Bottom Left Corner
        if (bottomTrim && leftTrim && bottomLeftIsClear) {
            // Front
            emitter.square(direction, 0f - TRIM_WIDTH, 0f - TRIM_WIDTH, 0f, 0f, 1f - THICKNESS);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Back
            emitter.square(backDirection, 1f, 0f - TRIM_WIDTH, 1f + TRIM_WIDTH, 0f, 0f);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_180);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Left
            emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R90), 0f, 0f - TRIM_WIDTH, THICKNESS, 0f, 0f - TRIM_WIDTH);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Bottom
            Quad top = new Quad(1f, 1f - THICKNESS, 1f + TRIM_WIDTH, 1f);
            top.rotate(directionUtil.getRotation(direction, Direction.NORTH));
            emitter.square(Direction.DOWN, top.left, top.bottom, top.right, top.top, 0f - TRIM_WIDTH);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | downOppositeRotateFlag);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        
        // Bottom Right Corner
        if (bottomTrim && rightTrim && bottomRightIsClear) {
            // Front
            emitter.square(direction, 1f, 0f - TRIM_WIDTH, 1f + TRIM_WIDTH, 0f, 1f - THICKNESS);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_180);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Back
            emitter.square(backDirection, 0f - TRIM_WIDTH, 0f - TRIM_WIDTH, 0f, 0f, 0f);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Right
            emitter.square(directionUtil.rotate(direction, VariantSettings.Rotation.R270), 1f - THICKNESS, 0f - TRIM_WIDTH, 1f, 0f, 0f - TRIM_WIDTH);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Bottom
            Quad top = new Quad(0f - TRIM_WIDTH, 1f - THICKNESS, 0f, 1f);
            top.rotate(directionUtil.getRotation(direction, Direction.NORTH));
            emitter.square(Direction.DOWN, top.left, top.bottom, top.right, top.top, 0f - TRIM_WIDTH);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimCornerEdge, MutableQuadView.BAKE_NORMALIZED | downRotateFlag);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        
        // Top-Right Inner Corner
        if (innerCornerTopRight) {
            // Front
            emitter.square(direction, 1f - TRIM_WIDTH, 1f, 1f, 1f + TRIM_WIDTH, 1f - THICKNESS);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimInnerCornerFront, MutableQuadView.BAKE_NORMALIZED);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Back
            emitter.square(backDirection, 0f, 1f, TRIM_WIDTH, 1f + TRIM_WIDTH, 0f);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimInnerCornerBack, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        
        // Right-Bottom Inner Corner
        if (innerCornerRightBottom) {
            // Front
            emitter.square(direction, 1f, 0f, 1f + TRIM_WIDTH, TRIM_WIDTH, 1f - THICKNESS);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimInnerCornerFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_90);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Back
            emitter.square(backDirection, 0f - TRIM_WIDTH, 0f, 0f, TRIM_WIDTH, 0f);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimInnerCornerBack, MutableQuadView.BAKE_NORMALIZED);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        
        // Bottom-Left Inner Corner
        if (innerCornerBottomLeft) {
            // Front
            emitter.square(direction, 0f, 0f - TRIM_WIDTH, TRIM_WIDTH, 0f, 1f - THICKNESS);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimInnerCornerFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_180);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Back
            emitter.square(backDirection, 1f - TRIM_WIDTH, 0f - TRIM_WIDTH, 1f, 0f, 0f);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimInnerCornerBack, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
        
        // Left-Top Inner Corner
        if (innerCornerLeftTop) {
            // Front
            emitter.square(direction, 0f - TRIM_WIDTH, 1f - TRIM_WIDTH, 0f, 1f, 1f - THICKNESS);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimInnerCornerFront, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_270);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
            // Back
            emitter.square(backDirection, 1f, 1f - TRIM_WIDTH, 1f + TRIM_WIDTH, 1f, 0f);
            emitter.uvUnitSquare();
            emitter.spriteBake(spriteTrimInnerCornerBack, MutableQuadView.BAKE_NORMALIZED | MutableQuadView.BAKE_ROTATE_180);
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }
    }
    
    private void setUV(final QuadEmitter emitter,
                       final float minU, final float minV, final float maxU, final float maxV) {
        emitter.uv(0, minU, minV);
        emitter.uv(1, minU, maxV);
        emitter.uv(2, maxU, maxV);
        emitter.uv(3, maxU, minV);
    }
    
}