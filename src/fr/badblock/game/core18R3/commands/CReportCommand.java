package fr.badblock.game.core18R3.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;

import fr.badblock.game.core18R3.listeners.ChatListener;
import fr.badblock.game.core18R3.players.data.ChatData;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class CReportCommand extends AbstractCommand {
	
	public static Map<String, Long> lastReport = new HashMap<>();
	
	public CReportCommand() {
		super("creport", new TranslatableString("commands.creport.usage"), GamePermission.PLAYER, GamePermission.PLAYER, GamePermission.PLAYER);
		this.allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer player = (BadblockPlayer) sender;
		if (args.length != 1) return false;
		String idString = args[0];
		int id = -1;
		try {
			id = Integer.parseInt(idString);
		}catch(Exception error) {
			return false;
		}
		if (!ChatListener.messages.containsKey(id)) {
			player.sendTranslatedMessage("commands.creport.unknownmessageonthisserver");
			return true;
		}
		if (lastReport.containsKey(player.getName())) {
			long l = lastReport.get(player.getName());
			if (l > System.currentTimeMillis()) {
				player.sendTranslatedMessage("commands.creport.toofast");
				return true;
			}
		}
		ChatData chatData = ChatListener.messages.get(id);
		if (player.getName().equalsIgnoreCase(chatData.playerName)) {
			player.sendTranslatedMessage("commands.creport.schizophrenia");
			return true;
		}
		GameAPI api = GameAPI.getAPI();
		api.getRabbitSpeaker().sendAsyncUTF8Publisher("badfilter", "§b[REPORT] §7(By " + player.getName() + ") | §7" + chatData.playerName + " §8» §7" + chatData.message, 5000, false);
		api.getSqlDatabase().call("INSERT INTO reportMsg(player, byPlayer, message, timestamp) VALUES('" + secure(chatData.playerName) + "', '" + secure(player.getName()) + "', '" + secure(chatData.message) + "', '" + System.currentTimeMillis() + "')", SQLRequestType.UPDATE);
		player.sendTranslatedMessage("commands.creport.reportedmessage", chatData.playerName, chatData.message);
		lastReport.put(player.getName(), System.currentTimeMillis() + 30_000L);
		return true;
	}
	
	public String secure(String str) {
		if (str == null) {
			return null;
		}

		if (str.replaceAll("[a-zA-Z0-9_!@#$%^&*()-=+~.;:,\\Q[\\E\\Q]\\E<>{}\\/? ]", "").length() < 1) {
			return str;
		}

		String clean_string = str;
		clean_string = clean_string.replaceAll("\\\\", "\\\\\\\\");
		clean_string = clean_string.replaceAll("\\n", "\\\\n");
		clean_string = clean_string.replaceAll("\\r", "\\\\r");
		clean_string = clean_string.replaceAll("\\t", "\\\\t");
		clean_string = clean_string.replaceAll("\\00", "\\\\0");
		clean_string = clean_string.replaceAll("'", "\\\\'");
		clean_string = clean_string.replaceAll("\\\"", "\\\\\"");

		if (clean_string.replaceAll("[a-zA-Z0-9_!@#$%^&*()-=+~.;:,\\Q[\\E\\Q]\\E<>{}\\/?\\\\\"' ]", "").length() < 1) {
			return clean_string;
		}
		try {
			java.sql.Statement stmt = GameAPI.getAPI().getSqlDatabase().createStatement();
			String qry = "SELECT QUOTE('" + clean_string + "')";

			stmt.executeQuery(qry);
			java.sql.ResultSet resultSet = stmt.getResultSet();
			resultSet.first();
			String r = resultSet.getString(1);
			return r.substring(1, r.length() - 1);
		} catch (Exception error) {
			error.printStackTrace();
			return str;
		}
	}
	
}
