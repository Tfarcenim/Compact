package com.tfar.compressed;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.resource.VanillaResourceType;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static com.tfar.compressed.Configs.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class CompressedColor {

  @SubscribeEvent
  public static void setupResourcePack(FMLClientSetupEvent event) {
    if (config_loaded_blocks == Compressed.MOD_BLOCKS.size())return;
    handle();
    // avoid time consuming reload
    ResourcePack.makeResourcePack();
    ResourcePackList<ClientResourcePackInfo> resourcePackList = Minecraft.getInstance().getResourcePackList();
    ClientResourcePackInfo modResourcePack = resourcePackList.getPackInfo("file/compressed");
    // force resource pack to be enabled, then when refreshed it'll be auto added with 0 user input.
    ObfuscationReflectionHelper.setPrivateValue(ResourcePackInfo.class,modResourcePack,true,"field_195806_h");
    List<ClientResourcePackInfo> enabled = (List<ClientResourcePackInfo>) resourcePackList.getEnabledPacks();
    enabled.add(modResourcePack);
    Minecraft.getInstance().resourcePackRepository.setEnabledPacks(enabled);
    //we only need to reload models
    ForgeHooksClient.refreshResources(Minecraft.getInstance(), VanillaResourceType.MODELS);
  }

  private static void handle() {

    try {
      FileWriter writer = new FileWriter(configFile);
      JsonObject element = g.fromJson(Configs.s,JsonElement.class).getAsJsonObject();
      element.remove("loaded_blocks");
      element.addProperty("loaded_blocks",Compressed.MOD_BLOCKS.size());
      writer.write(g.toJson(element,JsonObject.class));
      writer.flush();
    } catch (IOException ugh) {
      throw new RuntimeException("The default config is broken, report to mod author asap!", ugh);
    }
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
