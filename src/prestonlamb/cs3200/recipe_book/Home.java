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
	RecipeDbAdapter dbAdapter = null;
	public static final int RESULT_BAD = 0;
	public static final int RESULT_OK = 1;
	public static final int NAME_REQUEST = 1;
	public static final int INGREDIENTS_REQUEST = 2;
	public static final int DIRECTIONS_REQUEST = 3;
	public static final int RECIPES_REQUEST = 4;
	public static final String RECIPE_LIST_INTENT = "RecipeList";
	public static final String RECIPE_INTENT = "Recipe";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		if(dbAdapter == null){
			dbAdapter = new RecipeDbAdapter(this);
		}

//		dbAdapter.open();
//		int numRecords = dbAdapter.numberRecipesInDb();
//		if(numRecords > 1){
//			recipeList = dbAdapter.retrieveAllRecipes();				
//		}
//		dbAdapter.close();

		Intent intent = getIntent();
		if(intent.hasExtra(Home.RECIPE_LIST_INTENT)){
			recipeList = intent.getParcelableArrayListExtra(Home.RECIPE_LIST_INTENT);
		}
		
	}
	
	public void newRecipe(View v){
		Intent intent = new Intent(getApplicationContext(), NameRecipe.class);
		startActivityForResult(intent, NAME_REQUEST);
	}
	
	public void viewRecipes(View v){
		Intent intent = new Intent(getApplicationContext(), ViewRecipes.class);
		intent.putParcelableArrayListExtra(Home.RECIPE_LIST_INTENT, (ArrayList<? extends Parcelable>) recipeList);
		startActivity(intent);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){

		if(resultCode == RESULT_OK && requestCode == NAME_REQUEST){
			if(data.hasExtra("Recipe"));{
				Recipe newRecipe = data.getParcelableExtra(Home.RECIPE_INTENT);
				if(newRecipe != null){
					recipeList.add(newRecipe);
					dbAdapter.open();
					dbAdapter.insertRecipe(newRecipe);
					dbAdapter.close();
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
