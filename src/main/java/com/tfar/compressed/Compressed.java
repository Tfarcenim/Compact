package com.tfar.compressed;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(value = Compressed.MODID)
public class Compressed {
  // Directly reference a log4j logger.
  private static final Logger LOGGER = LogManager.getLogger();

  public static final String MODID = "compressed";

  public static final List<BlockCompressed> MOD_BLOCKS = new ArrayList<>();

  public static final List<String> registrynames = new ArrayList<>();

  static {
    try {
      if (false)
        Scripts.jsonStuff();
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  static {
    registrynames.add("cobblestone");
    registrynames.add("dirt");
    registrynames.add("netherrack");
    registrynames.add("sand");
    registrynames.add("gravel");
  }

  @SubscribeEvent
  public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {

    IForgeRegistry<Block> registry = blockRegistryEvent.getRegistry();

    Block.Properties properties = Block.Properties.create(Material.ROCK, MaterialColor.DIRT).hardnessAndResistance(1.5F, 6.0F);

    for (String material : registrynames)
    for (int i = 1; i < 33; i++)
      registerBlock(new BlockCompressed(properties, i, material), material+"_x" + i, registry);

  }

  @SubscribeEvent
  public static void onItemRegistry(final RegistryEvent.Register<Item> blockRegistryEvent) {

    IForgeRegistry<Item> registry = blockRegistryEvent.getRegistry();

    Item.Properties properties1 = new Item.Properties().group(ItemGroup.MISC);

    for (Block block : MOD_BLOCKS) {
      registerItem(new ItemBlock(block, properties1), block.getRegistryName().toString(), registry);
    }
  }

  private static void registerBlock(BlockCompressed block, String name, IForgeRegistry<Block> registry) {
    block.setRegistryName(name);
    registry.register(block);
    MOD_BLOCKS.add(block);
  }

  private static void registerItem(Item item, String name, IForgeRegistry<Item> registry) {
    item.setRegistryName(name);
    registry.register(item);
  }
}


