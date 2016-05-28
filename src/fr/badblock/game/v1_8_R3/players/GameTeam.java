package fr.badblock.game.v1_8_R3.players;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.api.PlayerJoinTeamEvent;
import fr.badblock.gameapi.events.api.PlayerJoinTeamEvent.JoinReason;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.data.TeamData;
import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.itemstack.ItemAction;
import fr.badblock.gameapi.utils.itemstack.ItemEvent;
import fr.badblock.gameapi.utils.itemstack.ItemStackExtra;
import fr.badblock.gameapi.utils.itemstack.ItemStackExtra.ItemPlaces;
import lombok.Getter;

public class GameTeam implements BadblockTeam {
	@Getter
	private String 	  key;
	@Getter
	private ChatColor color;
	@Getter
	private DyeColor  dyeColor;
	private String 	  hexColor;
	@Getter
	private int	   maxPlayers;

	private Map<Class<?>, TeamData> teamData = null;

	private List<UUID> 				players  = null;

	@SuppressWarnings("deprecation")
	public GameTeam(ConfigurationSection section, int maxPlayers){
		this.teamData 		= Maps.newConcurrentMap();
		this.players		= new ArrayList<>();
		this.maxPlayers 	= maxPlayers;

		this.key			= section.getString("key");
		this.color			= ChatColor.getByChar((char) section.getInt("colorCode"));
		this.dyeColor		= DyeColor.getByWoolData((byte) section.getInt("woolColor"));
		this.hexColor		= section.getString("hexColor");
	}

	@Override
	public TranslatableString getTabPrefix(ChatColor color) {
		return new TranslatableString("teams." + key + ".tabprefix", color.getChar());
	}

	@Override
	public TranslatableString getChatName() {
		return new TranslatableString("teams." + key + ".chatname");
	}

	@Override
	public TranslatableString getChatPrefix() {
		return new TranslatableString("teams." + key + ".chatprefix");
	}

	@Override
	public Color geNormalColor() {
		int start = 1;
		if(hexColor.length() == 6){
			start = 0;
		}

		int r = 0; int g = 0; int b = 0;
		try{
			r = Integer.valueOf(hexColor.substring(start, start + 2), 16) ;
			g = Integer.valueOf(hexColor.substring(start + 2, start + 4), 16);
			b = Integer.valueOf(hexColor.substring(start + 4, start + 6), 16);
		} catch(Exception e){
			System.out.println("Warning : " + hexColor + " is not a valid hexadecimal RGB color !");
		}
		return Color.fromRGB(r, g, b);
	}

	public BadblockPlayer getRandomPlayer(){
		Collection<BadblockPlayer> list = getOnlinePlayers();
		
		int max = new Random().nextInt(list.size());
		int i   = 0;
		
		for(BadblockPlayer player : list){
			
			if(i == max){
				return player;
			}
			
			i++;
			
		}
		
		return null;
	}
	
	@Override
	public int playersCurrentlyOnline() {
		int count = 0;

		for(UUID uniqueId : players){
			if(Bukkit.getPlayer(uniqueId) != null)
				count++;
		}

		return count;
	}

	@Override
	public Collection<BadblockPlayer> getOnlinePlayers() {
		List<BadblockPlayer> players = new ArrayList<>();

		for(UUID uniqueId : this.players){
			BadblockPlayer player = (BadblockPlayer) Bukkit.getPlayer(uniqueId);

			if(player != null)
				players.add(player);
		}

		return players;
	}

	@Override
	public Collection<String> getAllPlayers() {
		List<String> players = new ArrayList<>();

		for(UUID uniqueId : this.players){
			OfflinePlayer player = Bukkit.getOfflinePlayer(uniqueId);

			players.add(player.getName());
		}

		return players;
	}

	@Override
	public boolean joinTeam(BadblockPlayer player, JoinReason reason) {
		if(GameAPI.getAPI().getGameServer().getGameState() != GameState.WAITING) return false; // la partie a commencée, ouste !

		if(players.contains(player.getUniqueId())){
			player.sendTranslatedMessage("teams.already-in", getChatName()); return false;
		} else if(playersCurrentlyOnline() == getMaxPlayers()){
			player.sendTranslatedMessage("teams.full", getChatName()); return false;
		}

		PlayerJoinTeamEvent e = new PlayerJoinTeamEvent(player, player.getTeam(), this, reason);
		Bukkit.getPluginManager().callEvent(e);

		if(e.isCancelled()){
			e.getCancelReason().send(player); return false;
		}


		if(player.getTeam() != null){
			player.getTeam().leaveTeam(player); // on leave l'ancienne team
		}

		((GameBadblockPlayer) player).setTeam(this);
		players.add(player.getUniqueId());

		if(reason == JoinReason.WHILE_WAITING)
			player.sendTranslatedTitle("teams.joinTeam", getChatName());
		player.sendTimings(10, 40, 10);

		return true;
	}

	@Override
	public void leaveTeam(BadblockPlayer player) {
		if(players.contains(player.getUniqueId())){
			((GameBadblockPlayer) player).setTeam(null);
			players.remove(player.getUniqueId());
		}
	}

	@Override
	public <T extends TeamData> T teamData(Class<T> clazz) {
		try {
			if (!teamData.containsKey(clazz)) {
				teamData.put(clazz, (TeamData) clazz.getConstructor().newInstance());
			}

			return clazz.cast(teamData.get(clazz));
		} catch (Exception e) {
			e.printStackTrace();
			GameAPI.logError("Invalid TeamData class (" + clazz + ") ! Return null.");
			return null;
		}
	}

	private String[] lore(Locale locale){
		List<String> result = new ArrayList<>();

		if(playersCurrentlyOnline() == getMaxPlayers()){
			result.add(GameAPI.i18n().get(locale, "teams.joinitem.lorefull")[0]);
		} else {
			result.add(GameAPI.i18n().get(locale, "teams.joinitem.loreclick", playersCurrentlyOnline())[0]);
		}

		result.add("");

		result.add(GameAPI.i18n().get(locale, "teams.joinitem.players")[0]);
		
		int count = 0;
		
		for(UUID uniqueId : players){
			Player player = Bukkit.getPlayer(uniqueId);

			if(player != null){
				count++;
				result.add(GameAPI.i18n().get(locale, "teams.joinitem.knowPlayer", player.getName())[0]);
			}
		}
		
		int free = maxPlayers - count;
		
		for(;count<maxPlayers;count++){
			result.add(GameAPI.i18n().get(locale, "teams.joinitem.place")[0]);
		}
		
		result.add("");
		
		result.add(GameAPI.i18n().get(locale, "teams.joinitem.free", free)[0]);

		return result.toArray(new String[0]);
	}

	@SuppressWarnings("deprecation") @Override
	public ItemStackExtra createJoinItem(Locale locale) {
		return GameAPI.getAPI().createItemStackFactory()
				.displayName(GameAPI.i18n().get(locale, "teams.joinitem.displayname", getChatName())[0])
				.lore(lore(locale))
				.type(Material.WOOL)
				.durability(getDyeColor().getWoolData())
				.asExtra(playersCurrentlyOnline())
				.listenAs(new ItemEvent(){
					@Override
					public boolean call(ItemAction action, BadblockPlayer player) {
						player.closeInventory();
						joinTeam(player, JoinReason.WHILE_WAITING);
						return true;
					}
				}, ItemPlaces.INVENTORY_CLICKABLE);
	}
}
