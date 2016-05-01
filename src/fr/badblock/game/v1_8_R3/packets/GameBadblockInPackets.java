package fr.badblock.game.v1_8_R3.packets;

import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.in.Handshake;
import fr.badblock.gameapi.packets.in.LoginStart;
import fr.badblock.gameapi.packets.in.StatusPing;
import fr.badblock.gameapi.packets.in.StatusRequest;
import fr.badblock.gameapi.packets.in.play.PlayInAbilities;
import fr.badblock.gameapi.packets.in.play.PlayInArmAnimation;
import fr.badblock.gameapi.packets.in.play.PlayInBlockDig;
import fr.badblock.gameapi.packets.in.play.PlayInBlockPlace;
import fr.badblock.gameapi.packets.in.play.PlayInChat;
import fr.badblock.gameapi.packets.in.play.PlayInClientCommand;
import fr.badblock.gameapi.packets.in.play.PlayInCloseWindow;
import fr.badblock.gameapi.packets.in.play.PlayInCustomPayload;
import fr.badblock.gameapi.packets.in.play.PlayInEnchantItem;
import fr.badblock.gameapi.packets.in.play.PlayInEntityAction;
import fr.badblock.gameapi.packets.in.play.PlayInHeldItemSlot;
import fr.badblock.gameapi.packets.in.play.PlayInKeepAlive;
import fr.badblock.gameapi.packets.in.play.PlayInLook;
import fr.badblock.gameapi.packets.in.play.PlayInPosition;
import fr.badblock.gameapi.packets.in.play.PlayInPositionLook;
import fr.badblock.gameapi.packets.in.play.PlayInResourcePackStatus;
import fr.badblock.gameapi.packets.in.play.PlayInSetCreativeSlot;
import fr.badblock.gameapi.packets.in.play.PlayInSettings;
import fr.badblock.gameapi.packets.in.play.PlayInSpectate;
import fr.badblock.gameapi.packets.in.play.PlayInSteerVehicle;
import fr.badblock.gameapi.packets.in.play.PlayInTabComplete;
import fr.badblock.gameapi.packets.in.play.PlayInTransaction;
import fr.badblock.gameapi.packets.in.play.PlayInUpdateSign;
import fr.badblock.gameapi.packets.in.play.PlayInUseEntity;
import fr.badblock.gameapi.packets.in.play.PlayInWindowClick;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketHandshakingInSetProtocol;
import net.minecraft.server.v1_8_R3.PacketLoginInStart;
import net.minecraft.server.v1_8_R3.PacketPlayInAbilities;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInCloseWindow;
import net.minecraft.server.v1_8_R3.PacketPlayInCustomPayload;
import net.minecraft.server.v1_8_R3.PacketPlayInEnchantItem;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying.PacketPlayInLook;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying.PacketPlayInPosition;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying.PacketPlayInPositionLook;
import net.minecraft.server.v1_8_R3.PacketPlayInHeldItemSlot;
import net.minecraft.server.v1_8_R3.PacketPlayInKeepAlive;
import net.minecraft.server.v1_8_R3.PacketPlayInResourcePackStatus;
import net.minecraft.server.v1_8_R3.PacketPlayInSetCreativeSlot;
import net.minecraft.server.v1_8_R3.PacketPlayInSettings;
import net.minecraft.server.v1_8_R3.PacketPlayInSpectate;
import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;
import net.minecraft.server.v1_8_R3.PacketPlayInTransaction;
import net.minecraft.server.v1_8_R3.PacketPlayInUpdateSign;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;
import net.minecraft.server.v1_8_R3.PacketStatusInPing;
import net.minecraft.server.v1_8_R3.PacketStatusInStart;

@Getter public enum GameBadblockInPackets {
	HANDSHAKE(Handshake.class, PacketHandshakingInSetProtocol.class),
	LOGIN_START(LoginStart.class, PacketLoginInStart.class),
	PLAY_ABILITIES(PlayInAbilities.class, PacketPlayInAbilities.class),
	PLAY_ARM_ANIMATION(PlayInArmAnimation.class, PacketPlayInArmAnimation.class),
	PLAY_BLOCK_DIG(PlayInBlockDig.class, PacketPlayInBlockDig.class),
	PLAY_BLOCK_PLACE(PlayInBlockPlace.class, PacketPlayInBlockPlace.class),
	PLAY_CHAT(PlayInChat.class, PacketPlayInChat.class),
	PLAY_CLIENT_COMMAND(PlayInClientCommand.class, PacketPlayInClientCommand.class),
	PLAY_CLOSE_WINDOW(PlayInCloseWindow.class,PacketPlayInCloseWindow.class),
	PLAY_CUSTOM_PAYLOAD(PlayInCustomPayload.class, PacketPlayInCustomPayload.class),
	PLAY_ENCHANT_ITEM(PlayInEnchantItem.class, PacketPlayInEnchantItem.class),
	PLAY_ENTITY_ACTION(PlayInEntityAction.class, PacketPlayInEntityAction.class),
	PLAY_HELD_ITEM_SLOT(PlayInHeldItemSlot.class, PacketPlayInHeldItemSlot.class),
	PLAY_KEEPALIVE(PlayInKeepAlive.class, PacketPlayInKeepAlive.class),
	PLAY_LOOK(PlayInLook.class, PacketPlayInLook.class),
	PLAY_POSITION(PlayInPosition.class, PacketPlayInPosition.class),
	PLAY_POSITION_LOOK(PlayInPositionLook.class, PacketPlayInPositionLook.class),
	PLAY_RESOURCEPACK_STATUS(PlayInResourcePackStatus.class, PacketPlayInResourcePackStatus.class),
	PLAY_SET_CREATIVE_SLOT(PlayInSetCreativeSlot.class, PacketPlayInSetCreativeSlot.class),
	PLAY_SETTINGS(PlayInSettings.class, PacketPlayInSettings.class),
	PLAY_SPECCTATE(PlayInSpectate.class, PacketPlayInSpectate.class),
	PLAY_STEER_VEHICLE(PlayInSteerVehicle.class, PacketPlayInSteerVehicle.class),
	PLAY_TAB_COMPLETE(PlayInTabComplete.class, PacketPlayInTabComplete.class),
	PLAY_TRANSACTION(PlayInTransaction.class, PacketPlayInTransaction.class),
	PLAY_UPDATE_SIGN(PlayInUpdateSign.class, PacketPlayInUpdateSign.class),
	PLAY_USE_ENTITY(PlayInUseEntity.class, PacketPlayInUseEntity.class),
	PLAY_WINDOW_CLICK(PlayInWindowClick.class, PacketPlayInWindowClick.class),
	STATUS_PING(StatusPing.class, PacketStatusInPing.class),
	STATUS_REQUEST(StatusRequest.class, PacketStatusInStart.class);
	
	private Class<? extends BadblockInPacket> clazz;
	private Class<? extends Packet<?>>		  nmsClazz;
	private Class<?>					      gameClazz;
	
	GameBadblockInPackets(Class<? extends BadblockInPacket> clazz, Class<? extends Packet<?>> nmsClazz){
		this.clazz     = clazz;
		this.nmsClazz  = nmsClazz;
		try {
			this.gameClazz = Class.forName("fr.badblock.game.v1_8_R3.packets.in.Game" + clazz.getSimpleName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
