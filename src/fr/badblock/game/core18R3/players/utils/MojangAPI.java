package fr.badblock.game.core18R3.players.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MojangAPI
{

	public static Object getSkinProperty(String name)
	{
		List<String> output = readSURL("https://extdata.badblock-network.fr/skin.php?name=" + name);
		if (output.size() >= 2) {
			String value = output.get(0);
			String signature = output.get(1);
			return createProperty("textures", value, signature);
		}else return createProperty("textures", "", "");
	}

	private static List<String> readSURL(String url)
	{
		List<String> result = new ArrayList<>();
		try
		{
			HttpsURLConnection con = (HttpsURLConnection)new URL(url).openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.setDoOutput(true);


			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				result.add(line);
			}
			in.close();

		}
		catch (Exception e) {}
		return result;
	}
	
	public static Object createProperty(String name, String value, String signature)
	{
		try
		{
			return ReflectionUtil.invokeConstructor(Class.forName("com.mojang.authlib.properties.Property"), 
					new Class[] { String.class, String.class, String.class }, new Object[] { name, value, signature });
		}
		catch (Exception localException) {}
		return null;
	}

}
