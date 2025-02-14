package fr.badblock.game.core18R3.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Predicate;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.ControllerMove;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.IMonster;
import net.minecraft.server.v1_8_R3.PathfinderGoalDefendVillage;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalOfferFlower;
import net.minecraft.server.v1_8_R3.PathfinderGoalPanic;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

public class NMSIronGolem extends EntityIronGolem implements NMSCustomCreature {
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
	@Getter
	public Map<EntityType, TargetType> targets = new HashMap<>();
	
	public NMSIronGolem(World world) {
		super(world);

		targets = new HashMap<>();		
		
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
		EntityUtils.regen(this);
		this.goalSelector   = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);
		this.targetSelector = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);

		if(getCreatureBehaviour() != CreatureBehaviour.NORMAL)
			return;

		this.goalSelector.a(0, new PathfinderGoalFloat(this));

		if(!hasCreatureFlag(CreatureFlag.AGRESSIVE)) this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.25D));

	    this.goalSelector.a(2, new PathfinderGoalMoveTowardsTarget(this, 0.9D, 32.0F));
	    this.goalSelector.a(3, new PathfinderGoalMoveThroughVillage(this, 0.6D, true));
	    this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
	    this.goalSelector.a(5, new PathfinderGoalOfferFlower(this));
	    this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 0.6D));
	    this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
	    this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
	    
	    if(!hasCreatureFlag(CreatureFlag.AGRESSIVE)) return;
		
		this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, true));

	    this.targetSelector.a(1, new PathfinderGoalDefendVillage(this));
	    this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
	    
	    buildTargetSelector(3, EntityIronGolem.class, "PathfinderGoalNearestGolemTarget", new Class<?>[]{
	    	EntityCreature.class, Class.class, int.class, boolean.class, boolean.class, Predicate.class
	    }, this, EntityInsentient.class, 10, false, true, IMonster.e);
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
