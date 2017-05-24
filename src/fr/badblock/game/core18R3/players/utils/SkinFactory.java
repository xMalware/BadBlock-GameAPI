package fr.badblock.game.core18R3.players.utils;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mojang.authlib.properties.PropertyMap;

public class SkinFactory {
	
	public static void applySkin(Player p, Property props)
	{
		try
		{
			if (props == null) {
				return;
			}
			Object ep = ReflectionUtil.invokeMethod(p.getClass(), p, "getHandle");
			Object profile = ReflectionUtil.invokeMethod(ep.getClass(), ep, "getProfile");
			Object propmap = ReflectionUtil.invokeMethod(profile.getClass(), profile, "getProperties");
			ReflectionUtil.invokeMethod(propmap, "clear");
			ReflectionUtil.invokeMethod(propmap.getClass(), propmap, "put", 
					new Class[] { Object.class, Object.class }, new Object[] { "textures", props });
			 WrappedGameProfile gameProfile = WrappedGameProfile.fromPlayer(p);
			 Multimap<String, WrappedSignedProperty> properties = gameProfile.getProperties();
			 properties.clear();
			 properties.put("textures", convertToProperty(props) );
			 sendUpdate(p, gameProfile);
		}
		catch (Exception localException) {}
	}
	
	  public static WrappedSignedProperty convertToProperty(Property property)
	  {
	    return WrappedSignedProperty.fromValues("textures", property.getValue(), property.getSignature());
	  }
	
