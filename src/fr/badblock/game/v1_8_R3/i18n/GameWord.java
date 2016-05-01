package fr.badblock.game.v1_8_R3.i18n;

import fr.badblock.gameapi.utils.i18n.Word;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor
public class GameWord implements Word {
	private String pluralUndefined   	 = "";
	private String pluralDefined   	 	 = "";
	private String pluralSimple   	 	 = "";

	private String singularUndefined   	 = "";
	private String singularDefined   	 = "";
	private String singularSimple   	 = "";

	@Override
	public String get(boolean plural, WordDeterminant determinant) {
		switch(determinant){
			case DEFINED: return plural ? pluralDefined : singularDefined;
			case UNDEFINED: return plural ? pluralUndefined : singularUndefined;
			case SIMPLE: return plural ? pluralSimple : singularSimple;
			default: return "";
		}
	}

}
