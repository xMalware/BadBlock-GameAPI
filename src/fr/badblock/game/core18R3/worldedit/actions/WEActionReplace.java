package fr.badblock.game.core18R3.worldedit.actions;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.worldedit.WEBlockIterator;

public class WEActionReplace extends WEActionBlockModifier {

	public WEActionReplace(WEBlockIterator iterator, CommandSender sender, Material block, byte blockData, Material replacedBlock, byte replacedBlockData) {
		super(iterator, sender);
	
		editor.setData(block, blockData);
		editor.setDataReplace(replacedBlock, replacedBlockData); 
	}

	@Override
	public void apply(int x, int y, int z) {
		editor.replaceBlockAt(x, y, z);
	}

}
