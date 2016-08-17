package fr.badblock.game.core18R3.anticheat.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.game.core18R3.anticheat.ACPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.selections.Vector3f;

public class MoveUtils {
	public static void move(BadblockPlayer player, Vector3f position){
		ACPlayer ac = player.inGameData(ACPlayer.class);

		if(ac.lastMove == null){
			ac.lastMove = position;
		} else {
			Block block = player.getWorld().getBlockAt((int) position.getX(), (int) position.getY(), (int) position.getZ());

			if((block.isLiquid() && !ac.wasWater) || (!block.isLiquid() && ac.wasWater)){
				ac.wasWater = !ac.wasWater;
				ac.lastCount += 2;
			} else if(block.getType() == Material.AIR && ac.lastCount > 0){
				ac.lastCount--;
			}
			
			if(ac.lastCount >= 20){
				ac.kick(player, "Good bye Jésus !");
			}
		}

		ac.lastMove = position;
	}
}
