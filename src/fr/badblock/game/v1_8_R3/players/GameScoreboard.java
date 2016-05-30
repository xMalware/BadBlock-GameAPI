package fr.badblock.game.v1_8_R3.players;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.api.PlayerJoinTeamEvent;
import fr.badblock.gameapi.events.api.PlayerLoadedEvent;
import fr.badblock.gameapi.events.api.PlayerReloadedEvent;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardTeam;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardTeam.TeamFriendlyFire;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardTeam.TeamMode;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardTeam.TeamNameTag;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.scoreboard.BadblockScoreboard;
import fr.badblock.gameapi.utils.i18n.I18n;
import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.itemstack.CustomInventory;
import fr.badblock.gameapi.utils.itemstack.ItemAction;
import fr.badblock.gameapi.utils.itemstack.ItemEvent;
import fr.badblock.permissions.PermissibleGroup;
import fr.badblock.permissions.PermissionManager;

public class GameScoreboard extends BadListener implements BadblockScoreboard {
	private Scoreboard board 	       = Bukkit.getScoreboardManager().getNewScoreboard();
	private Objective  tabListHealth   = null;
	private Objective  belowNameHealth = null;
	private Objective  voteObjective   = null;

	private boolean    doTeamsPrefix   = false;
	private boolean    doGroupsPrefix  = false;

	Map<VoteElement, Integer> votes;

	public GameScoreboard(){

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		e.getPlayer().setScoreboard(board);
		
		BadblockPlayer p = (BadblockPlayer) e.getPlayer();
		
		if(doGroupsPrefix)
			sendTeams(p);
		if(doTeamsPrefix){
			if(p.getTeam() != null){
				joinTeam(p, null, p.getTeam());
			} else {
				GameAPI.getAPI().getTeams().forEach((knowTeam) -> {
					sendTeam(p, knowTeam, ChatColor.GRAY);
				});
			}
		}
	}
	
	@EventHandler
	public void onDataReceive(PlayerLoadedEvent e){
		if(!doGroupsPrefix) return;
		
		getHandler().getTeam(e.getPlayer().getMainGroup()).addEntry(e.getPlayer().getName());
	}
	
	@EventHandler
	public void onDataReload(PlayerReloadedEvent e){
		if(!doGroupsPrefix) return;
		
		Team team = getHandler().getEntryTeam(e.getPlayer().getName());
		
		if(!team.getName().equals(e.getPlayer().getMainGroup())){
			team.removeEntry(e.getPlayer().getName());
			getHandler().getTeam(e.getPlayer().getMainGroup()).addEntry(e.getPlayer().getName());
		}
	}

	@Override
	public Scoreboard getHandler() {
		return board;
	}

	@Override
	public void doTabListHealth(){
		if(tabListHealth == null){
			tabListHealth = board.registerNewObjective("health", "health");

			tabListHealth.setDisplaySlot(DisplaySlot.PLAYER_LIST);

			for(Player p : Bukkit.getOnlinePlayers()){
				tabListHealth.getScore(p.getName()).setScore((int)p.getHealth());
			}
		}
	}

	@Override
	public void doBelowNameHealth(){
		if(belowNameHealth == null){
			belowNameHealth = board.registerNewObjective("belowNameHealth", "health");
			belowNameHealth.setDisplayName(ChatColor.RED + "\u2764");

			belowNameHealth.setDisplaySlot(DisplaySlot.BELOW_NAME);

			for(Player p : Bukkit.getOnlinePlayers()){
				belowNameHealth.getScore(p.getName()).setScore((int)p.getHealth());
			}
		}
	}

	@EventHandler
	public void onPlayerJoinTeam(PlayerJoinTeamEvent e){
		if(doTeamsPrefix)
			joinTeam(e.getPlayer(), e.getPreviousTeam(), e.getNewTeam());
	}
	
	protected void joinTeam(BadblockPlayer p, BadblockTeam previous, BadblockTeam team){
		getHandler().getTeam(team.getKey()).addEntry(p.getName());
		
		if(previous != null){
			getHandler().getTeam(previous.getKey()).removeEntry(p.getName());
		}
		
		GameAPI.getAPI().getTeams().forEach((knowTeam) -> {
			if(team.equals(knowTeam)){
				sendTeam(p, knowTeam, ChatColor.GREEN);
			} else {
				sendTeam(p, knowTeam, ChatColor.GRAY);
			}
		});
	}
	
	protected void sendTeams(BadblockPlayer player){
		for(PermissibleGroup group : PermissionManager.getInstance().getGroups()){
			sendTeamData(group.getName(), new TranslatableString("permissions.tab." + group.getName()).getAsLine(player), player);
		}
	}
	
	/*
	 * Envoit le nouveau préfixe via packet, pour l'avoir custom sans le déclarer dans un scoreboard personnel (opti)
	 */
	protected void sendTeam(BadblockPlayer p, BadblockTeam team, ChatColor color){
		String displayName = team.getTabPrefix(color).getAsLine(p);
		sendTeamData(team.getKey(), displayName, p);
	}
	
	private void sendTeamData(String teamName, String displayName, BadblockPlayer p){
		GameAPI.getAPI().createPacket(PlayScoreboardTeam.class)
						.setMode(TeamMode.UPDATE)
						.setTeamName(teamName)
						.setFriendlyFire(TeamFriendlyFire.OFF)
						.setPrefix(displayName)
						.setDisplayName("")
						.setSuffix(ChatColor.RESET + (tabListHealth == null ? "" : " "))
						.setColor((byte) 0)
						.setNametagVisibility(TeamNameTag.always)
						.send(p);
	}
	
