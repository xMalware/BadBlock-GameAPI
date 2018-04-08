package fr.badblock.game.core18R3.worldedit.iterators;

import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.google.common.collect.Queues;

import fr.badblock.gameapi.utils.selections.CuboidSelection;
import fr.badblock.gameapi.worldedit.WEBlockIterator;

public class WELineIterator implements WEBlockIterator {
	private World world;
	private int minX, minY, minZ, maxX, maxY, maxZ;
	private Queue<int[]> blocks;
	private int size;
	
	private int cx = Integer.MAX_VALUE, cz = Integer.MAX_VALUE;
	
	public WELineIterator(CuboidSelection selection) {
		this.world = Bukkit.getWorld(selection.getWorldName());
		
		this.minX = (int)selection.getMinX();
		this.minY = (int)selection.getMinY();
		this.minZ = (int)selection.getMinZ();
		
		this.maxX = (int)selection.getMaxX();
		this.maxY = (int)selection.getMaxY();
		this.maxZ = (int)selection.getMaxZ();
	
		blocks = Queues.newLinkedBlockingDeque();
		buildList();
		
		size = blocks.size();
	}
	
	private void buildList()
	{
		int dx, dy, dz;
		
		dx = maxX - minX;
		dy = maxY - minY;
		dz = maxZ - minZ;

		int x = minX, y = minY, z = minZ;
		
		if(dz > dx && dz > dy)
		{
			double errorX = dz / 2d;
			double errorY = dz / 2d;
			
			while(z != maxZ)
			{
				blocks.add(new int[]{x, y, z});
				
				errorX -= dx;
				errorY -= dy;
				
				if(errorX < 0) {
					x++;
					errorX += dz;
				}
				
				if(errorY < 0) {
					y++;
					errorY += dz;
				}
				z++;
			}
		}
		else if(dx > dy)
		{
			double errorZ = dx / 2d;
			double errorY = dx / 2d;
			
			while(x != maxX)
			{
				blocks.add(new int[]{x, y, z});
				
				errorZ -= dz;
				errorY -= dy;
				
				if(errorZ < 0) {
					z++;
					errorZ += dx;
				}
				
				if(errorY < 0) {
					y++;
					errorY += dx;
				}
				x++;
			}
		}
		else
		{
			double errorX = dy / 2d;
			double errorZ = dy / 2d;
			
			while(y != maxY)
			{
				blocks.add(new int[]{x, y, z});
				
				errorX -= dx;
				errorZ -= dy;
				
				if(errorZ < 0) {
					z++;
					errorZ += dy;
				}
				
				if(errorX < 0) {
					x++;
					errorX += dy;
				}
				y++;
			}
		}
		
		blocks.add(new int[]{x, y, z});
	}
	
	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public long getCount() {
		return size;
	}

	@Override
	public boolean hasNextChunk() {
		if(!hasNext())
			return false;
		
		int[] chunk = blocks.peek();
		return (chunk[0] >> 4) != cx || (chunk[1] >> 4) != cz;
	}

	@Override
	public int[] getNextChunk() {
		int[] chunk = blocks.peek();
		
		this.cx = chunk[0] >> 4;
		this.cz = chunk[2] >> 4;
		
		return new int[]{ cx, cz };
	}

	@Override
	public boolean hasNext() {
		return blocks.size() > 0;
	}

	@Override
	public int[] getNextPosition() {
		int[] pos = blocks.poll();
		
		return new int[] {pos[0] & 15, pos[1], pos[2] & 15};
	}
}
