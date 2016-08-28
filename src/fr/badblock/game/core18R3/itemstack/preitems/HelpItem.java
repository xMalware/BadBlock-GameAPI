package fr.badblock.game.core18R3.itemstack.preitems;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.i18n.Locale;

public class HelpItem {
	public static ItemStack createHelp(Locale locale) {
		return GameAPI.getAPI().createItemStackFactory().type(Material.SKULL_ITEM).durability((short) 3)
				.displayName(GameAPI.i18n().get(locale, "items.help.displayname")[0])
				.lore(GameAPI.i18n().get(locale, "items.help.displayname")).asSkull(1, Skulls.QUESTION.getId());
	}
}
