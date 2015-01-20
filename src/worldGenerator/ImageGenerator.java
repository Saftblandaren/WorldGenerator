package worldGenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageGenerator {

	public ImageGenerator(World world) {
		
		int size = world.getSLOT_SIZE();
		//world.getSlots()
		for(int slotX = 0; slotX < world.getSlots(); slotX++){
			for(int slotY = 0; slotY < world.getSlots(); slotY++){
				System.out.println("Generating slot: " + slotX + " ; " + slotY);
				BufferedImage map = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
				BufferedImage height = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
				File f_map = null;
				File f_height = null;
				
				for(int x = 0; x < size; x++){
					for(int y = 0; y < size; y++){
						int p = world.getValue(slotX*size+x, slotY*size+y);
						map.setRGB(x, y, p);
						int ch = world.getHeight(slotX*size+x, slotY*size+y);
						/*
						//c = world.getRandom().nextInt(150)+50;
						if( c<0 || c>255){
							System.out.println("problem with : " + slotX*size+x + " , " + slotY*size+y);
						}*/
						p = (255<<24) | (ch<<16) | (ch<<8) | ch;
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
