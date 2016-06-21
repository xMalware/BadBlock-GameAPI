package fr.badblock.game.v1_8_R3.entities;

import java.lang.reflect.Field;

import org.bukkit.Location;

import fr.badblock.gameapi.utils.entities.CreatureType;
import fr.badblock.gameapi.utils.entities.CustomCreature;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ControllerMove;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityGiantZombie;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.Material;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

public class NMSGiant extends EntityGiantZombie implements CustomCreature {
	@Getter
	protected boolean invincible   	     = false,
					  allowedToFly 	     = false,
					  movable		 	 = true,
					  rideable			 = true,
					  agressive			 = true;

	private ControllerMove noMoveCache,
						   normalCache;

	public NMSGiant(World world) {
		super(world);

		noMoveCache = new NMSNoMoveController(this);
		normalCache = moveController;

		regenerateAttributes();
	}
	
	@Override
	public void setInvisible(boolean invisible) {
		super.setInvisible(invisible);
	}
	
	@Override
	public org.bukkit.entity.Entity getBukkit() {
		return getBukkitEntity();
	}

	@Override
	public void explode(Location location, float power, boolean flaming, boolean smoking) {
		world.createExplosion(this, location.getX(), location.getY(), location.getZ(), power, flaming, smoking);
	}
	
	@Override
	public void setAgressive(boolean agressive) {
		this.agressive = agressive;

		regenerateAttributes();
	}

	@Override
	public void setRideable(boolean rideable) {
		this.rideable = rideable;
	}
	
	@Override
	public void setInvincible(boolean invincible){
		this.invincible = invincible;
	}

	@Override
	public void setAllowedToFly(boolean allowedToFly){
		this.allowedToFly = allowedToFly;

		regenerateAttributes();
	}

	@Override
	public void setMovable(boolean movable){
		this.movable = movable;

		regenerateAttributes();

		if(!movable){
			moveController = noMoveCache;
		} else moveController = normalCache;
	}

	@Override
	public boolean isInvulnerable(DamageSource damageSource){
		if(invincible)
			return true;
		else return super.isInvulnerable(damageSource);
	}

	@Override
	public boolean isOnGround() {
		return super.onGround;
	}

