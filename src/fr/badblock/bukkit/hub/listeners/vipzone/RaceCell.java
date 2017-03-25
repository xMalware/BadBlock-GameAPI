package fr.badblock.bukkit.hub.listeners.vipzone;

import org.bukkit.Location;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RaceCell {

	public Location blockLocation;
	public Location cellLocation;
	public Location aheadCellLocaton;
	public Location breakableBlockLocation;

}
