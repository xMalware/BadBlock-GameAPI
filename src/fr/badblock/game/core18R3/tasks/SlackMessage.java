package fr.badblock.game.core18R3.tasks;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;

public class SlackMessage
{
	private final String message;
	private final String name;
	private final String iconUrl;
	private final boolean useMarkdown;
	private final String webhookUrl = "https://hooks.slack.com/services/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";
	
	public SlackMessage(String message, String name, String iconUrl, boolean useMarkdown)
	{
		this.message = message;
		this.name = name;
		this.useMarkdown = useMarkdown;
		this.iconUrl = iconUrl;
	}
	
	public void run()
	{
		new Thread() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				JSONObject json = new JSONObject();
				json.put("text", message);
				json.put("username", name);
				json.put("link_names", 1);
				json.put("parse", "full");
				json.put("icon_url", iconUrl);
				json.put("mrkdwn", Boolean.valueOf(useMarkdown));
				String jsonStr = "payload=" + json.toJSONString();
				try
				{
					HttpURLConnection webhookConnection = (HttpURLConnection)new URL(webhookUrl).openConnection();
					webhookConnection.setRequestMethod("POST");
					webhookConnection.setDoOutput(true);
					BufferedOutputStream bufOut = new BufferedOutputStream(webhookConnection.getOutputStream());Throwable localThrowable2 = null;
					try
					{
						bufOut.write(jsonStr.getBytes(StandardCharsets.UTF_8));
						bufOut.flush();
						bufOut.close();
					}
					catch (Throwable localThrowable1)
					{
						localThrowable2 = localThrowable1;throw localThrowable1;
					}
					finally
					{
						if (bufOut != null) {
							if (localThrowable2 != null) {
								try
								{
									bufOut.close();
								}
								catch (Throwable x2)
								{
									localThrowable2.addSuppressed(x2);
								}
							} else {
								bufOut.close();
							}
						}
					}
					webhookConnection.getResponseCode();
					webhookConnection.disconnect();
					webhookConnection = null;
				}
				catch (Exception ignored) {
					//ignored.printStackTrace();
				}
			}
		}.start();
	}
}
