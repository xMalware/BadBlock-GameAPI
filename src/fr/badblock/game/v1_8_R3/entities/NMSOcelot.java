package fr.badblock.game.v1_8_R3.entities;

import java.util.ArrayList;
import java.util.List;

import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.ControllerMove;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityChicken;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityOcelot;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.PathfinderGoalBreed;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalFollowOwner;
import net.minecraft.server.v1_8_R3.PathfinderGoalJumpOnBlock;
import net.minecraft.server.v1_8_R3.PathfinderGoalLeapAtTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalOcelotAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomTargetNonTamed;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

public class NMSOcelot extends EntityOcelot  implements NMSCustomCreature {
	@Getter@Setter
	private ControllerMove    normalController;
	
	@Getter@Setter
	public CreatureBehaviour  creatureBehaviour;
	@Getter
	public List<CreatureFlag> flags;

	public NMSOcelot(World world) {
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
	public void E() {
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
	    this.goalSelector.a(2, this.bm);
	    try {
			this.goalSelector.a(3, (PathfinderGoal) new Reflector(this).getFieldValue("bp"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	    this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, 1.0D, 10.0F, 5.0F));
	    this.goalSelector.a(6, new PathfinderGoalJumpOnBlock(this, 0.8D));
	    
	    this.goalSelector.a(9, new PathfinderGoalBreed(this, 0.8D));
	    this.goalSelector.a(10, new PathfinderGoalRandomStroll(this, 0.8D));
	    this.goalSelector.a(11, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 10.0F));

	    if(!hasCreatureFlag(CreatureFlag.AGRESSIVE)) return;

	    this.goalSelector.a(7, new PathfinderGoalLeapAtTarget(this, 0.3F));
	    this.goalSelector.a(8, new PathfinderGoalOcelotAttack(this));
	    this.targetSelector.a(1, new PathfinderGoalRandomTargetNonTamed<>(this, EntityChicken.class, false, null));
	}

	@Override
	public EntityInsentient getNMSEntity() {
		return this;
	}
}

