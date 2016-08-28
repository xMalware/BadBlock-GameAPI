package fr.badblock.game.core18R3.watchers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import fr.badblock.gameapi.packets.watchers.WatcherEntity;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.gameapi.utils.reflection.Reflector;
import net.minecraft.server.v1_8_R3.DataWatcher;

public class GameWatcherEntity implements WatcherEntity {
	protected MetadataMap map;

	private boolean onFire = false;
	private boolean invisible = false;

	private TranslatableString customName;

	public GameWatcherEntity(Class<? extends Entity> clazz) {
		map = new MetadataMap(clazz);
	}

	@Override
	public void applyToEntity(Entity entity) {
		CraftEntity applyTo = (CraftEntity) entity;

		DataWatcher watcher = convertToDatawatcher(applyTo.getHandle().getDataWatcher());
		try {
			new Reflector(applyTo.getHandle(), ReflectionUtils.getNMSClass("Entity")).setFieldValue("datawatcher",
					watcher);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DataWatcher convertToDatawatcher() {
		return map.toDatawatcher();
	}

	public DataWatcher convertToDatawatcher(BadblockPlayer player) {
		MetadataMap mapClone = map.clone();

		if (customName != null)
			mapClone.set(MetadataIndex.NAME_TAG, customName.getAsLine(player));

		return mapClone.toDatawatcher();
	}

	public DataWatcher convertToDatawatcher(DataWatcher watcher) {
		for (MetadataMap.Entry entry : map.getEntryList()) {
			int id = entry.index.getIndex();
			Object value = entry.value;

			if (value instanceof ItemStack) {
				value = CraftItemStack.asNMSCopy((ItemStack) value);
			}

			try {
				watcher.a(id, value);
			} catch (Exception e) {
				watcher.watch(id, value);
			}
		}

		return watcher;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List convertToWatchables() {
		ArrayList ret = Lists.newArrayList();
		ret.addAll(map.getEntryList().stream().map(MetadataMap.Entry::toWatchable).collect(Collectors.toList()));
		return ret;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List convertToWatchables(BadblockPlayer player) {
		MetadataMap mapClone = map.clone();

		if (customName != null)
			mapClone.set(MetadataIndex.NAME_TAG, customName.getAsLine(player));

		ArrayList ret = Lists.newArrayList();
		ret.addAll(mapClone.getEntryList().stream().map(MetadataMap.Entry::toWatchable).collect(Collectors.toList()));
		return ret;
	}

	public void set(MetadataIndex index, Object value) {
		map.set(index, value);
	}

	@Override
	public WatcherEntity setCustomName(String name) {
		customName = null;
		set(MetadataIndex.NAME_TAG, name);
		return this;
	}

	@Override
	public WatcherEntity setCustomName(TranslatableString name) {
		customName = name;
		set(MetadataIndex.NAME_TAG, customName.getAsLine(Locale.FRENCH_FRANCE));
		return this;
	}

	@Override
	public WatcherEntity setCustomNameVisible(boolean customNameVisible) {
		set(MetadataIndex.SHOW_NAME_TAG, customNameVisible ? 1 : 0);
		return this;
	}

	protected void setFlag() {
		int value = 0;

		if (onFire)
			value |= 0x01;
		if (invisible)
			value |= 0x20;

		set(MetadataIndex.STATUS, value);
	}

	@Override
	public WatcherEntity setInvisibile(boolean invisible) {
		this.invisible = invisible;
		setFlag();
		return this;
	}

	@Override
	public WatcherEntity setOnFire(boolean onFire) {
		this.onFire = onFire;
		setFlag();
		return this;
	}
}
