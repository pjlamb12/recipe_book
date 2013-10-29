package prestonlamb.cs3200.recipe_book;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

	String recipeName;
	List<String> ingredients = new ArrayList<String>();
	List<String> directions = new ArrayList<String>();
	
	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public List<String> getAllIngredients() {
		return ingredients;
	}
	
	public String getSingleIngredient(int index){
		return ingredients.get(index);
	}

	public void setAllIngredients(List<String> ingredients1) {
		this.ingredients = ingredients1;
	}
	
	public void addSingleIngredient(String ingredient){
		ingredients.add(ingredient);
	}
	
	public void deleteSingleIngredient(int index){
		ingredients.remove(index);
	}
	
	public void deleteAllIngredients(){
		ingredients.clear();
	}
	
	public int getIngredientsSize(){
		return ingredients.size();
	}

	public List<String> getAllDirections() {
		return directions;
	}

	public String getSingleDirection(int index){
		return directions.get(index);
	}

	public void setDirections(List<String> directions1) {
		this.directions = directions1;
	}
	
	public void addSingleDirection(String direction){
		directions.add(direction);
	}
	
	public void deleteSingleDirection(int index){
		directions.remove(index);
	}
	
	public void deleteAllDirections(){
		directions.clear();
	}
	
	public int getDirectionsSize(){
		return directions.size();
	}

}
