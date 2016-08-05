package fr.badblock.game.v1_8_R3.entities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import fr.badblock.gameapi.utils.entities.CreatureType;
import fr.badblock.gameapi.utils.entities.CustomCreature;
import fr.badblock.gameapi.utils.reflection.Reflector;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ControllerMove;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.PathfinderGoal;

public interface NMSCustomCreature extends CustomCreature {
	@Override
	default void setCreatureGenericAttribute(CreatureGenericAttribute attribute, double value) {
		EntityUtils.setAttributeValue(this, attribute, value);
	}

	@Override
	default double getCreatureGenericAttribute(CreatureGenericAttribute attribute) {
		return EntityUtils.getAttributeValue(this, attribute);
	}

	@Override
	default org.bukkit.entity.Entity getBukkit() {
		return getNMSEntity().getBukkitEntity();
	}

	@Override
	default CreatureType getEntityType() {
		return CreatureType.getByBukkitEntity(getBukkit());
	}

	default BlockPosition getBlockPosition() {
		try {
			Field field = Entity.class.getDeclaredField("an");

			field.setAccessible(true);
			return (BlockPosition) field.get(getNMSEntity());
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	default void setBlockPosition(BlockPosition blockPosition) {
		try {
			Field field = Entity.class.getDeclaredField("an");

			field.setAccessible(true);
			field.set(getNMSEntity(), blockPosition);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	default void setBe(float be) {
		try {
			new Reflector(getNMSEntity()).setFieldValue("be", be);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	default Random getRandom() {
		try {
			Field field = Entity.class.getDeclaredField("random");

			field.setAccessible(true);
			return (Random) field.get(getNMSEntity());
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	default void setYawPitch0(float yaw, float pitch){
		try {
			Method m = Entity.class.getDeclaredMethod("setYawPitch", Float.class, Float.class);
			m.setAccessible(true);
			m.invoke(getNMSEntity(), yaw, pitch);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	default void callA(double... args){
		try {
			Method m = Entity.class.getDeclaredMethod("a", Double[].class);
			m.setAccessible(true);
			m.invoke(getNMSEntity(), args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	default void callAEntity(EntityLiving e1, Entity e2){
		try {
			Method m = Entity.class.getDeclaredMethod("a", EntityLiving.class, Entity.class);
			m.setAccessible(true);
			m.invoke(getNMSEntity(), e1, e2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	default void buildGoalSelector(int pos, Class<?> mainClass, String name, List<Class<?>> args, Object... values){
		buildGoalSelector(pos, mainClass, name, args.toArray(new Class[0]), values);
	}
	
	default void buildGoalSelector(int pos, Class<?> mainClass, String name, Class<?>[] args, Object... values){
		try {
			Class<?> theClass = null;

			for(Class<?> clazz : mainClass.getDeclaredClasses()){
				if(clazz.getName().contains(name)){
					theClass = clazz;
				}
			}

			Constructor<?> constructor = theClass.getConstructor(args);
			constructor.setAccessible(true);

			getNMSEntity().goalSelector.a(4, (PathfinderGoal) constructor.newInstance( values ));
		} catch(Exception e){

		}
	}
	
	default void buildTargetSelector(int pos, Class<?> mainClass, String name, Class<?>[] args, Object... values){
		try {
			Class<?> theClass = null;

			for(Class<?> clazz : mainClass.getDeclaredClasses()){
				if(clazz.getName().contains(name)){
					theClass = clazz;
				}
			}

			Constructor<?> constructor = theClass.getConstructor(args);
			constructor.setAccessible(true);

			getNMSEntity().targetSelector.a(4, (PathfinderGoal) constructor.newInstance( values ));
		} catch(Exception e){

		}
	}

	public void callSuperMove(float f1, float f2);

	public void callSuperMoveFlying();
	
	public boolean callSuperRightClick(EntityHuman entityhuman);
	
	public boolean callSuperDamageEntity(DamageSource damagesource, float f);
	
	public ControllerMove getNormalController();

	public void setNormalController(ControllerMove controllerMove);

	public EntityInsentient getNMSEntity();
}