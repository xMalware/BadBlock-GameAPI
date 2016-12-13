package fr.badblock.game.core18R3.players.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.google.gson.JsonObject;

import fr.badblock.game.core18R3.listeners.LoginListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.achievements.PlayerAchievement;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.players.data.InGameKitData;
import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.run.BadblockGame;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.itemstack.ItemAction;
import fr.badblock.gameapi.utils.itemstack.ItemEvent;
import fr.badblock.gameapi.utils.itemstack.ItemStackExtra;
import fr.badblock.gameapi.utils.itemstack.ItemStackExtra.ItemPlaces;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor
public class GameKit implements PlayerKit {
	
	private String 	   kitName;
	private boolean    VIP;

	private String	   kitItemType;
	private short	   kitItemData;

	private KitLevel[] levels;

	private String[] loreVIP(BadblockPlayer player){
		List<String> result = new ArrayList<>();

		if(player.hasPermission(GamePermission.VIP)){
			for(String lore : GameAPI.i18n().get(player.getPlayerData().getLocale(), "kits.leftclick")){
				result.add(lore);
			} 
		}

		result.add("");

		for(String lore : GameAPI.i18n().get(player.getPlayerData().getLocale(), "kits." + kitName + ".itemLore")){
			result.add(lore);
		}

		result.add("");

		PlayerKit kit 	  = player.inGameData(InGameKitData.class).getChoosedKit();
		String    kitName = null;

		if(kit != null)
			kitName = kit.getKitName();

		if(this.kitName.equals(kitName))
			result.add(GameAPI.i18n().get(player.getPlayerData().getLocale(), "kits.alreadyChoosed")[0]);
		else {
			for(String lore : GameAPI.i18n().get(player.getPlayerData().getLocale(), "kits.loreVip")){
				result.add(lore);
			}
		}
		
		result.add(GameAPI.i18n().get(player.getPlayerData().getLocale(), "kits.gowebsite")[0]);

		return result.toArray(new String[0]);
	}

	private String[] lore(BadblockPlayer player){
		if(VIP) return loreVIP(player);

		List<String> result = new ArrayList<>();

		int 	level 	  = player.getPlayerData().getUnlockedKitLevel(this);
		boolean canUnlock = player.getPlayerData().canUnlockNextLevel(this);


		for(String lore : GameAPI.i18n().get(player.getPlayerData().getLocale(), "kits.leftclick")){
			result.add(lore);
		}

		if(level != getMaxLevel() && level != 0){
			for(String lore : GameAPI.i18n().get(player.getPlayerData().getLocale(), "kits.rightclick", level + 1)){
				result.add(lore);
			}
		}

		result.add("");

		for(String lore : GameAPI.i18n().get(player.getPlayerData().getLocale(), "kits." + kitName + ".itemLore")){
			result.add(lore);
		}

		result.add("");

		if(level != getMaxLevel()){
			if(canUnlock){
				result.add(GameAPI.i18n().get(player.getPlayerData().getLocale(), "kits.unlock")[0]);
			} else result.add(GameAPI.i18n().get(player.getPlayerData().getLocale(), "kits.toUnlock")[0]);

			for(PlayerAchievement achievement : getNeededAchievements(level + 1)){
				if(!player.getPlayerData().getAchievementState(achievement).isSucceeds()){
					result.add(GameAPI.i18n().get(player.getPlayerData().getLocale(), "kits.missingAchievement", achievement.getDisplayName())[0]);
				}
			}

			result.add(GameAPI.i18n().get(player.getPlayerData().getLocale(), "kits.missingBadcoins", getBadcoinsCost(level + 1))[0]);

			if(level >= 1) result.add("");
		}

		if(level >= 1){
			PlayerKit kit 	  = player.inGameData(InGameKitData.class).getChoosedKit();
			String    kitName = null;

			if(kit != null)
				kitName = kit.getKitName();

			if(!this.kitName.equals(kitName))
				result.add(GameAPI.i18n().get(player.getPlayerData().getLocale(), "kits.canChoose")[0]);
			else result.add(GameAPI.i18n().get(player.getPlayerData().getLocale(), "kits.alreadyChoosed")[0]);
		}

		result.add(GameAPI.i18n().get(player.getPlayerData().getLocale(), "kits.gowebsite")[0]);
		
		return result.toArray(new String[0]);
	}

