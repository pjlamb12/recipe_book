package prestonlamb.cs3200.recipe_book;

import java.util.ArrayList;

public class Ingredients {

	ArrayList<String> ingredients = new ArrayList<String>();

	public ArrayList<String> getIngredients() {
		return ingredients;
	}
	
	public String getSingleIngredient(int index){
		return ingredients.get(index);
	}

	public void setIngredients(ArrayList<String> ingredients) {
		this.ingredients = ingredients;
	}
	
	public void addSingleIngredient(String ingredient){
		ingredients.add(ingredient);
	}
	
	public void removeSingleIngredient(int index){
		ingredients.remove(index);
	}
}
