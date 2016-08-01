package fr.badblock.game.v1_8_R3.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.ControllerMove;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntitySpider;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalLeapAtTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

public class NMSSpider extends EntitySpider implements NMSCustomCreature {
	@Getter@Setter
	private ControllerMove    normalController;
	
	@Getter@Setter
	public CreatureBehaviour  creatureBehaviour;
	@Getter
	public List<CreatureFlag> flags;

	public NMSSpider(World world) {
		super(world);

		flags = new ArrayList<>();
		EntityUtils.prepare(this);
	}
	
	@Override
	public boolean isInvulnerable(DamageSource damageSource){
		if(hasCreatureFlag(CreatureFlag.INVINCIBLE))
			return true;
		else return super.isInvulnerable(damageSource);
	}

	@Override
	public boolean a(EntityHuman entityhuman){
		return EntityUtils.rightClick(this, entityhuman);
	}

	@Override
	protected void E() {
		EntityUtils.moveFlying(this);
	}

	@Override
	public void g(float sideMot, float forMot){
		EntityUtils.move(this, sideMot, forMot);
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f){
		return EntityUtils.damageEntity(this, damagesource, f);
	}
	
	@Override
	public void regenerateAttributes(){
		this.goalSelector   = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);
		this.targetSelector = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);

		if(getCreatureBehaviour() != CreatureBehaviour.NORMAL)
			return;

		this.goalSelector.a(1, new PathfinderGoalFloat(this));
		this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 0.8D));
		this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));

	    if(!hasCreatureFlag(CreatureFlag.AGRESSIVE)) return;

		this.goalSelector.a(4, new PathfinderGoalSpiderMeleeAttack(this, EntityHuman.class));
		this.goalSelector.a(4, new PathfinderGoalSpiderMeleeAttack(this, EntityIronGolem.class));

		this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));

		this.targetSelector.a(2, new PathfinderGoalSpiderNearestAttackableTarget<>(this, EntityHuman.class));
		this.targetSelector.a(3, new PathfinderGoalSpiderNearestAttackableTarget<>(this, EntityIronGolem.class));
	}

	@Override
	public EntityInsentient getNMSEntity() {
		return this;
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
	public boolean callSuperDamageEntity(DamageSource damagesource, float f) {
		return super.damageEntity(damagesource, f);
	}
	
	@SuppressWarnings("rawtypes")
	public static class PathfinderGoalSpiderNearestAttackableTarget<T extends EntityLiving> extends PathfinderGoalNearestAttackableTarget {
		@SuppressWarnings("unchecked")
		public PathfinderGoalSpiderNearestAttackableTarget(EntitySpider entityspider, Class<T> oclass) {
			super(entityspider, oclass, true);
		}

		public boolean a() {
			float f = this.e.c(1.0F);

			return f >= 0.5F ? false : super.a();
		}
	}

	public static class PathfinderGoalSpiderMeleeAttack extends PathfinderGoalMeleeAttack {
		public PathfinderGoalSpiderMeleeAttack(EntitySpider entityspider, Class<? extends Entity> oclass) {
			super(entityspider, oclass, 1.0D, true);
		}

		public boolean b() {
			float f = this.b.c(1.0F);
			if ((f >= 0.5F) && (this.b.bc().nextInt(100) == 0)) {
				this.b.setGoalTarget(null);
				return false;
			}
			
			return super.b();
		}

		protected double a(EntityLiving entityliving) {
			return 4.0F + entityliving.width;
		}
	}
}
