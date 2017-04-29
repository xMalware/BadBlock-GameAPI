package fr.badblock.bukkit.hub.commands;

import org.bukkit.command.CommandSender;

import com.google.gson.JsonObject;

import fr.badblock.bukkit.hub.inventories.selector.googleauth.AuthUtils;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class AuthRemoveCommand extends AbstractCommand {
	
	public AuthRemoveCommand() {
		super("authremove", new TranslatableString("hub.auth.authremove"), GamePermission.PLAYER, GamePermission.PLAYER, GamePermission.PLAYER);
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
			player.sendMessage("hub.auth.notanint");
			return true;
		}
		final int enteredTemporaryCode = secretId;
		String playerName = player.getName().toLowerCase();
		AuthUtils.getAuthKey(playerName, new Callback<String>() {
			@Override
			public void done(String string, Throwable throwable) {
				String secretKey = string;
				if (secretKey == null || secretKey.isEmpty()) {
					player.sendTranslatedMessage("hub.auth.nokeyassociatedwithyouraccount");
					return;
				}
				int currentTemporaryCode = AuthUtils.gAuth.getTotpPassword(secretKey);
				if (enteredTemporaryCode == currentTemporaryCode) {
					player.sendTranslatedMessage("hub.auth.removed");
					updateAuthKey(playerName, "");
					AuthUtils.tempPlayersKeys.remove(playerName);
					return;
				}
				player.sendTranslatedMessage("hub.auth.unabletoremove");
			}
		});
		return true;
	}
	
	public void updateAuthKey(String player, String secretCode) {
		JsonObject object = new JsonObject();
		object.addProperty("authKey", secretCode);
		GameAPI.getAPI().getLadderDatabase().updatePlayerData(player, object);
	}
	
}
