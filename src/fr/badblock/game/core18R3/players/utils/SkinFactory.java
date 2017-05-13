package fr.badblock.game.core18R3.players.utils;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import com.mojang.authlib.properties.PropertyMap;

import fr.badblock.game.core18R3.GamePlugin;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutHeldItemSlot;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutRespawn;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

public class SkinFactory {
	public static void applySkin(Player p, Object props)
	{
		/*try
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
		}
		catch (Exception localException) {}*/
	}
	
	public static PropertyMap getPropertyMap(Player p)
	{
		/*try
		{
			Object ep = ReflectionUtil.invokeMethod(p.getClass(), p, "getHandle");
			Object profile = ReflectionUtil.invokeMethod(ep.getClass(), ep, "getProfile");
			Object propmap = ReflectionUtil.invokeMethod(profile.getClass(), profile, "getProperties");
			return (PropertyMap) propmap;
		}
		catch (Exception localException) {}*/
		return null;
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

			PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[] { ep });

			PacketPlayOutEntityDestroy removeEntity = new PacketPlayOutEntityDestroy(new int[] { entId });

			PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(ep);

			PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[] { ep });

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
				else if (pOnline.canSee(p))
				{
					con.sendPacket(removeEntity);
					con.sendPacket(removeInfo);
					con.sendPacket(addInfo);
					con.sendPacket(addNamed);
					con.sendPacket(itemhand);
					con.sendPacket(helmet);
					con.sendPacket(chestplate);
					con.sendPacket(leggings);
					con.sendPacket(boots);
				}
			}
		}
		catch (Exception localException) {}*/
	}

	public static Object createProperty(String name, String value, String signature)
	{
		/*try
		{
			return ReflectionUtil.invokeConstructor(Class.forName("com.mojang.authlib.properties.Property"), 
					new Class[] { String.class, String.class, String.class }, new Object[] { name, value, signature });
		}
		catch (Exception localException) {}*/
		return null;
	}
}

