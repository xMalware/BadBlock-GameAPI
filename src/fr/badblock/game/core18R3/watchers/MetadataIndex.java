package fr.badblock.game.core18R3.watchers;

import static fr.badblock.game.core18R3.watchers.MetadataType.BYTE;
import static fr.badblock.game.core18R3.watchers.MetadataType.FLOAT;
import static fr.badblock.game.core18R3.watchers.MetadataType.INT;
import static fr.badblock.game.core18R3.watchers.MetadataType.ITEM;
import static fr.badblock.game.core18R3.watchers.MetadataType.SHORT;
import static fr.badblock.game.core18R3.watchers.MetadataType.STRING;
import static fr.badblock.game.core18R3.watchers.MetadataType.VECTOR3F;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Horse;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.minecart.PoweredMinecart;

public enum MetadataIndex {
	STATUS(0, BYTE, Entity.class),
	AIR_TIME(1, SHORT, Entity.class),
	NAME_TAG(2, STRING, Entity.class),
	SHOW_NAME_TAG(3, BYTE, Entity.class),

	UNKNOWN_BYTE(4, BYTE, LivingEntity.class), // The player sends that sometimes oO
	HEALTH(6, FLOAT, LivingEntity.class),
	POTION_COLOR(7, INT, LivingEntity.class),
	POTION_AMBIENT(8, BYTE, LivingEntity.class),
	ARROW_COUNT(9, BYTE, LivingEntity.class),
	HAS_AI(15, BYTE, LivingEntity.class),

	AGE(12, BYTE, Ageable.class),

	ARMORSTAND_FLAGS(10, BYTE, ArmorStand.class),
	ARMORSTAND_HEAD(11, VECTOR3F, ArmorStand.class),
	ARMORSTAND_BODY(12, VECTOR3F, ArmorStand.class),
	ARMORSTAND_LEFT_ARM(13, VECTOR3F, ArmorStand.class),
	ARMORSTAND_RIGHT_ARM(14, VECTOR3F, ArmorStand.class),
	ARMORSTAND_LEFT_LEG(15, VECTOR3F, ArmorStand.class),
	ARMORSTAND_RIGHT_LEG(16, VECTOR3F, ArmorStand.class),


	PLAYER_SKIN_FLAGS(10, BYTE, HumanEntity.class),
	PLAYER_HIDE_CAPE(16, BYTE, HumanEntity.class),
	ABSORPTION_HEARTS(17, FLOAT, HumanEntity.class),
	SCORE(18, INT, HumanEntity.class),

	HORSE_FLAGS(16, INT, Horse.class),
	HORSE_TYPE(19, BYTE, Horse.class),
	HORSE_STYLE(20, INT, Horse.class),
	HORSE_OWNER(21, STRING, Horse.class),
	HORSE_ARMOR(22, INT, Horse.class),

	BAT_HANGING(16, BYTE, Bat.class),

	TAMEABLE_FLAGS(16, BYTE, Entity.class),
	TAMEABLE_OWNERNAME(17, STRING, Entity.class),

	OCELOT_TYPE(18, BYTE, Ocelot.class),

	WOLF_FLAGS(16, BYTE, Wolf.class),
	WOLF_HEALTH(18, FLOAT, Wolf.class),
	WOLF_BEGGING(19, BYTE, Wolf.class),
	WOLF_COLOR(20, BYTE, Wolf.class),

	PIG_SADDLE(16, BYTE, Pig.class),

	RABBIT_TYPE(18, BYTE, Rabbit.class),

	SHEEP_DATA(16, BYTE, Sheep.class),

	VILLAGER_TYPE(16, INT, Villager.class),

	ENDERMAN_BLOCK(16, SHORT, Enderman.class),
	ENDERMAN_BLOCK_DATA(17, BYTE, Enderman.class),
	ENDERMAN_ALERTED(18, BYTE, Enderman.class),

	ZOMBIE_IS_CHILD(12, BYTE, Zombie.class),
	ZOMBIE_IS_VILLAGER(13, BYTE, Zombie.class),
	ZOMBIE_IS_CONVERTING(14, BYTE, Zombie.class),

	BLAZE_ON_FIRE(16, BYTE, Blaze.class),

	SPIDER_CLIMBING(16, BYTE, Spider.class),

	CREEPER_STATE(16, BYTE, Creeper.class),
	CREEPER_POWERED(17, BYTE, Creeper.class),

	GHAST_ATTACKING(16, BYTE, Ghast.class),

	SLIME_SIZE(16, BYTE, Slime.class),

	SKELETON_TYPE(13, BYTE, Skeleton.class),

	WITCH_AGGRESSIVE(21, BYTE, Witch.class),

	GOLEM_PLAYER_BUILT(16, BYTE, IronGolem.class),

	WITHER_TARGET_1(17, INT, Wither.class),
	WITHER_TARGET_2(18, INT, Wither.class),
	WITHER_TARGET_3(19, INT, Wither.class),
	WITHER_INVULN_TIME(20, INT, Wither.class),

	BOAT_HIT_TIME(17, INT, Boat.class),
	BOAT_DIRECTION(18, INT, Boat.class),
	BOAT_DAMAGE_TAKEN(19, FLOAT, Boat.class),

	MINECART_SHAKE_POWER(17, INT, Minecart.class),
	MINECART_SHAKE_DIRECTION(18, INT, Minecart.class),
	MINECART_DAMAGE_TAKEN(19, INT, Minecart.class),
	MINECART_BLOCK(20, INT, Minecart.class),
	MINECART_BLOCK_OFFSET(21, INT, Minecart.class),
	MINECART_BLOCK_SHOWN(22, BYTE, Minecart.class),

	FURNACE_MINECART_POWERED(16, BYTE, PoweredMinecart.class),

	ITEM_ITEM(10, ITEM, Item.class),

	ARROW_CRITICAL(16, BYTE, Arrow.class),

	FIREWORK_INFO(8, ITEM, Firework.class),

	ITEM_FRAME_ITEM(8, ITEM, ItemFrame.class),
	ITEM_FRAME_ROTATION(9, BYTE, ItemFrame.class),

	ENDER_CRYSTAL_HEALTH(8, INT, EnderCrystal.class),
	;

	private final int index;
	private final MetadataType type;
	private final Class<? extends Entity> appliesTo;

	private MetadataIndex(int index, MetadataType type, Class<? extends Entity> appliesTo) {
		this.index = index;
		this.type = type;
		this.appliesTo = appliesTo;
	}

	public int getIndex() {
		return index;
	}

	public MetadataType getType() {
		return type;
	}

	public Class<?> getAppliesTo() {
		return appliesTo;
	}

	public boolean appliesTo(Class<? extends Entity> clazz) {
		return appliesTo.isAssignableFrom(clazz);
	}

	public static MetadataIndex getIndex(int index, MetadataType type) {
		MetadataIndex output = null;
		for (MetadataIndex entry : values()) {
			if (entry.getIndex() == index && entry.getType().equals(type)) {
				output = entry;
				break;
			}
		}
		return output;
	}

	public static MetadataIndex getIndex(int index, MetadataType type, Class<? extends Entity> entityClass) {
		for (MetadataIndex entry : values())
			if (entry.getIndex() == index && entry.getType().equals(type) && entry.appliesTo(entityClass))
				return entry;
		return null;
	}
}