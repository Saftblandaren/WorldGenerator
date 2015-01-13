package worldGenerator;

import helpers.Spline2D;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageGenerator {

	public ImageGenerator(World world) {
		
		int size = world.getSize();
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		
		File f = null;
		
		int a = 255;
		int r=0;
		int g=0;
		int b=0;
		
		int p = (a<<24) | (r<<16) | (g<<8) | b;
		
		for(int i = 1; i < size-1; i++){
			for(int j= 1;j < size-1; j++){
				if(world.getValue(i, j) == 1)
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
