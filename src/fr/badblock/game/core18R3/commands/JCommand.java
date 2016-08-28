package fr.badblock.game.core18R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.run.BadblockGame;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

/**
 * Commande de j
 * @author LeLanN
 */
public class JCommand extends AbstractCommand {
	
	public JCommand() {
		super("j", new TranslatableString("commands.j.usage"), GamePermission.ADMIN);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		Block blockTarget = null;
		boolean first 	  = true;
		boolean can   	  = false;
		
		int maxBlock = 40;
		int block	 = 0;
		BadblockPlayer player = (BadblockPlayer) sender;
		Location location  = player.getEyeLocation().clone();
		Vector   direction = player.getEyeLocation().getDirection();
		
		Block 	   previous = location.getBlock();
		
		while(true){
			location = location.add(direction);
			Block b = location.getBlock();
			
			if(previous.equals(b)){
				continue;
			}
			
			previous = b;
			
			if(!b.getType().equals(Material.AIR)) {
				if(first){
					first = false;
				} else if(can){
					blockTarget = b;
					break;
				}
			} else if(!first){
				can = true;
			}
			
			block++;
			
			if(block > maxBlock)
				break;
		}
		
		if(blockTarget == null){
			player.sendTranslatedMessage("game.compass.tp.nothingpassthrough");
		} else {
			Location to = blockTarget.getLocation().add(0.5d, 1d, 0.5d);
			to.setYaw(player.getLocation().getYaw());
			to.setPitch(player.getLocation().getPitch());
			
			player.teleport(to);
		}
		return true;
	}
	
}
