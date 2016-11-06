package fr.badblock.game.core18R3.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import org.bukkit.inventory.ItemStack;

import com.google.common.base.Predicate;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.ControllerMove;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityAnimal;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityRabbit;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.PathfinderGoalBeg;
import net.minecraft.server.v1_8_R3.PathfinderGoalBreed;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalFollowOwner;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLeapAtTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalOwnerHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalOwnerHurtTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomTargetNonTamed;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

public class NMSWolf extends EntityWolf implements NMSCustomCreature {
	@Getter@Setter
	private Function<Random, List<ItemStack>> customLoots;
	@Getter@Setter
	private ControllerMove    normalController;
	
	@Getter@Setter
	public CreatureBehaviour  creatureBehaviour;
	@Getter
	public List<CreatureFlag> flags;
	@Getter@Setter
	public double speed = 1;

	public NMSWolf(World world) {
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void regenerateAttributes(){
		EntityUtils.regen(this);
		this.goalSelector   = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);
		this.targetSelector = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);

		if(getCreatureBehaviour() != CreatureBehaviour.NORMAL)
			return;

		
		this.goalSelector.a(1, new PathfinderGoalFloat(this));
	    this.goalSelector.a(2, this.bm);
	    this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
	    this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, 1.0D, 10.0F, 2.0F));
	    this.goalSelector.a(6, new PathfinderGoalBreed(this, 1.0D));
	    this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
	    this.goalSelector.a(8, new PathfinderGoalBeg(this, 8.0F));
	    this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
	    this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));
	    
	    if(!hasCreatureFlag(CreatureFlag.AGRESSIVE)) return;

	    this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, true));

	    this.targetSelector.a(1, new PathfinderGoalOwnerHurtByTarget(this));
	    this.targetSelector.a(2, new PathfinderGoalOwnerHurtTarget(this));
	    this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true, new Class[0]));
	    this.targetSelector.a(4, new PathfinderGoalRandomTargetNonTamed(this, EntityAnimal.class, false, new Predicate() {
	      public boolean a(Entity entity) {
	        return ((entity instanceof EntitySheep)) || ((entity instanceof EntityRabbit));
	      }
	      
	      public boolean apply(Object object) {
	        return a((Entity)object);
	      }
	    }));
	    this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget<>(this, EntitySkeleton.class, false));
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
	
	@Override
	protected void dropDeathLoot(boolean flag, int i) {
		if(customLoots == null)
			super.dropDeathLoot(flag, i);
		else EntityUtils.doDrops(this);
	}
}
