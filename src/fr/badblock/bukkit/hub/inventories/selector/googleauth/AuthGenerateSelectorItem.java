
package fr.badblock.bukkit.hub.inventories.selector.googleauth;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.Callback;

public class AuthGenerateSelectorItem extends CustomItem {

	@SuppressWarnings("deprecation")
	public AuthGenerateSelectorItem() {
		super("hub.items.authgenerateitem", Material.getMaterial(385), "hub.items.authgenerateitem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK, ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		player.closeInventory();
		AuthUtils.getAuthKey(player.getName(), new Callback<String>() {
			@Override
			public void done(String key, Throwable throwable) {
				// on lui prévient quand même qu'une clé a quand-même été générée avant
				if (key != null && !key.isEmpty()) {
					player.sendTranslatedMessage("hub.auth.keyalreadyexists");
				}
				// génération d'une nouvelle clé
				GoogleAuthenticatorKey authKey = AuthUtils.gAuth.createCredentials();
				String secretKey = authKey.getKey();
				AuthUtils.tempPlayersKeys.put(player.getName().toLowerCase(), secretKey);
				// demander une vérification du mot de passe avant sauvegarde
				// /authcheck <code>
				player.sendTranslatedMessage("hub.auth.generatedkey", secretKey);
				ItemStack itemStack = GameAPI.getAPI().generateGoogleAuthQrCode(player, secretKey, "https://badblock.fr/includes/img/logosmall.png");
				player.getInventory().setItem(6, itemStack);
				player.getInventory().setHeldItemSlot(6);
			}
		});
	}
	
}
