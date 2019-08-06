package com.tfar.compressed.recipes;

import com.tfar.compressed.Compressed;
import com.tfar.compressed.CompressedBlock;
import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class DeCompressionRecipe extends SpecialRecipe {
  public DeCompressionRecipe(ResourceLocation idIn) {
    super(idIn);
  }

  private Item toDecompress;

  /**
   * Used to check if a recipe matches current crafting inventory
   *
   * @param inv crafting inventory
   * @param worldIn the world
   */
  @Override
  public boolean matches(CraftingInventory inv, World worldIn) {
    //search for decompression recipes
    ItemStack toDecompress = ItemStack.EMPTY;

    for (int j = 0; j < inv.getSizeInventory(); ++j) {
      ItemStack stack = inv.getStackInSlot(j);
      if (stack.isEmpty())continue;
      if (!(Block.getBlockFromItem(stack.getItem()) instanceof CompressedBlock)) return false;
      //this will be true if a block is already here in which case it's not a valid decompression recipe
      if (!toDecompress.isEmpty())return false;
        toDecompress = stack;
    }
    if (toDecompress.isEmpty())return false;
    this.toDecompress = toDecompress.getItem();
    return true;
  }

  /**
   * Returns an Item that is the result of this recipe
   *
   * @param inv crafting inventory
   */
  @Nonnull
  @Override
  public ItemStack getCraftingResult(CraftingInventory inv) {
    //don't need to search we already have it
    Block deCompress = Block.getBlockFromItem(toDecompress);
    return new ItemStack(((CompressedBlock)deCompress).deCompression,9);
  }

  /**
   * Used to determine if this recipe can fit in a grid of the given width/height
   *
   * @param width
   * @param height
   */
  @Override
  public boolean canFit(int width, int height) {
    return true;
  }

  @Nonnull
  @Override
  public IRecipeSerializer<?> getSerializer() {
    return Compressed.decompression;
  }
}

