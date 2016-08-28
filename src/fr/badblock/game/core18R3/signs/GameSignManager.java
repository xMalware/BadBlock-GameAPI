package fr.badblock.game.core18R3.signs;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import fr.badblock.gameapi.signs.SignManager;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class GameSignManager implements SignManager {
	private Map<Location, TranslatableString> translatedSigns = new HashMap<>();

	@Override
	public void clearAllSigns() {
		translatedSigns.keySet().stream().map(location -> {
			return location.getBlock();
		}).filter(block -> {
			return block.getState() instanceof Sign;
		}).forEach(this::setSignNotTranslatable);

		translatedSigns.clear();
	}

	@Override
	public TranslatableString getTraduction(Block block) {
		if (block.getState() instanceof Sign) {
			return translatedSigns.get(block.getLocation());
		} else {
			throw new IllegalArgumentException("Provided block is not a sign!");
		}
	}

	@Override
	public boolean isSignTranslatable(Block block) {
		if (block.getState() instanceof Sign) {
			return translatedSigns.containsKey(block.getLocation());
		} else {
			throw new IllegalArgumentException("Provided block is not a sign!");
		}
	}

	@Override
	public void setSignNotTranslatable(Block block) {
		if (block.getState() instanceof Sign) {
			if (translatedSigns.containsKey(block.getLocation())) {
				translatedSigns.remove(block.getLocation());
				updateSign(block);
			}
		} else {
			throw new IllegalArgumentException("Provided block is not a sign!");
		}
	}

	@Override
	public void setSignTranslatable(Block block, String key, Object... args) {
		if (block.getState() instanceof Sign) {
			translatedSigns.put(block.getLocation(), new TranslatableString(key, args));
			updateSign(block);
		} else {
			throw new IllegalArgumentException("Provided block is not a sign!");
		}
	}

	@Override
	public void updateSign(Block block) {
		if (block.getState() instanceof Sign) {
			Sign sign = (Sign) block.getState();
			sign.update(true);
		} else {
			throw new IllegalArgumentException("Provided block is not a sign!");
		}
	}
}