	public static PropertyMap getPropertyMap(Player p)
	{
		try
		{
			Object ep = ReflectionUtil.invokeMethod(p.getClass(), p, "getHandle");
			Object profile = ReflectionUtil.invokeMethod(ep.getClass(), ep, "getProfile");
			Object propmap = ReflectionUtil.invokeMethod(profile.getClass(), profile, "getProperties");
			return (PropertyMap) propmap;
		}
		catch (Exception localException) {}
		return null;
	}
	private static void sendUpdate(Player player, WrappedGameProfile gameProfile)
			    throws FieldAccessException
			  {
			    sendUpdateSelf(player, gameProfile);
			    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			      if ((!onlinePlayer.equals(player)) && (onlinePlayer.canSee(player)))
			      {
			        onlinePlayer.hidePlayer(player);
			        onlinePlayer.showPlayer(player);
			      }
			    }
			  }
			  
			  @SuppressWarnings("deprecation")
			private static void sendUpdateSelf(Player player, WrappedGameProfile gameProfile)
			    throws FieldAccessException
			  {
			    Entity vehicle = player.getVehicle();
			    if (vehicle != null) {
			      vehicle.eject();
			    }
			    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
			    EnumWrappers.NativeGameMode gamemode = EnumWrappers.NativeGameMode.fromBukkit(player.getGameMode());
			    
			    PacketContainer removeInfo = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
			    removeInfo.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
			    
			    WrappedChatComponent displayName = WrappedChatComponent.fromText(player.getPlayerListName());
			    PlayerInfoData playerInfoData = new PlayerInfoData(gameProfile, 0, gamemode, displayName);
			    removeInfo.getPlayerInfoDataLists().write(0, Lists.newArrayList(new PlayerInfoData[] { playerInfoData }));
			    
			    PacketContainer addInfo = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
			    addInfo.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
			    addInfo.getPlayerInfoDataLists().write(0, Lists.newArrayList(new PlayerInfoData[] { playerInfoData }));
			    
			    PacketContainer respawn = protocolManager.createPacket(PacketType.Play.Server.RESPAWN);
			    respawn.getIntegers().write(0, Integer.valueOf(player.getWorld().getEnvironment().getId()));
			    respawn.getDifficulties().write(0, EnumWrappers.Difficulty.valueOf(player.getWorld().getDifficulty().toString()));
			    respawn.getGameModes().write(0, gamemode);
			    respawn.getWorldTypeModifier().write(0, player.getWorld().getWorldType());
			    
			    Location location = player.getLocation().clone();
			    
			    PacketContainer teleport = protocolManager.createPacket(PacketType.Play.Server.POSITION);
			    teleport.getModifier().writeDefaults();
			    teleport.getDoubles().write(0, Double.valueOf(location.getX()));
			    teleport.getDoubles().write(1, Double.valueOf(location.getY()));
			    teleport.getDoubles().write(2, Double.valueOf(location.getZ()));
			    teleport.getFloat().write(0, Float.valueOf(location.getYaw()));
			    teleport.getFloat().write(1, Float.valueOf(location.getPitch()));
			    
			    teleport.getIntegers().writeSafely(0, Integer.valueOf(64199));
			    try
			    {
			      protocolManager.sendServerPacket(player, removeInfo);
			      
			      protocolManager.sendServerPacket(player, addInfo);
			      
			      protocolManager.sendServerPacket(player, respawn);
			      
			      protocolManager.sendServerPacket(player, teleport);
			      
			      player.updateInventory();
			      
			      PlayerInventory inventory = player.getInventory();
			      inventory.setHeldItemSlot(inventory.getHeldItemSlot());
			      
			      player.setHealth(player.getHealth());
			      player.setMaxHealth(player.getMaxHealth() + 1.0D);
			      player.setMaxHealth(player.getMaxHealth() - 1.0D);
			      
			      player.setItemInHand(player.getItemInHand());
			      
			      player.setWalkSpeed(player.getWalkSpeed());
			    }
			    catch (InvocationTargetException ex)
			    {
			      System.out.println("Exception sending instant skin change packet");
			      ex.printStackTrace();
			    }
			  }
	
	public static void updateSkin(Player p)
	{
		/*try
		{
			if (!p.isOnline()) {
				return;
			}
			CraftPlayer cp = (CraftPlayer)p;
			EntityPlayer ep = cp.getHandle();
			int entId = ep.getId();
			PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ep);
			PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(new int[] { entId });
			PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(ep);
			PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ep);
			@SuppressWarnings("deprecation")
			PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(ep.getWorld().worldProvider.getDimension(), 
					ep.getWorld().getDifficulty(), ep.getWorld().worldData.getType(), 
					EnumGamemode.getById(p.getGameMode().getValue()));
			PacketPlayOutEntityEquipment itemhand = new PacketPlayOutEntityEquipment(entId, 0, 
					CraftItemStack.asNMSCopy(p.getItemInHand()));
			PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entId, 4, 
					CraftItemStack.asNMSCopy(p.getInventory().getHelmet()));
			PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(entId, 3, 
					CraftItemStack.asNMSCopy(p.getInventory().getChestplate()));
			PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(entId, 2, 
					CraftItemStack.asNMSCopy(p.getInventory().getLeggings()));
			PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entId, 1, 
					CraftItemStack.asNMSCopy(p.getInventory().getBoots()));
			PacketPlayOutHeldItemSlot slot = new PacketPlayOutHeldItemSlot(p.getInventory().getHeldItemSlot());
			for (Player pOnline : ((CraftServer)Bukkit.getServer()).getOnlinePlayers())
			{
				final CraftPlayer craftOnline = (CraftPlayer)pOnline;
				PlayerConnection con = craftOnline.getHandle().playerConnection;
				if (pOnline.getName().equals(p.getName()))
				{
					con.sendPacket(removeInfo);
					Bukkit.getScheduler().runTaskLater(GamePlugin.getInstance(), new Runnable()
					{
						public void run()
						{
							con.sendPacket(addInfo);
							con.sendPacket(respawn);
							con.sendPacket(slot);
							craftOnline.updateScaledHealth();
							craftOnline.getHandle().triggerHealthUpdate();
							craftOnline.updateInventory();
							Bukkit.getScheduler().runTask(GamePlugin.getInstance(), new Runnable()
							{
								public void run()
								{
									craftOnline.getHandle().updateAbilities();
								}
							});
						}
					}, 5);
				}
				else if (pOnline.canSee(p))
				{
					con.sendPacket(removeEntity);
					con.sendPacket(removeInfo);
					Bukkit.getScheduler().runTaskLater(GamePlugin.getInstance(), new Runnable()
					{
						public void run()
						{
							con.sendPacket(addInfo);
							con.sendPacket(addNamed);
							con.sendPacket(itemhand);
							con.sendPacket(helmet);
							con.sendPacket(chestplate);
							con.sendPacket(leggings);
							con.sendPacket(boots);
						}
					}, 5);
				}
			}
		}
		catch (Exception localException) {}*/
	}
	public static Object createProperty(String name, String value, String signature)
	{
		try
		{
			return ReflectionUtil.invokeConstructor(Class.forName("com.mojang.authlib.properties.Property"), 
					new Class[] { String.class, String.class, String.class }, new Object[] { name, value, signature });
		}
		catch (Exception localException) {}
		return null;
	}
}
