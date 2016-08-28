package fr.badblock.game.core18R3.watchers;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.packets.watchers.WatcherEnderman;

public class GameWatcherEnderman extends GameWatcherLivingEntity implements WatcherEnderman {
	public GameWatcherEnderman(Class<? extends Entity> clazz) {
		super(clazz);
	}

	@SuppressWarnings("deprecation")
	@Override
	public WatcherEnderman setCarriedBlock(ItemStack block) {
		short type = (short) block.getType().getId();
		byte data = block.getData().getData();

		set(MetadataIndex.ENDERMAN_BLOCK, type);
		set(MetadataIndex.ENDERMAN_BLOCK_DATA, data);

		return this;
	}

	@Override
	public WatcherEnderman setScreaming(boolean screaming) {
		set(MetadataIndex.ENDERMAN_ALERTED, (byte) ((screaming) ? 1 : 0));
		return this;
	}
}
