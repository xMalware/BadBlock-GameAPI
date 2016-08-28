package fr.badblock.game.core18R3.watchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.bukkit.entity.Entity;

import com.google.common.collect.ImmutableList;

import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.DataWatcher.WatchableObject;
import net.minecraft.server.v1_8_R3.ItemStack;

public class MetadataMap {
	public static final class Entry implements Comparable<Entry> {
		public static Entry fromWatchable(WatchableObject nmsObject, Class<? extends Entity> entity) {
			int index = nmsObject.a();
			int itemType = nmsObject.c();
			MetadataIndex metadataIndex = null;
			for (MetadataIndex i : MetadataIndex.values()) {
				if (i.getIndex() == index && i.getType().getId() == itemType && i.appliesTo(entity)) {
					metadataIndex = i;
					break;
				}
			}

			if (metadataIndex == null)
				throw new IllegalArgumentException(
						"This watchable object is not appliable to the entity. Index = " + index + " / Type = "
								+ itemType + " / Entity = " + entity.getName() + " / Value = " + nmsObject.b());
			return new Entry(metadataIndex, nmsObject.b());
		}
		public final MetadataIndex index;

		public final Object value;

		public Entry(MetadataIndex index, Object value) {
			this.index = index;
			this.value = value;
		}

		@Deprecated
		public Entry(WatchableObject nmsObject) {
			int index = nmsObject.a();
			int itemType = nmsObject.c();
			this.index = MetadataIndex.getIndex(index, MetadataType.byId(itemType));
			this.value = nmsObject.b();
		}

		@Override
		public int compareTo(Entry o) {
			return o.index.getIndex() - index.getIndex();
		}

		@Override
		public String toString() {
			return index + "=" + value;
		}

		public WatchableObject toWatchable() {
			return new WatchableObject(index.getType().getId(), index.getIndex(), value);
		}
	}
	private final Map<MetadataIndex, Object> map = new EnumMap<>(MetadataIndex.class);
	private final List<Entry> changes = new ArrayList<>(4);

	private final Class<? extends Entity> entityClass;

	public MetadataMap(Class<? extends Entity> entityClass) {
		this.entityClass = entityClass;
		set(MetadataIndex.STATUS, 0);
	}

	public void addEntries(List<Entry> entries) {
		for (Entry entry : entries)
			set(entry.index, entry.value);
	}

	@Override
	public MetadataMap clone() {
		MetadataMap map = new MetadataMap(entityClass);
		map.changes.addAll(changes);
		map.map.putAll(this.map);
		return map;
	}

	public boolean containsKey(MetadataIndex index) {
		return map.containsKey(index);
	}

	public Object get(MetadataIndex index) {
		return map.get(index);
	}

	@SuppressWarnings("unchecked")
	private <T> T get(MetadataIndex index, MetadataType expected, T def) {
		if (index.getType() != expected) {
			throw new IllegalArgumentException("Cannot get " + index + ": is " + index.getType() + ", not " + expected);
		}
		T t = (T) map.get(index);
		if (t == null) {
			return def;
		}
		return t;
	}

	public boolean getBit(MetadataIndex index, int bit) {
		return (getNumber(index).intValue() & bit) != 0;
	}

	public byte getByte(MetadataIndex index) {
		return get(index, MetadataType.BYTE, (byte) 0);
	}

	public List<Entry> getChanges() {
		Collections.sort(changes);
		return ImmutableList.copyOf(changes);
	}

	public Class<? extends Entity> getEntityClass() {
		return entityClass;
	}

	public List<Entry> getEntryList() {
		List<Entry> result = new ArrayList<>(map.size());
		for (Map.Entry<MetadataIndex, Object> entry : map.entrySet()) {
			result.add(new Entry(entry.getKey(), entry.getValue()));
		}
		Collections.sort(result);
		return result;
	}

	public float getFloat(MetadataIndex index) {
		return get(index, MetadataType.FLOAT, 0f);
	}

	public int getInt(MetadataIndex index) {
		return get(index, MetadataType.INT, 0);
	}

	public ItemStack getItem(MetadataIndex index) {
		return get(index, MetadataType.ITEM, null);
	}

	public Number getNumber(MetadataIndex index) {
		if (!containsKey(index)) {
			return 0;
		}
		Object o = get(index);
		if (!(o instanceof Number)) {
			throw new IllegalArgumentException("Index " + index + " is of non-number type " + index.getType());
		}
		return (Number) o;
	}

	public short getShort(MetadataIndex index) {
		return get(index, MetadataType.SHORT, (short) 0);
	}

	public String getString(MetadataIndex index) {
		return get(index, MetadataType.STRING, null);
	}

	public void merge(MetadataMap toMerge) {
		map.putAll(toMerge.map);
		changes.addAll(toMerge.changes);
	}

	public void resetChanges() {
		changes.clear();
	}

	public void set(MetadataIndex index, Object value) {
		if (value instanceof Number) {
			Number n = (Number) value;
			switch (index.getType()) {
			case BYTE:
				value = n.byteValue();
				break;
			case SHORT:
				value = n.shortValue();
				break;
			case INT:
				value = n.intValue();
				break;
			case FLOAT:
				value = n.floatValue();
				break;
			default:
				break;
			}
		}

		if (!index.getType().getDataType().isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException(
					"Cannot assign " + value + " to " + index + ", expects " + index.getType());
		}

		if (!index.appliesTo(entityClass)) {
			throw new IllegalArgumentException("Index " + index + " does not apply to " + entityClass.getSimpleName()
					+ ", only " + index.getAppliesTo().getSimpleName());
		}

		Object prev = map.put(index, value);
		if (!Objects.equals(prev, value)) {
			changes.add(new Entry(index, value));
		}
	}

	public void setBit(MetadataIndex index, int bit, boolean status) {
		if (status) {
			set(index, getNumber(index).intValue() | bit);
		} else {
			set(index, getNumber(index).intValue() & ~bit);
		}
	}

	public DataWatcher toDatawatcher() {
		DataWatcher watcher = new DataWatcher(null);
		for (MetadataMap.Entry entry : getEntryList()) {
			int id = entry.index.getIndex();
			Object value = entry.value;
			try {
				watcher.a(id, value);
			} catch (Exception e) {
				watcher.watch(id, value);
			}
		}
		return watcher;
	}
}