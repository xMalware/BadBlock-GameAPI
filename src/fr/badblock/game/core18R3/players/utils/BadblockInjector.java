package fr.badblock.game.core18R3.players.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.game.core18R3.packets.GameBadblockInPackets;
import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.game.core18R3.packets.GameBadblockOutPacket.GameBadblockOutPackets;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockOutPacket;
import fr.badblock.gameapi.packets.GlobalPacketListener;
import fr.badblock.gameapi.packets.InPacketListener;
import fr.badblock.gameapi.packets.OutPacketListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.AllArgsConstructor;
import net.minecraft.server.v1_8_R3.Packet;

@AllArgsConstructor
public class BadblockInjector extends ChannelDuplexHandler {
	private GameBadblockPlayer player;

	@Override
	public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
		boolean cancel = false;

		for (GameBadblockInPackets packet : GameBadblockInPackets.values()) {
			try {
				// Le packet recherch� dans la boucle est pas celui qui est re�u
				if (!packet.getNmsClazz().equals(msg.getClass())) continue;

				Set<InPacketListener<?>> listeners = GamePlugin.getInstance().getPacketInListeners().get(packet.getClazz());
				Set<GlobalPacketListener> globalListeners = GamePlugin.getInstance().getPacketGlobalListeners();

				// Aucun listener pour ce packet
				if((listeners == null || listeners.isEmpty()) && (globalListeners == null || globalListeners.isEmpty())) break;
				if (listeners == null || listeners.isEmpty()) {
					if (packet.name().contains("SPAWN")) continue;
				}

				// Cr�ation de notre packet sp�cial
				Packet<?> pack = (Packet<?>) msg;
				Constructor<?> constructor = ReflectionUtils.getConstructor(packet.getGameClazz(), pack.getClass());						
				GameBadblockInPacket inPacket = (GameBadblockInPacket) constructor.newInstance(pack);


				Method method = InPacketListener.class.getMethod("listen", BadblockPlayer.class, BadblockInPacket.class);

				if (listeners != null && !listeners.isEmpty())
					listeners.forEach(listener -> {
						try {
							method.invoke(listener, player, (BadblockInPacket) inPacket);
						} catch (Exception e) {
							e.printStackTrace();
						}
					});

				globalListeners.forEach(listener -> {
					try {
						listener.listen(player, inPacket);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});

				cancel = inPacket.isCancelled();
				msg = inPacket.toNms();
			} catch(Exception error) {
				error.printStackTrace();
			}
		}

		if(!cancel) {
			super.channelRead(channelHandlerContext, msg);
		}
	}

	@Override
	public void write(ChannelHandlerContext channelHandlerContext, Object msg, final ChannelPromise promise) throws Exception {
		boolean cancel = false;
		if (msg == null || msg.getClass() == null) return;
		for (GameBadblockOutPackets packet : GameBadblockOutPackets.values()) {
			try {
				// Le packet recherch� dans la boucle est pas celui qui est re�u
				if (packet == null || packet.getNmsClazz() == null) continue;
				if (!packet.getNmsClazz().equals(msg.getClass())) continue;
				Set<OutPacketListener<?>> listeners = GamePlugin.getInstance().getPacketOutListeners().get(packet.getClazz());
				//Set<GlobalPacketListener> globalListeners = GamePlugin.getInstance().getPacketGlobalListeners();

				// Aucun listener pour ce packet
				if((listeners == null || listeners.isEmpty())/* && (globalListeners == null || globalListeners.isEmpty())*/) break;
				/*if (listeners == null || listeners.isEmpty()) {
					if (packet.name().contains("SPAWN")) continue;
				}*/
				Packet<?> pack = (Packet<?>) msg;
				Constructor<?> constructor = ReflectionUtils.getConstructor(packet.getGameClazz(), pack.getClass());						
				GameBadblockOutPacket outPacket = (GameBadblockOutPacket) constructor.newInstance(pack);

				Method method = OutPacketListener.class.getMethod("listen", BadblockPlayer.class, BadblockOutPacket.class);

				if (listeners != null && !listeners.isEmpty())
					listeners.forEach(listener -> {
						try {
							method.invoke(listener, player, (BadblockOutPacket) outPacket);
						} catch (Exception e) {
							e.printStackTrace();
						}
					});

				/*globalListeners.forEach(listener -> {
					try {
						listener.listen(player, outPacket);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});*/

				cancel = outPacket.isCancelled();
				msg = outPacket.buildPacket(player);
			} catch(Exception error) {
			}
		}

		if(!cancel) {
			super.write(channelHandlerContext, msg, promise);
		}
	}
}
