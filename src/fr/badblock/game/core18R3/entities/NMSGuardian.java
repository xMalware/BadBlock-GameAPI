package fr.badblock.game.core18R3.entities;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import org.bukkit.inventory.ItemStack;

import com.google.common.base.Predicate;

import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ControllerMove;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EnchantmentManager;
import net.minecraft.server.v1_8_R3.EntityGuardian;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

public class NMSGuardian extends EntityGuardian implements NMSCustomCreature {
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

	private boolean callSuper = false;

	public NMSGuardian(World world) {
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

	private int tickPassenger = 2;

	@Override
	public void m(){
		try {
			if(passenger == null){
				tickPassenger = 2;
				super.m();
			} else if(tickPassenger > 0){
				tickPassenger--;
				super.m();
			} else {
				this.bx();
				float f = this.c(1.0F);

				if (f > 0.5F) {
					this.ticksFarFromPlayer += 2;
				}

				EntityLiving liv = (EntityLiving) this;
				Reflector    ref = new Reflector(liv);

				int val = ((Float) ref.getFieldValue("bn")).intValue();

				if (val > 0) {
					val--;
					ref.setFieldValue("bn", val);
				}

				if (this.bc > 0) {
					double d0 = this.locX + (this.bd - this.locX) / (double) this.bc;
					double d1 = this.locY + (this.be - this.locY) / (double) this.bc;
					double d2 = this.locZ + (this.bf - this.locZ) / (double) this.bc;
					double d3 = MathHelper.g(this.bg - (double) this.yaw);

					this.yaw = (float) ((double) this.yaw + d3 / (double) this.bc);
					this.pitch = (float) ((double) this.pitch + (this.bh - (double) this.pitch) / (double) this.bc);
					--this.bc;
					this.setPosition(d0, d1, d2);
					this.setYawPitch(this.yaw, this.pitch);
				} else if (!this.bM()) {
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
				if (this.bD()) {
					this.aY = false;
					this.aZ = 0.0F;
					this.ba = 0.0F;
					this.bb = 0.0F;
				} else if (this.bM()) {
					this.world.methodProfiler.a("newAi");
					this.doTick();
					this.world.methodProfiler.b();
				}

				this.world.methodProfiler.b();
				this.world.methodProfiler.a("jump");

				if (this.aY) {
					if (this.V()) {
						this.bG();
					} else if (this.ab()) {
						this.bH();
					} else if (this.onGround && (float) ref.getFieldValue("bn") == 0) {
						this.bF();
						ref.setFieldValue("bn", 10);
					}
				} else {
					ref.setFieldValue("bn", 0);
				}

				this.world.methodProfiler.b();
				this.world.methodProfiler.a("travel");
				this.aZ *= 0.98F;
				this.ba *= 0.98F;
				this.bb *= 0.9F;
				this.g(this.aZ, this.ba);
				this.world.methodProfiler.b();
				this.world.methodProfiler.a("push");
				if (!this.world.isClientSide) {
					this.bL();
				}

				this.world.methodProfiler.b();

				this.world.methodProfiler.a("looting");
				if (!this.world.isClientSide && this.bY() && !this.aP && this.world.getGameRules().getBoolean("mobGriefing")) {
					List<?> list = this.world.a(EntityItem.class, this.getBoundingBox().grow(1.0D, 0.0D, 1.0D));
					Iterator<?> iterator = list.iterator();

					while (iterator.hasNext()) {
						EntityItem entityitem = (EntityItem) iterator.next();

						if (!entityitem.dead && entityitem.getItemStack() != null && !entityitem.s()) {
							this.a(entityitem);
						}
					}
				}

				this.world.methodProfiler.b();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void E() {
		EntityUtils.moveFlying(this);
	}

	@Override
	public void g(float f, float f1) {
		if(!callSuper){
			EntityUtils.move(this, f, f1);
			return;
		} else callSuper = false;

		double d0;
		float f2;

		if (this.bM()) {
			float f3;
			float f4;

			if (this.V()) {
				d0 = this.locY;
				f3 = 0.8F;
				f4 = 0.02F;
				f2 = (float) EnchantmentManager.b(this);
				if (f2 > 3.0F) {
					f2 = 3.0F;
				}

				if (!this.onGround) {
					f2 *= 0.5F;
				}

				if (f2 > 0.0F) {
					f3 += (0.54600006F - f3) * f2 / 3.0F;
					f4 += (this.bI() * 1.0F - f4) * f2 / 3.0F;
				}

				this.a(f, f1, f4);
				this.move(this.motX, this.motY, this.motZ);
				this.motX *= (double) f3;
				this.motY *= 0.800000011920929D;
				this.motZ *= (double) f3;
				this.motY -= 0.02D;
				if (this.positionChanged && this.c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
					this.motY = 0.30000001192092896D;
				}
			} else if (this.ab()) {
				d0 = this.locY;
				this.a(f, f1, 0.02F);
				this.move(this.motX, this.motY, this.motZ);
				this.motX *= 0.5D;
				this.motY *= 0.5D;
				this.motZ *= 0.5D;
				this.motY -= 0.02D;
				if (this.positionChanged && this.c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ)) {
					this.motY = 0.30000001192092896D;
				}
			} else {
				float f5 = 0.91F;

				if (this.onGround) {
					f5 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(this.getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;
				}

				float f6 = 0.16277136F / (f5 * f5 * f5);

				if (this.onGround) {
					f3 = this.bI() * f6;
				} else {
					f3 = this.aM;
				}

				this.a(f, f1, f3);
				f5 = 0.91F;
				if (this.onGround) {
					f5 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(this.getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;
				}

				if (this.k_()) {
					f4 = 0.15F;
					this.motX = MathHelper.a(this.motX, (double) (-f4), (double) f4);
					this.motZ = MathHelper.a(this.motZ, (double) (-f4), (double) f4);
					this.fallDistance = 0.0F;
					if (this.motY < -0.15D) {
						this.motY = -0.15D;
					}
				}

				this.move(this.motX, this.motY, this.motZ);
				if (this.positionChanged && this.k_()) {
					this.motY = 0.2D;
				}

				if (this.world.isClientSide && (!this.world.isLoaded(new BlockPosition((int) this.locX, 0, (int) this.locZ)) || !this.world.getChunkAtWorldCoords(new BlockPosition((int) this.locX, 0, (int) this.locZ)).o())) {
					if (this.locY > 0.0D) {
						this.motY = -0.1D;
					} else {
						this.motY = 0.0D;
					}
				} else {
					this.motY -= 0.08D;
				}

				this.motY *= 0.9800000190734863D;
				this.motX *= (double) f5;
				this.motZ *= (double) f5;
			}
		}

		this.aA = this.aB;
		d0 = this.locX - this.lastX;
		double d1 = this.locZ - this.lastZ;

		f2 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
		if (f2 > 1.0F) {
			f2 = 1.0F;
		}

		this.aB += (f2 - this.aB) * 0.4F;
		this.aC += this.aB;
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

		this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
		this.goalSelector.a(7, this.goalRandomStroll = new PathfinderGoalRandomStroll(this, 1.0D, 80));
		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityGuardian.class, 12.0F, 0.01F));
		this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));

		this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityLiving.class, 10, true, false, getPrivatePredicate("EntitySelectorGuardianTargetHumanSquid", new Class<?>[]{EntityGuardian.class}, this)));
	}

	@Override
	public EntityInsentient getNMSEntity() {
		return this;
	}

	private Predicate<?> getPrivatePredicate(String name, Class<?>[] forConstructor, Object... objects){
		try {
			Class<?> theClass = null;

			for(Class<?> clazz : EntityGuardian.class.getDeclaredClasses()){
				if(clazz.getName().contains(name)){
					theClass = clazz;
				}
			}

			Constructor<?> constructor = theClass.getConstructor(forConstructor);
			constructor.setAccessible(true);

			return ((Predicate<?>) constructor.newInstance(objects));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void callSuperMove(float f1, float f2) {
		callSuper = true;
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

