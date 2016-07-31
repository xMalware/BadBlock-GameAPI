package fr.badblock.game.v1_8_R3.entities;

import fr.badblock.gameapi.utils.entities.CreatureType;
import net.minecraft.server.v1_8_R3.DifficultyDamageScaler;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EnumDifficulty;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.GroupDataEntity;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.MobEffectList;
import net.minecraft.server.v1_8_R3.World;

public class NMSCaveSpider extends NMSSpider {
	public NMSCaveSpider(World w){
		super(w);
		setSize(0.7F, 0.5F);
	}

	@Override
	public CreatureType getEntityType(){
		return CreatureType.CAVE_SPIDER;
	}
	
	@Override
	protected void initAttributes() {
		super.initAttributes();

		getAttributeInstance(GenericAttributes.maxHealth).setValue(12.0D);
	}

	@Override
	public boolean r(Entity e) {
		if (super.r(e)) {
			if ((e instanceof EntityLiving)) {
				int a = 0;
				if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
					a = 7;
				} else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
					a = 15;
				}
			
				if (a > 0) {
					((EntityLiving)e).addEffect(new MobEffect(MobEffectList.POISON.id, a * 20, 0));
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public GroupDataEntity prepare(DifficultyDamageScaler a, GroupDataEntity b) {
		return b;
	}

	@Override
	public float getHeadHeight() {
		return 0.45F;
	}
}
