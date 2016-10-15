package fr.badblock.game.core18R3.fakeentities;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.packets.BadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayNamedEntitySpawn;
import fr.badblock.gameapi.packets.out.play.PlayPlayerInfo;
import fr.badblock.gameapi.packets.out.play.PlayPlayerInfo.PlayerInfo;
import fr.badblock.gameapi.packets.out.play.PlayPlayerInfo.TabAction;
import fr.badblock.gameapi.packets.watchers.WatcherLivingEntity;
import fr.badblock.gameapi.players.BadblockPlayer;

public class GameFakePlayer extends GameFakeEntity<WatcherLivingEntity> {
	private PlayerInfo	playerInfos;

	public GameFakePlayer(int entityId, PlayerInfo playerInfos, WatcherLivingEntity watchers, Location location) {
		super(EntityType.PLAYER, entityId, watchers, location);
		
		this.playerInfos = playerInfos;
	}

	@Override
	public List<BadblockOutPacket> getSpawnPackets() {
		return 
				Arrays.asList(
						GameAPI.getAPI().createPacket(PlayPlayerInfo.class)
							.setAction(TabAction.ADD_PLAYER)
							.addPlayer(playerInfos.uniqueId, playerInfos),
						GameAPI.getAPI().createPacket(PlayNamedEntitySpawn.class)
							.setEntityId(getId())
							.setUniqueId(playerInfos.uniqueId)
							.setItemInHand(Material.AIR)
							.setLocation(getLocation())
							.setWatchers(getWatchers())
				);
	}
	
	@Override
	public void remove0(BadblockPlayer player){
		super.remove0(player);
		GameAPI.getAPI().createPacket(PlayPlayerInfo.class)
						.setAction(TabAction.REMOVE_PLAYER)
						.addPlayer(playerInfos.uniqueId, playerInfos);
	}

}
