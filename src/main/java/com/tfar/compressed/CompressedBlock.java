package com.tfar.compressed;


import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CompressedBlock extends Block {

  public final int compression_level;
  public final ResourceLocation material_name;

  public CompressedBlock(Properties properties, int compression_level, ResourceLocation material_name) {
    super(properties);
    this.compression_level = compression_level;
    this.material_name = material_name;
  }

  @Override
  @Nonnull
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(@Nonnull ItemStack stack, @Nullable IBlockReader worldIn,@Nonnull List<ITextComponent> tooltip,@Nonnull ITooltipFlag flagIn) {
    tooltip.add(new StringTextComponent(this.getNumber().toString()+ " blocks"));
  }

  @Override
  public String getTranslationKey() {
    return "block.compressed";
  }

  protected BigInteger getNumber(){
    BigInteger value = BigInteger.valueOf(1);
    for (int i = 0; i < this.compression_level; i++) value = value.multiply(BigInteger.valueOf(9));
    return value;
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
    return new ArrayList<>(Collections.singletonList(new ItemStack(this)));
  }
}
