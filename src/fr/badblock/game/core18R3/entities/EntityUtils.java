package fr.badblock.game.core18R3.entities;

import java.lang.reflect.Field;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.utils.entities.CustomCreature.CreatureBehaviour;
import fr.badblock.gameapi.utils.entities.CustomCreature.CreatureFlag;
import fr.badblock.gameapi.utils.entities.CustomCreature.CreatureGenericAttribute;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EnchantmentManager;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityBat;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.IAttribute;
import net.minecraft.server.v1_8_R3.Material;
import net.minecraft.server.v1_8_R3.MathHelper;

public class EntityUtils {
	public static void prepare(NMSCustomCreature creature, CreatureFlag... flags){
		EntityInsentient entity = creature.getNMSEntity();

		creature.setNormalController(entity.getControllerMove());
		
		if(entity.isFireProof())
			creature.addCreatureFlag(CreatureFlag.FIREPROOF);

		if (creature.getEntityType() == null) throw new NullPointerException("Unknown entity type :o");
		if(creature.getEntityType().isHostile())
			creature.addCreatureFlag(CreatureFlag.AGRESSIVE);
		else entity.getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE).setValue(3.0d);
		
		creature.setCreatureBehaviour(creature instanceof EntityBat ? CreatureBehaviour.FLYING : CreatureBehaviour.NORMAL);
		creature.regenerateAttributes();
	}
	
	private static IAttribute getAttribute(CreatureGenericAttribute from){
		switch(from){
			case DAMAGE:
				return GenericAttributes.ATTACK_DAMAGE;
			case SPEED:
				return GenericAttributes.MOVEMENT_SPEED;
		};
		
		return null;
	}
	
	public static double getAttributeValue(NMSCustomCreature creature, CreatureGenericAttribute attribute){
		return creature.getNMSEntity().getAttributeInstance(getAttribute(attribute)).getValue();
	}
	
	public static void setAttributeValue(NMSCustomCreature creature, CreatureGenericAttribute attribute, double value){
		creature.getNMSEntity().getAttributeInstance(getAttribute(attribute)).setValue(value);
	}
	
	public static void move(NMSCustomCreature creature, float sideMot, float forMot){
		EntityInsentient entity = creature.getNMSEntity();
		
		if(creature.getCreatureBehaviour() == CreatureBehaviour.MOTIONLESS)
			return;

		if(entity.passenger != null){
			movePassenger(creature); return;
		} 

		if(creature.getCreatureBehaviour() != CreatureBehaviour.FLYING || entity.getGoalTarget() != null){
			creature.callSuperMove(sideMot, forMot);
			return;
		}

		if(entity.inWater) {
			entity.a(sideMot, forMot, 0.02F);
			entity.move(entity.motX, entity.motY, entity.motZ);

			entity.motX *= 0.800000011920929D;
			entity.motY *= 0.800000011920929D;
			entity.motZ *= 0.800000011920929D;
		} else if (isInLava(entity)) {
			entity.a(sideMot, forMot, 0.02F);
			entity.move(entity.motX, entity.motY, entity.motZ);
			entity.motX *= 0.5D;
			entity.motY *= 0.5D;
			entity.motZ *= 0.5D;
		} else {
			float f1 = 0.91F;

			if(entity.onGround) {
				BlockPosition pos = new BlockPosition(MathHelper.floor(entity.locX), MathHelper.floor(entity.getBoundingBox().b) - 1, MathHelper.floor(entity.locZ));
				f1 = entity.world.getType(pos).getBlock().frictionFactor * 0.91F;
			}

			float f2 = 0.16277136F / (f1 * f1 * f1);
			entity.a(sideMot, forMot, entity.onGround ? 0.1F * f2 : 0.02F);

			entity.move(entity.motX, entity.motY, entity.motZ);

			entity.motX *= f1;
			entity.motY *= f1;
			entity.motZ *= f1;
		}

		entity.aE = entity.aF;

		double d1 = entity.locX - entity.lastX;
		double d2 = entity.locZ - entity.lastZ;

		float f3 = MathHelper.sqrt(d1 * d1 + d2 * d2) * 4.0F;

		if (f3 > 1.0F) {
			f3 = 1.0F;
		}

		entity.aF += (f3 - entity.aF) * 0.4F;
		entity.aG += entity.aF;
	}
	
	public static void movePassenger(NMSCustomCreature creature){
		EntityInsentient entity = creature.getNMSEntity();
		
		entity.lastYaw = entity.yaw = entity.passenger.yaw;
		entity.pitch = entity.passenger.pitch * 0.5F;
		
		entity.aI = entity.aG = entity.yaw;

		float sideMot = ((EntityLiving) entity.passenger).aZ * 0.5F;
		float forMot = ((EntityLiving) entity.passenger).ba;

		if(forMot <= 0.0F) {
			forMot *= 0.25F;// Make backwards slower
		}

		sideMot *= 0.75F;
		
		if(creature.getCreatureBehaviour() != CreatureBehaviour.FLYING){
			Field jump = null; //Jumping
			try {
				jump = EntityLiving.class.getDeclaredField("aY");
			} catch (NoSuchFieldException | SecurityException e1) {
				e1.printStackTrace();
			}

			jump.setAccessible(true);

			entity.S = 1.0F;

			entity.k(0.35F);
			creature.callSuperMove(sideMot, forMot);

			if (jump != null && entity.onGround) {
				try {
					if (jump.getBoolean(entity.passenger)) {
						entity.motY = 0.5d;
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			return;
		} else entity.motY = -entity.pitch/45;

		entity.a(sideMot, forMot, (float) creature.getCreatureGenericAttribute(CreatureGenericAttribute.SPEED));
		
		if(sideMot != 0 || forMot != 0) {
			entity.move(entity.motX, entity.motY, entity.motZ);

			entity.motX *= 0.8D;
			entity.motY *= 0.8D;
			entity.motZ *= 0.8D;
		}
	}
	
	public static void moveFlying(NMSCustomCreature creature){
		EntityInsentient entity = creature.getNMSEntity();

		if(creature.getCreatureBehaviour() == CreatureBehaviour.MOTIONLESS){
			return;
		}

		if(entity.passenger != null || entity.getGoalTarget() != null){
			return;	
		}
		
		creature.callSuperMoveFlying();

		if(creature.getCreatureBehaviour() != CreatureBehaviour.FLYING)
			return;

		BlockPosition position = creature.getBlockPosition();
		Random		  random   = creature.getRandom();
		
		if((position == null) || (random.nextInt(30) == 0) || (position.c((int) entity.locX, (int) entity.locY, (int) entity.locZ) < 4.0F)) {
			position = new BlockPosition((int) entity.locX + random.nextInt(7) - random.nextInt(7),
					(int) entity.locY + random.nextInt(6) - 2,
					(int) entity.locZ + random.nextInt(7) - random.nextInt(7));
			creature.setBlockPosition(position);
		}
		
		double d1 = position.getX() + 0.5D - entity.locX;
		double d2 = position.getY() + 0.1D - entity.locY;
		double d3 = position.getZ() + 0.5D - entity.locZ;

		entity.motX += (Math.signum(d1) * 0.5D - entity.motX) * 0.10000000149011612D;
		entity.motY += (Math.signum(d2) * 0.699999988079071D - entity.motY) * 0.10000000149011612D;
		entity.motZ += (Math.signum(d3) * 0.5D - entity.motZ) * 0.10000000149011612D;

		float f1 = (float)(Math.atan2(entity.motZ, entity.motX) * 180.0D / 3.1415927410125732D) - 90.0F;
		float f2 = MathHelper.g(f1 - entity.yaw);

		creature.setBe(0.5f);
		entity.yaw += f2;
	}
	
	public static boolean rightClick(NMSCustomCreature creature, EntityHuman entityhuman){
		EntityInsentient entity = creature.getNMSEntity();

		if(!creature.hasCreatureFlag(CreatureFlag.RIDEABLE)){
			return creature.callSuperRightClick(entityhuman);
		}
		
		if(creature.callSuperRightClick(entityhuman))
			return true;
		
		if(!entity.world.isClientSide && (entity.passenger == null || entity.passenger == entityhuman)) {
			entityhuman.mount(entity);
			return true;
		}

		return false;
	}
	
	public static boolean damageEntity(NMSCustomCreature creature, DamageSource damagesource, float f){
		if(creature.getEntityType().isHostile()){
			return creature.callSuperDamageEntity(damagesource, f);
		}
		
		if(creature.getNMSEntity().isInvulnerable(damagesource))
			return false;
		
		if(creature.callSuperDamageEntity(damagesource, f)){
			Entity entity = damagesource.getEntity();
			return (entity.passenger != entity) && (entity.vehicle != entity);
		}
		
		return false;
	}
	
	public static boolean attack(NMSCustomCreature creature, Entity entity){
		if(!creature.hasCreatureFlag(CreatureFlag.AGRESSIVE)) return false;
		
		EntityInsentient thisEntity = creature.getNMSEntity();
		
		float f = (float) creature.getCreatureGenericAttribute(CreatureGenericAttribute.DAMAGE);

		int i = 0;
		if ((entity instanceof EntityLiving)) {
			f += EnchantmentManager.a(thisEntity.bA(), ((EntityLiving)entity).getMonsterType());
			i += EnchantmentManager.a(thisEntity);
		}
		
		boolean flag = entity.damageEntity(DamageSource.mobAttack(thisEntity), f);
		
		if (flag) {
			if (i > 0) {
				entity.g(-MathHelper.sin(thisEntity.yaw * 3.1415927F / 180.0F) * i * 0.5F, 0.1D, MathHelper.cos(thisEntity.yaw * 3.1415927F / 180.0F) * i * 0.5F);
				thisEntity.motX *= 0.6D;
				thisEntity.motZ *= 0.6D;
			}
			int j = EnchantmentManager.getFireAspectEnchantmentLevel(thisEntity);
			if (j > 0) {
				EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(creature.getBukkit(), entity.getBukkitEntity(), j * 4);
				Bukkit.getPluginManager().callEvent(combustEvent);
				if (!combustEvent.isCancelled()) {
					entity.setOnFire(combustEvent.getDuration());
				}
			}
			
			
			creature.callAEntity(thisEntity, entity);
		}
		
		return flag;
	}
	
	private static boolean isInLava(Entity entity){
		return entity.world.a(entity.getBoundingBox().grow(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.LAVA);
	}
	
	public static void doDrops(NMSCustomCreature creature){
		if(creature.getCustomLoots() == null)
			return;
		
		EntityInsentient ent = creature.getNMSEntity();
		
		for(ItemStack item : creature.getCustomLoots().apply(ent.bc())){
			net.minecraft.server.v1_8_R3.ItemStack itemNms = CraftItemStack.asNMSCopy(item);
			
			ent.a(itemNms.getItem(), itemNms.count);
		}
	}
}
