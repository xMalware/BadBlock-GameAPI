package fr.badblock.game.core18R3.watchers;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Vector3f;

public enum MetadataType {
	BYTE(Byte.class), SHORT(Short.class), INT(Integer.class), FLOAT(Float.class), STRING(String.class), ITEM(
			ItemStack.class), POSITION(BlockPosition.class), VECTOR3F(Vector3f.class);

	public static MetadataType byClass(Class<?> clazz) {
		for (MetadataType type : values())
			if (type.getDataType().equals(clazz))
				return type;
		return null;
	}

	public static MetadataType byId(int id) {
		return values()[id];
	}

	private final Class<?> dataType;

	private MetadataType(Class<?> dataType) {
		this.dataType = dataType;
	}

	public Class<?> getDataType() {
		return dataType;
	}

	public int getId() {
		return ordinal();
	}
}
