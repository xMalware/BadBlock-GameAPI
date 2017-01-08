package fr.badblock.game.core18R3.players.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class MojangAPI
{

	public static String getUUID(String name)
			throws MojangAPI.SkinRequestException
	{
		String output = readURL("https://api.mojang.com/users/profiles/minecraft/" + name);
		if (output.isEmpty()) {
			throw new SkinRequestException(Locale.NOT_PREMIUM);
		}
		if (output.contains("\"error\"")) {
			throw new SkinRequestException(Locale.RATE_LIMITED);
		}
		return output.substring(7, 39);
	}

	public static Object getSkinProperty(String name, String uuid)
			throws MojangAPI.SkinRequestException
	{
		String sigbeg = "\"signature\":\"";
		String mid = "\",\"name\":\"textures\",\"value\":\"";
		String valend = "\"}]";

		String signature = "";
		String value = "";
		String output = readSURL("https://badblock.fr/skin.php?name=" + name);
		if (output.isEmpty() || output.length() < 2)
		{
			output = readURL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
		}else{
			String o = "";
			for (char c : output.toCharArray())
				if (Character.isDigit(c) || c == '_' || c == '-' || Character.isLetter(c))
					o += c;
			if (!o.isEmpty()) {
				output = readURL("https://api.mojang.com/users/profiles/minecraft/" + o);
				output = output.substring(7, 39);
				output = readURL("https://sessionserver.mojang.com/session/minecraft/profile/" + output + "?unsigned=false");
			}
		}
		value = getStringBetween(output, mid, valend);
		signature = getStringBetween(output, sigbeg, mid);
		return createProperty("textures", value, signature);
	}

	private static String readURL(String url)
	{
		try
		{
			HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "SkinsRestorer");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.setDoOutput(true);


			StringBuilder output = new StringBuilder();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				output.append(line);
			}
			in.close();

			return output.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String readSURL(String url)
	{
		try
		{
			HttpsURLConnection con = (HttpsURLConnection)new URL(url).openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.setDoOutput(true);


			StringBuilder output = new StringBuilder();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				output.append(line);
			}
			in.close();

			return output.toString();
		}
		catch (Exception e) {}
		return "";
	}

	public static String getStringBetween(String base, String begin, String end)
	{
		try
		{
			Pattern patbeg = Pattern.compile(Pattern.quote(begin));
			Pattern patend = Pattern.compile(Pattern.quote(end));

			int resbeg = 0;
			int resend = base.length() - 1;

			Matcher matbeg = patbeg.matcher(base);
			while (matbeg.find()) {
				resbeg = matbeg.end();
			}
			Matcher matend = patend.matcher(base);
			while (matend.find()) {
				resend = matend.start();
			}
			return base.substring(resbeg, resend);
		}
		catch (Exception e) {}
		return base;
	}

	public static class SkinRequestException
	extends Exception
	{
		private static final long serialVersionUID = 5969055162529998032L;
		private String reason;

		public SkinRequestException(String reason)
		{
			this.reason = reason;
		}

		public String getReason()
		{
			return this.reason;
		}
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
