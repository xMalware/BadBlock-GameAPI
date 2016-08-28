package fr.badblock.game.core18R3.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.ControllerMove;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntityPigZombie;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalPanic;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

public class NMSZombie extends EntityZombie implements NMSCustomCreature {
	@Getter
	@Setter
	private ControllerMove normalController;

	@Getter
	@Setter
	public CreatureBehaviour creatureBehaviour;
	@Getter
	public List<CreatureFlag> flags;

	public NMSZombie(World world) {
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
	public void regenerateAttributes() {
		this.goalSelector = new PathfinderGoalSelector(
				(world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);
		this.targetSelector = new PathfinderGoalSelector(
				(world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);

		if (getCreatureBehaviour() != CreatureBehaviour.NORMAL)
			return;

		this.goalSelector.a(0, new PathfinderGoalFloat(this));

		if (!hasCreatureFlag(CreatureFlag.AGRESSIVE))
			this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.4D));

		this.goalSelector.a(0, new PathfinderGoalFloat(this));

		this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
		this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
		this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));

		if (!hasCreatureFlag(CreatureFlag.AGRESSIVE))
			return;

		this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));

		if (this.world.spigotConfig.zombieAggressiveTowardsVillager) {
			this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityVillager.class, 1.0D, true));
			this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityVillager.class, false));
		}
		this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityIronGolem.class, 1.0D, true));
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityIronGolem.class, true));
	}
}
