package fr.badblock.game.core18R3.gameserver;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DevAliveFactory {
	public String name;
	public boolean open;
	public int players;
	public int slots;
	public boolean openStaff;
}