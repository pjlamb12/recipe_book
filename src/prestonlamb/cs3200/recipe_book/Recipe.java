package prestonlamb.cs3200.recipe_book;

import java.util.ArrayList;

public class Recipe {

	Ingredients ingredients;
	Directions directions;
	
	public String getIngredient(int index){	
		return ingredients.getSingleIngredient(index);
	}
	
	public String getDirection(int index){
		return directions.getSingleDirection(index);
	}
	
	public String getIngredients(){
		StringBuilder ingredientList = new StringBuilder();
		ArrayList<String> theIngredients = ingredients.getIngredients();
		
		for(String ingredient : theIngredients){
			ingredientList.append(ingredient);
			ingredientList.append("\n");
		}
		
		return ingredientList.toString();
	}
	
	public String getDirections(){
		StringBuilder directionList = new StringBuilder();
		ArrayList<String> theDirections = directions.getDirections();
		
		for(String direction : theDirections){
			directionList.append(direction);
			directionList.append("\n");
		}
		
		return directionList.toString();
	}
	
	public void addIngredient(String ingredient){
		ingredients.addSingleIngredient(ingredient);
	}
	
	public void setIngredients(Ingredients ingredients){
		this.ingredients = ingredients;
	}
	
	public void deleteIngredient(int index){
		ingredients.removeSingleIngredient(index);
	}
	
	public void addDirection(String direction){
		directions.addSingleDirection(direction);
	}
	
	public void setDirections(Directions directions){
		this.directions = directions;
	}
	
	public void deleteDirection(int index){
		directions.removeSingleDirection(index);
	}
}
