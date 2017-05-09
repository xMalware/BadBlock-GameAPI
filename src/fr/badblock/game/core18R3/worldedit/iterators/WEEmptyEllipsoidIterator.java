package fr.badblock.game.core18R3.worldedit.iterators;

import fr.badblock.gameapi.utils.selections.CuboidSelection;

public class WEEmptyEllipsoidIterator extends WEEllipsoidIterator {
	public WEEmptyEllipsoidIterator(CuboidSelection selection) {
		super(selection);
	}

	@Override
	protected boolean accept(int[] coords) {
		boolean accept = super.accept(coords);

		if(!accept)
			return false;
		
		boolean nearbyEmpty = false;
		
		nearbyEmpty |= !super.accept(new int[]{coords[0] - 1, coords[1], coords[2]});
		nearbyEmpty |= !super.accept(new int[]{coords[0] + 1, coords[1], coords[2]});
		nearbyEmpty |= !super.accept(new int[]{coords[0], coords[1] - 1, coords[2]});
		nearbyEmpty |= !super.accept(new int[]{coords[0], coords[1] + 1, coords[2]});
		nearbyEmpty |= !super.accept(new int[]{coords[0], coords[1], coords[2] - 1});
		nearbyEmpty |= !super.accept(new int[]{coords[0], coords[1], coords[2] + 1});
		
		return nearbyEmpty;
	}
}
