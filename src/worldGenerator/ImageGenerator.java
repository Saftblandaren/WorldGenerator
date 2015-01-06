package worldGenerator;

import helpers.Spline2D;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageGenerator {

	public ImageGenerator(Spline2D spline, int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		
		File f = null;
		
		int a = 255;
		int r=0;
		int g=0;
		int b=0;
		
		int p = (a<<24) | (r<<16) | (g<<8) | b;
		
		for(int i = 1; i < size-1; i++){
			int y = spline.getValue(i);
			y = Math.min(y,size);
			y = Math.max(y, 1);

			img.setRGB(i, y, p);
		}
		
		try{
			f= new File("world.png");
			ImageIO.write(img, "png", f);
			
		}catch(IOException e){
			System.out.println("Error: " + e);
		}
		
	}

}
