package fr.badblock.game.core18R3.watchers;

import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherArmorStand;
import fr.badblock.gameapi.utils.selections.Vector3f;

public class GameWatcherArmorStand extends GameWatcherLivingEntity implements WatcherArmorStand {
	public GameWatcherArmorStand(Class<? extends Entity> clazz) {
		super(clazz);
	}

	@Override
	public WatcherArmorStand addFlag(ArmorStandFlag flag) {
		map.setBit(MetadataIndex.ARMORSTAND_FLAGS, flag.getValue(), true);
		return this;
	}

	@Override
	public WatcherArmorStand removeFlag(ArmorStandFlag flag) {
		map.setBit(MetadataIndex.ARMORSTAND_FLAGS, flag.getValue(), false);
		return this;
	}

	@Override
	public WatcherArmorStand setBodyRotation(Vector3f position) {
		Object obj = new net.minecraft.server.v1_8_R3.Vector3f((float) position.getX(), (float) position.getY(),
				(float) position.getZ());
		set(MetadataIndex.ARMORSTAND_BODY, obj);
		return this;
	}

	@Override
	public WatcherArmorStand setHeadRotation(Vector3f position) {
		Object obj = new net.minecraft.server.v1_8_R3.Vector3f((float) position.getX(), (float) position.getY(),
				(float) position.getZ());
		set(MetadataIndex.ARMORSTAND_HEAD, obj);
		return this;
	}

	@Override
	public WatcherArmorStand setLeftArmRotation(Vector3f position) {
		Object obj = new net.minecraft.server.v1_8_R3.Vector3f((float) position.getX(), (float) position.getY(),
				(float) position.getZ());
		set(MetadataIndex.ARMORSTAND_LEFT_ARM, obj);
		return this;
	}

	@Override
	public WatcherArmorStand setLeftLegRotation(Vector3f position) {
		Object obj = new net.minecraft.server.v1_8_R3.Vector3f((float) position.getX(), (float) position.getY(),
				(float) position.getZ());
		set(MetadataIndex.ARMORSTAND_LEFT_LEG, obj);
		return this;
	}

	@Override
	public WatcherArmorStand setRightArmRotation(Vector3f position) {
		Object obj = new net.minecraft.server.v1_8_R3.Vector3f((float) position.getX(), (float) position.getY(),
				(float) position.getZ());
		set(MetadataIndex.ARMORSTAND_RIGHT_ARM, obj);
		return this;
	}

	@Override
	public WatcherArmorStand setRightLegRotation(Vector3f position) {
		Object obj = new net.minecraft.server.v1_8_R3.Vector3f((float) position.getX(), (float) position.getY(),
				(float) position.getZ());
		set(MetadataIndex.ARMORSTAND_RIGHT_LEG, obj);
		return this;
	}
}
