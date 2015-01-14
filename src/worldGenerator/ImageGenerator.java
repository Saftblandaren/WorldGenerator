package worldGenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageGenerator {

	public ImageGenerator(World world) {
		
		int size = world.getSLOT_SIZE();
		for(int slotX = 0; slotX < world.getSlots(); slotX++){
			for(int slotY = 0; slotY < world.getSlots(); slotY++){
				BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
				File f = null;
				
				int a = 255;
				int r=0;
				int g=0;
				int b=0;
				
				int p = (a<<24) | (r<<16) | (g<<8) | b;
				
				for(int x = 0; x < size; x++){
					for(int y = 0; y < size; y++){
						if(world.getValue(slotX*x, slotY*y) == 1){
							img.setRGB(x, y, p);
						}
					}
				}
				
				try{
					f= new File("world_" + slotX + "_" + slotY + ".png");
					ImageIO.write(img, "png", f);
					
				}catch(IOException e){
					System.out.println("Error: " + e);
				}
				
			}
		}
		
	}

}
