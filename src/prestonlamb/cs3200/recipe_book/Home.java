package prestonlamb.cs3200.recipe_book;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class Home extends Activity {

	List<Recipe> recipeList = new ArrayList<Recipe>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		Intent intent = getIntent();
		Recipe newRecipe = new Recipe();
		newRecipe = intent.getParcelableExtra("Recipe");
		if(newRecipe != null){
			recipeList.add(newRecipe);			
		}
	}
	
	public void newRecipe(View v){
		Intent intent = new Intent(getApplicationContext(), NameRecipe.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
