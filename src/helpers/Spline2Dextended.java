package helpers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Spline2Dextended {
	
	private Vector3f start;
	private List<Spline2DSegment> segments;
	
	public Spline2Dextended(Vector3f start) {
		this.start = start;
		segments = new ArrayList<Spline2DSegment>();
	}
	
	public void addPoint(Vector3f point){
		if (segments.isEmpty()){
			segments.add(new Spline2DSegment(start, point));
		}else{
			Vector3f last = segments.get(segments.size() - 1).getEnd();
			segments.add(new Spline2DSegment(last, point));
		}
		
	}
	
	public void addPoint(Vector2f point){
		float dx;
		float dy;
		if (segments.isEmpty()){
			dx = point.x - start.x;
			dy = point.y - start.y;
		}else{
			Vector3f last = segments.get(segments.size() - 1).getEnd();
			dx = point.x - last.x;
			dy = point.y - last.y;
		}
		
		addPoint(new Vector3f(point.x, point.y, dy/dx));
	}
	
	public void closePolar(){
		addPoint(new Vector3f(360.0f, start.y, start.z));
	}
	
	public Float getValue(float x){
		for(Spline2DSegment segment: segments){
			Float value = segment.getValue(x);
			if(value == null){
				continue;
			}else{
				return value;
			}
		}
		
		return null;
	}
	

}
