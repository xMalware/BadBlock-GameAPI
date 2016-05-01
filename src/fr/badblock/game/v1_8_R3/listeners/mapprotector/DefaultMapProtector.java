package fr.badblock.game.v1_8_R3.listeners.mapprotector;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.block.Action;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.servers.MapProtector;

/**
 * MapProtector très permissif (ne change rien)
 * @author LeLanN
 */
public class DefaultMapProtector implements MapProtector {
	@Override
	public boolean blockPlace(BadblockPlayer player, Block block) {
		return true;
	}

	@Override
	public boolean blockBreak(BadblockPlayer player, Block block) {
		return true;
	}

	@Override
	public boolean modifyItemFrame(BadblockPlayer player, Entity itemFrame) {
		return true;
	}

	@Override
	public boolean canLostFood(BadblockPlayer player) {
		return true;
	}

	@Override
	public boolean canUseBed(BadblockPlayer player, Block bed) {
		return true;
	}

	@Override
	public boolean canUsePortal(BadblockPlayer player) {
		return true;
	}

	@Override
	public boolean canDrop(BadblockPlayer player) {
		return true;
	}

	@Override
	public boolean canPickup(BadblockPlayer player) {
		return true;
	}

	@Override
	public boolean canFillBucket(BadblockPlayer player) {
		return true;
	}

	@Override
	public boolean canEmptyBucket(BadblockPlayer player) {
		return true;
	}

	@Override
	public boolean canInteract(BadblockPlayer player, Action action, Block block) {
		return true;
	}

	@Override
	public boolean canInteractEntity(BadblockPlayer player, Entity entity) {
		return true;
	}

	@Override
	public boolean canEnchant(BadblockPlayer player, Block table) {
		return true;
	}

	@Override
	public boolean canBeingDamaged(BadblockPlayer player) {
		return true;
	}

	@Override
	public boolean canBlockDamage(Block block) {
		return true;
	}

	@Override
	public boolean allowFire(Block block) {
		return true;
	}

	@Override
	public boolean allowMelting(Block block) {
		return true;
	}

	@Override
	public boolean allowBlockFormChange(Block block) {
		return true;
	}

	@Override
	public boolean allowPistonMove(Block block) {
		return true;
	}

	@Override
	public boolean allowBlockPhysics(Block block) {
		return true;
	}

	@Override
	public boolean allowLeavesDecay(Block block) {
		return true;
	}

	@Override
	public boolean allowRaining() {
		return true;
	}

	@Override
	public boolean modifyItemFrame(Entity itemframe) {
		return true;
	}

	@Override
	public boolean canSpawn(Entity entity) {
		return true;
	}

	@Override
	public boolean canCreatureSpawn(Entity creature, boolean isPlugin) {
		return true;
	}

	@Override
	public boolean canItemSpawn(Item item) {
		return true;
	}

	@Override
	public boolean canItemDespawn(Item item) {
		return true;
	}

	@Override
	public boolean allowExplosion(Location location) {
		return true;
	}

	@Override
	public boolean allowInteract(Entity entity) {
		return true;
	}

	@Override
	public boolean canCombust(Entity entity) {
		return true;
	}

	@Override
	public boolean canEntityBeingDamaged(Entity entity) {
		return true;
	}

	@Override
	public boolean canSoilChange(Block soil) {
		return true;
	}

	@Override
	public boolean destroyArrow() {
		return true;
	}

	@Override
	public boolean healOnJoin(BadblockPlayer player) {
		return true;
	}
}
