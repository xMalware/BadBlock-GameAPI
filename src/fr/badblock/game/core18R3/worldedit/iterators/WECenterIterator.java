package fr.badblock.game.core18R3.worldedit.iterators;

import org.bukkit.Bukkit;
import org.bukkit.World;

import fr.badblock.gameapi.utils.selections.CuboidSelection;
import fr.badblock.gameapi.worldedit.WEBlockIterator;

public class WECenterIterator implements WEBlockIterator {
	private World world;
	private int x, y, z;
	private boolean val = true;
	
	public WECenterIterator(CuboidSelection selection) {
		this.world = Bukkit.getWorld(selection.getWorldName());
		
		this.x = (int)((selection.getMaxX() + selection.getMinX()) / 2);
		this.y = (int)((selection.getMaxY() + selection.getMinY()) / 2);
		this.z = (int)((selection.getMaxZ() + selection.getMinZ()) / 2);
	}
	
	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public long getCount() {
		return 1;
	}

	@Override
	public boolean hasNextChunk() {
		if(!hasNext())
			return false;
		
		return true;
	}

	@Override
	public int[] getNextChunk() {
		return new int[]{ x >> 4, z >> 4 };
	}

	@Override
	public boolean hasNext() {
		return val;
	}

	@Override
	public int[] getNextPosition() {
		val = false;
		return new int[] { x & 15, y, z & 15 };
	}
}
