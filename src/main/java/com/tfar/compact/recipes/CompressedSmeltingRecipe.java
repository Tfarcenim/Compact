package com.tfar.compact.recipes;

import com.tfar.compact.Compact;
import com.tfar.compact.CompressedBlock;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class CompressedSmeltingRecipe extends AbstractCookingRecipe {
  public CompressedSmeltingRecipe(IRecipeType<?> p_i50032_1_, ResourceLocation p_i50032_2_, String p_i50032_3_, Ingredient p_i50032_4_, ItemStack p_i50032_5_, float p_i50032_6_, int p_i50032_7_) {
    super(p_i50032_1_, p_i50032_2_, p_i50032_3_, p_i50032_4_, p_i50032_5_, p_i50032_6_, p_i50032_7_);
  }

  @Override
  public boolean matches(IInventory iInventory, World world) {
    ItemStack inputStack = iInventory.getStackInSlot(0);
    ItemStack[] stacks = this.ingredient.getMatchingStacks();
    for (ItemStack stack : stacks)
      if (Block.getBlockFromItem(stack.getItem()) instanceof CompressedBlock) {
        CompressedBlock compressedBlock = (CompressedBlock)Block.getBlockFromItem(stack.getItem());
        Block blockBase = compressedBlock.base_block;
        if (world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(new ItemStack(
                blockBase)), world).isPresent()
                && Compact.compressible.contains(Block.getBlockFromItem(world.getRecipeManager()
                .getRecipe(IRecipeType.SMELTING, new Inventory(new ItemStack(blockBase)),
                world).get().getRecipeOutput().getItem()))) {
          return true;
        }
      }
      return false;
  }

  @Override
  public ItemStack getRecipeOutput() {
    return new ItemStack(Items.NETHER_STAR);
  }

  @Nonnull
  @Override
  public IRecipeSerializer<?> getSerializer() {
    return Compact.smelting;
  }
}
