package fr.badblock.game.v1_8_R3.internalutils;

public class Base64Url {
	private static final String base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
	
	public static long decode(String decode){
		long pow 	= 1;
		long result = 0;
		
		for(int i=decode.length()-1;i>0;i--){
			result += base64.indexOf(decode.charAt(i)) * pow;
			pow *= 64;
		}
		
		if(decode.charAt(0) == '-')
			result = -result;
		
		
		return result;
	}
	
	public static String encode(long from){
		boolean negative = from < 0;
		if(negative) from = -from;
		
		String result = "";
		
		do {
			long div = from / 64;
			long res = from - 64 * div;
			
			from = div;
			
			result = base64.charAt((int) res) + result;
		} while(from != 0);
		
		return (negative ? "-" : "+") + result;
	}
}