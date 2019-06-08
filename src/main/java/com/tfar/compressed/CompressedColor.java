package com.tfar.compressed;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ColorHandlerEvent;

public class CompressedColor {

  public static void registerBlockColors(ColorHandlerEvent.Block event) {
    BlockColors colors = event.getBlockColors();
    final IBlockColor compressedColor = (state, blockAccess, pos, tintIndex) -> Math.max(0,0xffffff - 0x080808 * ((BlockCompressed) state.getBlock()).compression_level + (tintIndex == 0 ?  0 : 4));
    for (BlockCompressed block : Compressed.MOD_BLOCKS)
      colors.register(compressedColor, block);
  }

  public static void registerItemColors(final ColorHandlerEvent.Item event) {
    final ItemColors itemColors = event.getItemColors();
    final BlockColors blockColors = event.getBlockColors();


    final IItemColor itemBlockColor = (stack, tintIndex) -> {
      final IBlockState state = ((ItemBlock) stack.getItem()).getBlock().getDefaultState();
      return blockColors.getColor(state, null, null, tintIndex);
    };
    for (BlockCompressed block : Compressed.MOD_BLOCKS)
      itemColors.register(itemBlockColor, block);
  }
}
