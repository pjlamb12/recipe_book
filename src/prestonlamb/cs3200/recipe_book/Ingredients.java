package prestonlamb.cs3200.recipe_book;

import java.util.ArrayList;
import java.util.List;

public class Ingredients {

	List<String> ingredients = new ArrayList<String>();

	public List<String> getIngredients() {
		return ingredients;
	}
	
	public String getSingleIngredient(int index){
		return ingredients.get(index);
	}

	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}
	
	public void addSingleIngredient(String ingredient){
		ingredients.add(ingredient);
	}
	
	public void removeSingleIngredient(int index){
		ingredients.remove(index);
	}
}
