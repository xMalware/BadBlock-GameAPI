package fr.badblock.game.v1_8_R3.players;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class OtherPermissions {
	public static boolean has(Player player, String perm){
		Plugin plugin = Bukkit.getPluginManager().getPlugin("PermissionsEx");
		
		if(plugin != null){
			return PermissionsEx.getUser(player).has(perm);
		}
		
		return false;
	}
}
