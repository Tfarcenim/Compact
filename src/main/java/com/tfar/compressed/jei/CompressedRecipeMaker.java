package com.tfar.compressed.jei;

import com.tfar.compressed.Compressed;
import com.tfar.compressed.CompressedBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class CompressedRecipeMaker {
  public static List<ShapelessRecipe> createCompressions(){
    List<ShapelessRecipe> recipes = new ArrayList<>();
    String group = "compressed.compression";
    for (CompressedBlock block : Compressed.MOD_BLOCKS){
      ItemStack compressedResult = new ItemStack(block);
      ItemStack compressedInput = new ItemStack(block.deCompression);
      Ingredient compressedIngredient = Ingredient.fromStacks(compressedInput);
      NonNullList<Ingredient> inputs = NonNullList.withSize(9,compressedIngredient);
      ResourceLocation id = new ResourceLocation(Compressed.MODID,compressedResult.getItem().getRegistryName().getPath());
      ShapelessRecipe recipe = new ShapelessRecipe(id, group, compressedResult, inputs);
      recipes.add(recipe);
    }
    return recipes;
  }

  public static List<ShapelessRecipe> createDeCompressions(){
    List<ShapelessRecipe> recipes = new ArrayList<>();
    String group = "compressed.decompression";
    for (CompressedBlock block : Compressed.MOD_BLOCKS){
      ItemStack decompressedResult = new ItemStack(block.deCompression,9);
      ItemStack compressedInput = new ItemStack(block);
      Ingredient compressedIngredient = Ingredient.fromStacks(compressedInput);
      NonNullList<Ingredient> inputs = NonNullList.withSize(1,compressedIngredient);
      ResourceLocation id = new ResourceLocation(Compressed.MODID,"de"+decompressedResult.getItem().getRegistryName().getPath());
      ShapelessRecipe recipe = new ShapelessRecipe(id, group, decompressedResult, inputs);
      recipes.add(recipe);
    }
    return recipes;
  }
}
