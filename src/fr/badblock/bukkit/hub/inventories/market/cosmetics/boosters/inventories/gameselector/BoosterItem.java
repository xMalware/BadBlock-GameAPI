package fr.badblock.bukkit.hub.inventories.market.cosmetics.boosters.inventories.gameselector;

import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.boosters.inventories.RealTimeBoosterManager;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.data.boosters.PlayerBooster;
import fr.badblock.gameapi.utils.general.TimeUnit;
import lombok.Getter;
import lombok.Setter;

public class BoosterItem extends CustomItem {

	@Getter@Setter private String gamePrefix;

	public BoosterItem(String gamePrefix, Material material) {
		super("hub.items.booster.gamechoose_names." + gamePrefix, material, "");
		this.setGamePrefix(gamePrefix);
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK, ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction action, Block clickedBlock) {
		HubPlayer hubPlayer = HubPlayer.get(player);
		if (hubPlayer.lastBooster == null) {
			player.sendTranslatedMessage("hub.items.booster.unknownbooster");
			return;
		}
		if (RealTimeBoosterManager.stockage.containsKey(this.getGamePrefix())) {
			PlayerBooster playerBooster = RealTimeBoosterManager.stockage.get(this.getGamePrefix());
			if (playerBooster != null) {
				if (playerBooster.isValid() && playerBooster.isEnabled()) {
					System.out.println("unable");
					player.sendTranslatedMessage("hub.items.booster.alreadygame");	
					return;
				}
			}
		}
		player.closeInventory();
		hubPlayer.lastBooster.setGameName(this.getGamePrefix());
		hubPlayer.lastBooster.setExpire(System.currentTimeMillis() + hubPlayer.lastBooster.getBooster().getLength());
		hubPlayer.lastBooster.setEnabled(true);
		hubPlayer.lastBooster.setUsername(player.getName());
		player.sendTranslatedMessage("hub.boosters.disabled.enabledsuccessfully", this.getGamePrefix());
		player.playSound(Sound.NOTE_PLING);
		player.saveGameData();
		RealTimeBoosterManager.stockage.put(this.getGamePrefix(), hubPlayer.getLastBooster());
		new Thread() {
			@Override
			public void run() {
				try {
					Statement statement = GameAPI.getAPI().getSqlDatabase().createStatement();
					statement.executeUpdate("UPDATE keyValues SET value = '"
							+ BadBlockHub.getInstance().getGson().toJson(RealTimeBoosterManager.stockage) + "' WHERE `key` = 'booster'");
					statement.close();
				}catch(Exception error) {
					error.printStackTrace();
				}
				GameAPI.getAPI().getRabbitSpeaker().sendSyncUTF8Publisher("boosterRefresh", getGamePrefix(), 10000L, false);
				String[] data = player.getTranslatedMessage("hub.boosters.launched", player.getName(), getGamePrefix(),
						((int)((hubPlayer.lastBooster.getBooster().getCoinsMultiplier() - 1) * 100)), ((int)((hubPlayer.lastBooster.getBooster().getXpMultiplier() - 1) * 100)),
						TimeUnit.SECOND.toFrench(hubPlayer.lastBooster.getBooster().getLength() / 1000L));
				for (String string : data) {
					if (string.isEmpty() || string.equals("")) string = " ";
					GameAPI.getAPI().getRabbitSpeaker().sendSyncUTF8Message("guardian.broadcast", string, 5000L, false);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	@Override
	public ItemStack toItemStack(BadblockPlayer player) {
		return build(this.getMaterial(), this.getAmount(), this.getData(), player.getTranslatedMessage(this.getName())[0], player.getTranslatedMessage("hub.items.booster.gamechooselore", this.getGamePrefix()));
	}

}
