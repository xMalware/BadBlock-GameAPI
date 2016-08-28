package fr.badblock.game.core18R3.entities;

import java.util.ArrayList;
import java.util.List;

import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.ControllerMove;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.PathfinderGoalBreed;
import net.minecraft.server.v1_8_R3.PathfinderGoalEatTile;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalFollowParent;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalPanic;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.PathfinderGoalTempt;
import net.minecraft.server.v1_8_R3.World;

public class NMSSheep extends EntitySheep implements NMSCustomCreature {
	public static class CustomPathfinderGoalEatTile extends PathfinderGoalEatTile {

		public CustomPathfinderGoalEatTile(EntityInsentient entityinsentient) {
			super(entityinsentient);
			set();
		}

		@Override
		public void d() {
			set();
		}

		private void set() {
			try {
				new Reflector(this).setFieldValue("a", 40);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Getter
	@Setter
	private ControllerMove normalController;
	@Getter
	@Setter
	public CreatureBehaviour creatureBehaviour;

	@Getter
	public List<CreatureFlag> flags;

	public NMSSheep(World world) {
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
	public void E() {
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

		try {
			new Reflector(this).setFieldValue("bq", new CustomPathfinderGoalEatTile(this));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (getCreatureBehaviour() != CreatureBehaviour.NORMAL)
			return;

		if (!hasCreatureFlag(CreatureFlag.AGRESSIVE)) {
			this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.25D));
		}

		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalTempt(this, 1.1D, Items.WHEAT, false));
		this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.1D));
		this.goalSelector.a(5, new PathfinderGoalEatTile(this));
		this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));

		if (!hasCreatureFlag(CreatureFlag.AGRESSIVE))
			return;

		this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));

		this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityIronGolem.class, 1.0D, true));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityIronGolem.class, true));
	}
}
