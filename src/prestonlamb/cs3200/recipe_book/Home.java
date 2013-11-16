package prestonlamb.cs3200.recipe_book;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.View;

public class Home extends Activity {

	List<Recipe> recipeList = new ArrayList<Recipe>();
	public static final int RESULT_BAD = 0;
	public static final int RESULT_OK = 1;
	public static final int NAME_REQUEST = 1;
	public static final int INGREDIENTS_REQUEST = 2;
	public static final int DIRECTIONS_REQUEST = 3;
	public static final int RECIPES_REQUEST = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Intent intent = getIntent();
		if(intent.hasExtra("RecipeList")){
			recipeList = intent.getParcelableArrayListExtra("RecipeList");
		}
		
	}
	
	public void newRecipe(View v){
		Intent intent = new Intent(getApplicationContext(), NameRecipe.class);
		startActivityForResult(intent, NAME_REQUEST);
	}
	
	public void viewRecipes(View v){
		Intent intent = new Intent(getApplicationContext(), ViewRecipes.class);
		intent.putParcelableArrayListExtra("RecipeList", (ArrayList<? extends Parcelable>) recipeList);
		startActivity(intent);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){

		if(resultCode == RESULT_OK && requestCode == NAME_REQUEST){
			if(data.hasExtra("Recipe"));{
				Recipe newRecipe = data.getParcelableExtra("Recipe");
				if(newRecipe != null){
					recipeList.add(newRecipe);			
				}
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
