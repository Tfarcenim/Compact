package com.tfar.compressed;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;

public class Configs {

  public static Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

  private static boolean error = false;
  private static File configFile = new File("config/compressed.json");
  private static BufferedInputStream in = new BufferedInputStream(Configs.class.getResourceAsStream("/default.json"));
  public static JsonArray jsonRead;

  public static int max;

  static {
    String s;
    try {
      s = IOUtils.toString(in, Charset.defaultCharset());
    } catch (IOException e) {
      throw new RuntimeException("The default config is broken, report to mod author asap!", e);
    }
    jsonRead = (JsonArray) g.fromJson(s, JsonObject.class).get("compressible");
    max = g.fromJson(s, JsonObject.class).getAsJsonPrimitive("max").getAsInt();
  }

  public static void handleConfig() {

    writeConfig();
    readConfig();

  }

  private static void writeConfig() {

    if (configFile.exists())return;
    try {
      FileWriter writer = new FileWriter(configFile);
      writer.write(g.toJson(jsonRead));
      writer.flush();
    } catch (IOException ugh) {
      //I expect this from a user, but you?!
      throw new RuntimeException("The default config is broken, report to mod author asap!", ugh);
    }

  }

  private static void readConfig() {
    try {
      FileReader reader = new FileReader(configFile);

      //config configFile
      JsonArray config = new JsonParser().parse(reader).getAsJsonArray();

      for (JsonElement element : config) {
        Compressed.registrynames.add(new ResourceLocation(element.getAsString()));
      }
    } catch (Exception e) {
      Compressed.LOGGER.fatal(e);
    }
  }
}
