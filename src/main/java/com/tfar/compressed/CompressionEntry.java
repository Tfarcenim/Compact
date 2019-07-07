package com.tfar.compressed;

import net.minecraft.util.ResourceLocation;

public class CompressionEntry {
  public final ResourceLocation registry_name;
  public final String texture;

  public CompressionEntry(ResourceLocation registry_name, String texture) {
    this.registry_name = registry_name;
    this.texture = texture;
  }
}
