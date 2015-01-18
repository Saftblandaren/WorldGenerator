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
				BufferedImage map = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
				BufferedImage height = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
				File f_map = null;
				File f_height = null;
				
				int a = 255;
				int r=0;
				int g=0;
				int b=0;
				
				int p1 = (a<<24) | (r<<16) | (g<<8) | b;
				
				r=255;
				g=255;
				b=255;
				
				int p2 = (a<<24) | (r<<16) | (g<<8) | b;
				
				for(int x = 0; x < size; x++){
					for(int y = 0; y < size; y++){
						if(world.getValue(slotX*size+x, slotY*size+y) == 1){
							map.setRGB(x, y, p2);
						}else{
							map.setRGB(x, y, p1);
						}
						int c = world.getHeight(slotX*size+x, slotY*size+y);
						/*
						//c = world.getRandom().nextInt(150)+50;
						if( c<0 || c>255){
							System.out.println("problem with : " + slotX*size+x + " , " + slotY*size+y);
						}*/
						int p = (a<<24) | (c<<16) | (c<<8) | c;
						height.setRGB(x, y, p);
					}
				}
				
				try{
					f_map= new File("world_preview\\world_" + slotY + "_" + slotX + ".png");
					ImageIO.write(map, "png", f_map);
					f_height= new File("height_preview\\height_" + slotY + "_" + slotX + ".png");
					ImageIO.write(height, "png", f_height);
					
				}catch(IOException e){
					System.out.println("Error: " + e);
				}
				
			}
		}
		
	}

}
