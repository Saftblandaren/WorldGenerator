package worldGenerator;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		
		Random random = new Random();
		World world = new World(random);
		/**
		new ImageGenerator(world.getWorld_buff(), world.getSize());
		
		Spline2D spline = new Spline2D(0, 50, -0.5f);
		spline.addVertex(150, 400, 0f);
		spline.addVertex(512, 20, 0f);
		*/
		new ImageGenerator(world);
	}

}
