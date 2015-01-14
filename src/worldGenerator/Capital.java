package worldGenerator;

public class Capital extends Camp {

	public Capital(World world, int slotX, int slotY) {
		super(world, slotX, slotY);
		
		// Make Radius larger for capital
		sizeRadius += (int) (world.getSLOT_SIZE()*0.1f + random.nextInt((int) (world.getSLOT_SIZE()*0.1f)));
		
		// And following has to be remade
		setPosition();
		setOutline();
	}

}
