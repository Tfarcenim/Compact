package com.tfar.compressed;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

public class Configs {

  public static Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
  public static int config_loaded_blocks;

  public static final Set<CompressionEntry> COMPRESSION_ENTRIES = new HashSet<>();

  public static final File configFile = new File("config/compressed.json");
  private static BufferedInputStream in = new BufferedInputStream(Configs.class.getResourceAsStream("/compressed.json"));

  public static String s;

  static {
    try {
      s = IOUtils.toString(in, Charset.defaultCharset());
    } catch (IOException e) {
      throw new RuntimeException("The default config is broken, report to mod author asap!", e);
    }
  }

  public static void handleConfig() {
    writeConfig();
    readConfig();
  }

  private static void writeConfig() {

    if (configFile.exists())return;
    try {
      FileWriter writer = new FileWriter(configFile);
      JsonElement element = g.fromJson(s,JsonElement.class);
      writer.write(g.toJson(element,JsonElement.class));
      writer.flush();
    } catch (IOException ugh) {
      //I expect this from a user, but you?!
      throw new RuntimeException("The default config is broken, report to mod author asap!", ugh);
    }
  }

  private static void readConfig() {
    try {
      FileReader reader = new FileReader(configFile);
      JsonElement config = new JsonParser().parse(reader);
      config_loaded_blocks = config.getAsJsonObject().get("loaded_blocks").getAsInt();
      for (JsonElement element : config.getAsJsonObject().get("compressible").getAsJsonArray()) {
        CompressionEntry entry = g.fromJson(element,CompressionEntry.class);
        COMPRESSION_ENTRIES.add(entry);
      }
    } catch (Exception e) {
      Compressed.LOGGER.fatal(e);
    }
  }
}
