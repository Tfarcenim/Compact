package com.tfar.compact;

public class CompressionEntry {
  public final String registry_name;
  public final String texture;
  public final int max_compression;

  public CompressionEntry(String registry_name, String texture, int max_compression) {
    this.registry_name = registry_name;
    this.texture = texture;
    this.max_compression = max_compression;
  }
}
