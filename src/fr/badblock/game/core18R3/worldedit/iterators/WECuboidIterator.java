package fr.badblock.game.core18R3.worldedit.iterators;

import fr.badblock.gameapi.utils.selections.CuboidSelection;

public class WECuboidIterator extends WEAbstractCuboidIterator {
	public WECuboidIterator(CuboidSelection selection) {
		super(selection);
		
		parseNext();
	}

	@Override
	protected boolean accept(int[] coords) {
		return true;
	}
}
