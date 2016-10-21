package fr.badblock.game.core18R3.entities;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityTypes;

public enum CustomEntities {
	ARMOR_STAND(EntityType.ARMOR_STAND, EntityArmorStand.class, NMSArmorStand.class);

	private String name;
	private int id;
	private EntityType entityType;
	private Class<? extends Entity> nmsClass;
	private Class<? extends Entity> customClass;

	@SuppressWarnings("deprecation")
	private CustomEntities(EntityType entityType, Class<? extends Entity> nmsClass, Class<? extends Entity> customClass) {
		this.name = entityType.getName();
		this.id = entityType.getTypeId();
		this.entityType = entityType;
		this.nmsClass = nmsClass;
		this.customClass = customClass;
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public Class<? extends Entity> getNMSClass() {
		return nmsClass;
	}

	public Class<? extends Entity> getCustomClass() {
		return customClass;
	}

	public static EntityType getTypeFromEntity(Entity entity){
		for(CustomEntities creature : values()){
			if(creature.nmsClass.isInstance(entity))
				return creature.entityType;
		}
		
		return EntityType.UNKNOWN;
	}
	
	public static void registerEntities() {
		for (CustomEntities entity : values()) /*Get our entities*/
			a(entity.getCustomClass(), entity.getName(), entity.getID());
	}

	@SuppressWarnings("rawtypes")
	public static void unregisterEntities() {
		for (CustomEntities entity : values()) {
			// Remove our class references.
			try {
				((Map) getPrivateStatic(EntityTypes.class, "d")).remove(entity.getCustomClass());
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				((Map) getPrivateStatic(EntityTypes.class, "f")).remove(entity.getCustomClass());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (CustomEntities entity : values())
			try {
				a(entity.getNMSClass(), entity.getName(), entity.getID());
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@SuppressWarnings("rawtypes")
	private static Object getPrivateStatic(Class clazz, String f)
			throws Exception {
		Field field = clazz.getDeclaredField(f);
		field.setAccessible(true);
		return field.get(null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void a(Class paramClass, String paramString, int paramInt) {
		try {
			((Map) getPrivateStatic(EntityTypes.class, "c")).put(paramString,
					paramClass);
			((Map) getPrivateStatic(EntityTypes.class, "d")).put(paramClass,
					paramString);
			((Map) getPrivateStatic(EntityTypes.class, "e")).put(
					Integer.valueOf(paramInt), paramClass);
			((Map) getPrivateStatic(EntityTypes.class, "f")).put(paramClass,
					Integer.valueOf(paramInt));
			((Map) getPrivateStatic(EntityTypes.class, "g")).put(paramString,
					Integer.valueOf(paramInt));
		} catch (Exception unused){}
	}

	public static CustomEntities getByType(EntityType type){
		for(CustomEntities creat : values())
			if(creat.getEntityType() == type)
				return creat;
		return null;
	}
}