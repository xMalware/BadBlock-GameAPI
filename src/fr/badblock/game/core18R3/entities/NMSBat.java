package fr.badblock.game.core18R3.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.ControllerMove;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityBat;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

public class NMSBat extends EntityBat implements NMSCustomCreature {
	@Getter
	@Setter
	private ControllerMove normalController;

	@Getter
	@Setter
	public CreatureBehaviour creatureBehaviour;
	@Getter
	public List<CreatureFlag> flags;

	public NMSBat(World world) {
		super(world);

		flags = new ArrayList<>();
		EntityUtils.prepare(this);
	}

	@Override
	public boolean a(EntityHuman entityhuman) {
		return EntityUtils.rightClick(this, entityhuman);
	}

	@Override
	public boolean callSuperDamageEntity(DamageSource damagesource, float f) {
		return super.damageEntity(damagesource, f);
	}

	@Override
	public void callSuperMove(float f1, float f2) {
		super.g(f1, f2);
	}

	@Override
	public void callSuperMoveFlying() {
		super.E();
	}

	@Override
	public boolean callSuperRightClick(EntityHuman entityhuman) {
		return super.a(entityhuman);
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		return EntityUtils.damageEntity(this, damagesource, f);
	}

	@Override
	protected void E() {
		EntityUtils.moveFlying(this);
	}

	@Override
	public void g(float sideMot, float forMot) {
		EntityUtils.move(this, sideMot, forMot);
	}

	@Override
	public EntityInsentient getNMSEntity() {
		return this;
	}

	@Override
	public boolean isInvulnerable(DamageSource damageSource) {
		if (hasCreatureFlag(CreatureFlag.INVINCIBLE))
			return true;
		else
			return super.isInvulnerable(damageSource);
	}

	@Override
	public boolean r(Entity entity) {
		return EntityUtils.attack(this, entity);
	}

	@Override
	public void regenerateAttributes() {
		this.goalSelector = new PathfinderGoalSelector(
				(world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);
		this.targetSelector = new PathfinderGoalSelector(
				(world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);

		if (getCreatureBehaviour() != CreatureBehaviour.NORMAL)
			return;

		this.goalSelector.a(0, new PathfinderGoalFloat(this));

		this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
		this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
	}
}
