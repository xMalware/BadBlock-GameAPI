package fr.badblock.game.core18R3.commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;

import com.google.gson.JsonObject;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.listeners.ShopListener;
import fr.badblock.game.core18R3.merchant.shops.Merchant;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.configuration.values.MapNamedLocation;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.merchants.CustomMerchantRecipe;

public class ShopCommand extends AbstractCommand {
	public ShopCommand() {
		super("badshops", new TranslatableString("commands.badshops.usage"), GamePermission.ADMIN, "shop", "sh");
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length < 2)
			return false;

		BadblockPlayer player = (BadblockPlayer) sender;
		args[0] = args[0].toLowerCase();

		if (args[0].equals("edit")) {
			Merchant merchant = get(args[1]);
			Inventory inventory = Bukkit.createInventory(null, 9 * 4, "Villager");

			int i = 0;

			for (CustomMerchantRecipe recipe : merchant.getRecipes().getHandle()) {
				inventory.setItem(i, recipe.getFirstItem());
				inventory.setItem(i + 9, recipe.getSecondItem());
				inventory.setItem(i + 27, recipe.getResult());

				i++;
				if (i == 9)
					break;
			}

			ShopListener.inEdit.put(player.getUniqueId(), merchant);
			player.openInventory(inventory);
		} else if (args[0].equals("addhandler") && args.length > 2) {
			Merchant merchant = get(args[1]);

			if (merchant.getEntities().containsKey(args[2].toLowerCase())) {
				player.sendTranslatedMessage("commands.badshops.already-handler", args[1], args[2]);
			} else {
				args[2] = args[2].toLowerCase();
				merchant.getHandlers().add(new MapNamedLocation(player.getLocation(), args[2]));
				merchant.addEntity(player.getLocation(), args[2]);

				merchant.save().save(file(merchant));

				player.sendTranslatedMessage("commands.badshops.added-handler", args[1], args[2]);
			}
		} else if (args[0].equals("removehandler") && args.length > 2) {
			Merchant merchant = get(args[1]);

			if (!merchant.getEntities().containsKey(args[2].toLowerCase())) {
				player.sendTranslatedMessage("commands.badshops.no-handler", args[1], args[2]);
			} else {
				merchant.getEntities().get(args[2].toLowerCase()).destroy();
				merchant.getEntities().remove(args[2].toLowerCase());

				for (int i = 0; i < merchant.getHandlers().size(); i++) {
					if (merchant.getHandlers().get(i).getName().equalsIgnoreCase(args[2])) {
						merchant.getHandlers().remove(i);
						break;
					}

				}

				merchant.save().save(file(merchant));

				player.sendTranslatedMessage("commands.badshops.removed-handler", args[1], args[2]);
			}
		} else if (args[0].equals("handlers")) {
			player.sendTranslatedMessage("commands.badshops.handlers", args[1],
					StringUtils.join(get(args[1]).getEntities().keySet(), ", "));
		} else
			return false;

		return true;
	}

	private File file(Merchant merchant) {
		return new File(GamePlugin.getInstance().getShopFolder(), merchant.getName().toLowerCase() + ".json");
	}

	private Merchant get(String name) {
		File file = new File(GamePlugin.getInstance().getShopFolder(), name.toLowerCase() + ".json");
		;

		if (!file.exists()) {
			Merchant merchant = new Merchant(name.toLowerCase(), GameAPI.getAPI().loadConfiguration(new JsonObject()));
			merchant.save().save(file);

			return merchant;
		} else
			return new Merchant(name.toLowerCase(), GameAPI.getAPI().loadConfiguration(file));
	}
}
