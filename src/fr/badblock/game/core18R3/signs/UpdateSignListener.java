package fr.badblock.game.core18R3.signs;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.packets.OutPacketListener;
import fr.badblock.gameapi.packets.out.play.PlayUpdateSign;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.signs.SignManager;

public class UpdateSignListener extends OutPacketListener<PlayUpdateSign> {
	private static final SignManager manager = GameAPI.getAPI().getSignManager();
	
	@Override
	public void listen(BadblockPlayer player, PlayUpdateSign packet) {
		if( !manager.isSignTranslatable(packet.getBlock()) ){
			return;
		}
		
		packet.setLinesI18n( manager.getTraduction(packet.getBlock()) );
	}
}
