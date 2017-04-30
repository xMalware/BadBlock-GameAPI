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
			player.sendTranslatedMessage("hub.auth.notanint");
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
				if (AuthUtils.gAuth.authorize(secretKey, enteredTemporaryCode)) {
					player.sendTranslatedMessage("hub.auth.removed");
					player.getInventory().setItem(6, null);
					player.getInventory().setHeldItemSlot(4);
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
