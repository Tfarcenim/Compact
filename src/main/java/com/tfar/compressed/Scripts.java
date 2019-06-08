package com.tfar.compressed;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Scripts {

  public static Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

  private static final String[] recipe = {
          "###",
          "###",
          "###"
  };

  public static void jsonStuff() throws IOException {

    for (BlockCompressed block : Compressed.MOD_BLOCKS) {
      blockstates(block.compression_level, block.material_name);
      block(block.compression_level, block.material_name);
      item(block.compression_level, block.material_name);
      recipe(block.compression_level, block.material_name);
      reverse(block.compression_level, block.material_name);
    }
    lang();
  }

  private static void blockstates(int compression_level, String material) throws IOException {

    //if (blockstatefile.exists()) return;
    JsonObject variants = new JsonObject();
    String model = "compressed:block/" + material + "_x" + compression_level;
    JsonObject obj = new JsonObject();
    obj.addProperty("model", model);
    variants.add("", obj);

    JsonObject blockstates = new JsonObject();
    blockstates.add("variants", variants);

    File blockstatefile = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\Compressed\\1.13\\src\\main\\resources\\assets\\compressed\\blockstates\\" + material + "_x" + compression_level + ".json");
    FileWriter writer = new FileWriter(blockstatefile);
    writer.write(g.toJson(blockstates));
    writer.flush();
  }

  private static void block(int compression_level, String material) throws IOException {
    // if (blockfile.exists()) return;
    JsonObject all = new JsonObject();
    all.addProperty("all", "block/" + material);

    JsonObject block = new JsonObject();
    block.addProperty("parent", "compressed:block/cube_all_tinted");
    block.add("textures", all);

    File blockfile = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\Compressed\\1.13\\src\\main\\resources\\assets\\compressed\\models\\block\\" + material + "_x" + compression_level + ".json");
    FileWriter writer = new FileWriter(blockfile);
    writer.write(g.toJson(block));
    writer.flush();
  }

  private static void item(int compression_level, String material) throws IOException {
    File itemfile = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\Compressed\\1.13\\src\\main\\resources\\assets\\compressed\\models\\item\\" + material + "_x" + compression_level + ".json");
    // if (blockfile.exists()) return;

    JsonObject item = new JsonObject();
    item.addProperty("parent", "compressed:block/" + material + "_x" + compression_level);

    FileWriter writer = new FileWriter(itemfile);
    writer.write(g.toJson(item));
    writer.flush();
  }

  private static void recipe(int compression_level, String material) throws IOException {

    JsonObject recipe = new JsonObject();
    recipe.addProperty("type", "crafting_shaped");
    JsonArray pattern = new JsonArray();
    for (String s : Scripts.recipe)
      pattern.add(s);
    recipe.add("pattern", pattern);

    JsonObject a = new JsonObject();
    a.addProperty("item", (compression_level != 1) ? "compressed:" + material + "_x" + (compression_level - 1) : "minecraft:" + material);
    JsonObject key = new JsonObject();
    key.add("#", a);
    recipe.add("key", key);
    JsonObject result = new JsonObject();
    result.addProperty("item", "compressed:" + material + "_x" + compression_level);
    recipe.add("result", result);
    File recipefile = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\Compressed\\1.13\\src\\main\\resources\\data\\compressed\\recipes\\" + material + "_x" + compression_level + ".json");
    FileWriter writer = new FileWriter(recipefile);
    writer.write(g.toJson(recipe));
    writer.flush();
  }

  private static void reverse(int compression_level, String material) throws IOException {

    JsonObject recipe = new JsonObject();
    recipe.addProperty("type", "crafting_shapeless");
    JsonArray ingredients = new JsonArray();
    JsonObject item = new JsonObject();

    item.addProperty("item", "compressed:" + material + "_x" + compression_level);
    ingredients.add(item);
    recipe.add("ingredients", ingredients);

    JsonObject result = new JsonObject();
    result.addProperty("item", (compression_level != 1) ? "compressed:" + material + "_x" + (compression_level - 1) : "minecraft:" + material);
    result.addProperty("count", 9);
    recipe.add("result", result);
    File recipefile = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\Compressed\\1.13\\src\\main\\resources\\data\\compressed\\recipes\\" + material + "_x" + compression_level + "_reverse.json");
    FileWriter writer = new FileWriter(recipefile);
    writer.write(g.toJson(recipe));
    writer.flush();
  }
  private static void lang() throws IOException {
    JsonObject lang = new JsonObject();
    for (BlockCompressed block : Compressed.MOD_BLOCKS){
      lang.addProperty("block.compressed."+block.material_name+"_x"+block.compression_level, block.compression_level+"x"+" Compressed "+ block.material_name.substring(0,1).toUpperCase()+block.material_name.substring(1));
    }
    File langfile = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\Compressed\\1.13\\src\\main\\resources\\assets\\compressed\\lang\\en_us.json");
    FileWriter writer = new FileWriter(langfile);
    writer.write(g.toJson(lang));
    writer.flush();
  }
}



