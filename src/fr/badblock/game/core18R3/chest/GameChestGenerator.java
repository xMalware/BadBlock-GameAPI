package fr.badblock.game.core18R3.chest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.configuration.values.MapItemStack;
import fr.badblock.gameapi.servers.ChestGenerator;
import fr.badblock.gameapi.utils.general.JsonUtils;
import fr.badblock.gameapi.utils.general.MathsUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public class GameChestGenerator extends BadListener implements ChestGenerator {
	private List<Location> 	   filledChests = new ArrayList<>();
	private boolean 		   work 		= false;
	
	private File 			   configFile;
	private ChestConfiguration config;
	private Random 			   random 	    = new Random();
	
	@Override
	public void setConfigurationFile(File file) {
		this.configFile = file;
		this.config 	= JsonUtils.load(file, ChestConfiguration.class);
		
		saveConfig();
	}
	
	private void saveConfig(){
		JsonUtils.save(configFile, config, true);
	}

	@Override
	public ItemStack[] generateChest(int lines) {
		int maxItems = config.maxItemsPerLine * lines;
		int items = MathsUtils.integerRandomInclusive(config.maxItemsPerLine * lines, (maxItems * 2) / 3);
		
		ItemStack[] result = new ItemStack[lines * 9];
		
		int max = config.itemStacks.stream().mapToInt(item -> { return item.probability; }).sum();
		
		for(int i=0;i<items;i++){
			int value = new Random().nextInt(max);
			int curr  = 0;
			
			ChestMapItemStack res = null;
			
			for(ChestMapItemStack item : config.itemStacks){
				curr += item.probability;
				
				if(value <= curr){
					res = item;
					break;
				}
			}
			
			while(true){
				int pos = random.nextInt(result.length);
				
				if(result[pos] == null){
					result[pos] = res.getHandle();
					break;
				}
			}
		}
		
		return result;
	}

	@Override
	public void beginJob() {
		work = true;
	}

	@Override
	public void resetChests() {
		filledChests.clear();
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(!work || e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		Block block = e.getClickedBlock();
		
		if(block.getType() != Material.CHEST || filledChests.contains(block.getLocation()))
			return;
		
		generate0( (Chest) block.getState() );

		Block relative = getNearbyChest(block);
		
		if(relative != null)
			generate0( (Chest) block.getState() );
	}

	@Override
	public void addItemInConfiguration(ItemStack item, int probability) {
		config.itemStacks.add(new ChestMapItemStack(item, probability));
		saveConfig();
	}
	
	private void generate0(Chest chest){
		Inventory inv = chest.getBlockInventory();
		
		inv.setContents(generateChest(3));
	}
	
	private Block getNearbyChest(Block block){
		BlockFace[] faces = new BlockFace[]{BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH};
		
		for(BlockFace face : faces){
			Block relative = block.getRelative(face);
			
			if(relative.getType() == Material.CHEST)
				return relative;
		}
		
		return null;
	}
	
	@NoArgsConstructor
	public static class ChestConfiguration {
		public int 				       maxItemsPerLine = 3;
		public List<ChestMapItemStack> itemStacks	   = new ArrayList<>();
	}
	
	@NoArgsConstructor
	@Data@EqualsAndHashCode(callSuper=false)
	public static class ChestMapItemStack extends MapItemStack {
		private int probability;
		
		public ChestMapItemStack(ItemStack is, int prob){
			super(is);
			
			this.probability = prob;
		}
	}
}
