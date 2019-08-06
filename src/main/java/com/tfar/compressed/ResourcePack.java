package com.tfar.compressed;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ResourcePack {
  public static void makeResourcePack() {
    String dir = "resources/compressed";
    Path path = Paths.get(dir);
      try {
        Files.createDirectories(path);
        File mcmeta = new File(dir+"/pack.mcmeta");
        if (!mcmeta.exists()) {
          String str = "{\n" +
                  "    \"pack\": {\n" +
                  "        \"description\": \"Assets for Compressed mod, DO NOT REMOVE!\",\n" +
                  "        \"pack_format\": 4,\n" +
                  "        \"_comment\": \"A pack_format of 4 requires json lang files. Note: we require v4 pack meta for all mods.\"\n" +
                  "    }\n" +
                  "}";
          FileWriter writer = new FileWriter(mcmeta);
          writer.write(str);
          writer.flush();
        }
        //dir += "/assets";
        Path assets = Paths.get(dir);
        Files.createDirectories(assets);

        Path main = Paths.get(dir);
        Files.createDirectories(main);
        String blockstates = dir + "/blockstates";
        String blocks = dir + "/models/block";
        String items = dir + "/models/item";
        Path main1 = Paths.get(blockstates);
        Files.createDirectories(main1);
        Path main2 = Paths.get(blocks);
        Files.createDirectories(main2);
        Path main3 = Paths.get(items);
        Files.createDirectories(main3);

        for (CompressedBlock block : Compressed.MOD_BLOCKS) {
          String domain = block.material_name.getNamespace().equals("minecraft") ? "" : block.material_name.getNamespace() + ".";
          File blockstatefile = new File(blockstates + "/" + domain + block.material_name.getPath() + "_x" + block.compression_level + ".json");
          if (!blockstatefile.exists()) {
            String model = "compressed:block/cube_all_" + domain + block.material_name.getPath();
            JsonObject obj = new JsonObject();
            obj.addProperty("model", model);
            JsonObject variants = new JsonObject();
            variants.add("", obj);

            JsonObject blockstate = new JsonObject();
            blockstate.add("variants", variants);

            FileWriter writer = new FileWriter(blockstatefile);
            writer.write(Configs.g.toJson(blockstate));
            writer.flush();
          }

          File itemfile = new File(items + "/" + domain +block.material_name.getPath() + "_x" + block.compression_level + ".json");
          if (!itemfile.exists()) {
            JsonObject itemmodel = new JsonObject();
            itemmodel.addProperty("parent", "compressed:block/cube_all_" + domain + block.material_name.getPath());

            FileWriter writer = new FileWriter(itemfile);
            writer.write(Configs.g.toJson(itemmodel));
            writer.flush();
          }
        }
        for (CompressionEntry entry : Configs.COMPRESSION_ENTRIES) {
          String domain = new ResourceLocation(entry.registry_name).getNamespace().equals("minecraft") ? "" : new ResourceLocation(entry.registry_name).getNamespace() + ".";
          File blockfile = new File(blocks + "/cube_all_" + domain + new ResourceLocation(entry.registry_name).getPath() + ".json");
          if (!blockfile.exists()) {

            JsonObject blockmodel = new JsonObject();
            blockmodel.addProperty("parent", "compressed:block/cube_all_tinted");
            JsonObject textures = new JsonObject();
            textures.addProperty("all",entry.texture);
            blockmodel.add("textures", textures);
            FileWriter writer = new FileWriter(blockfile);
            writer.write(Configs.g.toJson(blockmodel));
            writer.flush();
          }
        }
      } catch (IOException e) {
        //fail to create directory
        e.printStackTrace();
      }
    }
}
