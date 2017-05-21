package fr.badblock.game.core18R3.itemstack;

import java.util.Base64;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Wool;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import fr.badblock.gameapi.utils.reflection.Reflector;

public class ItemStackFactoryUtils {
	public static ItemStack applyLeatheredArmor(ItemStack leather, Color color)
	{
		if(leather.getItemMeta() instanceof LeatherArmorMeta){
			LeatherArmorMeta meta = (LeatherArmorMeta) leather.getItemMeta();
			meta.setColor(color);
			leather.setItemMeta(meta);
		}

		return leather;
	}
	
	public static ItemStack applySkull(ItemStack skull, String owner)
	{
		if(skull.getItemMeta() instanceof SkullMeta){
			SkullMeta meta = (SkullMeta) skull.getItemMeta();
			meta.setOwner(owner);
			skull.setItemMeta(meta);
		}

		return skull;
	}
	
	public static ItemStack applyCustomSkull(ItemStack skull, String url)
	{
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
	    PropertyMap propertyMap = profile.getProperties();
	    
	    if (propertyMap == null)
	        throw new IllegalStateException("Profile doesn't contain a property map");
	    
	    byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
	    propertyMap.put("textures", new Property("textures", new String(encodedData)));
	    
	    try {
			new Reflector(skull.getItemMeta()).setFieldValue("profile", profile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		return skull;
	}

	public static ItemStack applyWool(ItemStack colored, DyeColor color)
	{
		if(colored.getData() instanceof Wool){
			Wool meta = (Wool) colored.getItemMeta();
			meta.setColor(color);
			colored.setData(meta);
		}

		return colored;
	}
}
