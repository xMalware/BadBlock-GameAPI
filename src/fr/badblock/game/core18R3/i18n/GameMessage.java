package fr.badblock.game.core18R3.i18n;

import java.util.Arrays;
import java.util.Random;

import fr.badblock.gameapi.utils.i18n.Message;

public class GameMessage implements Message {
	private boolean useHeader,
					useShortHeader,
					useFooter;
	
	private String[][] messages;
	
	public GameMessage(){}
	
	public GameMessage(String whenUnknow){
		this.useHeader 		= false;
		this.useShortHeader = false;
		this.useFooter		= false;
		
		verify(whenUnknow);
	}
	
	public void verify(String whenUnknow){
		if(messages == null || messages.length == 0){
			messages = new String[][]{new String[]{whenUnknow}};
		}
	}
	
	@Override
	public boolean useHeader() {
		return useHeader;
	}

	@Override
	public boolean useShortHeader() {
		return useShortHeader;
	}

	@Override
	public boolean useFooter() {
		return useFooter;
	}

	@Override
	public boolean isRandomMessage() {
		return messages.length > 1;
	}

	@Override
	public String[] getUnformattedMessage() {
		String[] message = messages[new Random().nextInt(messages.length)];
		return Arrays.copyOf(message, message.length);
	}

	@Override
	public String[][] getAllMessages() {
		return messages;
	}
}
