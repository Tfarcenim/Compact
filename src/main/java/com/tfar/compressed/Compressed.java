package com.tfar.compressed;

import com.tfar.compressed.recipes.CompressionRecipe;
import com.tfar.compressed.recipes.DeCompressionRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(value = Compressed.MODID)
public class Compressed {

  public Compressed() {
    ResourcePack.makeResourcePack();
  }

  // Directly reference a log4j logger.
  public static final Logger LOGGER = LogManager.getLogger();

  public static final String MODID = "compressed";
  public static final List<CompressedBlock> MOD_BLOCKS = new ArrayList<>();
  public static final Set<Block> compressible = new HashSet<>();

  public static final ItemGroup tab = new ItemGroup(MODID) {
    @Override
    public ItemStack createIcon() {
      return new ItemStack(Blocks.COBBLESTONE);
    }
  };

  @SubscribeEvent
  public static void registerSerials(RegistryEvent.Register<IRecipeSerializer<?>> event) {

    IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();

    SpecialRecipeSerializer<CompressionRecipe> compression = new SpecialRecipeSerializer<>(CompressionRecipe::new);
    compression.setRegistryName("compression");
    registry.register(compression);

    SpecialRecipeSerializer<DeCompressionRecipe> decompression = new SpecialRecipeSerializer<>(DeCompressionRecipe::new);
    decompression.setRegistryName("decompression");
    registry.register(decompression);

    // CookingRecipeSerializer<CompressedSmeltingRecipe> smelting =
    //         new CookingRecipeSerializer<>(CompressedSmeltingRecipe::new,200);
    //  compression.setRegistryName("smelting");
    //  registry.register(compression);
  }

  @SubscribeEvent
  public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {

    Configs.handleConfig();

    IForgeRegistry<Block> registry = blockRegistryEvent.getRegistry();
    Block.Properties properties = Block.Properties.create(Material.ROCK, MaterialColor.DIRT).hardnessAndResistance(1.5F, 6.0F);

    for (CompressionEntry entry : Configs.COMPRESSION_ENTRIES) {
      for (int i = 1; i < entry.max_compression + 1; i++) {
        String domain = new ResourceLocation(entry.registry_name).getNamespace().equals("minecraft") ? "" : new ResourceLocation(entry.registry_name).getNamespace() + ".";
        registerBlock(new CompressedBlock(properties, i, new ResourceLocation(entry.registry_name)), domain + new ResourceLocation(entry.registry_name).getPath() + "_x" + i, registry);
      }
    }
  }

  @SubscribeEvent
  public static void onItemRegistry(final RegistryEvent.Register<Item> blockRegistryEvent) {

    IForgeRegistry<Item> registry = blockRegistryEvent.getRegistry();
    Item.Properties properties1 = new Item.Properties().group(tab);

    for (CompressedBlock block : MOD_BLOCKS) {
      registerItem(new BlockItem(block, properties1) {

        @Nonnull
        @Override
        public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
          return new TranslationTextComponent(getTranslationKey(),
                  ((CompressedBlock) ((BlockItem) stack.getItem()).getBlock()).compression_level)
                  .appendSibling(
                          new TranslationTextComponent("block." +
                                  ((CompressedBlock) ((BlockItem) stack.getItem())
                                          .getBlock()).material_name.getNamespace() + "." +
                                  ((CompressedBlock) ((BlockItem) stack.getItem())
                                          .getBlock()).material_name.getPath()));

        }

      }, block.getRegistryName().toString(), registry);
    }

  }


  private static void registerBlock(CompressedBlock block, String name, IForgeRegistry<Block> registry) {
    block.setRegistryName(name);
    registry.register(block);
    MOD_BLOCKS.add(block);
  }

  private static void registerItem(Item item, String name, IForgeRegistry<Item> registry) {
    item.setRegistryName(name);
    registry.register(item);
  }

  // this must be done after all block/item registry events are done or bad things may happen
  @SubscribeEvent
  public static void setup(FMLCommonSetupEvent e) {
    for (CompressionEntry entry : Configs.COMPRESSION_ENTRIES) {
      Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(entry.registry_name));

      if (block == null || block.isAir(block.getDefaultState(), null, null)) {
        Compressed.LOGGER.fatal("No block found for " + entry.registry_name);
        continue;
      }
      compressible.add(block);
      for (int i = 1; i < entry.max_compression + 1; i++) {
        String domain = new ResourceLocation(entry.registry_name).getNamespace().equals("minecraft") ? "" :
                new ResourceLocation(entry.registry_name).getNamespace() + ".";
        CompressedBlock compressedBlock = (CompressedBlock)ForgeRegistries.BLOCKS.getValue(
                new ResourceLocation(MODID,domain + new ResourceLocation(entry.registry_name).getPath() + "_x" + i));

        setCompression(compressedBlock,entry.max_compression);
        setDeCompression(compressedBlock);
       compressedBlock.base_block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(entry.registry_name));
      }
    }
  }


  private static void setDeCompression(CompressedBlock c) {
    if (c.compression_level == 1)
      c.setDeCompression(ForgeRegistries.BLOCKS.getValue(c.material_name));
    else {
      String domain = c.material_name.getNamespace().equals("minecraft") ? "" : c.material_name.getNamespace() + ".";
      c.setDeCompression(ForgeRegistries.BLOCKS.getValue(
              new ResourceLocation(Compressed.MODID,
                      domain + c.material_name.getPath() + "_x" + (c.compression_level - 1))));
    }
  }

  private static void setCompression(CompressedBlock c, int max_compression) {
    if (c.compression_level == max_compression)
      c.setCompression(Blocks.AIR);
    else {
      String domain = c.material_name.getNamespace().equals("minecraft") ? "" : c.material_name.getNamespace() + ".";
      c.setCompression(ForgeRegistries.BLOCKS.getValue(
              new ResourceLocation(Compressed.MODID,
                      domain + c.material_name.getPath() + "_x" + (c.compression_level + 1))));
    }
  }

  @ObjectHolder(MODID + ":compression")
  public static final IRecipeSerializer<?> compression = null;

  @ObjectHolder(MODID + ":decompression")
  public static final IRecipeSerializer<?> decompression = null;

  @ObjectHolder(MODID + ":smelting")
  public static final IRecipeSerializer<?> smelting = null;

}


