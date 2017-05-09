package fr.badblock.game.core18R3.worldedit.iterators;

import fr.badblock.gameapi.utils.selections.CuboidSelection;

public class WECylinderIterator extends WEAbstractCuboidIterator {
	private double lengthX;
	private double lengthZ;
	
	private double centerX;
	private double centerZ;
	
	public WECylinderIterator(CuboidSelection selection) {
		super(selection);
		
		this.lengthX = (int)Math.ceil(Math.abs(selection.getMaxX() - selection.getMinX() + 1) / 2);
		this.lengthZ = (int)Math.ceil(Math.abs(selection.getMaxZ() - selection.getMinZ() + 1) / 2);
		
		this.centerX = (selection.getMaxX() + selection.getMinX()) / 2;
		this.centerZ = (selection.getMaxZ() + selection.getMinZ()) / 2;
		
		parseNext();
	}

	@Override
	protected boolean accept(int[] coords) {
		double x = Math.min(coords[0] - centerX, coords[0] + 1 - centerX) / lengthX;
		double z = Math.min(coords[2] - centerZ, coords[2] + 1 - centerZ) / lengthZ;		
		
		return x * x + z * z <= 1;
	}
}
