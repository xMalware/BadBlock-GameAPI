package fr.badblock.bukkit.hub.commands;

import org.bukkit.command.CommandSender;

import com.google.gson.JsonObject;

import fr.badblock.bukkit.hub.inventories.selector.googleauth.AuthUtils;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class AuthCheckCommand extends AbstractCommand {
	
	public AuthCheckCommand() {
		super("authcheck", new TranslatableString("hub.auth.authcheck"), GamePermission.PLAYER, GamePermission.PLAYER, GamePermission.PLAYER);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length != 1) return false;
		BadblockPlayer player = (BadblockPlayer) sender;
		int secretId = -1;
		try {
			secretId = Integer.parseInt(args[0]);
			if (secretId < 0) throw new NullPointerException();
		}catch(Exception error) {
			player.sendTranslatedMessage("hub.auth.notanint");
			return true;
		}
		String playerName = player.getName().toLowerCase();
		String secretKey = AuthUtils.tempPlayersKeys.get(playerName);
		if (secretKey == null || secretKey.isEmpty()) {
			player.sendTranslatedMessage("hub.auth.pleasegeneratebeforecheck");
			return true;
		}
		int enteredTemporaryCode = secretId;
		int currentTemporaryCode = AuthUtils.gAuth.getTotpPassword(secretKey);
		if (enteredTemporaryCode == currentTemporaryCode) {
			player.sendTranslatedMessage("hub.auth.checked");
			updateAuthKey(playerName, secretKey);
			AuthUtils.tempPlayersKeys.remove(playerName);
			return true;
		}
		player.sendTranslatedMessage("hub.auth.unabletocheck");
		return true;
	}
	
	public void updateAuthKey(String player, String secretCode) {
		JsonObject object = new JsonObject();
		object.addProperty("authKey", secretCode);
		GameAPI.getAPI().getLadderDatabase().updatePlayerData(player, object);
	}
	
}
