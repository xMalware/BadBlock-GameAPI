package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInFlying;
import fr.badblock.gameapi.utils.selections.Vector3f;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

@Getter public class GamePlayInFlying extends GameBadblockInPacket implements PlayInFlying {
	protected Vector3f position;
	private boolean  hasPosition;
	private boolean  hasLook;
	private boolean  onGround;
	
	public GamePlayInFlying(PacketPlayInFlying packet){
		super(packet);
		this.position = new Vector3f(packet.a(), packet.b(), packet.c());
		this.hasPosition = packet.g();
		this.hasLook = packet.h();
		this.onGround = packet.f();
	}

	@Override
	public boolean hasPosition() {
		return this.hasPosition;
	}

	@Override
	public boolean hasLook() {
		return this.hasLook;
	}
}
