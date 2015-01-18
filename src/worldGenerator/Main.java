package worldGenerator;

import helpers.Spline2D;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		
		Random random = new Random();
		World world = new World(random);
		//Spline2D outline = world.getCapital().getOutline();
		/*
		System.out.println("");
		for (int i=0; i<=360;i++){
			System.out.println("Value for i = " + i + " is : " + outline.getValue(i));
		}
		/**
		new ImageGenerator(world.getWorld_buff(), world.getSize());
		/**
		Spline2D spline = new Spline2D(0, 50, -0.5f);
		spline.addVertex(150, 400, 0f);
		spline.addVertex(512, 20, 0f);
		*/
		new ImageGenerator(world);
	}

}
