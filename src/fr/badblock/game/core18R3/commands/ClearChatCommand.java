package fr.badblock.game.core18R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class ClearChatCommand extends AbstractCommand {
	public ClearChatCommand() {
		super("clearchat", new TranslatableString("commands.clearchat.usage"), GamePermission.BMODERATOR, GamePermission.BMODERATOR, GamePermission.BMODERATOR, "cc", "clearc");
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		for(int i=0;i<150;i++){
			for(Player player : Bukkit.getOnlinePlayers())
				player.sendMessage(" ");
		}
		
		return true;
	}
}
