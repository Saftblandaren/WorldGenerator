package worldGenerator;

import helpers.Spline2D;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		
		Random random = new Random();
		World world = new World(random);
		new ImageGenerator(world);
		System.out.println("\nDone!");
	}

}