	@Override
	public boolean isInLava() {
		return this.world.a(getBoundingBox().grow(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.LAVA);
	}

	@Override
	public boolean isInWater() {
		return super.inWater;
	}

	@Override
	public void setFireProof(boolean fireProof) {
		super.fireProof = true;
	}

	@Override
	public void setSpeed(double speed) {
		getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
	}

	@Override
	public double getSpeed() {
		return getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
	}
	
	@Override
	public void setDamage(double damage) {
		getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(damage);
	}

	@Override
	public double getDamage() {
		return getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue();
	}

	@Override
	public CreatureType getEntityType() {
		return CreatureType.GIANT;
	}

	@Override
	public boolean a(EntityHuman entityhuman){
		if(!rideable)
			return super.a(entityhuman);

		if(super.a(entityhuman)) {
			return true;
		}
		if((!this.world.isClientSide) && ((this.passenger == null) || (this.passenger == entityhuman))) {
			entityhuman.mount(this);
			return true;
		}

		return false;
	}

	@Override
	protected void E() {
		if(!movable){
			return;
		}

		if(passenger == null){
			super.E();
		} else {
			return;	
		}

		if(!allowedToFly)
			return;

		if ((this.an == null) || (this.random.nextInt(30) == 0) || (this.an.c((int)this.locX, (int)this.locY, (int)this.locZ) < 4.0F)) {
			this.an = new BlockPosition((int)this.locX + this.random.nextInt(7) - this.random.nextInt(7), (int)this.locY + this.random.nextInt(6) - 2, (int)this.locZ + this.random.nextInt(7) - this.random.nextInt(7));
		}
		double d1 = this.an.getX() + 0.5D - this.locX;
		double d2 = this.an.getY() + 0.1D - this.locY;
		double d3 = this.an.getZ() + 0.5D - this.locZ;

		this.motX += (Math.signum(d1) * 0.5D - this.motX) * 0.10000000149011612D;
		this.motY += (Math.signum(d2) * 0.699999988079071D - this.motY) * 0.10000000149011612D;
		this.motZ += (Math.signum(d3) * 0.5D - this.motZ) * 0.10000000149011612D;

		float f1 = (float)(Math.atan2(this.motZ, this.motX) * 180.0D / 3.1415927410125732D) - 90.0F;
		float f2 = MathHelper.g(f1 - this.yaw);

		this.be = 0.5F;

		this.yaw += f2;
	}

	public void gPassenger(){
		this.lastYaw = this.yaw = this.passenger.yaw;
		this.pitch = this.passenger.pitch * 0.5F;
		this.setYawPitch(this.yaw, this.pitch);
		this.aI = this.aG = this.yaw;

		float sideMot = ((EntityLiving) this.passenger).aZ * 0.5F;
		float forMot = ((EntityLiving) this.passenger).ba;

		if(forMot <= 0.0F) {
			forMot *= 0.25F;// Make backwards slower
		}

		sideMot *= 0.75F;
		if(!allowedToFly){
			Field jump = null; //Jumping
			try {
				jump = EntityLiving.class.getDeclaredField("aY");
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}

			jump.setAccessible(true);

			this.S = 1.0F;

			this.k(0.35F);
			super.g(sideMot, forMot);

			if (jump != null && this.onGround) {
				try {
					if (jump.getBoolean(this.passenger)) {
						this.motY = 0.5d;
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			return;
		} else motY = -pitch/45;


		a(sideMot, forMot, (float) getSpeed());

		if(sideMot != 0 || forMot != 0) {
			move(motX, motY, motZ);

			this.motX *= 0.8D;
			this.motY *= 0.8D;
			this.motZ *= 0.8D;
		}
	}

	@Override
	public void g(float sideMot, float forMot){
		if(!movable)
			return;

		if(passenger != null){
			gPassenger(); return;
		} 

		if(!allowedToFly){
			super.g(sideMot, forMot);
			return;
		}

		if(isInWater()) {
			a(sideMot, forMot, 0.02F);
			move(this.motX, this.motY, this.motZ);

			this.motX *= 0.800000011920929D;
			this.motY *= 0.800000011920929D;
			this.motZ *= 0.800000011920929D;
		} else if (isInLava()) {
			a(sideMot, forMot, 0.02F);
			move(this.motX, this.motY, this.motZ);
			this.motX *= 0.5D;
			this.motY *= 0.5D;
			this.motZ *= 0.5D;
		} else {
			float f1 = 0.91F;

			if(this.onGround) {
				BlockPosition pos = new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(this.locZ));
				f1 = this.world.getType(pos).getBlock().frictionFactor * 0.91F;
			}

			float f2 = 0.16277136F / (f1 * f1 * f1);
			a(sideMot, forMot, this.onGround ? 0.1F * f2 : 0.02F);

			move(this.motX, this.motY, this.motZ);

			this.motX *= f1;
			this.motY *= f1;
			this.motZ *= f1;
		}

		this.aE = this.aF;

		double d1 = this.locX - this.lastX;
		double d2 = this.locZ - this.lastZ;

		float f3 = MathHelper.sqrt(d1 * d1 + d2 * d2) * 4.0F;

		if (f3 > 1.0F) {
			f3 = 1.0F;
		}

		this.aF += (f3 - this.aF) * 0.4F;
		this.aG += this.aF;
	}

    protected void regenerateAttributes(){
		this.goalSelector   = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);
		this.targetSelector = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);

		if(allowedToFly || !movable)
			return;

		this.goalSelector.a(0, new PathfinderGoalFloat(this));
	    this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
	    this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
	    this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
	    this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));

		if(!agressive) return;

	    this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
	}
}
