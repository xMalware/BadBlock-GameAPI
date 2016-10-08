package fr.badblock.game.core18R3.players;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardDisplayObjective;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardDisplayObjective.ObjectivePosition;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardObjective;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardObjective.ObjectiveMode;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardObjective.ObjectiveType;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardScore;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardScore.ScoreMode;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardTeam;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardTeam.TeamMode;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.scoreboard.BadblockScoreboardGenerator;
import fr.badblock.gameapi.players.scoreboard.CustomObjective;
import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * @author LeLanN
 */
public class GameCustomObjective implements CustomObjective {
	private static final String colorChar = Character.toString(ChatColor.COLOR_CHAR);

	
	private Map<Integer, CustomTeam> 	teams;
	
	private UUID 		  			 	player;
	private String		  		     	name;
	private String					 	displayName;
	@Setter
	private BadblockScoreboardGenerator generator;
	
	public GameCustomObjective(String name){
		this.teams 		 = new HashMap<>();
		this.name  		 = name;
		this.displayName = name;
		
		for(int i=1;i<=15;i++){ // on prÃ©pare toutes les teams, inutile de les unregisters elles ne gÃ¨nent pas :3
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
		((GameBadblockPlayer) player).setCustomObjective(this); // on donne Ã  la classe l'info
		
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
		this.displayName = GameAPI.i18n().replaceColors(displayName);
		
		if(player == null) return;
		
		GameAPI.getAPI().createPacket(PlayScoreboardObjective.class).setDisplayName(this.displayName)
		   													    .setMode(ObjectiveMode.UPDATE)
																.setObjectiveName(name)
																.setObjectiveType(ObjectiveType.INTEGER)
																.send(getAssignedPlayer());
	}

	@Override
	public void changeLine(int line, String text) {
		if(line < 1 || line > 15) return;

		CustomTeam team = teams.get(line);
		text = GameAPI.i18n().replaceColors(text);

		String prefix = text.substring(0, text.length() <= 16 ? text.length() : 16);
		String suffix = "";


		if(prefix.endsWith(colorChar)){
			prefix = prefix.substring(0, prefix.length() - 1);

			suffix += colorChar;
		} else {
			String lastColor = lastColor(prefix);
			suffix = lastColor == null ? "" : lastColor;
		}

		suffix += (text.length() > 16 ? text.substring(16) : "");

		if(suffix.length() > 16){
			suffix = suffix.substring(0,  16);
		}
		
		team.prefix = prefix;
		team.suffix = suffix;
		
		if(player != null && getAssignedPlayer() != null && getAssignedPlayer().isOnline()){
			changeTeam(getAssignedPlayer(), team);
			setScore(getAssignedPlayer(), team.name, line);
		}
	}

	@Override
	public void removeLine(int line) {
		if(line < 1 || line > 15) return;
	
		CustomTeam custom = teams.get(line);
		custom.prefix = "";
		custom.suffix = "";
		
		if(player != null && getAssignedPlayer().isOnline()){
			removeScore(getAssignedPlayer(), custom.name);
		}
	}

	@Override
	public void reset() {
		for(int i=1;i<=15;i++){
			removeLine(i);
		}
	}
	
	@Override
	public void generate() {
		if(generator != null)
			generator.generate();
	}
	
	protected void sendTeam(BadblockPlayer player, CustomTeam team){
		if(Bukkit.getScoreboardManager().getMainScoreboard().getTeam(team.name) == null) { // la team existe dÃ©jÃ  on ajoute juste le 'joueur'
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
	
	private static String lastColor(String str){
		str = ChatColor.translateAlternateColorCodes('&', str);
		ChatColor last = null;
		ChatColor encod = null;

		char code = ChatColor.COLOR_CHAR;
		
		for(int i=0;i<str.length();i++){
			if(str.charAt(i) == code && str.length() > i + 1){
				ChatColor color = ChatColor.getByChar(str.charAt(i + 1));

				last = color;

				if(color.isColor() && str.length() > i + 2){
					if(str.charAt(i + 2) == code && str.length() > i + 3){
						encod = ChatColor.getByChar(str.charAt(i + 3));

						i += 2;
					} else encod = null;
				} else encod = null;

				i++;

				if(color != null){
					last = color;
				}
			}
		}

		return last == null ? "" : (last + "" + (encod == null ? "" : encod.toString()));
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
