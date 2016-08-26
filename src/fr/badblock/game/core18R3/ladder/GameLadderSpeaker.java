package fr.badblock.game.core18R3.ladder;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.bukkit.Bukkit;

import com.google.common.collect.Queues;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.databases.LadderSpeaker;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.permissions.PermissionManager;
import fr.badblock.protocol.PacketHandler;
import fr.badblock.protocol.Protocol;
import fr.badblock.protocol.packets.Packet;
import fr.badblock.protocol.packets.PacketHelloworld;
import fr.badblock.protocol.packets.PacketLadderStop;
//import fr.badblock.protocol.packets.PacketLadderStop;
import fr.badblock.protocol.packets.PacketPlayerChat;
import fr.badblock.protocol.packets.PacketPlayerData;
import fr.badblock.protocol.packets.PacketPlayerData.DataAction;
import fr.badblock.protocol.packets.PacketPlayerData.DataType;
import fr.badblock.protocol.packets.PacketPlayerJoin;
import fr.badblock.protocol.packets.PacketPlayerLogin;
//import fr.badblock.protocol.packets.PacketPlayerLogin;
import fr.badblock.protocol.packets.PacketPlayerPlace;
import fr.badblock.protocol.packets.PacketPlayerQuit;
import fr.badblock.protocol.packets.PacketReconnectionInvitation;
import fr.badblock.protocol.packets.PacketSimpleCommand;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingJoin;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingKeepalive;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingKeepalive.ServerStatus;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingPing;
import fr.badblock.protocol.packets.matchmaking.PacketMatchmakingPong;
import fr.badblock.protocol.socket.SocketHandler;

public class GameLadderSpeaker implements LadderSpeaker, PacketHandler {
	private final Map<String,  Callback<JsonObject>> requestedPlayers = new HashMap<>();
	private final Map<String,  Callback<JsonObject>> requestedIps     = new HashMap<>();
	private final Map<Integer,  Callback<Integer>> 	 requestedPing    = new HashMap<>();

	private SocketHandler					  		 socketHandler;
	private String 									 ip;
	private int    									 port;
	private int										 nextKey		  = 0;

	private boolean trying   = false;

	private Queue<Packet>							 waitingPackets;

	public GameLadderSpeaker(String ip, int port) throws IOException {
		this.ip 		   = ip;
		this.port		   = port;

		Socket socket 	   = new Socket(ip, port);
		this.socketHandler = new SocketHandler(socket, Protocol.LADDER_TO_BUKKIT, Protocol.BUKKIT_TO_LADDER, this, false);

		this.socketHandler.start();

		this.socketHandler.getOut().writeUTF(Bukkit.getIp());
		this.socketHandler.getOut().writeInt(Bukkit.getPort());

		this.waitingPackets = Queues.newLinkedBlockingDeque();
	}

	public void sendPacket(Packet packet){
		if(socketHandler == null || socketHandler.getSocket().isClosed()){
			if(trying){
				if(packet instanceof PacketPlayerData)
					waitingPackets.add(packet);
				return;
			}

			trying = true;
			new Thread(){
				@Override
				public void run(){
					try {
						Socket socket = new Socket(ip, port);
						socketHandler = new SocketHandler(socket, Protocol.LADDER_TO_BUKKIT, Protocol.BUKKIT_TO_LADDER, GameLadderSpeaker.this, false);

						socketHandler.start();

						socketHandler.getOut().writeUTF(Bukkit.getIp());
						socketHandler.getOut().writeInt(Bukkit.getPort());

						socketHandler.sendPacket(packet);

						Packet p = null;

						while((p = waitingPackets.poll()) != null){
							socketHandler.sendPacket(p);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							Thread.sleep(15_000L);
						} catch (InterruptedException unused){}

						trying = false;
					}
				}
			}.start();
		} else socketHandler.sendPacket(packet);
	}

	@Override
	public void getPlayerData(BadblockPlayer player, Callback<JsonObject> callback) {
		requestedPlayers.put(player.getName().toLowerCase(), callback);
		sendPacket(new PacketPlayerData(DataType.PLAYER, DataAction.REQUEST, player.getName(), "*"));
	}

	@Override
	public void getPlayerData(String player, Callback<JsonObject> callback) {
		requestedPlayers.put(player.toLowerCase(), callback);
		sendPacket(new PacketPlayerData(DataType.PLAYER, DataAction.REQUEST, player, "*"));
	}

