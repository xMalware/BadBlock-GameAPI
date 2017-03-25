package fr.badblock.bukkit.hub.inventories.settings.settings;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.settings.BackSettingsItem;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.settings.items.CyanStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.clans.CanJoinWhileRunningSettingsItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.directmessages.PrivateMessagesSettingsItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.friends.FriendsSettingsItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.language.LanguageSettingsItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.lobbychat.LobbyChatSettingsItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.party.PartyInviteSettingsItem;

public class SettingsSettingsInventory extends CustomInventory {

	public SettingsSettingsInventory() {
		// super("§cParamètres/Statistiques", 5);
		super("hub.items.settingssettingsinventory", 6);
		BlueStainedGlassPaneItem blueStainedGlassPaneItem = new BlueStainedGlassPaneItem();
		this.setItem(blueStainedGlassPaneItem, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48,
				49, 50, 51, 52, 53);
		this.setItem(20, new PartyInviteSettingsItem());
		this.setItem(22, new FriendsSettingsItem());
		this.setItem(24, new LobbyChatSettingsItem());
		this.setItem(29, new CanJoinWhileRunningSettingsItem());
		this.setItem(31, new LanguageSettingsItem());
		this.setItem(33, new PrivateMessagesSettingsItem());
		this.setItem(53, new BackSettingsItem());
		this.setNoFilledItem(new CyanStainedGlassPaneItem());
	}

}
