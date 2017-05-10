package fr.badblock.game.core18R3.commands.we;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.players.BadblockPlayer;

public abstract class SelectionNeededCommand extends WorldEditCommand {
	public SelectionNeededCommand(String commandName) {
		super(commandName);
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
}