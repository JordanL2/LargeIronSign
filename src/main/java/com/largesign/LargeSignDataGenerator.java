package com.largesign;

import java.util.Optional;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.MultipartBlockStateSupplier;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.When;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class LargeSignDataGenerator implements DataGeneratorEntrypoint {
	
	private static class LargeSignModelGenerator extends FabricModelProvider {
		
		private LargeSignModelGenerator(FabricDataOutput generator) {
			super(generator);
		}
	 
		@Override
		public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
			MultipartBlockStateSupplier multiPartBlockStateSupplier = MultipartBlockStateSupplier.create(LargeSignBlock.LARGE_SIGN_BLOCK);
			
			for (LargeSignCharacter character : LargeSignCharacter.values()) {
				multiPartBlockStateSupplier
					.with(When.allOf(
							When.create().set(LargeSignBlock.FACING, Direction.NORTH),
							When.create().set(LargeSignBlock.CHAR, character)
						  ),
						  BlockStateVariant.create()
						  	.put(VariantSettings.Y, 
						  		 VariantSettings.Rotation.R0)
						  	.put(VariantSettings.MODEL, 
								 character.getPath())
						  	);

				multiPartBlockStateSupplier
						.with(When.allOf(
								When.create().set(LargeSignBlock.FACING, Direction.EAST),
								When.create().set(LargeSignBlock.CHAR, character)
							  ),
							  BlockStateVariant.create()
							  	.put(VariantSettings.Y, 
							  		 VariantSettings.Rotation.R90)
							  	.put(VariantSettings.MODEL, 
									 character.getPath())
							  	);

				multiPartBlockStateSupplier
						.with(When.allOf(
								When.create().set(LargeSignBlock.FACING, Direction.SOUTH),
								When.create().set(LargeSignBlock.CHAR, character)
							  ),
							  BlockStateVariant.create()
							  	.put(VariantSettings.Y, 
							  		 VariantSettings.Rotation.R180)
							  	.put(VariantSettings.MODEL, 
									 character.getPath())
							  	);

				multiPartBlockStateSupplier
						.with(When.allOf(
								When.create().set(LargeSignBlock.FACING, Direction.WEST),
								When.create().set(LargeSignBlock.CHAR, character)
							  ),
							  BlockStateVariant.create()
							  	.put(VariantSettings.Y, 
							  		 VariantSettings.Rotation.R270)
							  	.put(VariantSettings.MODEL, 
									 character.getPath())
							  	);

				Model model = new Model(
						Optional.of(new Identifier(LargeSign.MOD_ID, "block/large_sign_parent")),
						Optional.of(character.getSuffix()),
						LargeSignBlock.SYMBOL);
				TextureMap textures = new TextureMap();
				textures.put(LargeSignBlock.SYMBOL, character.getPath());
				model.upload(LargeSignBlock.LARGE_SIGN_BLOCK, textures, blockStateModelGenerator.modelCollector);
			}

			blockStateModelGenerator.blockStateCollector.accept(multiPartBlockStateSupplier);
		}

		@Override
		public void generateItemModels(ItemModelGenerator itemModelGenerator) {
			// Item config written manually
		}

	}
	 
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack fabricDataGenerator = generator.createPack();
        
        fabricDataGenerator.addProvider(LargeSignModelGenerator::new);
    }
 
}