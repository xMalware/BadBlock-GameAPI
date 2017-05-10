package fr.badblock.game.core18R3.worldedit.iterators;

import fr.badblock.gameapi.utils.selections.CuboidSelection;

public class WEWallsIterator extends WEAbstractCuboidIterator {
	public WEWallsIterator(CuboidSelection selection) {
		super(selection);
		parseNext();
	}

	@Override
	protected boolean accept(int[] coords) {
		return coords[0] == minX || coords[0] == maxX || coords[2] == minZ || coords[2] == maxZ;
	}
}
