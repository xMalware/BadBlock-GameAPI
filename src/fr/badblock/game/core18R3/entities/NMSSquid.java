package fr.badblock.game.core18R3.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ControllerMove;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EnchantmentManager;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntitySquid;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

public class NMSSquid extends EntitySquid implements NMSCustomCreature {
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
	
	public NMSSquid(World world) {
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

		buildTargetSelector(2, EntitySquid.class, "PathfinderGoalSquid", new Class<?>[]{EntitySquid.class}, this);
	}

	@Override
	public EntityInsentient getNMSEntity() {
		return this;
	}
	
	@Override
	public void callSuperMove(float f1, float f2) {
		callSuper = true;
		g(f1, f2);
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
}
