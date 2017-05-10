package fr.badblock.game.core18R3.commands.we.selection;

import org.bukkit.ChatColor;

import fr.badblock.game.core18R3.commands.we.SelectionNeededCommand;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class DelselCommand extends SelectionNeededCommand {
	public DelselCommand() {
		super("delsel");
	}

	@Override
	protected boolean exec(BadblockPlayer concerned, String[] args) {
		GameBadblockPlayer player = (GameBadblockPlayer) concerned;
		player.setFirstVector(null);
		player.setSecondVector(null);
		
		player.sendMessage(ChatColor.GRAY + "Pouf!");
		
		return true;
	}
}