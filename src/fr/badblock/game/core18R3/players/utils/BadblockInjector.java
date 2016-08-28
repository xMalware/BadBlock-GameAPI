package fr.badblock.game.core18R3.players.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.game.core18R3.packets.GameBadblockInPackets;
import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.game.core18R3.packets.GameBadblockOutPacket.GameBadblockOutPackets;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockOutPacket;
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
				if (!packet.getNmsClazz().equals(msg.getClass()))
					continue;

				// Aucun listener pour ce packet
				if (GamePlugin.getInstance().getPacketInListeners().get(packet.getClazz()) == null) {
					break;
				}
				// Cr�ation de notre packet sp�cial
				Packet<?> pack = (Packet<?>) msg;
				Constructor<?> constructor = ReflectionUtils.getConstructor(packet.getGameClazz(), pack.getClass());
				GameBadblockInPacket inPacket = (GameBadblockInPacket) constructor.newInstance(pack);

				Method method = InPacketListener.class.getMethod("listen", BadblockPlayer.class,
						BadblockInPacket.class);

				GamePlugin.getInstance().getPacketInListeners().get(packet.getClazz()).forEach(listener -> {
					try {
						method.invoke(listener, player, inPacket);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});

				if (inPacket.isCancelled())
					cancel = true;
				msg = inPacket.toNms();
			} catch (Exception error) {
				error.printStackTrace();
			}
		}

		if (!cancel) {
			super.channelRead(channelHandlerContext, msg);
		}
	}

	@Override
	public void write(ChannelHandlerContext channelHandlerContext, Object msg, final ChannelPromise promise)
			throws Exception {
		boolean cancel = false;

		for (GameBadblockOutPackets packet : GameBadblockOutPackets.values()) {
			try {
				// Le packet recherch� dans la boucle est pas celui qui est re�u
				if (!packet.getNmsClazz().equals(msg.getClass()))
					continue;
				// Aucun listener pour ce packet
				if (!GamePlugin.getInstance().getPacketOutListeners().containsKey(packet.getClazz()))
					break;
				// Cr�ation de notre packet sp�cial
				Packet<?> pack = (Packet<?>) msg;
				Constructor<?> constructor = ReflectionUtils.getConstructor(packet.getGameClazz(), pack.getClass());
				GameBadblockOutPacket outPacket = (GameBadblockOutPacket) constructor.newInstance(pack);

				Method method = OutPacketListener.class.getMethod("listen", BadblockPlayer.class,
						BadblockOutPacket.class);

				GamePlugin.getInstance().getPacketOutListeners().get(packet.getClazz()).forEach(listener -> {
					try {
						method.invoke(listener, player, outPacket);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});

				if (outPacket.isCancelled())
					cancel = true;
				msg = outPacket.buildPacket(player);
			} catch (Exception error) {
				error.printStackTrace();
			}
		}

		if (!cancel) {
			super.write(channelHandlerContext, msg, promise);
		}
	}
}
