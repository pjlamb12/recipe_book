package prestonlamb.cs3200.recipe_book;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable, Serializable {

	private static final long serialVersionUID = 1L;
	int id;
	String recipeName;
	String category;
	List<String> ingredients;
	List<String> directions;
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Recipe(){
		ingredients = new ArrayList<String>();
		directions = new ArrayList<String>();
		id = -1;
	}
	
	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	@Override
	public int describeContents() {
		return 1;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(recipeName);
		dest.writeString(category);
		dest.writeList(ingredients);
		dest.writeList(directions);
	}
	
	public Recipe(Parcel parcel){
		recipeName = parcel.readString();
		category = parcel.readString();
		ingredients = new ArrayList<String>();
		parcel.readList(ingredients, null);
		directions = new ArrayList<String>();
		parcel.readList(directions, null);
	}
	
	public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {

		@Override
		public Recipe createFromParcel(Parcel source) {
			return new Recipe(source);
		}

		@Override
		public Recipe[] newArray(int size) {
			return new Recipe[size];
		}
	};

}
