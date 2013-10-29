package prestonlamb.cs3200.recipe_book;

import java.util.ArrayList;
import java.util.List;

public class Directions {
	List<String> directions = new ArrayList<String>();

	public List<String> getDirections() {
		return directions;
	}
	
	public String getSingleDirection(int index){
		return directions.get(index);
	}

	public void setDirections(List<String> directions) {
		this.directions = directions;
	}
	
	public void addSingleDirection(String direction){
		directions.add(direction);
	}
	
	public void removeSingleDirection(int index){
		directions.remove(index);
	}
	
}
