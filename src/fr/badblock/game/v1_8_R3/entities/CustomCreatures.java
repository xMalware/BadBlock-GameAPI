package fr.badblock.game.v1_8_R3.entities;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import fr.badblock.gameapi.utils.entities.CustomCreature;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.BiomeBase.BiomeMeta;
import net.minecraft.server.v1_8_R3.EntityBlaze;
import net.minecraft.server.v1_8_R3.EntityCaveSpider;
import net.minecraft.server.v1_8_R3.EntityChicken;
import net.minecraft.server.v1_8_R3.EntityCow;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityEnderman;
import net.minecraft.server.v1_8_R3.EntityEndermite;
import net.minecraft.server.v1_8_R3.EntityGhast;
import net.minecraft.server.v1_8_R3.EntityGiantZombie;
import net.minecraft.server.v1_8_R3.EntityGuardian;
import net.minecraft.server.v1_8_R3.EntityHorse;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntityMagmaCube;
import net.minecraft.server.v1_8_R3.EntityOcelot;
import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.EntityPigZombie;
import net.minecraft.server.v1_8_R3.EntityRabbit;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntitySilverfish;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.EntitySnowman;
import net.minecraft.server.v1_8_R3.EntitySpider;
import net.minecraft.server.v1_8_R3.EntitySquid;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.EntityWitch;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.EntityZombie;

public enum CustomCreatures {
	BLAZE(EntityType.BLAZE, EntityBlaze.class, NMSBlaze.class),
	CAVE_SPIDER(EntityType.CAVE_SPIDER, EntityCaveSpider.class, NMSCaveSpider.class),
	CHICKEN(EntityType.CHICKEN, EntityChicken.class, NMSChicken.class),
	COW(EntityType.COW, EntityCow.class, NMSCow.class),
	CREEPER(EntityType.CREEPER, EntityCreeper.class, NMSCreeper.class),
	ENDER_DRAGON(EntityType.ENDER_DRAGON, EntityEnderDragon.class, NMSZombie.class),//TODO
	ENDERMAN(EntityType.ENDERMAN, EntityEnderman.class, NMSEnderman.class),
	ENDERMITE(EntityType.ENDERMITE, EntityEndermite.class, NMSEndermite.class),
	GHAST(EntityType.GHAST, EntityGhast.class, NMSZombie.class),//TODO
	GIANT(EntityType.GIANT, EntityGiantZombie.class, NMSGiant.class),
	GUARDIAN(EntityType.GUARDIAN, EntityGuardian.class, NMSZombie.class),//TODO
	HORSE(EntityType.HORSE, EntityHorse.class, NMSHorse.class),
	IRON_GOLEM(EntityType.IRON_GOLEM, EntityIronGolem.class, NMSIronGolem.class),
	MAGMA_CUBE(EntityType.MAGMA_CUBE, EntityMagmaCube.class, NMSMagmaCube.class),
	OCELOT(EntityType.OCELOT, EntityOcelot.class, NMSOcelot.class),
	PIG(EntityType.PIG, EntityPig.class, NMSPig.class),
	PIG_ZOMBIE(EntityType.PIG_ZOMBIE, EntityPigZombie.class, NMSPigZombie.class),
	RABBIT(EntityType.RABBIT, EntityRabbit.class, NMSRabbit.class),
	SHEEP(EntityType.SHEEP, EntitySheep.class, NMSSheep.class),
	SILVERFISH(EntityType.SILVERFISH, EntitySilverfish.class, NMSSilverfish.class),
	SKELETON(EntityType.SKELETON, EntitySkeleton.class, NMSSkeleton.class),
	SLIME(EntityType.SLIME, EntitySlime.class, NMSSlime.class),
	SNOWMAN(EntityType.SNOWMAN, EntitySnowman.class, NMSSnowman.class),
	SPIDER(EntityType.SPIDER, EntitySpider.class, NMSSpider.class),
	SQUID(EntityType.SQUID, EntitySquid.class, NMSSquid.class),
	WITCH(EntityType.WITCH, EntityWitch.class, NMSWitch.class),
	WITHER(EntityType.WITHER, EntityWither.class, NMSZombie.class),//TODO
	WOLF(EntityType.WOLF, EntityWolf.class, NMSWolf.class),
	ZOMBIE(EntityType.ZOMBIE, EntityZombie.class, NMSZombie.class);
	

	private String name;
	private int id;
	private EntityType entityType;
	private Class<? extends EntityInsentient> nmsClass;
	private Class<? extends EntityInsentient> customClass;

	@SuppressWarnings("deprecation")
	private CustomCreatures(EntityType entityType, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass) {
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

	public Class<? extends EntityInsentient> getNMSClass() {
		return nmsClass;
	}

	public Class<? extends EntityInsentient> getCustomClass() {
		return customClass;
	}

	public static void registerEntities() {
		for (CustomCreatures entity : values()) /*Get our entities*/
			a(entity.getCustomClass(), entity.getName(), entity.getID());
		/*Get all biomes on the server*/
		BiomeBase[] biomes;
		try {
			biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
		} catch (Exception exc) {
			return;
		}
		for (BiomeBase biomeBase : biomes) {
			if (biomeBase == null)
				break;
			for (String field : new String[] { "at", "au", "av", "aw" }) //Lists that hold all entity types
				try {
					Field list = BiomeBase.class.getDeclaredField(field);
					list.setAccessible(true);
					@SuppressWarnings("unchecked")
					List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);

					for (BiomeMeta meta : mobList)
						for (CustomCreatures entity : values())
							if (entity.getNMSClass().equals(meta.b)) /*Test if the entity has the custom entity type*/
								meta.b = entity.getCustomClass(); //Set it's meta to our custom class's meta
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	@SuppressWarnings("rawtypes")
	public static void unregisterEntities() {
		for (CustomCreatures entity : values()) {
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

		for (CustomCreatures entity : values())
			try {
				a(entity.getNMSClass(), entity.getName(), entity.getID());
			} catch (Exception e) {
				e.printStackTrace();
			}

		BiomeBase[] biomes;
		try {
			biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
		} catch (Exception exc) {
			return;
		}
		for (BiomeBase biomeBase : biomes) {
			if (biomeBase == null)
				break;

			for (String field : new String[] { "at", "au", "av", "aw" })
				try {
					Field list = BiomeBase.class.getDeclaredField(field);
					list.setAccessible(true);
					@SuppressWarnings("unchecked")
					List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);

					for (BiomeMeta meta : mobList)
						for (CustomCreatures entity : values())
							if (entity.getCustomClass().equals(meta.b))
								meta.b = entity.getNMSClass();
				} catch (Exception e) {
					e.printStackTrace();
				}
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

	public static CustomCreatures getByType(EntityType type){
		for(CustomCreatures creat : values())
			if(creat.getEntityType() == type)
				return creat;
		return null;
	}
	
	public static CustomCreature getCustomCreature(Entity entity){
		Class<? extends EntityInsentient> clazz = null;
		
		for(CustomCreatures creature : values()){
			if(creature.getEntityType() == entity.getType()){
				clazz = creature.getCustomClass(); break;
			}
		}
		
		return clazz == null ? null : (CustomCreature) ((CraftEntity) entity).getHandle();
	}
}