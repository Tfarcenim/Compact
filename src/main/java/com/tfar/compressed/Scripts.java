package com.tfar.compressed;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Scripts {

  public static Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

  public static void jsonStuff() throws IOException {

    for (CompressedBlock block : Compressed.MOD_BLOCKS) {
      blockstates(block.compression_level, block.material_name.getPath());
      item(block.compression_level, block.material_name.getPath());
    }
    lang();
  }

  private static void blockstates(int compression_level, String material) throws IOException {

    //if (blockstatefile.exists()) return;
    String model = "compressed:block/cube_all_" + material;
    JsonObject obj = new JsonObject();
    obj.addProperty("model", model);
    JsonObject variants = new JsonObject();
    variants.add("", obj);

    JsonObject blockstates = new JsonObject();
    blockstates.add("variants", variants);

    File blockstatefile = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\Compressed\\src\\main\\resources\\assets\\compressed\\blockstates\\" + material + "_x" + compression_level + ".json");
    FileWriter writer = new FileWriter(blockstatefile);
    writer.write(g.toJson(blockstates));
    writer.flush();
  }

  private static void item(int compression_level, String material) throws IOException {
    File itemfile = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\Compressed\\src\\main\\resources\\assets\\compressed\\models\\item\\" + material + "_x" + compression_level + ".json");
    // if (blockfile.exists()) return;

    JsonObject item = new JsonObject();
    item.addProperty("parent", "compressed:block/cube_all_" + material);

    FileWriter writer = new FileWriter(itemfile);
    writer.write(g.toJson(item));
    writer.flush();
  }

  private static void lang() throws IOException {
    JsonObject lang = new JsonObject();
    for (CompressedBlock block : Compressed.MOD_BLOCKS){
      lang.addProperty("block.compressed."+block.material_name+"_x"+block.compression_level, block.compression_level+"x"+" Compressed "+ block.material_name.getPath().substring(0,1).toUpperCase()+block.material_name.getPath().substring(1));
    }
    File langfile = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\Compressed\\src\\main\\resources\\assets\\compressed\\lang\\en_us.json");
    FileWriter writer = new FileWriter(langfile);
    writer.write(g.toJson(lang));
    writer.flush();
  }
}



