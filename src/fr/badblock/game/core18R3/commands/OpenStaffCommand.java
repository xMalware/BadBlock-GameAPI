package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.game.core18R3.gameserver.threading.GameServerKeeperAliveTask;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class OpenStaffCommand extends AbstractCommand {
	public OpenStaffCommand() {
		super("openstaff", new TranslatableString("commands.openstaff.usage"), GamePermission.ADMIN, GamePermission.ADMIN, GamePermission.ADMIN, "adm");
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(GameAPI.getAPI().getRunType() != RunType.DEV)
			return true;
		
		if(GameServerKeeperAliveTask.switchOpenStaff())
			sendTranslatedMessage(sender, "commands.openstaff.open");
		else sendTranslatedMessage(sender, "commands.openstaff.close");
		
		return true;
	}
	
}
