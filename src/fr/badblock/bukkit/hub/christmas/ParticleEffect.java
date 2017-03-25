package fr.badblock.bukkit.hub.christmas;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public enum ParticleEffect
{
  HUGE_EXPLOSION(
  





    "hugeexplosion"),  LARGE_EXPLODE(
  





    "largeexplode"),  FIREWORKS_SPARK(
  





    "fireworksSpark"),  BUBBLE(
  





    "bubble", true),  SUSPEND(
  





    "suspend", true),  DEPTH_SUSPEND(
  





    "depthSuspend"),  TOWN_AURA(
  





    "townaura"),  CRIT(
  





    "crit"),  MAGIC_CRIT(
  





    "magicCrit"),  SMOKE(
  





    "smoke"),  MOB_SPELL(
  





    "mobSpell"),  MOB_SPELL_AMBIENT(
  





    "mobSpellAmbient"),  SPELL(
  





    "spell"),  INSTANT_SPELL(
  





    "instantSpell"),  WITCH_MAGIC(
  





    "witchMagic"),  NOTE(
  





    "note"),  PORTAL(
  





    "portal"),  ENCHANTMENT_TABLE(
  





    "enchantmenttable"),  EXPLODE(
  





    "explode"),  FLAME(
  





    "flame"),  LAVA(
  





    "lava"),  FOOTSTEP(
  





    "footstep"),  SPLASH(
  





    "splash"),  WAKE(
  





    "wake"),  LARGE_SMOKE(
  





    "largesmoke"),  CLOUD(
  





    "cloud"),  RED_DUST(
  





    "reddust"),  SNOWBALL_POOF(
  





    "snowballpoof"),  DRIP_WATER(
  





    "dripWater"),  DRIP_LAVA(
  





    "dripLava"),  SNOW_SHOVEL(
  





    "snowshovel"),  SLIME(
  





    "slime"),  HEART(
  





    "heart"),  ANGRY_VILLAGER(
  





    "angryVillager"),  HAPPY_VILLAGER(
  





    "happyVillager");
  
  private static final Map<String, ParticleEffect> NAME_MAP;
  private final String name;
  private final boolean requiresWater;
  
  static
  {
    NAME_MAP = new HashMap<>();
    for (ParticleEffect effect : values()) {
      NAME_MAP.put(effect.name, effect);
    }
  }
  
  private ParticleEffect(String name, boolean requiresWater)
  {
    this.name = name;
    this.requiresWater = requiresWater;
  }
  
  private ParticleEffect(String name)
  {
    this(name, false);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public boolean getRequiresWater()
  {
    return this.requiresWater;
  }
  
  public static ParticleEffect fromName(String name)
  {
    for (Map.Entry<String, ParticleEffect> entry : NAME_MAP.entrySet()) {
      if (((String)entry.getKey()).equalsIgnoreCase(name)) {
        return (ParticleEffect)entry.getValue();
      }
    }
    return null;
  }
  
  private static boolean isWater(Location location)
  {
    Material material = location.getBlock().getType();
    return (material == Material.WATER) || (material == Material.STATIONARY_WATER);
  }
  
  private static boolean isBlock(int id)
  {
    @SuppressWarnings("deprecation")
	Material material = Material.getMaterial(id);
    return (material != null) && (material.isBlock());
  }
  
  public void display(Location center, float offsetY, float offsetZ, float speed, int amount, float offsetX, double range)
    throws IllegalArgumentException
  {
    if ((this.requiresWater) && (!isWater(center))) {
      throw new IllegalArgumentException("There is no water at the center location");
    }
    new ParticleEffectPacket(this.name, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, range);
  }
  
  public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players)
    throws IllegalArgumentException
  {
    if ((this.requiresWater) && (!isWater(center))) {
      throw new IllegalArgumentException("There is no water at the center location");
    }
    new ParticleEffectPacket(this.name, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, players);
  }
  
  public static void displayIconCrack(int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range)
  {
    new ParticleEffectPacket("iconcrack_" + id + "_" + data, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, range);
  }
  
  public static void displayIconCrack(int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players)
  {
    new ParticleEffectPacket("iconcrack_" + id + "_" + data, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, players);
  }
  
  public static void displayBlockCrack(int id, byte data, float offsetX, float offsetY, float offsetZ, int amount, Location center, double range)
    throws IllegalArgumentException
  {
    if (!isBlock(id)) {
      throw new IllegalArgumentException("Invalid block id");
    }
    new ParticleEffectPacket("blockcrack_" + id + "_" + data, offsetX, offsetY, offsetZ, 0.0F, amount).sendTo(center, range);
  }
  
  public static void displayBlockCrack(int id, byte data, float offsetX, float offsetY, float offsetZ, int amount, Location center, List<Player> players)
    throws IllegalArgumentException
  {
    if (!isBlock(id)) {
      throw new IllegalArgumentException("Invalid block id");
    }
    new ParticleEffectPacket("blockcrack_" + id + "_" + data, offsetX, offsetY, offsetZ, 0.0F, amount).sendTo(center, players);
  }
  
  public static void displayBlockDust(int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range)
    throws IllegalArgumentException
  {
    if (!isBlock(id)) {
      throw new IllegalArgumentException("Invalid block id");
    }
    new ParticleEffectPacket("blockdust_" + id + "_" + data, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, range);
  }
  
  public static void displayBlockDust(int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players)
    throws IllegalArgumentException
  {
    if (!isBlock(id)) {
      throw new IllegalArgumentException("Invalid block id");
    }
    new ParticleEffectPacket("blockdust_" + id + "_" + data, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, players);
  }
  
  public static final class ParticleEffectPacket
  {
    private static Constructor<?> packetConstructor;
    private static Method getHandle;
    private static Field playerConnection;
    private static Method sendPacket;
    private static boolean initialized;
    private final String name;
    private final float offsetX;
    private final float offsetY;
    private final float offsetZ;
    private final float speed;
    private final int amount;
    private Object packet;
    
    public ParticleEffectPacket(String name, float offsetX, float offsetY, float offsetZ, float speed, int amount)
      throws IllegalArgumentException
    {
      initialize();
      if (speed < 0.0F) {
        throw new IllegalArgumentException("The speed is lower than 0");
      }
      if (amount < 1) {
        throw new IllegalArgumentException("The amount is lower than 1");
      }
      this.name = name;
      this.offsetX = offsetX;
      this.offsetY = offsetY;
      this.offsetZ = offsetZ;
      this.speed = speed;
      this.amount = amount;
    }
    
    public static void initialize()
      throws ParticleEffect.ParticleEffectPacket.VersionIncompatibleException
    {
      if (initialized) {
        return;
      }
      try
      {
        int version = Integer.parseInt(Character.toString(ReflectionUtils.PackageType.getServerVersion().charAt(3)));
        Class<?> packetClass = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass(version < 7 ? "Packet63WorldParticles" : ReflectionUtils.PacketType.PLAY_OUT_WORLD_PARTICLES.getName());
        packetConstructor = ReflectionUtils.getConstructor(packetClass, new Class[0]);
        getHandle = ReflectionUtils.getMethod("CraftPlayer", ReflectionUtils.PackageType.CRAFTBUKKIT_ENTITY, "getHandle", new Class[0]);
        playerConnection = ReflectionUtils.getField("EntityPlayer", ReflectionUtils.PackageType.MINECRAFT_SERVER, false, "playerConnection");
        sendPacket = ReflectionUtils.getMethod(playerConnection.getType(), "sendPacket", new Class[] { ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("Packet") });
      }
      catch (Exception exception)
      {
        throw new VersionIncompatibleException("Your current bukkit version seems to be incompatible with this library", exception);
      }
      initialized = true;
    }
    
    public static boolean isInitialized()
    {
      return initialized;
    }
    
    public void sendTo(Location center, Player player)
      throws ParticleEffect.ParticleEffectPacket.PacketInstantiationException, ParticleEffect.ParticleEffectPacket.PacketSendingException
    {
      if (this.packet == null) {
        try
        {
          this.packet = packetConstructor.newInstance(new Object[0]);
          ReflectionUtils.setValue(this.packet, true, "a", this.name);
          ReflectionUtils.setValue(this.packet, true, "b", Float.valueOf((float)center.getX()));
          ReflectionUtils.setValue(this.packet, true, "c", Float.valueOf((float)center.getY()));
          ReflectionUtils.setValue(this.packet, true, "d", Float.valueOf((float)center.getZ()));
          ReflectionUtils.setValue(this.packet, true, "e", Float.valueOf(this.offsetX));
          ReflectionUtils.setValue(this.packet, true, "f", Float.valueOf(this.offsetY));
          ReflectionUtils.setValue(this.packet, true, "g", Float.valueOf(this.offsetZ));
          ReflectionUtils.setValue(this.packet, true, "h", Float.valueOf(this.speed));
          ReflectionUtils.setValue(this.packet, true, "i", Integer.valueOf(this.amount));
        }
        catch (Exception exception)
        {
          throw new PacketInstantiationException("Packet instantiation failed", exception);
        }
      }
      try
      {
        sendPacket.invoke(playerConnection.get(getHandle.invoke(player, new Object[0])), new Object[] { this.packet });
      }
      catch (Exception exception)
      {
        throw new PacketSendingException("Failed to send the packet to player '" + player.getName() + "'", exception);
      }
    }
    
    public void sendTo(Location center, List<Player> players)
      throws IllegalArgumentException
    {
      if (players.isEmpty()) {
        throw new IllegalArgumentException("The player list is empty");
      }
      for (Player player : players) {
        sendTo(center, player);
      }
    }
    
    public void sendTo(Location center, double range)
      throws IllegalArgumentException
    {
      if (range < 1.0D) {
        throw new IllegalArgumentException("The range is lower than 1");
      }
      String worldName = center.getWorld().getName();
      double squared = range * range;
      for (Player player : Bukkit.getOnlinePlayers()) {
        if ((player.getWorld().getName().equals(worldName)) && (player.getLocation().distanceSquared(center) <= squared)) {
          sendTo(center, player);
        }
      }
    }
    
    private static final class PacketInstantiationException
      extends RuntimeException
    {
      private static final long serialVersionUID = 3203085387160737484L;
      
      public PacketInstantiationException(String message, Throwable cause) {}
    }
    
    private static final class PacketSendingException
      extends RuntimeException
    {
      private static final long serialVersionUID = 3203085387160737484L;
      
      public PacketSendingException(String message, Throwable cause) {}
    }
    
    private static final class VersionIncompatibleException
      extends RuntimeException
    {
      private static final long serialVersionUID = 3203085387160737484L;
      
      public VersionIncompatibleException(String message, Throwable cause) {}
    }
  }
}
