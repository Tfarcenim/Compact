package com.tfar.compressed;


import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.List;

public class BlockCompressed extends Block {

  public final int compression_level;
  public final String material_name;

  public BlockCompressed(Properties properties, int compression_level, String material_name) {
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
  public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    tooltip.add(new TextComponentString(this.getNumber().toString()+ " blocks"));
  }

  protected BigInteger getNumber(){
    BigInteger value = BigInteger.valueOf(1);
    for (int i = 0; i < this.compression_level; i++) value = value.multiply(BigInteger.valueOf(9));
    return value;
  }
}
