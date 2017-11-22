package fr.badblock.game.core18R3.players.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import fr.badblock.gameapi.utils.general.Callback;

public class MojangAPI
{
	
	public static void getSkinPropertyObject(String name, Callback<Property> callback)
	{
		List<String> output = readSURL("https://extdata.badblock-network.fr/skin.php?name=" + name);
		System.out.println("Output SKIN : " + output.size());
		if (output.size() >= 2) {
			String value = output.get(0);
			String signature = output.get(1);
			callback.done(createProperty("textures", value, signature), null);
		}else callback.done(createProperty("textures", "", ""), null);
	}

	private static List<String> readSURL(String url)
	{
		List<String> result = new ArrayList<>();
		try
		{
			HttpsURLConnection con = (HttpsURLConnection)new URL(url).openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setConnectTimeout(2000);
			con.setReadTimeout(2000);
			con.setDoOutput(true);


			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				result.add(line);
			}
			in.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public static Property createProperty(String name, String value, String signature)
	{
		try
		{
			return new Property(name, value, signature);
		}
		catch (Exception localException) {}
		return null;
	}

}