	@Override
	public ItemStackExtra getKitItem(BadblockPlayer player){
		return GameAPI.getAPI().createItemStackFactory()
				.type(Material.matchMaterial(kitItemType))
				.durability(kitItemData)
				.displayName(GameAPI.getAPI().getI18n().get(player.getPlayerData().getLocale(), "kits." + kitName + ".itemDisplayname")[0])
				.lore(lore(player))
				.asExtra(isVIP() ? (player.hasPermission(GamePermission.VIP) ? 1 : 0) : 1)
				.listenAs(new ItemEvent(){
					@Override
					public boolean call(ItemAction action, BadblockPlayer player) {
						if(VIP){

							if(player.hasPermission(GamePermission.VIP)){
								player.sendTranslatedMessage("kits.selected", new TranslatableString("kits." + kitName + ".itemDisplayname"));	
							} else {
								player.sendTranslatedMessage("kits.canNotChooseVIP", new TranslatableString("kits." + kitName + "itemDisplayname"));	
								if (GameAPI.getAPI().getGameServer().isJoinableWhenRunning() && GameAPI.getAPI().getGameServer().getGameState().equals(GameState.RUNNING)) {
									LoginListener.manageRunningJoin(player);
								}
								return true;
							}

						} else if(player.getPlayerData().getUnlockedKitLevel(GameKit.this) == 0){
							if(player.getPlayerData().canUnlockNextLevel(GameKit.this)){
								player.getPlayerData().unlockNextLevel(GameKit.this);
								player.sendTranslatedMessage("kits.unlockLevel", new TranslatableString("kits." + kitName + ".itemDisplayname"), 1);
							} else {
								player.sendTranslatedMessage("kits.canNotUnlockLevel", new TranslatableString("kits." + kitName + ".itemDisplayname"), 1);
								if (GameAPI.getAPI().getGameServer().isJoinableWhenRunning() && GameAPI.getAPI().getGameServer().getGameState().equals(GameState.RUNNING)) {
									LoginListener.manageRunningJoin(player);
								}
								player.closeInventory();
								return true;
							}
						} else {
							player.sendTranslatedMessage("kits.selected", new TranslatableString("kits." + kitName + ".itemDisplayname"));	
						}

						player.inGameData(InGameKitData.class).setChoosedKit(GameKit.this);
						player.getPlayerData().setLastUsedKit(GameAPI.getInternalGameName(), getKitName());
						player.saveGameData();
						if (GameAPI.getAPI().getGameServer().isJoinableWhenRunning() && GameAPI.getAPI().getGameServer().getGameState().equals(GameState.RUNNING)) {
							LoginListener.manageRunningJoin(player);
						}
						player.closeInventory();

						return true;
					}
				}, ItemPlaces.INVENTORY_CLICKABLE)
				.listen(new ItemEvent(){
					@Override
					public boolean call(ItemAction action, BadblockPlayer player) {
						int next = player.getPlayerData().getUnlockedKitLevel(GameKit.this) + 1;

						if(VIP){

							if(player.hasPermission(GamePermission.VIP)){
								player.sendTranslatedMessage("kits.selected", new TranslatableString("kits." + kitName + ".itemDisplayname"));	
							} else {
								player.sendTranslatedMessage("kits.canNotChooseVIP", new TranslatableString("kits." + kitName + ".itemDisplayname"));	
								if (GameAPI.getAPI().getGameServer().isJoinableWhenRunning() && GameAPI.getAPI().getGameServer().getGameState().equals(GameState.RUNNING)) {
									LoginListener.manageRunningJoin(player);
								}
								return true;
							}

						} else if(player.getPlayerData().canUnlockNextLevel(GameKit.this)){
							player.getPlayerData().unlockNextLevel(GameKit.this);
							player.sendTranslatedMessage("kits.unlockLevel", new TranslatableString("kits." + kitName + ".itemDisplayname"), next);
						} else {
							player.sendTranslatedMessage("kits.canNotUnlockLevel", new TranslatableString("kits." + kitName + ".itemDisplayname"), next);
							if (GameAPI.getAPI().getGameServer().isJoinableWhenRunning() && GameAPI.getAPI().getGameServer().getGameState().equals(GameState.RUNNING)) {
								LoginListener.manageRunningJoin(player);
							}
							player.closeInventory();
							return true;
						}

						player.inGameData(InGameKitData.class).setChoosedKit(GameKit.this);
						player.getPlayerData().setLastUsedKit(GameAPI.getInternalGameName(), getKitName());
						player.saveGameData();
						if (GameAPI.getAPI().getGameServer().isJoinableWhenRunning() && GameAPI.getAPI().getGameServer().getGameState().equals(GameState.RUNNING)) {
							LoginListener.manageRunningJoin(player);
						}
						player.closeInventory();

						return true;
					}
				}, ItemAction.INVENTORY_RIGHT_CLICK);
	}

