package prestonlamb.cs3200.recipe_book;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class Home extends Activity {

	RecipeDbAdapter dbAdapter = null;
	public static final int RESULT_BAD = 0;
	public static final int RESULT_OK = 1;
	public static final int NAME_REQUEST = 1;
	public static final int INGREDIENTS_REQUEST = 2;
	public static final int DIRECTIONS_REQUEST = 3;
	public static final int RECIPES_REQUEST = 4;
	public static final String RECIPE_LIST_INTENT = "RecipeList";
	public static final String RECIPE_INTENT = "Recipe";
	public static final String RECIPE_ID_INTENT = "RecipeId";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}
	
	public void newRecipe(View v){
		Intent intent = new Intent(getApplicationContext(), NameRecipe.class);
		startActivityForResult(intent, NAME_REQUEST);
	}
	
	public void viewRecipes(View v){
		Intent intent = new Intent(getApplicationContext(), ViewRecipes.class);
		startActivity(intent);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){

		if(resultCode == RESULT_OK && requestCode == NAME_REQUEST){
			if(data.hasExtra(Home.RECIPE_INTENT));{
				Recipe newRecipe = data.getParcelableExtra(Home.RECIPE_INTENT);
				int recipe_id = data.getIntExtra(RECIPE_ID_INTENT, -1);
				newRecipe.setId(recipe_id);
				if(newRecipe != null){
					if(dbAdapter == null){
						dbAdapter = new RecipeDbAdapter(this);
					}
					dbAdapter.open();
					if(newRecipe.getId() == -1){
						dbAdapter.insertRecipe(newRecipe);						
					}else{
						dbAdapter.updateRecipeWhereID(newRecipe);
					}
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

	public void exportRecipes(View v){
		try {
			String destination = Environment.getExternalStorageDirectory().toString() + "/recipes.db";
			FileOutputStream fileOut = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/" + "recipes.db");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			RecipeDbAdapter dbAdapter = new RecipeDbAdapter(this);
			dbAdapter.open();
			List<Recipe> recipeList = dbAdapter.retrieveAllRecipes();
			dbAdapter.close();
			for(Recipe recipe : recipeList){
				out.writeObject(recipe);
			}
			out.close();
			fileOut.close();
			Toast.makeText(this, "File written out to " + destination, Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void importRecipes(){
		
	}
}
