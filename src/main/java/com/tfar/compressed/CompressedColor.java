package com.tfar.compressed;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.ClientResourcePackInfo;
import net.minecraft.item.BlockItem;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class CompressedColor {

  @SubscribeEvent
  public static void setupResourcePack(FMLClientSetupEvent event){
    ResourcePack.makeResourcePack();
 //   ResourcePackList<ClientResourcePackInfo> resourcePackList = Minecraft.getInstance().getResourcePackList();
//    ClientResourcePackInfo modResourcePack = resourcePackList.getPackInfo("file/compressed");
  //  Minecraft.getInstance().getResourcePackList().enabled.add(modResourcePack);

    //   List<ClientResourcePackInfo> collectionEnabl = (List<ClientResourcePackInfo>) Minecraft.getInstance().getResourcePackList().getEnabledPacks();
  //  for (ClientResourcePackInfo clientResourcePackInfo : collectionEnabl) {
//      if ("file/compressed".equals(clientResourcePackInfo.getName())){
     //   ObfuscationReflectionHelper.setPrivateValue(ResourcePackInfo.class,clientResourcePackInfo,true,"field_195806_h");
  //    }
  //  }
    //Minecraft.getInstance().getResourcePackList().addPackFinder(new MargResourcePackFinder(file, Compressed.MODID));
  }

  @SubscribeEvent
  @SuppressWarnings("unused")
  public static void registerBlockColors(ColorHandlerEvent.Block event) {
    BlockColors colors = event.getBlockColors();
    final IBlockColor compressedColor = (state, blockAccess, pos, tintIndex) -> Math.max(0,0xffffff - 0x080808 * ((CompressedBlock) state.getBlock()).compression_level + (tintIndex == 0 ?  0 : 4));
    for (CompressedBlock block : Compressed.MOD_BLOCKS)
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
    for (CompressedBlock block : Compressed.MOD_BLOCKS)
      itemColors.register(itemBlockColor, block);
  }
}
