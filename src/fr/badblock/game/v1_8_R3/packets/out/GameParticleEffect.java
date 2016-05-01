package fr.badblock.game.v1_8_R3.packets.out;

import org.bukkit.util.Vector;

import fr.badblock.gameapi.particles.ParticleData;
import fr.badblock.gameapi.particles.ParticleEffect;
import fr.badblock.gameapi.particles.ParticleEffectType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GameParticleEffect implements ParticleEffect {
	private ParticleEffectType type			= null;
	private ParticleData	   data 		= null;
	private boolean			   longDistance = false;
	private Vector			   offset		= null;
	private float			   speed		= 0f;
	private int				   amount		= 0;

	public GameParticleEffect(ParticleEffectType type){
		this.type = type;
	}
}
