package fr.badblock.game.core18R3.packets.out;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;

import com.mojang.authlib.GameProfile;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayPlayerInfo;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayPlayerInfo extends GameBadblockOutPacket implements PlayPlayerInfo {
	private Map<UUID, PlayerInfo> players;
	private TabAction 		      action;
	
	@Override
	public PlayPlayerInfo addPlayer(UUID uniqueId, PlayerInfo info) {
		players.put(uniqueId, info);
		return this;
	}

	@Override
	public PlayPlayerInfo removePlayer(UUID uniqueId) {
		players.remove(uniqueId);
		return this;
	}
	
	@Override
	public PlayerInfo getInfos(UUID uniqueId) {
		return players.get(uniqueId);
	}

	@Override
	public Set<UUID> getPlayers() {
		return players.keySet();
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public GamePlayPlayerInfo(PacketPlayOutPlayerInfo packet){
		this.players = new HashMap<>();
		
		try {
			Reflector reflector = new Reflector(packet);
			
			action = TabAction.valueOf( ((EnumPlayerInfoAction) reflector.getFieldValue("a")).name() );
			List<PlayerInfoData> datas = (List<PlayerInfoData>) reflector.getFieldValue("b");
			
			for(PlayerInfoData data : datas){
				new PlayerInfo(data.a().getId(), data.a().getName(), data.a().getProperties(), GameMode.getByValue(data.c().getId()), data.b(), toLegacy(data.d()));
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		List<PlayerInfoData> playerInfoData = new ArrayList<>();
		
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		
		players.forEach((uuid, info) -> {
			try {
				PlayerInfoData data = createObjectInstance(PlayerInfoData.class);

				Reflector reflector = new Reflector(data);
				
				reflector.setFieldValue("b", info.ping);
				reflector.setFieldValue("c", EnumGamemode.getById(info.ping));
				
				GameProfile profile = new GameProfile(uuid, info.name);
				new Reflector(profile).setFieldValue("properties", info.profile);
				
				reflector.setFieldValue("d", profile);
				reflector.setFieldValue("e", getChat(info.displayName));

				playerInfoData.add(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		Reflector reflector = new Reflector(packet);
		
		reflector.setFieldValue("a", EnumPlayerInfoAction.valueOf(action.name()));
		reflector.setFieldValue("b", playerInfoData);
		
		return packet;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T createObjectInstance(Class<T> c){
		if(c.isEnum() || c.isArray()){ // Could not load an Enum or an Array from an JObject
			return null;
		} else if(c.isInterface()){

		} else {
			try{
				return getConstructor(c).newInstance();
			} catch(Exception e){}
			try {
				Class<?> unsafe = Class.forName("sun.misc.Unsafe");
				Field f = unsafe.getDeclaredField("theUnsafe"); f.setAccessible(true);
				return (T) unsafe.getMethod("allocateInstance", Class.class).invoke(f.get(null), c);
			} catch (Exception e){}
		}
		return null;
	}
	
	private static <T> Constructor<T> getConstructor(Class<T> c){
		try {
			return c.getDeclaredConstructor();
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Can not get the default constructor of " + c.getSimpleName() + ".", e);
		} catch(SecurityException e){
			throw new RuntimeException("Can not get the default constructor of " + c.getSimpleName() + ".", e);
		}
	}
}
