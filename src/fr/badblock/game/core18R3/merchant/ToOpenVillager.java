package fr.badblock.game.core18R3.merchant;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ToOpenVillager {
	BadblockPlayer 		  player;
	GameMerchantInventory inventory;
	TranslatableString 	  customName;
}
