package com.tfar.compressed;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(value = Compressed.MODID)
public class Compressed {
  // Directly reference a log4j logger.
  public static final Logger LOGGER = LogManager.getLogger();

  public static final String MODID = "compressed";
  public static final List<BlockCompressed> MOD_BLOCKS = new ArrayList<>();
  public static final List<ResourceLocation> registrynames = new ArrayList<>();
  public static final Set<Block> compressible = new HashSet<>();


  static {
    try {
      if (false)
        Scripts.jsonStuff();
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  @SubscribeEvent
  public static void registerSerials(RegistryEvent.Register<IRecipeSerializer<?>> event) {

    IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();
    SpecialRecipeSerializer<CompressionRecipe> obj = new SpecialRecipeSerializer<>(CompressionRecipe::new);
    obj.setRegistryName("compression");
    registry.register(obj);
  }

  @SubscribeEvent
  public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {

    Configs.handleConfig();

    for (ResourceLocation registryname : registrynames) {
      Block block = ForgeRegistries.BLOCKS.getValue(registryname);
      compressible.add(block);
    }

    IForgeRegistry<Block> registry = blockRegistryEvent.getRegistry();

    Block.Properties properties = Block.Properties.create(Material.ROCK, MaterialColor.DIRT).hardnessAndResistance(1.5F, 6.0F);

    for (ResourceLocation material : registrynames)
    for (int i = 1; i < Configs.max + 1; i++)
      registerBlock(new BlockCompressed(properties, i, material), material.getPath()+"_x" + i, registry);
  }

  @SubscribeEvent
  public static void onItemRegistry(final RegistryEvent.Register<Item> blockRegistryEvent) {

    IForgeRegistry<Item> registry = blockRegistryEvent.getRegistry();

    Item.Properties properties1 = new Item.Properties().group(ItemGroup.MISC);

    for (Block block : MOD_BLOCKS) {
      registerItem(new BlockItem(block, properties1), block.getRegistryName().toString(), registry);
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

  @ObjectHolder(MODID + ":compression")
  public static final IRecipeSerializer<?> RECIPE = null;

}


