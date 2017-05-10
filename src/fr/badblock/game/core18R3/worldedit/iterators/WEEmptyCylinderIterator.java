package fr.badblock.game.core18R3.worldedit.iterators;

import fr.badblock.gameapi.utils.selections.CuboidSelection;

public class WEEmptyCylinderIterator extends WECylinderIterator {
	public WEEmptyCylinderIterator(CuboidSelection selection) {
		super(selection);
	}

	@Override
	protected boolean accept(int[] coords) {
		return super.accept(coords) && nearbyEmpty(coords[0], coords[1], coords[2]);
	}
	
	private boolean nearbyEmpty(int x, int y, int z)
	{
		if(!selection.isInSelection(x - 1, y, z) || !super.accept(new int[]{x - 1, y, z}))
			return true;
		if(!selection.isInSelection(x + 1, y, z) || !super.accept(new int[]{x + 1, y, z}))
			return true;
		if(!selection.isInSelection(x, y, z - 1) || !super.accept(new int[]{x, y, z - 1}))
			return true;
		if(!selection.isInSelection(x, y, z + 1) || !super.accept(new int[]{x, y, z + 1}))
			return true;
		
		return false;
	}
}
