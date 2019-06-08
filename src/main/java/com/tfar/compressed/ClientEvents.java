package com.tfar.compressed;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.CLIENT)
@SuppressWarnings("unused")
public class ClientEvents {
  @SubscribeEvent
  public static void colors(ColorHandlerEvent.Block e) {
    CompressedColor.registerBlockColors(e);
  }

  @SubscribeEvent
  public static void colors(ColorHandlerEvent.Item e) {
    CompressedColor.registerItemColors(e);
  }
}
