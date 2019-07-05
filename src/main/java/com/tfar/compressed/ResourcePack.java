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
    String dir = "resourcepacks/compressed";
    Path path = Paths.get(dir);
    //  if (!Files.exists(path)) {
    try {
      Files.createDirectories(path);
      dir += "/assets";
      Path assets = Paths.get(dir);
      Files.createDirectories(assets);
      //  File file = new File("resourcepacks/compressed/pack.mcmeta");
      File mcmeta = new File("resourcepacks/compressed/pack.mcmeta");
      if (!mcmeta.exists()) {
        String str = "{\n" +
                "    \"pack\": {\n" +
                "        \"description\": \"compressed resources\",\n" +
                "        \"pack_format\": 4,\n" +
                "        \"_comment\": \"A pack_format of 4 requires json lang files. Note: we require v4 pack meta for all mods.\"\n" +
                "    }\n" +
                "}";
        FileWriter writer = new FileWriter(mcmeta);
        writer.write(str);
        writer.flush();
      }
      dir += "/compressed";
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
        File blockstatefile = new File(blockstates + "/" + block.material_name.getPath() + "_x" + block.compression_level + ".json");
        if (!blockstatefile.exists()) {
          String model = "compressed:block/cube_all_" + block.material_name.getPath();
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

        File itemfile = new File(items+"/" + block.material_name.getPath() + "_x" + block.compression_level + ".json");
        if (!itemfile.exists()) {

          JsonObject itemmodel = new JsonObject();
          itemmodel.addProperty("parent", "compressed:block/cube_all_" + block.material_name.getPath());

          FileWriter writer = new FileWriter(itemfile);
          writer.write(Configs.g.toJson(itemmodel));
          writer.flush();
        }
      }
      for (ResourceLocation entry: Compressed.registrynames){
        File blockfile = new File(blocks+"/cube_all_" + entry.getPath() +".json");
        if (!blockfile.exists()) {

          JsonObject blockmodel = new JsonObject();
          blockmodel.addProperty("parent", "compressed:block/cube_all_tinted");
          JsonObject textures = new JsonObject();
          textures.addProperty("all","block/"+entry.getPath());
          blockmodel.add("textures",textures);
          FileWriter writer = new FileWriter(blockfile);
          writer.write(Configs.g.toJson(blockmodel));
          writer.flush();
        }
      }
    } catch (IOException e) {
      //fail to create directory
      e.printStackTrace();
    }
    //  }
  }
}
