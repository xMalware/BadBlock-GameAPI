package fr.badblock.game.v1_8_R3.watchers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import fr.badblock.gameapi.packets.watchers.WatcherEntity;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.gameapi.utils.reflection.Reflector;
import net.minecraft.server.v1_8_R3.DataWatcher;

public class GameWatcherEntity implements WatcherEntity {
	protected MetadataMap map;

	private boolean onFire 	  = false;
	private boolean invisible = false;
	
	public GameWatcherEntity(Class<? extends Entity> clazz){
		map = new MetadataMap(clazz);
	}
	
	@Override
	public WatcherEntity setOnFire(boolean onFire) {
		this.onFire = onFire;
		setFlag();
		return this;
	}

	@Override
	public WatcherEntity setInvisibile(boolean invisible) {
		this.invisible = invisible;
		setFlag();
		return this;
	}

	@Override
	public WatcherEntity setCustomName(String customName) {
		set(MetadataIndex.NAME_TAG, customName);
		return this;
	}

	@Override
	public WatcherEntity setCustomNameVisible(boolean customNameVisible) {
		set(MetadataIndex.SHOW_NAME_TAG, customNameVisible ? 1 : 0);
		return this;
	}

	protected void setFlag(){
		int value = 0;
		
		if(onFire)
			value |= 0x01;
		if(invisible)
			value |= 0x20;
		
		set(MetadataIndex.STATUS, value);
	}
	
	
	public void set(MetadataIndex index, Object value){
		map.set(index, value);
	}

	@Override
	public void applyToEntity(Entity entity) {
		CraftEntity applyTo = (CraftEntity) entity;
		
		DataWatcher watcher = convertToDatawatcher(applyTo.getHandle().getDataWatcher());
		try {
			new Reflector(applyTo.getHandle(), ReflectionUtils.getNMSClass("Entity")).setFieldValue("datawatcher", watcher);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public DataWatcher convertToDatawatcher(DataWatcher watcher) {
		for(MetadataMap.Entry entry : map.getEntryList()) {
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
	
	public DataWatcher convertToDatawatcher(){
		return map.toDatawatcher();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List convertToWatchables(){
		ArrayList ret = Lists.newArrayList();
		ret.addAll(map.getEntryList().stream().map(MetadataMap.Entry::toWatchable).collect(Collectors.toList()));
		return ret;
	}
}