	@Override
	public PlayerAchievement[] getNeededAchievements(int level) {
		if(level <= 0 || level > getMaxLevel()){
			throw new IllegalArgumentException("Level must be between 1 and " + getMaxLevel() + ", not " + level);
		}

		KitLevel kitLevel = levels[level - 1];
		PlayerAchievement[] result = new PlayerAchievement[kitLevel.getNeededAchievements().length];


		for(int i=0;i<kitLevel.getNeededAchievements().length;i++){
			String 			  name 		  = kitLevel.getNeededAchievements()[i];
			PlayerAchievement achievement = BadblockGame.current.getGameData().getAchievements().getGameAchievement(name);
			result[i]					  = achievement;
		}

		return result;
	}

	@Override
	public int getBadcoinsCost(int level) {
		if(level <= 0 || level > getMaxLevel()){
			throw new IllegalArgumentException("Level must be between 1 and " + getMaxLevel() + ", not " + level);
		}

		KitLevel kitLevel = levels[level - 1];

		return kitLevel.getBadcoinsCost();
	}

	@Override
	public int getMaxLevel(){
		return levels.length;
	}

	@Override
	public void giveKit(BadblockPlayer player) {
		int level = 1;
		
		if(VIP){
			if(!player.hasPermission(GamePermission.VIP))
				return;
		} else {
			level = player.getPlayerData().getUnlockedKitLevel(this);

			if(level == 0) return; // le joueur n'a pas d�bloqu� le kit :o

	
			if(level <= 0 || level > getMaxLevel()){
				throw new IllegalArgumentException("Level must be between 1 and " + getMaxLevel() + ", not " + level);
			}
		}
		
		KitLevel kitLevel = levels[level - 1];
		GameAPI.getAPI().getKitContentManager().give(kitLevel.getStuff(), player);
	}

	@Override
	public void giveKit(BadblockPlayer player, Material... withoutMaterials) {
		int level = 1;
		
		if(VIP){
			if(!player.hasPermission(GamePermission.VIP))
				return;
		} else {
			level = player.getPlayerData().getUnlockedKitLevel(this);

			if(level == 0) return; // le joueur n'a pas d�bloqu� le kit :o

	
			if(level <= 0 || level > getMaxLevel()){
				throw new IllegalArgumentException("Level must be between 1 and " + getMaxLevel() + ", not " + level);
			}
		}
		
		KitLevel kitLevel = levels[level - 1];
		GameAPI.getAPI().getKitContentManager().give(kitLevel.getStuff(), player, withoutMaterials);
	}

	@Data@AllArgsConstructor@NoArgsConstructor
	public static class KitLevel {
		private String[]   neededAchievements;
		private int 	   badcoinsCost;

		private JsonObject stuff;
	}
}
