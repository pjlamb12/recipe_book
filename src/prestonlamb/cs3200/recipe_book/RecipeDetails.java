package prestonlamb.cs3200.recipe_book;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class RecipeDetails extends Activity {

	Recipe recipe;
	List<Recipe> recipeList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_details);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent = getIntent();
		recipe = intent.getParcelableExtra(Home.RECIPE_INTENT);
		recipeList = intent.getParcelableArrayListExtra(Home.RECIPE_LIST_INTENT);
		
		TextView recipeName = (TextView)findViewById(R.id.recipe_name_view);
		recipeName.setText(recipe.getRecipeName());
		
		StringBuilder ingredientParagraph = new StringBuilder();
		for(String ingredient : recipe.getAllIngredients()){
			ingredientParagraph.append(ingredient);
			ingredientParagraph.append("\n");
		}
		TextView allIngredients = (TextView)findViewById(R.id.all_ingredients);
		allIngredients.setText(ingredientParagraph);
		
		StringBuilder directionParagraph = new StringBuilder();
		for(String direction : recipe.getAllDirections()){
			directionParagraph.append(direction);
			directionParagraph.append("\n");
		}
		TextView allDirections = (TextView)findViewById(R.id.all_directions);
		allDirections.setText(directionParagraph);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recipe_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = NavUtils.getParentActivityIntent(this);
			intent.putParcelableArrayListExtra(Home.RECIPE_LIST_INTENT, (ArrayList<? extends Parcelable>) recipeList);
			NavUtils.navigateUpTo(this, intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
