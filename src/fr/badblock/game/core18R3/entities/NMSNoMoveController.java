package fr.badblock.game.core18R3.entities;

import net.minecraft.server.v1_8_R3.ControllerMove;
import net.minecraft.server.v1_8_R3.EntityInsentient;

public class NMSNoMoveController extends ControllerMove {
	public NMSNoMoveController(EntityInsentient entity) {
		super(entity);
	}
	
	@Override public void c(){}
}
