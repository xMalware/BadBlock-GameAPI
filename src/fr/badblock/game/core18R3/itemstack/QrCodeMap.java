package fr.badblock.game.core18R3.itemstack;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrCodeMap extends MapRenderer {
	private String value;
	
	public QrCodeMap(String value)
	{
		this.value = value;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		try {
			Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			
			hintMap.put(EncodeHintType.MARGIN, 1);
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			
			map.setCenterX(Integer.MAX_VALUE);
			map.setCenterZ(Integer.MAX_VALUE);
			
			for(int i = 0; i < canvas.getCursors().size(); i++)
				canvas.getCursors().removeCursor(canvas.getCursors().getCursor(0));
			
			canvas.setCursors(new MapCursorCollection());				
						
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(value, BarcodeFormat.QR_CODE, 128, 128, hintMap);
			int width = byteMatrix.getWidth();
 
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < width; j++) {
					if (byteMatrix.get(i, j)) {
						canvas.setPixel(i, j, MapPalette.DARK_GRAY);
					}
					else canvas.setPixel(i, j, MapPalette.WHITE);
				}
			}
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
}
