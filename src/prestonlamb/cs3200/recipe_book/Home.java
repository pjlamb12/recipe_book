package prestonlamb.cs3200.recipe_book;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class Home extends Activity {

	List<Recipe> recipeList = new ArrayList<Recipe>();
	public static final int RESULT_BAD = 0;
	public static final int RESULT_OK = 1;
	public static final int NAME_REQUEST = 1;
	public static final int INGREDIENTS_REQUEST = 2;
	public static final int DIRECTIONS_REQUEST = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
	}
	
	public void newRecipe(View v){
		Intent intent = new Intent(getApplicationContext(), NameRecipe.class);
		startActivityForResult(intent, 1);
	}
	
	public void viewRecipes(View v){
		Intent intent = new Intent(getApplicationContext(), ViewRecipes.class);
		intent.putParcelableArrayListExtra("RecipeList", (ArrayList<? extends Parcelable>) recipeList);
		startActivity(intent);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){

		if(resultCode == RESULT_OK && requestCode == NAME_REQUEST){
			if(data.hasExtra("Recipe"));{
				Toast.makeText(getApplicationContext(), "In the result", Toast.LENGTH_LONG).show();
				Recipe newRecipe = new Recipe();
				newRecipe = data.getParcelableExtra("Recipe");
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
