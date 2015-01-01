package worldGenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageGenerator {

	public ImageGenerator(HashMap<String, Integer> pixbuff, int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		
		File f = null;
		
		int a = 255;
		int r;
		int g;
		int b;
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				String key = String.valueOf(i) + ":" + String.valueOf(j);
				if(pixbuff.get(key)!=null){
					r=0;
					g=0;
					b=0;
				}else{
					r=255;
					g=255;
					b=255;
				}
				
				int p = (a<<24) | (r<<16) | (g<<8) | b;
				
				img.setRGB(i, j, p);
			}
		}
		
		try{
			f= new File("world.png");
			ImageIO.write(img, "png", f);
			
		}catch(IOException e){
			System.out.println("Error: " + e);
		}
		
	}

}
