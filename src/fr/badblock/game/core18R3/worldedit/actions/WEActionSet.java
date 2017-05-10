package fr.badblock.game.core18R3.worldedit.actions;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.worldedit.WEBlockIterator;

public class WEActionSet extends WEActionBlockModifier {

	public WEActionSet(WEBlockIterator iterator, CommandSender sender, Material block, byte blockData) {
		super(iterator, sender);
	
		editor.setData(block, blockData);
	}

	@Override
	public void apply(int x, int y, int z) {
		editor.setBlockAt(x, y, z);
	}

}
