package fr.badblock.game.core18R3.packets.out;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayEntityEffect;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEffect;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayEntityEffect extends GameBadblockOutPacket implements PlayEntityEffect {
	private int 			 entityId;
	private PotionEffectType potionEffect;
	private byte			 amplifier;
	private int				 durationInSeconds;
	private boolean			 hideParticles;
	
	public PlayEntityEffect load(PotionEffect effect) {
		this.potionEffect      = effect.getType();
		this.amplifier	       = (byte) effect.getAmplifier();
		this.durationInSeconds = (effect.getDuration() * 50) / 1000;
		this.hideParticles     = effect.hasParticles();
		
		return this;
	}

	@SuppressWarnings("deprecation")
	public GamePlayEntityEffect(PacketPlayOutEntityEffect packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			entityId 		  = (int) reflector.getFieldValue("a");
			potionEffect 	  = PotionEffectType.getById((byte) reflector.getFieldValue("b"));
			amplifier         = (byte) reflector.getFieldValue("c");
			durationInSeconds = (int) reflector.getFieldValue("d");
			hideParticles     = (int) reflector.getFieldValue("e") == 0;
		} catch(Exception e){}
	}
	
	@SuppressWarnings("deprecation") @Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutEntityEffect packet = new PacketPlayOutEntityEffect();
		Reflector reflector = new Reflector(packet);
		
		reflector.setFieldValue("a", entityId);
		reflector.setFieldValue("b", (byte) potionEffect.getId());
		reflector.setFieldValue("c", amplifier);
		reflector.setFieldValue("d", durationInSeconds > 32767 ? 32767 : durationInSeconds);
		reflector.setFieldValue("e", hideParticles ? 0 : 1);
		
		return packet;
	}
}