	@Override
	public void updatePlayerData(BadblockPlayer player, JsonObject toUpdate) {
		updatePlayerData(player.getName(), toUpdate);
	}

	@Override
	public void updatePlayerData(String player, JsonObject toUpdate) {
		if(toUpdate != null)
			sendPacket(new PacketPlayerData(DataType.PLAYER, DataAction.MODIFICATION, player, toUpdate.toString()));
	}

	@Override
	public void getIpPlayerData(BadblockPlayer player, Callback<JsonObject> callback) {
		String ip = player.getAddress().getAddress().getHostAddress();

		requestedIps.put(ip, callback);
		sendPacket(new PacketPlayerData(DataType.IP, DataAction.REQUEST, ip, "*"));
	}

	@Override
	public void updateIpPlayerData(BadblockPlayer player, JsonObject toUpdate) {
		String ip = player.getAddress().getAddress().getHostAddress();
		if(toUpdate != null)
			sendPacket(new PacketPlayerData(DataType.IP, DataAction.MODIFICATION, ip, toUpdate.toString()));
	}

	@Override
	public void askForPermissions() {
		sendPacket(new PacketPlayerData(DataType.PERMISSION, DataAction.REQUEST, "*", ""));
	}

	@Override
	public void keepAlive(GameState state, int current, int max){
		sendPacket(new PacketMatchmakingKeepalive(ServerStatus.getStatus(state.getId()), current, max));
	}

	@Override
	public void sendPing(String[] servers, Callback<Integer> count){
		requestedPing.put(nextKey, count);
		sendPacket(new PacketMatchmakingPing(nextKey, servers));
		nextKey++;
	}

	@Override
	public void sendReconnectionInvitation(String name, boolean invited) {
		//sendPacket(new PacketReconnectionInvitation(name, invited));
	}

	@Override
	public void executeCommand(String command) {
		sendPacket(new PacketSimpleCommand(command));
	}
	
	@Override
	public void executeCommand(BadblockPlayer player, String command) {
		sendPacket(new PacketSimpleCommand(player.getName(), command));
	}

	@Override
	public void handle(PacketPlayerData packet) {
		if(packet.getType() == DataType.PLAYER && packet.getAction() == DataAction.SEND){
			Callback<JsonObject> callback = requestedPlayers.get(packet.getKey().toLowerCase());

			if(callback != null){
				callback.done(new JsonParser().parse(packet.getData()).getAsJsonObject(), null);
			} else {
				GameBadblockPlayer player = (GameBadblockPlayer) Bukkit.getPlayer(packet.getKey());

				if(player != null){
					player.updateData(new JsonParser().parse(packet.getData()).getAsJsonObject());
				}
			}
		} else if(packet.getType() == DataType.IP && packet.getAction() == DataAction.SEND){
			Callback<JsonObject> callback = requestedIps.get(packet.getKey().toLowerCase());

			if(callback != null){
				callback.done(new JsonParser().parse(packet.getData()).getAsJsonObject(), null);
			}
		} else if(packet.getType() == DataType.PERMISSION && packet.getAction() == DataAction.SEND){
			new PermissionManager(new JsonParser().parse(packet.getData()).getAsJsonArray());
		}
	}

	@Override
	public void handle(PacketMatchmakingPong packet){
		if(requestedPing.containsKey(packet.getId())){
			requestedPing.get(packet.getId()).done(packet.getPlayerCount(), null);
		}
	}

	@Override public void handle(PacketPlayerChat packet){}
	@Override public void handle(PacketPlayerJoin packet){}
	@Override public void handle(PacketPlayerQuit packet){}
	@Override public void handle(PacketPlayerPlace packet){}
	@Override public void handle(PacketHelloworld packet){}
	@Override public void handle(PacketMatchmakingJoin packet){}
	@Override public void handle(PacketMatchmakingKeepalive packet){}
	@Override public void handle(PacketMatchmakingPing packet){}
	@Override public void handle(PacketPlayerLogin packet){}
	@Override public void handle(PacketReconnectionInvitation packet){}

	@Override
	public void handle(PacketLadderStop packet) {
		try {
			Thread.sleep(10000L);
			Bukkit.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}

	@Override public void handle(PacketSimpleCommand packet) {
	}
}