package fr.badblock.game.core18R3.watchers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;

import fr.badblock.gameapi.packets.watchers.WatcherHorse;

public class GameWatcherHorse extends GameWatcherAgeable implements WatcherHorse {
	private List<HorseFlag> flags = new ArrayList<>();
	
	public GameWatcherHorse(Class<? extends Entity> clazz){
		super(clazz);
	}
	
	@Override
	public WatcherHorse addHorseFlag(HorseFlag flag) {
		if(!flags.contains(flag))
			flags.add(flag);
		
		setFlag();
		return this;
	}

	@Override
	public WatcherHorse removeFlag(HorseFlag flag) {
		if(flags.contains(flag))
			flags.remove(flag);
		
		setFlag();
		return this;
	}
	
	public void setFlag(){
		int result = 0;
		
		for(HorseFlag flag : flags){
			result |= flag.getValue();
		}
		
		set(MetadataIndex.HORSE_FLAGS, result);
	}

	@Override
	public WatcherHorse setArmor(HorseArmor armor) {
		set(MetadataIndex.HORSE_ARMOR, armor.getValue());
		return this;
	}

	@Override
	public WatcherHorse setType(Variant horse) {
		set(MetadataIndex.HORSE_TYPE, (byte) horse.ordinal());
		return this;
	}

	@Override
	public WatcherHorse setColor(Color color, Style style) {
		set(MetadataIndex.HORSE_STYLE, color.ordinal() & 0xFF | style.ordinal() << 8);
		return this;
	}
}
