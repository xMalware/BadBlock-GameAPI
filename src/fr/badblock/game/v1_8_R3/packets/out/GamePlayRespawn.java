package fr.badblock.game.v1_8_R3.packets.out;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;

import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayRespawn;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.EnumDifficulty;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutRespawn;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayRespawn extends GameBadblockOutPacket implements PlayRespawn {
	
	private World.Environment dimension;
	private Difficulty		  difficulty;
	private GameMode		  gameMode;
	private WorldType		  worldType;
	
	
	private Packet<?> packet;
	
	@SuppressWarnings("deprecation")
	public GamePlayRespawn(PacketPlayOutRespawn packet){
		this.packet = packet;
		
		Reflector reflector = new Reflector(packet);
		
		try {
			dimension  = Environment.getEnvironment((int) reflector.getFieldValue("a"));
			difficulty = Difficulty.getByValue(((EnumDifficulty) reflector.getFieldValue("b")).ordinal());
			gameMode   = GameMode.getByValue(((EnumGamemode) reflector.getFieldValue("c")).getId());
			worldType  = WorldType.getByName(((net.minecraft.server.v1_8_R3.WorldType) reflector.getFieldValue("d")).name());
		} catch(Exception e){}
		
		
	}
	
	@SuppressWarnings("deprecation") @Override
	public Packet<?> buildPacket() throws Exception {
		if(dimension == null)
			dimension = Environment.NORMAL;
		if(difficulty == null)
			difficulty = Difficulty.NORMAL;
		if(worldType == null)
			worldType = WorldType.NORMAL;
		
		return new PacketPlayOutRespawn(dimension.getId(),
				EnumDifficulty.getById(difficulty.getValue()),
				net.minecraft.server.v1_8_R3.WorldType.getType(worldType.getName()),
				EnumGamemode.getById(gameMode.getValue())
		);
	}

}
