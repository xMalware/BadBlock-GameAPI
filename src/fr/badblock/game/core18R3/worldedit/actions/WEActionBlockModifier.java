package fr.badblock.game.core18R3.worldedit.actions;

import org.bukkit.command.CommandSender;

import fr.badblock.game.core18R3.worldedit.WorldEditSimpleEditor;
import fr.badblock.gameapi.worldedit.WEAction;
import fr.badblock.gameapi.worldedit.WEBlockIterator;
import fr.badblock.gameapi.worldedit.WESimpleEditor;

public abstract class WEActionBlockModifier implements WEAction {
	private WEBlockIterator iterator;
	protected CommandSender sender;
	protected WESimpleEditor editor;
	
	private long count;
	
	public WEActionBlockModifier(WEBlockIterator iterator, CommandSender sender) {
		this.iterator = iterator;
		this.sender = sender;
		this.editor = new WorldEditSimpleEditor(iterator.getWorld());
	}
	
	@Override
	public long getTotalIterationCount() {
		return iterator.getCount();
	}

	@Override
	public long getIterationCount() {
		return count;
	}

	@Override
	public CommandSender getApplicant() {
		return sender;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public void next() {
		count++;
		
		if(iterator.hasNextChunk())
		{
			editor.finalizeWorkOnChunk();
			changeChunk();
		}
		
		int[] position = iterator.getNextPosition();
		apply(position[0], position[1], position[2]);
	}

	@Override
	public void notifyStart()
	{
		changeChunk();
	}

	@Override
	public void notifyEnd() {
		editor.finalizeWorkOnChunk();
	}
	
	private void changeChunk()
	{
		int[] v = iterator.getNextChunk();
		editor.setCurrentChunk(v[0], v[1]);
	}
	
	public abstract void apply(int x, int y, int z);
}
