package com.tfar.compressed.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tfar.compressed.Compressed;
import com.tfar.compressed.CompressedBlock;
import com.tfar.compressed.ResourcePack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.resources.ClientResourcePackInfo;
import net.minecraft.item.BlockItem;
import net.minecraft.resources.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static com.tfar.compressed.Configs.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class CompressedColor {

  private static CompressedResourcePack resourcePack = new CompressedResourcePack();

  @SubscribeEvent
  public static void setupResourcePack(FMLClientSetupEvent event) {
  //  if (config_loaded_blocks != -1)return;
    handle();
    ResourcePack.makeResourcePack();

    ResourcePackList<ClientResourcePackInfo> rps = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getInstance(), "field_110448_aq");
    rps.addPackFinder(new IPackFinder() {

      @Override
      public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> nameToPackMap, ResourcePackInfo.IFactory<T> packInfoFactory) {
        NativeImage img = null;
        try {
          img = NativeImage.read(resourcePack.getRootResourceStream("pack.png"));
        } catch (IOException e) {
          LogManager.getLogger().error("Could not load compressed's pack.png", e);
        }
        @SuppressWarnings("unchecked")
        T var3 = (T) new ClientResourcePackInfo("compressed", true, () -> resourcePack, new StringTextComponent(resourcePack.getName()), new StringTextComponent("Assets for Compressed"),
                PackCompatibility.COMPATIBLE, ResourcePackInfo.Priority.BOTTOM, true, img,true);
        nameToPackMap.put("compressed", var3);
      }
    });

    Minecraft.getInstance().getResourceManager().addResourcePack(resourcePack);

    //ForgeHooksClient.refreshResources(Minecraft.getInstance(), VanillaResourceType.MODELS);
  }

  private static void handle() {

    try {
      FileReader reader = new FileReader(configFile);
      JsonElement element = new JsonParser().parse(reader);
      FileWriter writer = new FileWriter(configFile);
      JsonObject config = element.getAsJsonObject();
      config.remove("loaded_blocks");
      config.addProperty("loaded_blocks", Compressed.MOD_BLOCKS.size());
      writer.write(g.toJson(config,JsonObject.class));
      writer.flush();
    } catch (IOException ugh) {
      throw new RuntimeException("Impossible, the file existed moments ago", ugh);
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
