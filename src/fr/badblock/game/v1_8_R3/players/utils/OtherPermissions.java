package fr.badblock.game.v1_8_R3.players.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class OtherPermissions {
	public static boolean has(Player player, String perm){
		Plugin plugin = Bukkit.getPluginManager().getPlugin("PermissionsEx");
		
		if(plugin != null){
			
			try {
				Class<?> permissionExClass = Class.forName("ru.tehkode.permissions.bukkit.PermissionsEx");
				
				Object result = permissionExClass.getMethod("getUser", Player.class).invoke(null, player);
				return (boolean) result.getClass().getMethod("has", String.class).invoke(result, perm);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
}
