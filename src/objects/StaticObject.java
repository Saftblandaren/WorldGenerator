package objects;

public abstract class StaticObject {
	private int x;
	private int y;
	private int rotZ;
	private float scale;
	
	public StaticObject(int x, int y, int rotZ, float scale) {
		this.x = x;
		this.y = y;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getRotZ() {
		return rotZ;
	}

	public float getScale() {
		return scale;
	}
	
	
	

}
