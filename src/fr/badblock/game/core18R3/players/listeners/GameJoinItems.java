package fr.badblock.game.core18R3.players.listeners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.api.PlayerLoadedEvent;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.data.InGameKitData;
import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.run.BadblockGame;
import fr.badblock.gameapi.servers.JoinItems;
import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.itemstack.CustomInventory;
import fr.badblock.gameapi.utils.itemstack.ItemAction;
import fr.badblock.gameapi.utils.itemstack.ItemEvent;
import fr.badblock.gameapi.utils.itemstack.ItemStackExtra.ItemPlaces;
import lombok.NonNull;

public class GameJoinItems extends BadListener implements JoinItems {
	private int 			       kitItemSlot   = -1;
	private int 			       teamItemSlot  = -1;
	private int 			       voteItemSlot  = -1;
	private int 			       leaveItemSlot = -1;
	private int 			       achieItemSlot = -1;
	
	
	private String 			       fallbackServer = "lobby";
	private List<PlayerKit>        kits;
	private Map<String, PlayerKit> knowKits;
	private List<BadblockTeam>     teams;
	
	private BadblockGame		   game;
	
	private boolean				   clear = true,
								   ended = false;
	
	public GameJoinItems(){

	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		if(clear && !ended){
			BadblockPlayer player = (BadblockPlayer) e.getPlayer();
			
			player.clearInventory();
		}
	}
	
	@EventHandler
	public void onPlayerDataLoaded(PlayerLoadedEvent e){
		if(ended) return;
		
		Locale locale = e.getPlayer().getPlayerData().getLocale();
		
		if(kitItemSlot != -1){
			String lastUsedKit = e.getPlayer().getPlayerData().getLastUsedKit(GameAPI.getInternalGameName());
			
			if(lastUsedKit != null && knowKits.containsKey(lastUsedKit)){
				PlayerKit kit = knowKits.get(lastUsedKit);
				if(!kit.isVIP() || e.getPlayer().hasPermission(GamePermission.VIP)){
					e.getPlayer().inGameData(InGameKitData.class).setChoosedKit(knowKits.get(lastUsedKit));					
				}
			}
			
			ItemEvent event = new ItemEvent(){
				@Override
				public boolean call(ItemAction action, BadblockPlayer player) {
					CustomInventory inventory = GameAPI.getAPI().createCustomInventory(kits.size() / 9, GameAPI.i18n().get(locale, "joinitems.kit.inventoryName")[0]);
					
					int slot = 0;
					
					for(PlayerKit kit : kits){
						if(kit != null){
							inventory.addClickableItem(slot, kit.getKitItem(player));
						}
						
						slot++;
					}
					if (slot == 0) {
						player.sendTranslatedMessage("game.nokit");
						return true;
					}
					
					inventory.openInventory(player);
					
					return true;
				}
			};
			
			ItemStack item = GameAPI.getAPI().createItemStackFactory().type(Material.DIAMOND_SWORD)
													 .doWithI18n(locale)
													 .displayName("joinitems.kit.displayname")
													 .lore("joinitems.kit.lore")
													 .asExtra(1)
													 .listenAs(event, ItemPlaces.HOTBAR_CLICKABLE)
													 .getHandler();
			
			e.getPlayer().getInventory().setItem(kitItemSlot, item);
		}
		
		if(achieItemSlot != -1){
			ItemEvent event = new ItemEvent(){
				@Override
				public boolean call(ItemAction action, BadblockPlayer player) {
					game.getGameData().getAchievements().openInventory(player);
					return true;
				}
			};
			
			ItemStack item = GameAPI.getAPI().createItemStackFactory().type(Material.NETHER_STAR)
													 .doWithI18n(locale)
													 .displayName("joinitems.achievements.displayname")
													 .lore("joinitems.achievements.lore")
													 .asExtra(1)
													 .listenAs(event, ItemPlaces.HOTBAR_CLICKABLE)
													 .getHandler();
			
			e.getPlayer().getInventory().setItem(achieItemSlot, item);
		}
		
		if(teamItemSlot != -1){
			ItemEvent event = new ItemEvent(){
				@Override
				public boolean call(ItemAction action, BadblockPlayer player) {
					CustomInventory inventory = GameAPI.getAPI().createCustomInventory(teams.size() / 9, GameAPI.i18n().get(locale, "joinitems.team.inventoryName")[0]);
					
					int slot = 0;
					
					for(BadblockTeam team : teams){
						if(team != null){
							inventory.addClickableItem(slot, team.createJoinItem(locale));
						}
						
						slot++;
					}
					
					inventory.openInventory(player);
					
					return true;
				}
			};
			
			ItemStack item = GameAPI.getAPI().createItemStackFactory().type(Material.WOOL)
													 .doWithI18n(locale)
													 .displayName("joinitems.team.displayname")
													 .lore("joinitems.team.lore")
													 .asExtra(1)
													 .listenAs(event, ItemPlaces.HOTBAR_CLICKABLE)
													 .getHandler();
			
			e.getPlayer().getInventory().setItem(teamItemSlot, item);
		}
		
		if(voteItemSlot != -1){
			ItemEvent event = new ItemEvent(){
				@Override
				public boolean call(ItemAction action, BadblockPlayer player) {
					GameAPI.getAPI().getBadblockScoreboard().openVoteInventory(player);
					return true;
				}
			};
			
			ItemStack item = GameAPI.getAPI().createItemStackFactory().type(Material.PAPER)
													 .doWithI18n(locale)
													 .displayName("joinitems.vote.displayname")
													 .lore("joinitems.vote.lore")
													 .asExtra(1)
													 .listenAs(event, ItemPlaces.HOTBAR_CLICKABLE)
													 .getHandler();
			
			e.getPlayer().getInventory().setItem(voteItemSlot, item);
		}
		
		if(leaveItemSlot != -1){
			ItemEvent event = new ItemEvent(){
				@Override
				public boolean call(ItemAction action, BadblockPlayer player) {
					e.getPlayer().sendPlayer(fallbackServer);
					return true;
				}
			};
			
			ItemStack item = GameAPI.getAPI().createItemStackFactory().type(Material.DARK_OAK_DOOR_ITEM)
													 .doWithI18n(locale)
													 .displayName("joinitems.leave.displayname")
													 .lore("joinitems.leave.lore")
													 .asExtra(1)
													 .listenAs(event, ItemPlaces.HOTBAR_CLICKABLE)
													 .getHandler();
			
			e.getPlayer().getInventory().setItem(leaveItemSlot, item);
		}
		
		e.getPlayer().updateInventory();
	}
	
