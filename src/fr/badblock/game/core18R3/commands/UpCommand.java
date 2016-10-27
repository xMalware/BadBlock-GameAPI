package fr.badblock.game.core18R3.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class UpCommand extends AbstractCommand {
	public UpCommand() {
		super("up", new TranslatableString("commands.up.usage"), GamePermission.ADMIN, GamePermission.ADMIN, GamePermission.ADMIN);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer concerned = (BadblockPlayer) sender;

		Block    toChange = concerned.getLocation().getBlock();
		Location location = concerned.getLocation().getBlock().getRelative(BlockFace.UP).getLocation().add(0.5d, 0, 0.5d);

		location.setYaw(concerned.getLocation().getYaw());
		location.setPitch(concerned.getLocation().getPitch());
		
		if(toChange.getType() == Material.AIR){
			toChange.setType(Material.GLASS);
		}
		
		concerned.teleport(location);
		concerned.sendMessage(GameAPI.i18n().replaceColors("&7Whoosh!"));
		
		return true;
	}
}