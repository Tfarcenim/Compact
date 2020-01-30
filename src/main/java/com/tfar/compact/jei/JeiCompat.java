package com.tfar.compact.jei;

import com.tfar.compact.Compact;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JeiCompat implements IModPlugin {
  @Override
  public ResourceLocation getPluginUid() {
    return new ResourceLocation(Compact.MODID, Compact.MODID);
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    registration.addRecipes(CompressedRecipeMaker.createCompressions(), VanillaRecipeCategoryUid.CRAFTING);
    registration.addRecipes(CompressedRecipeMaker.createDeCompressions(), VanillaRecipeCategoryUid.CRAFTING);
  }
}
