package com.tfar.compressed;

import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CompressionRecipe extends SpecialRecipe {
  public CompressionRecipe(ResourceLocation idIn) {
    super(idIn);
  }

  /**
   * Used to check if a recipe matches current crafting inventory
   *
   * @param inv
   * @param worldIn
   */
  @Override
  public boolean matches(CraftingInventory inv, World worldIn) {
    //first, search for compression recipes
    boolean isCompression = true;
    final ItemStack cachedStack = inv.getStackInSlot(0);
    if (inv.getSizeInventory() != 9
    ||cachedStack.isEmpty()
      || !(cachedStack.getItem() instanceof BlockItem)
            || (!Compressed.compressible.contains(Block.getBlockFromItem(cachedStack.getItem()))

    && !(Block.getBlockFromItem(cachedStack.getItem()) instanceof CompressedBlock)

    )) isCompression = false;
    else {
      for (int j = 0; j < inv.getSizeInventory(); ++j) {
        if (cachedStack.getItem() != inv.getStackInSlot(j).getItem()) {
          isCompression = false;
          break;
        }
      }
    }
    if (isCompression)
      return !(Block.getBlockFromItem(cachedStack.getItem()) instanceof CompressedBlock) || ((CompressedBlock)
              Block.getBlockFromItem(cachedStack.getItem())).compression_level < Configs.max;
    //then search for decompression recipes
    List<ItemStack> notEmpty = new ArrayList<>();

    for (int j = 0; j < inv.getSizeInventory(); ++j) {
      ItemStack stack = inv.getStackInSlot(j);
      if (!stack.isEmpty() && (!(stack.getItem() instanceof BlockItem)
              || !(((BlockItem) stack.getItem()).getBlock() instanceof CompressedBlock))) {
        return false;
      }
      if (!stack.isEmpty())
      notEmpty.add(stack);
    }
    return notEmpty.size() == 1;
  }

  /**
   * Returns an Item that is the result of this recipe
   *
   * @param inv
   */
  @Nonnull
  @Override
  public ItemStack getCraftingResult(CraftingInventory inv) {
    //first, search for compression recipes
    boolean isCompression = true;
    final ItemStack cachedStack = inv.getStackInSlot(0);
    if (cachedStack.isEmpty()
            || !(cachedStack.getItem() instanceof BlockItem)
            || (!Compressed.compressible.contains(Block.getBlockFromItem(cachedStack.getItem()))
    && !(Block.getBlockFromItem(cachedStack.getItem()) instanceof CompressedBlock))) isCompression = false;
    else {
      for (int j = 0; j < inv.getSizeInventory(); ++j) {
        if (cachedStack.getItem() != inv.getStackInSlot(j).getItem()) {
          isCompression = false;
          break;
        }
      }
    }
    Block block = Block.getBlockFromItem(cachedStack.getItem());

    if (isCompression){
      if (block instanceof CompressedBlock){
        String material = ((CompressedBlock) block).material_name.getPath();

        int compression = ((CompressedBlock) block).compression_level;
        return new ItemStack(ForgeRegistries.BLOCKS.getValue(
                new ResourceLocation(Compressed.MODID,material + "_x" + (compression+1))));
      }
      else {
        String path =  block.getRegistryName().getPath();
        return new ItemStack(ForgeRegistries.BLOCKS.getValue(
                new ResourceLocation(Compressed.MODID,path + "_x" + 1)));
      }
    }
    //then search for decompression recipes
    List<ItemStack> notEmpty = new ArrayList<>();

    for (int j = 0; j < inv.getSizeInventory(); ++j) {
      ItemStack stack = inv.getStackInSlot(j);
      if (!stack.isEmpty() && (!(stack.getItem() instanceof BlockItem)
              || !(((BlockItem) stack.getItem()).getBlock() instanceof CompressedBlock))) {
        return ItemStack.EMPTY;
      }
      if(!stack.isEmpty())
      notEmpty.add(stack);
    }

    if(notEmpty.size() != 1)return ItemStack.EMPTY;
    block = Block.getBlockFromItem(notEmpty.get(0).getItem());
    int compression = ((CompressedBlock)block).compression_level;
    if (compression == 1)return new ItemStack(ForgeRegistries.BLOCKS.getValue(((CompressedBlock) block).material_name),9);
    return new ItemStack(ForgeRegistries.BLOCKS.getValue(
            new ResourceLocation(Compressed.MODID,
                    ((CompressedBlock) block).material_name.getPath() + "_x" + (compression-1))),9);
  }


  @Nonnull
  @Override
  public IRecipeSerializer<?> getSerializer() {
    return Compressed.RECIPE;
  }
}

