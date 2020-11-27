package com.tfar.compact.client;

import com.tfar.compact.Compact;
import com.tfar.compact.CompressedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class CompressedColor {

  @SubscribeEvent
  @SuppressWarnings("unused")
  public static void registerBlockColors(ColorHandlerEvent.Block event) {
    BlockColors colors = event.getBlockColors();
    final IBlockColor compressedColor = (state, blockAccess, pos, tintIndex) -> Math.max(0,0xffffff - 0x080808 * ((CompressedBlock) state.getBlock()).compression_level + (tintIndex == 0 ?  0 : 4));
    for (CompressedBlock block : Compact.MOD_BLOCKS)
      colors.register(compressedColor, block);
  }

  @SubscribeEvent
  @SuppressWarnings("unused")
  public static void registerItemColors(final ColorHandlerEvent.Item event) {
    final ItemColors itemColors = event.getItemColors();
    final BlockColors blockColors = event.getBlockColors();


    final IItemColor itemBlockColor = (stack, tintIndex) -> {
      final BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
      return blockColors.getColor(state, null, null,tintIndex);
    };
    for (CompressedBlock block : Compact.MOD_BLOCKS)
      itemColors.register(itemBlockColor, block);
  }
}
