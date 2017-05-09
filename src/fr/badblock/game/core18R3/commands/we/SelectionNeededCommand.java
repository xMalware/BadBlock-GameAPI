package fr.badblock.game.core18R3.commands.we;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public abstract class SelectionNeededCommand extends AbstractCommand {
	public SelectionNeededCommand(String commandName) {
		super("/" + commandName, new TranslatableString("commands.worldedit." + commandName + ".usage"), GamePermission.ADMIN, GamePermission.ADMIN, GamePermission.ADMIN);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer concerned = (BadblockPlayer) sender;

		if(concerned.getSelection() == null)
		{
			concerned.sendTranslatedMessage("commands.worldedit.noselection");
		}
		else
		{
			return exec(concerned, args);
		}
		
		return true;
	}
	
	protected abstract boolean exec(BadblockPlayer player, String[] args);
}