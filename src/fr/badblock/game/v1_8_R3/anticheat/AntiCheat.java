package fr.badblock.game.v1_8_R3.anticheat;

import fr.badblock.game.v1_8_R3.anticheat.listeners.MoveListener;
import fr.badblock.game.v1_8_R3.anticheat.listeners.MoveRelListener;

public class AntiCheat {
	public static void load(){
		new MoveListener().register();
		new MoveRelListener().register();
	}
}
