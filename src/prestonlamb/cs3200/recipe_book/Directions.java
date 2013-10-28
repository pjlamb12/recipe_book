package prestonlamb.cs3200.recipe_book;

import java.util.ArrayList;

public class Directions {
	ArrayList<String> directions;

	public ArrayList<String> getDirections() {
		return directions;
	}
	
	public String getSingleDirection(int index){
		return directions.get(index);
	}

	public void setDirections(ArrayList<String> directions) {
		this.directions = directions;
	}
	
	public void addSingleDirection(String direction){
		directions.add(direction);
	}
	
	public void removeSingleDirection(int index){
		directions.remove(index);
	}
	
}
