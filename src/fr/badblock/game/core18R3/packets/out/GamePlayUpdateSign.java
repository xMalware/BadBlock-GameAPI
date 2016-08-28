package fr.badblock.game.core18R3.packets.out;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayUpdateSign;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EnumChatFormat;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateSign;
import net.minecraft.server.v1_8_R3.World;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true, fluent = false)
public class GamePlayUpdateSign extends GameBadblockOutPacket implements PlayUpdateSign {
	private TranslatableString linesI18n;
	private String[] lines;
	private Block block;

	public GamePlayUpdateSign(PacketPlayOutUpdateSign packet) {
		Reflector reflector = new Reflector(packet);

		try {
			World world = (World) reflector.getFieldValue("a");
			BlockPosition position = (BlockPosition) reflector.getFieldValue("b");
			block = Bukkit.getWorld(world.getWorldData().getName()).getBlockAt(position.getX(), position.getY(),
					position.getZ());

			IChatBaseComponent[] nmsLines = (IChatBaseComponent[]) reflector.getFieldValue("c");

			lines = new String[nmsLines.length];

			for (int i = 0; i < nmsLines.length; i++) {
				lines[i] = BaseComponent.toLegacyText(fromChat(nmsLines[i]));
			}
		} catch (Exception e) {
		}
	}

	@Override
	public Packet<?> buildPacket() throws Exception {
		return null;
	}

	@Override
	public Packet<?> buildPacket(BadblockPlayer player) throws Exception {
		World world = ((CraftWorld) block.getWorld()).getHandle();
		BlockPosition position = new BlockPosition(block.getX(), block.getY(), block.getZ());

		IChatBaseComponent[] nmsLines = new IChatBaseComponent[4];

		String[] lines;

		if (linesI18n == null) {
			lines = this.lines;
		} else
			lines = linesI18n.get(player);

		for (int i = 0; i < 4; i++) {
			String text = " ";

			if (lines.length > i) {
				text = lines[i];
			}

			while (text.startsWith("�f")) {
				text = text.substring(2);
			}

			nmsLines[i] = getChat(text);
			nmsLines[i].getChatModifier().setColor(EnumChatFormat.BLACK);
		}

		return new PacketPlayOutUpdateSign(world, position, nmsLines);
	}

}