	@Override
	public void registerKitItem(int slot, Map<String, PlayerKit> kits, File kitListConfig) {
		this.kitItemSlot = slot;
		
		this.kits 	  = new ArrayList<>();
		this.knowKits = kits;
		
		FileConfiguration configuration = YamlConfiguration.loadConfiguration(kitListConfig);
		
		if(!configuration.contains("kitsInventory")){
			configuration.set("kitsInventory", Arrays.asList("null"));
		}
		
		List<String> kitNames = configuration.getStringList("kitsInventory");
		
		if(kitNames.size() % 9 != 0){
			
			for(int i=0;i<kitNames.size() % 9;i++){
				kitNames.add("null");
			}
			
			configuration.set("kitsInventory", kitNames);
			
		}
		
		for(String kitName : kitNames){
			if(kits.containsKey(kitName.toLowerCase())){
				this.kits.add(kits.get(kitName.toLowerCase()));
			} else this.kits.add(null);
		}
		
		try {
			configuration.save(kitListConfig);
		} catch (IOException e){}
	}

	@Override
	public void registerTeamItem(int slot, File teamListConfig) {
		this.teamItemSlot = slot;
		
		this.teams = new ArrayList<>();
		
		FileConfiguration configuration = YamlConfiguration.loadConfiguration(teamListConfig);
		
		if(!configuration.contains("teamsInventory")){
			configuration.set("teamsInventory", Arrays.asList("null"));
		}
		
		List<String> teamNames = configuration.getStringList("teamsInventory");
		
		if(teamNames.size() % 9 != 0){
			
			for(int i=0;i<teamNames.size() % 9;i++){
				teamNames.add("null");
			}
			
			configuration.set("teamsInventory", teamNames);
			
		}
		
		for(String teamName : teamNames){
			BadblockTeam team = GameAPI.getAPI().getTeam(teamName);
			
			if(team != null){
				this.teams.add( team );
			} else this.teams.add(null);
		}
		
		try {
			configuration.save(teamListConfig);
		} catch (IOException e){}
	}

	@Override
	public void registerAchievementsItem(int slot, BadblockGame game) {
		this.achieItemSlot = slot;
		this.game 		   = game;
	}
	
	@Override
	public void registerVoteItem(int slot) {
		this.voteItemSlot = slot;
	}

	@Override
	public void registerLeaveItem(int slot, @NonNull String fallbackServer) {
		this.leaveItemSlot  = slot;
		this.fallbackServer = fallbackServer;
	}

	@Override
	public void doClearInventory(boolean clear) {
		this.clear = clear;
	}

	@Override
	public void end() {
		this.ended = true;
	}
}
