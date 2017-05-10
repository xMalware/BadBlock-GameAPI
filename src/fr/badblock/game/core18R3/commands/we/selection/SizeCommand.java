package fr.badblock.game.core18R3.commands.we.selection;

import fr.badblock.game.core18R3.commands.we.SelectionNeededCommand;
import fr.badblock.game.core18R3.commands.we.SetCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.selections.CuboidSelection;

public class SizeCommand extends SelectionNeededCommand {
	public SizeCommand() {
		super("size");
	}

	@Override
	protected boolean exec(BadblockPlayer concerned, String[] args) {
		CuboidSelection selection = concerned.getSelection();
		
		long dx = (long)( selection.getMaxX() - selection.getMinX() + 1 );
		long dy = (long)( selection.getMaxY() - selection.getMinY() + 1 );
		long dz = (long)( selection.getMaxZ() - selection.getMinZ() + 1 );
		
		concerned.sendTranslatedMessage("commands.worldedit.selection.size", SetCommand.formatLong(dx * dy * dz));
		return true;
	}
}