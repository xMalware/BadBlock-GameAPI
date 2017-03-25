package fr.badblock.bukkit.hub.objects;

import java.util.Arrays;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.scoreboard.BadblockScoreboardGenerator;
import fr.badblock.gameapi.players.scoreboard.CustomObjective;
import fr.badblock.gameapi.run.BadblockGame;

public class HubScoreboard extends BadblockScoreboardGenerator {
	private CustomObjective objective;
	private BadblockPlayer player;

	public HubScoreboard(BadblockPlayer player) {
		this.objective = GameAPI.getAPI().buildCustomObjective("hub");
		this.player = player;
		objective.showObjective(player);
		int id = 0;
		String hubName = GameAPI.getServerName();
		String idString = "";
		for (Character character : hubName.toCharArray())
			if (Character.isDigit(character))
				idString += character.toString();
		if (idString.equals(""))
			idString = "0";
		try {
			id = Integer.parseInt(idString);
		} catch (Exception error) {
			error.printStackTrace();
		}
		objective.setDisplayName("&b&o" + i18n("hub.scoreboard.title", id));
		objective.setGenerator(this);
		objective.generate();

		doBadblockFooter(objective);
	}

	@Override
	public void generate() {
		try {
			objective.changeLine(15, "&8&m----------------------");

			int i = 14;
			objective.changeLine(i, "");
			i--;
			objective.changeLine(i, i18n("hub.scoreboard.shoppoints", (player.getPlayerData().getShopPoints() != -1
					? player.getPlayerData().getShopPoints() : i18n("hub.scoreboard.shoppoints_nowebsiteaccount"))));
			i--;
			objective.changeLine(i, i18n("hub.scoreboard.badcoins", player.getPlayerData().getBadcoins()));
			i--;
			objective.changeLine(i, "");
			double calc = (((double) (player.getPlayerData().getXp()) * 1.0D) / ((double) (player.getPlayerData().getXpUntilNextLevel()) * 1.0D)) * 1.0D;
			calc *= 100.0D;
			objective.changeLine(i, i18n("hub.scoreboard.level", player.getPlayerData().getLevel(), (int) calc));
			i--;

			int total = Arrays.asList(BadblockGame.values()).stream().filter(game -> game.getGameData() != null).mapToInt(game -> game.getGameData().getAchievements().getAllAchievements().size()).sum();

			int have  = Arrays.asList(BadblockGame.values()).stream().filter(game -> game.getGameData() != null).mapToInt(game -> {
				return (int) game.getGameData().getAchievements().getAllAchievements().stream().filter(
						playerAchievement -> player.getPlayerData().getAchievementState(playerAchievement)!= null && player.getPlayerData().getAchievementState(playerAchievement).isSucceeds()
						).count();
			}).sum();

			objective.changeLine(i, i18n("hub.scoreboard.achievements", have, total));
			i--;
			objective.changeLine(i, "");
			i--;
			objective.changeLine(i, i18n("hub.scoreboard.rank", player.getTabGroupPrefix()));
			i--;
			objective.changeLine(i, i18n("hub.scoreboard.players", HubPlayer.a));
			i--;
			objective.changeLine(i, "");

			objective.changeLine(2, "&8&m----------------------");
		} catch(Exception e){
			Thread current = Thread.currentThread();
			
			new Thread(){
				@Override
				public void run(){
					try {
						Thread.sleep(1000L);
						current.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					generate();
				}
			}.start();
		}
	}

	private String i18n(String key, Object... args) {
		return player.getTranslatedMessage(key, args)[0];
	}
}