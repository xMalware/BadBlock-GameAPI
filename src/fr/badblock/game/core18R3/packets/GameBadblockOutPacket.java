package fr.badblock.game.core18R3.packets;

import java.util.HashMap;
import java.util.Map;

import fr.badblock.gameapi.packets.BadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayAbilities;
import fr.badblock.gameapi.packets.out.play.PlayAnimation;
import fr.badblock.gameapi.packets.out.play.PlayAttachEntity;
import fr.badblock.gameapi.packets.out.play.PlayBed;
import fr.badblock.gameapi.packets.out.play.PlayBlockAction;
import fr.badblock.gameapi.packets.out.play.PlayBlockBreakAnimation;
import fr.badblock.gameapi.packets.out.play.PlayBlockChange;
import fr.badblock.gameapi.packets.out.play.PlayCamera;
import fr.badblock.gameapi.packets.out.play.PlayChangeGameState;
import fr.badblock.gameapi.packets.out.play.PlayChat;
import fr.badblock.gameapi.packets.out.play.PlayCloseWindow;
import fr.badblock.gameapi.packets.out.play.PlayCollect;
import fr.badblock.gameapi.packets.out.play.PlayCombatEvent;
import fr.badblock.gameapi.packets.out.play.PlayCustomPayload;
import fr.badblock.gameapi.packets.out.play.PlayDisconnect;
import fr.badblock.gameapi.packets.out.play.PlayEntityDestroy;
import fr.badblock.gameapi.packets.out.play.PlayEntityEffect;
import fr.badblock.gameapi.packets.out.play.PlayEntityEquipment;
import fr.badblock.gameapi.packets.out.play.PlayEntityHeadRotation;
import fr.badblock.gameapi.packets.out.play.PlayEntityLook;
import fr.badblock.gameapi.packets.out.play.PlayEntityMetadata;
import fr.badblock.gameapi.packets.out.play.PlayEntityRelativeMove;
import fr.badblock.gameapi.packets.out.play.PlayEntityStatus;
import fr.badblock.gameapi.packets.out.play.PlayEntityTeleport;
import fr.badblock.gameapi.packets.out.play.PlayEntityVelocity;
import fr.badblock.gameapi.packets.out.play.PlayExperience;
import fr.badblock.gameapi.packets.out.play.PlayExplosion;
import fr.badblock.gameapi.packets.out.play.PlayHeldItemSlot;
import fr.badblock.gameapi.packets.out.play.PlayKeepAlive;
import fr.badblock.gameapi.packets.out.play.PlayLogin;
import fr.badblock.gameapi.packets.out.play.PlayMap;
import fr.badblock.gameapi.packets.out.play.PlayMapChunk;
import fr.badblock.gameapi.packets.out.play.PlayMapChunkBulk;
import fr.badblock.gameapi.packets.out.play.PlayMultiBlockChange;
import fr.badblock.gameapi.packets.out.play.PlayNamedEntitySpawn;
import fr.badblock.gameapi.packets.out.play.PlayNamedSoundEffect;
import fr.badblock.gameapi.packets.out.play.PlayOpenSignEditor;
import fr.badblock.gameapi.packets.out.play.PlayOpenWindow;
import fr.badblock.gameapi.packets.out.play.PlayPlayerInfo;
import fr.badblock.gameapi.packets.out.play.PlayPlayerListHeaderFooter;
import fr.badblock.gameapi.packets.out.play.PlayPosition;
import fr.badblock.gameapi.packets.out.play.PlayRemoveEntityEffect;
import fr.badblock.gameapi.packets.out.play.PlayResourcePackSend;
import fr.badblock.gameapi.packets.out.play.PlayRespawn;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardDisplayObjective;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardObjective;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardScore;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardTeam;
import fr.badblock.gameapi.packets.out.play.PlayServerDifficulty;
import fr.badblock.gameapi.packets.out.play.PlaySetSlot;
import fr.badblock.gameapi.packets.out.play.PlaySpawnEntityCreature;
import fr.badblock.gameapi.packets.out.play.PlaySpawnEntityExperienceOrb;
import fr.badblock.gameapi.packets.out.play.PlaySpawnEntityObject;
import fr.badblock.gameapi.packets.out.play.PlaySpawnEntityPainting;
import fr.badblock.gameapi.packets.out.play.PlaySpawnEntityWeather;
import fr.badblock.gameapi.packets.out.play.PlaySpawnPosition;
import fr.badblock.gameapi.packets.out.play.PlayStatistic;
import fr.badblock.gameapi.packets.out.play.PlayTabComplete;
import fr.badblock.gameapi.packets.out.play.PlayTileEntityData;
import fr.badblock.gameapi.packets.out.play.PlayTitle;
import fr.badblock.gameapi.packets.out.play.PlayTransaction;
import fr.badblock.gameapi.packets.out.play.PlayUpdateAttributes;
import fr.badblock.gameapi.packets.out.play.PlayUpdateHealth;
import fr.badblock.gameapi.packets.out.play.PlayUpdateSign;
import fr.badblock.gameapi.packets.out.play.PlayUpdateTime;
import fr.badblock.gameapi.packets.out.play.PlayWindowData;
import fr.badblock.gameapi.packets.out.play.PlayWindowItems;
import fr.badblock.gameapi.packets.out.play.PlayWorldBorder;
import fr.badblock.gameapi.packets.out.play.PlayWorldParticles;
import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAbilities;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutCloseWindow;
import net.minecraft.server.v1_8_R3.PacketPlayOutCollect;
import net.minecraft.server.v1_8_R3.PacketPlayOutCombatEvent;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEffect;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_8_R3.PacketPlayOutExperience;
import net.minecraft.server.v1_8_R3.PacketPlayOutExplosion;
import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutHeldItemSlot;
import net.minecraft.server.v1_8_R3.PacketPlayOutKeepAlive;
import net.minecraft.server.v1_8_R3.PacketPlayOutKickDisconnect;
import net.minecraft.server.v1_8_R3.PacketPlayOutLogin;
import net.minecraft.server.v1_8_R3.PacketPlayOutMap;
import net.minecraft.server.v1_8_R3.PacketPlayOutMapChunk;
import net.minecraft.server.v1_8_R3.PacketPlayOutMapChunkBulk;
import net.minecraft.server.v1_8_R3.PacketPlayOutMultiBlockChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutRemoveEntityEffect;
import net.minecraft.server.v1_8_R3.PacketPlayOutResourcePackSend;
import net.minecraft.server.v1_8_R3.PacketPlayOutRespawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.PacketPlayOutServerDifficulty;
import net.minecraft.server.v1_8_R3.PacketPlayOutSetSlot;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityExperienceOrb;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityPainting;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityWeather;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutStatistic;
import net.minecraft.server.v1_8_R3.PacketPlayOutTabComplete;
import net.minecraft.server.v1_8_R3.PacketPlayOutTileEntityData;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTransaction;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateAttributes;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateHealth;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateSign;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateTime;
import net.minecraft.server.v1_8_R3.PacketPlayOutWindowData;
import net.minecraft.server.v1_8_R3.PacketPlayOutWindowItems;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public abstract class GameBadblockOutPacket extends GameBadblockPacket implements BadblockOutPacket {
	@Override
	public void send(BadblockPlayer player) {
		if(player == null) return;
		
		player.sendPacket(this);
	}
	
	public abstract Packet<?> buildPacket() throws Exception;

	public Packet<?> buildPacket(BadblockPlayer player) throws Exception {
		return buildPacket();
	}
	
	@Getter public enum GameBadblockOutPackets {
		ABILITIES(PlayAbilities.class, PacketPlayOutAbilities.class),
		ANIMATION(PlayAnimation.class, PacketPlayOutAnimation.class),
		ATTACH_ENTITY(PlayAttachEntity.class, PacketPlayOutAttachEntity.class),
		BED(PlayBed.class, PacketPlayOutBed.class),
		BLOCK_ACTION(PlayBlockAction.class, PacketPlayOutBlockAction.class),
		BLOCK_BREAK_ANIMATION(PlayBlockBreakAnimation.class, PacketPlayOutBlockBreakAnimation.class),
		BLOCK_CHANGE(PlayBlockChange.class, PacketPlayOutBlockChange.class),
		CAMERA(PlayCamera.class, PacketPlayOutCamera.class),
		CHANGE_GAMESTATE(PlayChangeGameState.class, PacketPlayOutGameStateChange.class),
		CHAT(PlayChat.class, PacketPlayOutChat.class),
		CLOSE_WINDOW(PlayCloseWindow.class, PacketPlayOutCloseWindow.class),
		COLLECT(PlayCollect.class, PacketPlayOutCollect.class),
		COMBAT_EVENT(PlayCombatEvent.class, PacketPlayOutCombatEvent.class),
		CUSTOM_PAYLOAD(PlayCustomPayload.class, PacketPlayOutCustomPayload.class),
		DISCONNECT(PlayDisconnect.class, PacketPlayOutKickDisconnect.class),
		ENTITY_DESTROY(PlayEntityDestroy.class, PacketPlayOutEntityDestroy.class),
		ENTITY_EFFECT(PlayEntityEffect.class, PacketPlayOutEntityEffect.class),
		ENTITY_EQUIPMENT(PlayEntityEquipment.class, PacketPlayOutEntityEquipment.class),
		ENTITY_HEAD_ROTATION(PlayEntityHeadRotation.class, PacketPlayOutEntityHeadRotation.class),
		ENTITY_LOOK(PlayEntityLook.class, PacketPlayOutEntityLook.class),
		ENTITY_METADATA(PlayEntityMetadata.class, PacketPlayOutEntityMetadata.class),
		ENTITY_RELATIVE_MOVE(PlayEntityRelativeMove.class, PacketPlayOutRelEntityMove.class),
		ENTITY_STATUS(PlayEntityStatus.class, PacketPlayOutEntityStatus.class),
		ENTITY_TELEPORT(PlayEntityTeleport.class, PacketPlayOutEntityTeleport.class),
		ENTITY_VELOCITY(PlayEntityVelocity.class, PacketPlayOutEntityVelocity.class),
		EXPERIENCE(PlayExperience.class, PacketPlayOutExperience.class),
		EXPLOSION(PlayExplosion.class, PacketPlayOutExplosion.class),
		HELD_ITEM_SLOT(PlayHeldItemSlot.class, PacketPlayOutHeldItemSlot.class),
		KEEP_ALIVE(PlayKeepAlive.class, PacketPlayOutKeepAlive.class),
		LOGIN(PlayLogin.class, PacketPlayOutLogin.class),
		MAP(PlayMap.class, PacketPlayOutMap.class),
		MAP_CHUNK(PlayMapChunk.class, PacketPlayOutMapChunk.class),
		MAP_CHUNK_BULK(PlayMapChunkBulk.class, PacketPlayOutMapChunkBulk.class),
		MULTI_BLOCK_CHANGE(PlayMultiBlockChange.class, PacketPlayOutMultiBlockChange.class),
		NAMED_ENTITY_SPAWN(PlayNamedEntitySpawn.class, PacketPlayOutNamedEntitySpawn.class),
		NAMED_SOUND_EFFECT(PlayNamedSoundEffect.class, PacketPlayOutNamedSoundEffect.class),
		OPEN_SIGN_EDITOR(PlayOpenSignEditor.class, PacketPlayOutOpenSignEditor.class),
		OPEN_WINDOW(PlayOpenWindow.class, PacketPlayOutOpenWindow.class),
		PLAYER_INFO(PlayPlayerInfo.class, PacketPlayOutPlayerInfo.class),
		PLAYER_LIST_HEADER_FOOTER(PlayPlayerListHeaderFooter.class, PacketPlayOutPlayerListHeaderFooter.class),
		POSITION(PlayPosition.class, PacketPlayOutPosition.class),
		REMOVE_ENTITY_EFFECT(PlayRemoveEntityEffect.class, PacketPlayOutRemoveEntityEffect.class),
		RESOURCE_PACK_SEND(PlayResourcePackSend.class, PacketPlayOutResourcePackSend.class),
		SCOREBOARD_DISPLAYOBJECTIVE(PlayScoreboardDisplayObjective.class, PacketPlayOutScoreboardDisplayObjective.class),
		SCOREBOARD_OBJECTIVE(PlayScoreboardObjective.class, PacketPlayOutScoreboardObjective.class),
		SCOREBOARD_SCORE(PlayScoreboardScore.class, PacketPlayOutScoreboardScore.class),
		SCOREBOARD_TEAM(PlayScoreboardTeam.class, PacketPlayOutScoreboardTeam.class),
		SERVER_DIFFICULTY(PlayServerDifficulty.class, PacketPlayOutServerDifficulty.class),
		SET_SLOT(PlaySetSlot.class, PacketPlayOutSetSlot.class),
		SPAWN_ENTITY_CREATURE(PlaySpawnEntityCreature.class, PacketPlayOutSpawnEntityLiving.class),
		SPAWN_ENTITY_EXPERIENCE_ORB(PlaySpawnEntityExperienceOrb.class, PacketPlayOutSpawnEntityExperienceOrb.class),
		SPAWN_ENTITY_OBJECT(PlaySpawnEntityObject.class, PacketPlayOutEntity.class),
		SPAWN_ENTITY_PAINTING(PlaySpawnEntityPainting.class, PacketPlayOutSpawnEntityPainting.class),
		SPAWN_ENTITY_WEATHER(PlaySpawnEntityWeather.class, PacketPlayOutSpawnEntityWeather.class),
		SPAWN_POSITION(PlaySpawnPosition.class, PacketPlayOutSpawnPosition.class),
		STATISTIC(PlayStatistic.class, PacketPlayOutStatistic.class),
		TAB_COMPLETE(PlayTabComplete.class, PacketPlayOutTabComplete.class),
		TILE_ENTITY_DATA(PlayTileEntityData.class, PacketPlayOutTileEntityData.class),
		TITLE(PlayTitle.class, PacketPlayOutTitle.class),
		TRANSACTION(PlayTransaction.class, PacketPlayOutTransaction.class),
		UPDATE_ATTRIBUTES(PlayUpdateAttributes.class, PacketPlayOutUpdateAttributes.class),
		UPDATE_HEALTH(PlayUpdateHealth.class, PacketPlayOutUpdateHealth.class),
		UPDATE_SIGN(PlayUpdateSign.class, PacketPlayOutUpdateSign.class),
		UPDATE_TIME(PlayUpdateTime.class, PacketPlayOutUpdateTime.class),
		WINDOW_DATA(PlayWindowData.class, PacketPlayOutWindowData.class),
		WINDOW_ITEMS(PlayWindowItems.class, PacketPlayOutWindowItems.class),
		WORLD_BORDER(PlayWorldBorder.class, PacketPlayOutWorldBorder.class),
		RESPAWN(PlayRespawn.class, PacketPlayOutRespawn.class),
		WORLD_PARTICLES(PlayWorldParticles.class, PacketPlayOutWorldParticles.class);
		
		private static Map<Class<? extends BadblockOutPacket>, Class<? extends GameBadblockOutPacket>> packets = new HashMap<>();
		
		static {
			for(GameBadblockOutPackets pack : values())
				packets.put(pack.clazz, pack.gameClazz);
		}
		
		private Class<? extends BadblockOutPacket>     clazz;
		private Class<? extends GameBadblockOutPacket> gameClazz;
		private Class<? extends Packet<?>> 			   nmsClazz;

		@SuppressWarnings("unchecked")
		<T extends BadblockOutPacket> GameBadblockOutPackets(Class<T> clazz, Class<? extends Packet<?>> nmsClass){
			this.clazz 	   = clazz;
			this.nmsClazz  = nmsClass;
			
			try {
				this.gameClazz = (Class<? extends GameBadblockOutPacket>) Class.forName("fr.badblock.game.v1_8_R3.packets.out.Game" + clazz.getSimpleName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		public static Class<? extends GameBadblockOutPacket> getPacketClass(Class<? extends BadblockOutPacket> clazz){
			return packets.get(clazz);
		}
	}
}
