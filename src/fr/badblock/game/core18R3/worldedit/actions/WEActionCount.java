package fr.badblock.game.core18R3.worldedit.actions;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.i18n.Word.WordDeterminant;
import fr.badblock.gameapi.utils.i18n.messages.GameMessages;
import fr.badblock.gameapi.worldedit.WEBlockIterator;

public class WEActionCount extends WEActionBlockModifier {
	private int count = 0;
	private Material material;
	private byte data;
	
	public WEActionCount(WEBlockIterator iterator, CommandSender sender, Material material, byte data) {
		super(iterator, sender);
		
		editor.setData(material, data);
		
		this.material = material;
		this.data = data;
	}

	@Override
	public void apply(int x, int y, int z) {
		if(editor.hasSameData(x, y, z))	
			count++;
	}
	
	@Override
	public void notifyEnd() {
		// Don't call super: no change o:
		
		if(sender == null)
			return;
		GameAPI.i18n().sendMessage(sender, "commands.worldedit.selection.count", count, GameMessages.material(material, count > 1, WordDeterminant.SIMPLE), data);
	}
}
