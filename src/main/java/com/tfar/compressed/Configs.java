package com.tfar.compressed;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Configs {

  public static Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
  public static int config_loaded_blocks;

  public static final List<CompressionEntry> COMPRESSION_ENTRIES = new ArrayList<>();

  public static File configFile = new File("config/compressed.json");
  private static BufferedInputStream in = new BufferedInputStream(Configs.class.getResourceAsStream("/default.json"));

  public static int max;
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
      max = config.getAsJsonObject().get("max").getAsInt();
      config_loaded_blocks = config.getAsJsonObject().get("loaded_blocks").getAsInt();
      for (JsonElement element : config.getAsJsonObject().get("compressible").getAsJsonArray()) {
        COMPRESSION_ENTRIES.add(new CompressionEntry(new ResourceLocation(element.getAsJsonObject().get("registry_name").getAsString())
                ,element.getAsJsonObject().get("texture").getAsString()));
        String registryName = element.getAsJsonObject().get("registry_name").getAsString();
        Compressed.registrynames.add(new ResourceLocation(registryName));
      }
    } catch (Exception e) {
      Compressed.LOGGER.fatal(e);
    }
  }
}
