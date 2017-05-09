package fr.badblock.game.core18R3.worldedit.iterators;

import org.bukkit.Bukkit;
import org.bukkit.World;

import fr.badblock.gameapi.utils.selections.CuboidSelection;
import fr.badblock.gameapi.worldedit.WEBlockIterator;

public abstract class WEAbstractCuboidIterator implements WEBlockIterator {
	private World world;
	protected int count;

	protected int minX, minY, minZ, maxX, maxY, maxZ;
	protected int minCX, minCZ, maxCX, maxCZ;
	protected int cX, cZ;
	
	protected boolean chunkChange = true;
	protected boolean sendChunkChange = true;
	protected int curX, curY, curZ;

	protected int[] next;
	
	public WEAbstractCuboidIterator(CuboidSelection selection) {
		this.world = Bukkit.getWorld(selection.getWorldName());

		this.minX = (int)selection.getMinX();
		this.minY = (int)selection.getMinY();
		this.minZ = (int)selection.getMinZ();

		this.maxX = (int)selection.getMaxX();
		this.maxY = (int)selection.getMaxY();
		this.maxZ = (int)selection.getMaxZ();

		this.count = (maxX + 1 - minX) * (maxY + 1 - minY) * (maxZ + 1 - minZ);

		this.curX = minX & 15;
		this.curY = minY;
		this.curZ = minZ & 15;

		this.cX = minX >> 4;
		this.cZ = minZ >> 4;

		this.minCX = cX;
		this.minCZ = cZ;
		this.maxCX = maxX >> 4;
		this.maxCZ = maxZ >> 4;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public long getCount() {
		return count;
	}

	@Override
	public boolean hasNextChunk() {
		return sendChunkChange;
	}

	@Override
	public int[] getNextChunk() {
		sendChunkChange = false;
		
		return new int[] { cX, cZ };
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public int[] getNextPosition() {
		int[] res = next;
		
		parseNext();
		return res;
	}
	
	protected void parseNext()
	{
		int cx = 0, cz = 0;
		do
		{
			cx = this.cX << 4;
			cz = this.cZ << 4;
			
			calcNext();
		} while(hasNext() && !accept( new int[]{next[0] | cx, next[1], next[2] | cz} ));
	}
	
	private void calcNext()
	{
		if(cZ > maxCZ) // ended
		{
			next = null;
			return;
		}
		
		next = new int[] { curX, curY, curZ };

		if(chunkChange)
		{
			sendChunkChange = true;
			chunkChange = false;
		}
		
		curX++;

		if(curX < 16 && (curX | (cX << 4)) <= maxX)
			return;

		curX = (cX == minCX) ? minX & 15 : 0;
		curZ++;

		if(curZ < 16 && (curZ | (cZ << 4)) <= maxZ)
			return;

		curZ = (cZ == minCZ) ? minZ & 15 : 0;
		curY++;

		if(curY <= maxY)
			return;
				
		cX++;
		chunkChange = true;

		if(cX > maxCX)
		{
			cX = minCX;
			cZ++;
		}
		
		curX = (cX == minCX) ? minX & 15 : 0;
		curY = minY;
		curZ = (cZ == minCZ) ? minZ & 15 : 0;
	}
	
	protected abstract boolean accept(int[] coords);
}