	@Override
	public void doGroupsPrefix(){
		if(doTeamsPrefix){
			throw new RuntimeException("Already doing team prefixs");
		}
		
		doGroupsPrefix = true;
		
		PermissionManager.getInstance().getGroups().forEach((group) -> {
			
			if(getHandler().getTeam(group.getName()) == null){
				getHandler().registerNewTeam(group.getName());
			}
			
			Team teamHandler = getHandler().getTeam(group.getName());
			
			// On ne définit pas le préfixe car il faut le faire pour chaque joueur (préfixe allié / ennemi)
			teamHandler.setAllowFriendlyFire(false);
		});
	}
	
	@Override
	public void doTeamsPrefix(){
		if(doGroupsPrefix){
			throw new RuntimeException("Already doing group prefixs");
		}
		
		doTeamsPrefix = true;
		
		GameAPI.getAPI().getTeams().forEach((team) -> {
			
			if(getHandler().getTeam(team.getKey()) == null){
				getHandler().registerNewTeam(team.getKey());
			}
			
			Team teamHandler = getHandler().getTeam(team.getKey());
			
			// On ne définit pas le préfixe car il faut le faire pour chaque joueur (préfixe allié / ennemi)
			teamHandler.setAllowFriendlyFire(false);
		});
	}
	
	@Override
	public void beginVote(JsonArray maps) {
		votes = Maps.newLinkedHashMap();

		voteObjective = board.registerNewObjective("vote", "dummy");
		voteObjective.setDisplayName(GameAPI.i18n().get("vote.scoreboardName", GameAPI.getGameName())[0]);
		voteObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

		for(int i=0;i<maps.size();i++){
			JsonElement element = maps.get(i);

			if(element.isJsonObject()){
				VoteElement vote = GameAPI.getGson().fromJson(element, VoteElement.class);
				voteObjective.getScore(GameAPI.i18n().replaceColors(vote.getDisplayName())).setScore(0);
				votes.put(vote, 0);
			}
		}
	}

	@Override
	public void endVote() {
		if(votes == null || voteObjective == null) return;

		voteObjective.unregister();
		voteObjective = null;

		GameAPI.i18n().broadcast("vote.end", getWinner().getDisplayName(), getVotesForWinner());
	}

	@Override
	public void openVoteInventory(BadblockPlayer player) {
		Locale locale = player.getPlayerData().getLocale();
		
		I18n i18n = GameAPI.i18n();

		int lines = votes.size() / 9 + (votes.size() % 9 == 0 ? 0 : 1);

		if(lines == 0) lines++;
		else if(lines > 6) lines = 6;

		int slot   = 0;

		CustomInventory inventory = GameAPI.getAPI().createCustomInventory(lines, i18n.get(locale, "vote.inventoryDisplayname")[0]);

		for(Entry<VoteElement, Integer> entry : votes.entrySet()){
			if(slot == inventory.size()) break;

			final VoteElement element = entry.getKey();
			
			inventory.addClickableItem(slot, GameAPI.getAPI().createItemStackFactory()
					.type(Material.PAPER)
					.displayName(entry.getKey().getDisplayName())
					.lore(i18n.get(locale, "maps." + entry.getKey().getInternalName(), entry.getValue()))
					.create(1)
					, new ItemEvent(){
				@Override
				public boolean call(ItemAction action, BadblockPlayer player) {
					if(votes == null || voteObjective == null) return true;
					
					VoteInGameData data = player.inGameData(VoteInGameData.class);
					
					if(data.getElement() != null && votes.containsKey(data.getElement())){
						votes.put(data.getElement(), votes.get(data.getElement()) - 1);
						voteObjective.getScore(i18n.replaceColors(data.getElement().getDisplayName())).setScore(votes.get(data.getElement()));
					}
					
					data.setElement(element);
					
					votes.put(data.getElement(), votes.get(data.getElement()) + 1);
					voteObjective.getScore(i18n.replaceColors(data.getElement().getDisplayName())).setScore(votes.get(data.getElement()));
				
					player.closeInventory();
					player.sendTranslatedActionBar("vote.success", data.getElement().getDisplayName());
				
					return true;
				}
			});
			
			slot++;
		}
		
		inventory.openInventory(player);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();
		
		VoteInGameData data = player.inGameData(VoteInGameData.class);
		
		if(data.getElement() != null && votes.containsKey(data.getElement()) && voteObjective != null){
			votes.put(data.getElement(), votes.get(data.getElement()) - 1);
			voteObjective.getScore(GameAPI.i18n().replaceColors(data.getElement().getDisplayName())).setScore(votes.get(data.getElement()));
		}
		
		if(getHandler().getEntryTeam(player.getName()) != null && GameAPI.getAPI().getGameServer().getGameState() != GameState.RUNNING){
			getHandler().getEntryTeam(player.getName()).removeEntry(player.getName());
		}
	}
	
	@Override
	public VoteElement getWinner() {
		if(votes == null) return null;

		Entry<VoteElement, Integer> max = null;

		for(Entry<VoteElement, Integer> value : votes.entrySet()){
			if(max == null || max.getValue() < value.getValue())
				max = value;
		}

		return max == null ? null : max.getKey();
	}

	@Override
	public int getVotesForWinner() {
		if(votes == null) return 0;

		Entry<VoteElement, Integer> max = null;

		for(Entry<VoteElement, Integer> value : votes.entrySet()){
			if(max == null || max.getValue() < value.getValue())
				max = value;
		}

		return max == null ? null : max.getValue();
	}
}
