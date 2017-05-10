package fr.badblock.game.core18R3.worldedit.iterators;

import fr.badblock.gameapi.utils.selections.CuboidSelection;

public class WEEllipsoidIterator extends WEAbstractCuboidIterator {
	private double lengthX;
	private double lengthY;
	private double lengthZ;
	
	private double centerX;
	private double centerY;
	private double centerZ;
	
	public WEEllipsoidIterator(CuboidSelection selection) {
		super(selection);
		
		this.lengthX = (int)Math.ceil(Math.abs(selection.getMaxX() - selection.getMinX() + 1) / 2);
		this.lengthY = (int)Math.ceil(Math.abs(selection.getMaxY() - selection.getMinY() + 1) / 2);
		this.lengthZ = (int)Math.ceil(Math.abs(selection.getMaxZ() - selection.getMinZ() + 1) / 2);
		
		this.centerX = (selection.getMaxX() + selection.getMinX()) / 2;
		this.centerY = (selection.getMaxY() + selection.getMinY()) / 2;
		this.centerZ = (selection.getMaxZ() + selection.getMinZ()) / 2;
		
		parseNext();
	}

	@Override
	protected boolean accept(int[] coords) {
		double x = Math.min(coords[0] - centerX, coords[0] + 1 - centerX) / lengthX;
		double y = Math.min(coords[1] - centerY, coords[1] + 1 - centerY) / lengthY;
		double z = Math.min(coords[2] - centerZ, coords[2] + 1 - centerZ) / lengthZ;		
		
		return x * x + y * y + z * z <= 1;
	}
}
