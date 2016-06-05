package fr.badblock.game.v1_8_R3.entities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.SpigotTimings;
import org.bukkit.event.entity.EntityCombustByEntityEvent;

import fr.badblock.gameapi.utils.entities.CreatureType;
import fr.badblock.gameapi.utils.entities.CustomCreature;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ControllerMove;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EnchantmentManager;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntitySquid;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.Material;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

public class NMSEnderDragon extends EntityEnderDragon implements CustomCreature {
	@Getter
	private boolean invincible   	     = false,
	allowedToFly 	     = false,
	movable		 	     = true,
	rideable			 = false,
	agressive			 = false;

	private ControllerMove noMoveCache,
	normalCache;

	public NMSEnderDragon(World world) {
		super(world);

		noMoveCache = new NMSNoMoveController(this);
		normalCache = moveController;

		regenerateAttributes();
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
	public CreatureType getEntityType() {
		return CreatureType.ENDER_DRAGON;
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

	@Override
	public void K(){
		if(isAllowedToFly() || isInvincible() || isRideable())
			return;
		else super.K();
	}

	@Override
	public void m(){
		if (this.bc > 0)
		{
			double d0 = this.locX + (this.bd - this.locX) / this.bc;
			double d1 = this.locY + (this.be - this.locY) / this.bc;
			double d2 = this.locZ + (this.bf - this.locZ) / this.bc;
			double d3 = MathHelper.g(this.bg - this.yaw);

			this.yaw = ((float)(this.yaw + d3 / this.bc));
			this.pitch = ((float)(this.pitch + (this.bh - this.pitch) / this.bc));
			this.bc -= 1;
			setPosition(d0, d1, d2);
			setYawPitch(this.yaw, this.pitch);
		}
		else if (!bM())
		{
			this.motX *= 0.98D;
			this.motY *= 0.98D;
			this.motZ *= 0.98D;
		}
		if (Math.abs(this.motX) < 0.005D) {
			this.motX = 0.0D;
		}
		if (Math.abs(this.motY) < 0.005D) {
			this.motY = 0.0D;
		}
		if (Math.abs(this.motZ) < 0.005D) {
			this.motZ = 0.0D;
		}
		this.world.methodProfiler.a("ai");
		SpigotTimings.timerEntityAI.startTiming();
		if (bD())
		{
			this.aY = false;
			this.aZ = 0.0F;
			this.ba = 0.0F;
			this.bb = 0.0F;
		}
		else if (bM())
		{
			this.world.methodProfiler.a("newAi");
			doTick();
			this.world.methodProfiler.b();
		}
		SpigotTimings.timerEntityAI.stopTiming();

		this.world.methodProfiler.b();
		this.world.methodProfiler.a("jump");
		if (this.aY)
		{
			if (V())
			{
				bG();
			}
			else if (ab())
			{
				bH();
			}
			else if ((this.onGround))
			{
				bF();
			}
		}
		this.world.methodProfiler.b();
		this.world.methodProfiler.a("travel");
		this.aZ *= 0.98F;
		this.ba *= 0.98F;
		this.bb *= 0.9F;
		SpigotTimings.timerEntityAIMove.startTiming();
		g(this.aZ, this.ba);
		SpigotTimings.timerEntityAIMove.stopTiming();
		this.world.methodProfiler.b();
		this.world.methodProfiler.a("push");
		if (!this.world.isClientSide)
		{
			SpigotTimings.timerEntityAICollision.startTiming();
			bL();
			SpigotTimings.timerEntityAICollision.stopTiming();
		}
		this.world.methodProfiler.b();

		this.world.methodProfiler.a("looting");
		if ((!this.world.isClientSide) && (bY()) && (!this.aP) && (this.world.getGameRules().getBoolean("mobGriefing")))
		{
			List<?> list = this.world.a(EntityItem.class, getBoundingBox().grow(1.0D, 0.0D, 1.0D));
			Iterator<?> iterator = list.iterator();
			while (iterator.hasNext())
			{
				EntityItem entityitem = (EntityItem)iterator.next();
				if ((!entityitem.dead) && (entityitem.getItemStack() != null) && (!entityitem.s())) {
					a(entityitem);
				}
			}
		}
		this.world.methodProfiler.b();
	}

	protected void regenerateAttributes(){
		this.goalSelector   = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);
		this.targetSelector = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);

		if(allowedToFly || !movable)
			return;

		this.targetSelector.a(2, getPrivateGoal("PathfinderGoalSquid", new Class<?>[]{EntitySquid.class}, this));

		// ce n'est pas une créature : ne peut pas être agressif
	}

	private PathfinderGoal getPrivateGoal(String name, Class<?>[] forConstructor, Object... objects){
		try {
			Class<?> theClass = null;

			for(Class<?> clazz : EntitySquid.class.getDeclaredClasses()){
				if(clazz.getName().contains(name)){
					theClass = clazz;
				}
			}

			Constructor<?> constructor = theClass.getConstructor(forConstructor);
			constructor.setAccessible(true);

			return ((PathfinderGoal) constructor.newInstance(objects));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f){
		if(!agressive) return false;

		if(isInvulnerable(damagesource)) {
			return false;
		}

		if(super.damageEntity(damagesource, f)){
			Entity entity = damagesource.getEntity();

			return (this.passenger != entity) && (this.vehicle != entity);
		}
		return false;
	}

	@Override
	public boolean r(Entity entity){
		if(!agressive) return false;

		float f = (float)getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue();
		int i = 0;
		if ((entity instanceof EntityLiving)) {
			f += EnchantmentManager.a(bA(), ((EntityLiving)entity).getMonsterType());
			i += EnchantmentManager.a(this);
		}
		boolean flag = entity.damageEntity(DamageSource.mobAttack(this), f);
		if (flag) {
			if (i > 0) {
				entity.g(-MathHelper.sin(this.yaw * 3.1415927F / 180.0F) * i * 0.5F, 0.1D, MathHelper.cos(this.yaw * 3.1415927F / 180.0F) * i * 0.5F);
				this.motX *= 0.6D;
				this.motZ *= 0.6D;
			}
			int j = EnchantmentManager.getFireAspectEnchantmentLevel(this);
			if (j > 0) {
				EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(getBukkitEntity(), entity.getBukkitEntity(), j * 4);
				Bukkit.getPluginManager().callEvent(combustEvent);
				if (!combustEvent.isCancelled()) {
					entity.setOnFire(combustEvent.getDuration());
				}
			}

			a(this, entity);
		}

		return flag;
	}
}
