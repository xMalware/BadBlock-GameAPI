package fr.badblock.game.core18R3.worldedit;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import fr.badblock.gameapi.utils.reflection.Reflector;
import fr.badblock.gameapi.worldedit.WESimpleEditor;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.ChunkCoordIntPair;
import net.minecraft.server.v1_8_R3.ChunkSection;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.LongHashMap;
import net.minecraft.server.v1_8_R3.PlayerChunkMap;
import net.minecraft.server.v1_8_R3.WorldServer;

public class WorldEditSimpleEditor implements WESimpleEditor {
	public static final boolean[] ticking_blocks = new boolean[1 << 16];
	public static final boolean[] tileentities_blocks = new boolean[1 << 16];
	
	static
	{
		for(int i = 0; i < 65536; i++)
		{
			IBlockData data = Block.d.a(i);
			ticking_blocks[i] = data != null && data.getBlock().isTicking();
			tileentities_blocks[i] = data != null && data.getBlock().isTileEntity();
		}
	}

	private WorldServer world;

	private char value, replaceValue;
	
	private Chunk chunk;
	private ChunkSection[] sections;
	
	private boolean empty;
	private boolean ticking;
	private boolean modified;

	public WorldEditSimpleEditor(org.bukkit.World world) {
		this.world = ((CraftWorld)world).getHandle();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setData(Material material, byte data) {
		if(!material.isBlock())
			throw new IllegalArgumentException(material.name() + " is not a block");

		value = (char)(material.getId() << 4 | data);
	
		empty = material == Material.AIR;
		ticking = ticking_blocks[value];
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setDataReplace(Material material, byte data) {
		if(!material.isBlock())
			throw new IllegalArgumentException(material.name() + " is not a block");

		replaceValue = (char)(material.getId() << 4 | data);
	}

	@Override
	public void setCurrentChunk(int x, int z)
	{
		boolean save = world.chunkProviderServer.forceChunkLoad;
		world.chunkProviderServer.forceChunkLoad = true;
		
		this.chunk = world.getChunkAt(x, z);
		this.sections = this.chunk.getSections();
		
		this.modified = false;
		
		world.chunkProviderServer.forceChunkLoad = save;
	}
	
	private ChunkSection getSection(int y)
	{
		ChunkSection section = sections[y];

		if(section == null)
		{
			return (sections[y] = new ChunkSection(y << 4, !this.world.worldProvider.o()));
		}
		return section;
	}

	int c = 0;
	
	@Override
	public void setBlockAt(int x, int y, int z) {
		ChunkSection sec = getSection(y >> 4);
		char[] array = sec.getIdArray();
		int pos = ((y & 15) << 8) | (z << 4) | x;
		
		if((array[pos] >> 4 == 0) != empty)
		{
			if(empty)
			{
				sec.nonEmptyBlockCount++;
			}
			else
			{
				sec.nonEmptyBlockCount--;
			}
		}
		
		if(ticking_blocks[array[pos]] != ticking)
		{
			if(ticking)
			{
				sec.tickingBlockCount++;
			}
			else
			{
				sec.tickingBlockCount--;
			}
		}

		if(tileentities_blocks[array[pos]])
		{
			world.t(new BlockPosition(chunk.locX << 4 | x, y, chunk.locZ << 4 | z));
		}
		
		if(!this.modified)
			this.modified = true;
		
		sec.getIdArray()[pos] = value;
	}
	
	@Override
	public void replaceBlockAt(int x, int y, int z) {
		if(getSection(y >> 4).getIdArray()[(x << 8) | (z << 4) | (y & 15)] != replaceValue)
			return;
		
		setBlockAt(x, y, z);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void finalizeWorkOnChunk() {
		if(!this.modified)
			return;
		
		chunk.initLighting();

		PlayerChunkMap map = world.getPlayerChunkMap();
		
		try
		{
			LongHashMap<?> hashMap = (LongHashMap<?>)new Reflector(map).getFieldValue("d");
			Object o = hashMap.getEntry(chunk.locX + 2147483647L | chunk.locZ + 2147483647L << 32);

			if(o == null)
				return;
			
			ChunkCoordIntPair coord = new ChunkCoordIntPair(chunk.locX, chunk.locZ);
			
			for(EntityPlayer player : (List<EntityPlayer>)new Reflector(o).getFieldValue("b"))
			{
				if(!player.chunkCoordIntPairQueue.contains(coord))
					player.chunkCoordIntPairQueue.add(coord);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
