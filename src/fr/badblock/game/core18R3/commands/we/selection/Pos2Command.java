package fr.badblock.game.core18R3.commands.we.selection;

import fr.badblock.game.core18R3.commands.we.WorldEditCommand;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.selections.Vector3f;

public class Pos2Command extends WorldEditCommand {
	public Pos2Command() {
		super("pos2");
	}

	@Override
	protected boolean exec(BadblockPlayer concerned, String[] args) {
		if(args.length != 3)
			return false;
		
		GameBadblockPlayer player = (GameBadblockPlayer) concerned;
		
		try
		{
			int x = Integer.parseInt(args[0]);
			int y = Math.max(0, Math.min(256, Integer.parseInt(args[1])));
			int z = Integer.parseInt(args[2]);
			
			player.setSecondVector(new Vector3f( x, y, z ));
			player.sendTranslatedMessage("commands.selection-second", x, y, z);

		}
		catch(Exception e)
		{
			return false;
		}
		
		return true;
	}
}