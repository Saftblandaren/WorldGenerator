package worldGenerator;

import helpers.Spline2D;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		/**
		Random random = new Random();
		World world = new World(random);
		new ImageGenerator(world.getWorld_buff(), world.getSize());
		*/
		Spline2D spline = new Spline2D(0,1,0f);
		spline.addVertex(1, 1, 1f);
	}

}
