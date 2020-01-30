package com.tfar.compact.recipes;

import com.tfar.compact.Compact;
import com.tfar.compact.CompressedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class CompressionRecipe extends SpecialRecipe {

  public CompressionRecipe(ResourceLocation id) {
    super(id);
  }

  @Override
  public boolean matches(CraftingInventory inv, World worldIn) {

    //search for compression recipes
    final ItemStack cachedStack = inv.getStackInSlot(0);
    if (inv.getSizeInventory() != 9 || (!Compact.compressible.contains(Block.getBlockFromItem(cachedStack.getItem()))

            && !(Block.getBlockFromItem(cachedStack.getItem()) instanceof CompressedBlock))
    ) return false;
    else {
      for (int j = 0; j < inv.getSizeInventory(); ++j) {
        // if the item doesn't match the original it's not a compression recipe
        if (cachedStack.getItem() != inv.getStackInSlot(j).getItem()) {
          return false;
        }
      }
    }
    return Compact.compressible.contains(Block.getBlockFromItem(cachedStack.getItem()))
            || ((CompressedBlock) Block.getBlockFromItem(cachedStack.getItem())).compression != Blocks.AIR;
  }

  /**
   * Returns an Item that is the result of this recipe
   *
   * @param inv
   */
  @Nonnull
  @Override
  public ItemStack getCraftingResult(CraftingInventory inv) {
    //don't bother searching we already have it
    final ItemStack toCompress = inv.getStackInSlot(0);
    Block block = Block.getBlockFromItem(toCompress.getItem());
    if (block instanceof CompressedBlock) {
      return new ItemStack(((CompressedBlock) block).compression);
    } else {
      // there is no easy way to retrieve compression levels from blocks we don't have access to
      String path = block.getRegistryName().getPath();
      String domain = block.getRegistryName().getNamespace().equals("minecraft") ? "" : block.getRegistryName().getNamespace() + ".";
      return new ItemStack(ForgeRegistries.BLOCKS.getValue(
              new ResourceLocation(Compact.MODID,domain + path + "_x" + 1)));
    }
  }

  @Nonnull
  @Override
  public IRecipeSerializer<?> getSerializer() {
    return Compact.compression;
  }

  @Override
  public boolean canFit(int width, int height) {
    return width >= 3 && height >= 3;
  }
}

