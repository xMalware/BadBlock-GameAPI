package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInEnchantItem;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInEnchantItem;

@Getter public class GamePlayInEnchantItem extends GameBadblockInPacket implements PlayInEnchantItem {
	private int windowId;
	private int enchant;
	
	public GamePlayInEnchantItem(PacketPlayInEnchantItem packet){
		super(packet);
		this.windowId = packet.a();
		this.enchant  = packet.b();
	}
}
