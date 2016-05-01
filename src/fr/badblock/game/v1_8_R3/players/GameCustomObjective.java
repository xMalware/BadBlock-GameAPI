package fr.badblock.game.v1_8_R3.players;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardDisplayObjective;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardObjective;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardScore;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardTeam;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardDisplayObjective.ObjectivePosition;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardObjective.ObjectiveMode;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardObjective.ObjectiveType;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardScore.ScoreMode;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardTeam.TeamMode;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.CustomObjective;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatColor;

/**
 * @author LeLanN
 */
public class GameCustomObjective implements CustomObjective {
	private Map<Integer, CustomTeam> teams;
	
	private UUID 		  			 player;
	private String		  		     name;
	private String					 displayName;
	
	public GameCustomObjective(String name){
		this.teams 		 = new HashMap<>();
		this.name  		 = name;
		this.displayName = name;
		
		for(int i=1;i<=15;i++){ // on prépare toutes les teams, inutile de les unregisters elles ne gènent pas :3
			CustomTeam custom = new CustomTeam(Identifiers.getLineIdentifier(i), "", "");
			teams.put(i, custom);
		}
	}
	
	@Override
	public BadblockPlayer getAssignedPlayer() {
		return player == null ? null : (BadblockPlayer) Bukkit.getPlayer(player);
	}

	@Override
	public void showObjective(BadblockPlayer player) {
		this.player = player.getUniqueId();
		((GameBadblockPlayer) player).setCustomObjective(this); // on donne à la classe l'info
		
		GameAPI.getAPI().createPacket(PlayScoreboardObjective.class).setDisplayName(displayName)
															    .setMode(ObjectiveMode.CREATE)
																.setObjectiveName(name)
																.setObjectiveType(ObjectiveType.INTEGER)
																.send(player);
		
		for(Integer id : teams.keySet()){
			CustomTeam custom = teams.get(id);
			sendTeam(player, custom);
			
			if(!custom.prefix.isEmpty()){
				setScore(player, custom.name, id);
			}
		}
		
		GameAPI.getAPI().createPacket(PlayScoreboardDisplayObjective.class).setObjectiveName(name)
																	   .setObjectivePosition(ObjectivePosition.SIDEBAR)
																	   .send(player);
	}

	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		
		if(player == null) return;
		
		GameAPI.getAPI().createPacket(PlayScoreboardObjective.class).setDisplayName(displayName)
		   													    .setMode(ObjectiveMode.UPDATE)
																.setObjectiveName(name)
																.setObjectiveType(ObjectiveType.INTEGER)
																.send(getAssignedPlayer());
	}

	@Override
	public void changeLine(int line, String text) {
		if(line < 1 || line > 15) return;
		
		CustomTeam custom = teams.get(line);
		custom.prefix = text.substring(0, text.length() <= 16 ? text.length() : 16);
		custom.suffix = text.length() > 16 ? text.substring(16) : "";

		if(player != null){
			changeTeam(getAssignedPlayer(), custom);
			setScore(getAssignedPlayer(), custom.name, line);
		}
	}

	@Override
	public void removeLine(int line) {
		if(line < 1 || line > 15) return;
	
		CustomTeam custom = teams.get(line);
		custom.prefix = "";
		custom.suffix = "";
		
		if(player != null){
			removeScore(getAssignedPlayer(), custom.name);
		}
	}

	@Override
	public void reset() {
		for(int i=1;i<=15;i++){
			removeLine(i);
		}
	}
	
	protected void sendTeam(BadblockPlayer player, CustomTeam team){
		if(Bukkit.getScoreboardManager().getMainScoreboard().getTeam(team.name) == null) { // la team existe déjà on ajoute juste le 'joueur'
			GameAPI.getAPI().createPacket(PlayScoreboardTeam.class)
							.setTeamName(team.name)
							.setPlayers(new String[]{team.name})
							.setMode(TeamMode.ADD_PLAYERS)
							.send(player);
		}
		
		GameAPI.getAPI().createPacket(PlayScoreboardTeam.class)
						.setPrefix(team.prefix)
						.setSuffix(team.suffix)
						.setTeamName(team.name)
						.setPlayers(new String[]{team.name})
						.setMode(TeamMode.CREATE)
						.send(player);
	}
	
	protected void changeTeam(BadblockPlayer player, CustomTeam team){
		GameAPI.getAPI().createPacket(PlayScoreboardTeam.class)
						.setPrefix(team.prefix)
						.setSuffix(team.suffix)
						.setTeamName(team.name)
						.setMode(TeamMode.UPDATE)
						.send(player);
	}
	
	public void setScore(BadblockPlayer player, String offlinePlayer, int score){
		GameAPI.getAPI().createPacket(PlayScoreboardScore.class)
						.setMode(ScoreMode.CHANGE)
						.setObjectiveName(name)
						.setScoreName(offlinePlayer)
						.setScore(score)
						.send(player);
	}
	
	public void removeScore(BadblockPlayer player, String offlinePlayer){
		GameAPI.getAPI().createPacket(PlayScoreboardScore.class)
						.setMode(ScoreMode.REMOVE)
						.setObjectiveName(name)
						.setScoreName(offlinePlayer)
						.send(player);
	}
	
	@AllArgsConstructor class CustomTeam {
		String name;
		String prefix;
		String suffix;
	}
	
	enum Identifiers {
		line1('0', 1),
		line2('1', 2),
		line3('2', 3),
		line4('3', 4),
		line5('4', 5),
		line6('5', 6),
		line7('6', 7),
		line8('7', 8),
		line9('8', 9),
		line10('9', 10),
		line11('a', 11),
		line12('b', 12),
		line13('c', 13),
		line14('d', 14),
		line15('e', 15);

		private static final Map<Integer, Character>	lookup	= new HashMap<Integer, Character>();

		static {
			for (Identifiers s : Identifiers.values())
				lookup.put(s.getLine(), s.getChar());
		}

		private Character								id;
		private Integer									line;

		Identifiers(Character id, int line) {
			this.id = id;
			this.line = line;
		}

		public static String getLineIdentifier(int line) {
			return ChatColor.getByChar(lookup.get(line).charValue()).toString() + ChatColor.RESET.toString();
		}

		public Character getChar() {
			return id;
		}

		public int getLine() {
			return line;
		}
	}
}
