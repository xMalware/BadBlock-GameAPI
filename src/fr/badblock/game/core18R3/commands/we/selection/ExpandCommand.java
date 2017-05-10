package fr.badblock.game.core18R3.commands.we.selection;

import org.bukkit.block.BlockFace;

import fr.badblock.game.core18R3.commands.we.SelectionNeededCommand;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.selections.CuboidSelection;

public class ExpandCommand extends SelectionNeededCommand {
	public ExpandCommand() {
		super("expand");
	}

	@Override
	protected boolean exec(BadblockPlayer concerned, String[] args) {
		CuboidSelection result = null;
		
		if(args.length == 2)
			result = execDirectional(concerned, args);
		else if(args.length == 6)
			result = execAllCoords(concerned, args);
		
		if(result == null)
			return false;
		
		GameBadblockPlayer player = (GameBadblockPlayer) concerned;
		player.setFirstVector(result.getFirstBound());
		player.setSecondVector(result.getSecondBound());
				
		return true;
	}
	
	protected CuboidSelection execAllCoords(BadblockPlayer concerned, String[] args)
	{
		try
		{
			return concerned.getSelection().augment(Integer.parseInt(args[0]),
					Integer.parseInt(args[1]),
					Integer.parseInt(args[2]),
					Integer.parseInt(args[3]),
					Integer.parseInt(args[4]),
					Integer.parseInt(args[5]));
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	protected CuboidSelection execDirectional(BadblockPlayer concerned, String[] args)
	{
		try
		{
			int val = Integer.parseInt(args[0]);
			
			switch(args[1].toLowerCase())
			{
				case "up":
					return concerned.getSelection().augment(val, BlockFace.UP);
				case "down":
					return concerned.getSelection().augment(val, BlockFace.DOWN);
				case "east":
					return concerned.getSelection().augment(val, BlockFace.EAST);
				case "west":
					return concerned.getSelection().augment(val, BlockFace.WEST);
				case "north":
					return concerned.getSelection().augment(val, BlockFace.NORTH);
				case "south":
					return concerned.getSelection().augment(val, BlockFace.SOUTH);
				case "all":
					return concerned.getSelection().augment(val);
				case "move":
					return concerned.getSelection().move(val);
				default:
					return null;
			}
		}
		catch(Exception e)
		{
			return null;
		}
	}
}