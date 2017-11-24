package fr.badblock.game.core18R3.chest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.DirectionalContainer;

import fr.badblock.game.core18R3.commands.ChestGeneratorCommand;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.configuration.values.MapItemStack;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.servers.ChestGenerator;
import fr.badblock.gameapi.utils.general.JsonUtils;
import fr.badblock.gameapi.utils.general.MathsUtils;
import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.itemstack.ItemStackUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class GameChestGenerator extends BadListener implements ChestGenerator {
	private List<Location> 	   filledChests    = new ArrayList<>();
	private Map<String, ChestGenData> chests   = new HashMap<>();
	private List<RemovedChest> removedChest    = new ArrayList<>();

	@Getter
	private boolean 		   working 		   = false;

	private File 			   configFile;
	private ChestConfiguration config;
	private Random 			   random 	       = new Random();
	@Getter@Setter
	private boolean			   removeOnOpen    = false;
	@Getter@Setter
	private boolean			   individualChest = false;
	@Getter@Setter
	private Function<BadblockPlayer, List<ISProb>> alternateItemsProvider = (p -> new ArrayList<>());

	@Override
	public void setConfigurationFile(File file) {
		if(isConfigurated())
			throw new IllegalStateException("ChestGenerator is already configurated!");

		new ChestGeneratorCommand(this);

		this.configFile = file;
		this.config 	= JsonUtils.load(file, ChestConfiguration.class);

		saveConfig();
	}

	private void saveConfig(){
		ChestConfiguration cloned = new ChestConfiguration();
		cloned.maxItemsPerLine = config.maxItemsPerLine;
		cloned.itemStacks	   = config.itemStacks.stream().filter(item -> {
			return item.keep;
		}).collect(Collectors.toList());

		JsonUtils.save(configFile, cloned, true);
	}

	@Override
	public ItemStack[] generateChest(BadblockPlayer player, int lines) {
		int maxItems = config.maxItemsPerLine * lines;
		int items = MathsUtils.integerRandomInclusive(config.maxItemsPerLine * lines, (maxItems * 2) / 3);

		ItemStack[] result = new ItemStack[lines * 9];

		List<ISProb> alternate = alternateItemsProvider.apply(player);

		int max = config.itemStacks.stream().mapToInt(item -> { return item.probability; }).sum();
		max += alternate.stream().mapToInt(item -> item.prob).sum();

		List<Material> itemsToAdd = new ArrayList<>();
		List<ChestMapItemStack> needed = config.itemStacks.stream().filter(item -> item.probability == -1).collect(Collectors.toList());
		Iterator<ChestMapItemStack> iterator = needed.iterator();

		if(max > 0)
			for(int i=0;i<items;i++){
				int value = new Random().nextInt(max);
				int curr  = 0;

				ItemStack res = null;

				while (iterator.hasNext())
				{
					ChestMapItemStack item = iterator.next();
					if (!itemsToAdd.contains(item.getType()))
					{
						res = item.getHandle(player.getPlayerData().getLocale());
						itemsToAdd.add(item.getType());
						break;
					}
				}

				if (res == null)
				{
					for (ChestMapItemStack item : config.itemStacks)
					{
						if (item.probability != -1 && res == null)
						{
							curr += item.probability;	

							if (value <= curr)
							{
								res = item.getHandle(player.getPlayerData().getLocale());
							}
						}
					}
				}

				if(res == null){
					for(ISProb is : alternate){
						curr += is.prob;

						if(value <= curr){
							res = is.stack;
							break;
						}
					}
				}

				int z = 0;
				int o = 9  * 8;
				while(z < o){
					z++;
					int pos = random.nextInt(result.length);

					if(result[pos] == null){
						result[pos] = res;
						break;
					}
				}
			}

		return result;
	}

	@Override
	public boolean isConfigurated(){
		return config != null && configFile != null;
	}

	@Override
	public void beginJob() {
		working = true;
	}

	@Override
	public void resetChests() {
		filledChests.clear();
		chests.clear();

		for(RemovedChest chest : removedChest){
			Block block = chest.location.getBlock();
			block.setType(Material.CHEST);

			DirectionalContainer container = (DirectionalContainer) block.getState().getData();
			container.setFacingDirection(chest.blockFace);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(!working || e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		BadblockPlayer player = (BadblockPlayer) e.getPlayer();

		Block block = e.getClickedBlock();

		if(block.getType() != Material.CHEST || filledChests.contains(block.getLocation()))
			return;

		if( this.isIndividualChest() )
		{
			e.setCancelled(true);
			ItemStack[] is = null;
			
			filledChests.add(block.getLocation());

			if(mustGenerateOwnChest(player, block.getLocation()))
			{
				Block relative = getNearbyChest(block);

				int size = (relative == null ? 3 : 6);

				is = generateChest(player, size);
				updateChest(player, block.getLocation(), is);
			}
			else
			{
				is = getChest(player, block.getLocation());
			}

			Inventory inv = Bukkit.createInventory((Chest) block.getState(), is.length);
			inv.setContents(is);

			player.openInventory(inv);
			player.playChestAnimation(block, true);

			return;
		}

		generate0( player, (Chest) block.getState() );

		Block relative = getNearbyChest(block);

		if(relative != null)
			generate0( player, (Chest) relative.getState() );
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e){
		if(isWorking() && isRemoveOnOpen() && e.getPlayer().getType() == EntityType.PLAYER){
			BadblockPlayer player = (BadblockPlayer) e.getPlayer();

			if(player.getGameMode().equals(GameMode.SPECTATOR)) return;
			if(e.getInventory().getHolder() instanceof Chest){
				Chest c = (Chest) e.getInventory().getHolder();

				remove0(c);
			} else if(e.getInventory().getHolder() instanceof DoubleChest){
				DoubleChest c = (DoubleChest) e.getInventory().getHolder();

				Block block = c.getLocation().getBlock();

				remove0( (Chest) block.getState() );

				Block relative = getNearbyChest(block);

				if(relative != null)
					remove0( (Chest) relative.getState() );
			}
		}
		else if(isWorking() && isIndividualChest() && e.getPlayer().getType() == EntityType.PLAYER)
		{
			BadblockPlayer player = (BadblockPlayer) e.getPlayer();

			if(player.getGameMode().equals(GameMode.SPECTATOR)) return;
			if(e.getInventory().getHolder() instanceof Chest){
				Chest c = (Chest) e.getInventory().getHolder();

				player.playChestAnimation(c.getBlock(), false);
				updateChest(player, c.getLocation(), e.getInventory().getContents());
			}
		}
	}

	private void remove0(Chest c){
		DirectionalContainer container = (DirectionalContainer) c.getData();
		removedChest.add( new RemovedChest(c.getBlock().getLocation(), container.getFacing()) );

		Set<ItemStack> toDrop = Arrays.stream(c.getInventory().getContents()).filter(item -> { return ItemStackUtils.isValid(item); }).collect(Collectors.toSet());

		//c.getBlock().getDrops().remove(new ItemStack(Material.CHEST));
		//c.getBlock().breakNaturally();

		Location spawn = c.getBlock().getLocation().add(0d, 0.3d, 0d);

		toDrop.forEach(item -> {
			spawn.getWorld().dropItem(spawn, item);
		});

		c.getInventory().clear();
		c.getBlock().setType(Material.AIR);
	}

	@Override
	public void addItemInConfiguration(ItemStack item, int probability, boolean save) {
		if(!isConfigurated())
			throw new IllegalStateException("ChestGenerator is not configurated!");

		config.itemStacks.add(new ChestMapItemStack(item, probability, save));
		saveConfig();
	}

	private void generate0(BadblockPlayer player, Chest chest){
		Inventory inv = chest.getBlockInventory();
		if (!empty(inv)) return;
		inv.setContents(generateChest(player, 3));
	}

	private boolean empty(Inventory inventory) {
		for(ItemStack item : inventory.getContents()) {
			if(item != null && !item.getType().equals(Material.AIR))
				return false;
		}
		return true;
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

	private boolean mustGenerateOwnChest(BadblockPlayer player, Location loc){
		if(!chests.containsKey(player.getName()))
			return true;

		return !chests.get( player.getName() ).has( loc );
	}

	private void updateChest(BadblockPlayer player, Location loc, ItemStack[] is){
		ChestGenData data = chests.get(player.getName());

		if(data == null)
		{
			data = new ChestGenData();
			chests.put(player.getName(), data);
		}

		data.chestUpdated(loc, is);
	}

	private ItemStack[] getChest(BadblockPlayer player, Location loc){
		ChestGenData data = chests.get(player.getName());

		if(data == null)
			data = chests.put(player.getName(), new ChestGenData());

		return data.chests.get(loc);
	}

	@AllArgsConstructor
	public static class RemovedChest {
		public Location  location;
		public BlockFace blockFace;
	}

	@NoArgsConstructor
	public static class ChestConfiguration {
		public int 				       maxItemsPerLine = 3;
		public List<ChestMapItemStack> itemStacks	   = new ArrayList<>();
	}

	@Data@EqualsAndHashCode(callSuper=false)
	public static class ChestMapItemStack extends MapItemStack {
		private 		  int 	  probability;
		private transient boolean keep;

		private 		  String  optional_i18n_displayname;
		private 		  String  optional_i18n_lore;

		public ChestMapItemStack(){
			this.keep = true;
		}

		public ChestMapItemStack(ItemStack is, int prob){
			super(is);

			this.probability = prob;
			this.keep		 = true;
		}

		public ChestMapItemStack(ItemStack is, int prob, boolean keep){
			super(is);

			this.probability = prob;
			this.keep		 = keep;
		}

		public ItemStack getHandle(Locale locale){
			ItemStack stack = getHandle();

			ItemMeta meta = stack.getItemMeta();

			if(optional_i18n_displayname != null)
			{
				meta.setDisplayName( GameAPI.i18n().get(optional_i18n_displayname)[0] );
			}

			if(optional_i18n_lore != null)
			{
				meta.setDisplayName( GameAPI.i18n().get(optional_i18n_lore)[0] );
			}

			stack.setItemMeta(meta);

			return stack;
		}
	}

	private class ChestGenData {
		public Map<Location, ItemStack[]> chests = new HashMap<>();

		public void chestUpdated(Location loc, ItemStack[] is){
			chests.put(loc, is);

			Block relative = getNearbyChest( loc.getBlock() );

			if(relative != null)
				chests.put(relative.getLocation(), is);
		}

		public boolean has(Location loc){
			return chests.containsKey(loc);
		}
	}
}
