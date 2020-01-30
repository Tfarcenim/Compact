package com.tfar.compact.client;

import com.google.common.collect.ImmutableSet;
import com.tfar.compact.Compact;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.resources.data.PackMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.*;
import java.util.function.Predicate;

public class CompressedResourcePack implements IResourcePack{

  public static final String loc ="compact_resources";

  @Override
  public InputStream getResourceStream(ResourcePackType type, ResourceLocation location) throws IOException {
    if (!resourceExists(type,location)) {
      return null;
    }
    if (type == ResourcePackType.CLIENT_RESOURCES) {


        File file = new File(new File(Minecraft.getInstance().gameDir, loc+"/" + location.getNamespace()), location.getPath());

        String realFileName = file.getCanonicalFile().getName();
        if (!realFileName.equals(file.getName()))
        {
          Compact.LOGGER.warn("[Compressed] Resource Location " + location.toString() + " only matches the file " + realFileName + " because RL is running in an environment that isn't case sensitive in regards to file names. This will not work properly on for example Linux.");
        }

        return new FileInputStream(file);
          }
    throw new FileNotFoundException(location.toString());
  }

  @Override
  @Nonnull
  public Collection<ResourceLocation> func_225637_a_(ResourcePackType p_225637_1_, String p_225637_2_, String p_225637_3_, int p_225637_4_, Predicate<String> p_225637_5_) {
    File folder = new File(Minecraft.getInstance().gameDir, loc);
    if (!folder.exists())
    {
      folder.mkdir();
    }
    HashSet<ResourceLocation> folders = new HashSet<>();

    Compact.LOGGER.log(Level.DEBUG, "Resource Loader Domains: ");

    File[] resourceDomains = folder.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

    for (File resourceFolder : resourceDomains)
    {
      Compact.LOGGER.info( "[NormalResourceLoader]  - " + resourceFolder.getName() + " | " + resourceFolder.getAbsolutePath());
      folders.add(new ResourceLocation(Compact.MODID,resourceFolder.getName()));
    }

    return folders;
  }

  @Override
  public boolean resourceExists(ResourcePackType type, ResourceLocation location) {
      File fileRequested = new File(new File( loc+"/" + location.getNamespace()), location.getPath());

      if (!fileRequested.isFile())
      {
        //Compressed.LOGGER.warn( "[Compressed] Asked for resource " + location.toString() + " but can't find a file at " + fileRequested.getAbsolutePath());
      }
      return fileRequested.isFile();
    }

  @Override
  public Set<String> getResourceNamespaces(ResourcePackType type) {
    return type == ResourcePackType.CLIENT_RESOURCES ? ImmutableSet.of(Compact.MODID) : Collections.emptySet();
  }

  @SuppressWarnings({ "unchecked", "null" })
  @Override
  public <T> T getMetadata(IMetadataSectionSerializer<T> arg0) {
    return "pack".equals(arg0.getSectionName()) ? (T) new PackMetadataSection(new StringTextComponent("Compressed resources"), 4) : null;
  }

  @Override
  public String getName() {
    return "Compressed resource pack";
  }

  public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType arg0, String arg1, int arg2, Predicate<String> arg3) {
    File folder = new File(Minecraft.getInstance().gameDir, loc);
    if (!folder.exists())
    {
      folder.mkdir();
    }
    HashSet<ResourceLocation> folders = new HashSet<>();

    Compact.LOGGER.log(Level.DEBUG, "Resource Loader Domains: ");

    File[] resourceDomains = folder.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

    for (File resourceFolder : resourceDomains)
    {
      Compact.LOGGER.info( "[NormalResourceLoader]  - " + resourceFolder.getName() + " | " + resourceFolder.getAbsolutePath());
      folders.add(new ResourceLocation(Compact.MODID,resourceFolder.getName()));
    }

    return folders;
  }

  @Override
  public InputStream getRootResourceStream(String arg0) throws IOException {
    File file = new File(Minecraft.getInstance().gameDir, loc+"/");

    String realFileName = file.getCanonicalFile().getName();
    if (!realFileName.equals(file.getName()))
    {
      //Compressed.LOGGER.log(Level.WARN, "[Compressed] Resource Location " + location.toString() + " only matches the file " + realFileName + " because RL is running in an environment that isn't case sensitive in regards to file names. This will not work properly on for example Linux.");
    }

    return new FileInputStream(file);
  }

  @Override
  public void close() {}
}