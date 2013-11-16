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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ViewRecipes extends Activity {

	List<Recipe> recipeList;
	public static final int DETAIL_REQUEST = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recipes);
		// Show the Up button in the action bar.
		setupActionBar();
		ListView listView = (ListView)findViewById(R.id.recipe_list);
		recipeList = getIntent().getParcelableArrayListExtra("RecipeList");
		List<String> recipeNames = new ArrayList<String>();
		for(Recipe recipe : recipeList){
			recipeNames.add(recipe.getRecipeName());
		}
		RecipeListArrayAdapter adptr = new RecipeListArrayAdapter(this, R.layout.recipe_list_layout, recipeNames);
		listView.setAdapter(adptr);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long itemId) {
				Recipe selectedRecipe = recipeList.get(position);
				Intent intent = new Intent(getApplicationContext(), RecipeDetails.class);
				intent.putExtra("Recipe", (Parcelable)selectedRecipe);
				intent.putParcelableArrayListExtra("RecipeList", (ArrayList<? extends Parcelable>) recipeList);
				startActivityForResult(intent, 1);
			}
			
		});
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
		getMenuInflater().inflate(R.menu.view_recipes, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = NavUtils.getParentActivityIntent(this);
			intent.putParcelableArrayListExtra("RecipeList", (ArrayList<? extends Parcelable>) recipeList);
			NavUtils.navigateUpTo(this, intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){

		if(resultCode == Home.RESULT_OK && requestCode == DETAIL_REQUEST){
			if(data.hasExtra("RecipeList"));{
				recipeList = data.getParcelableExtra("RecipeList");
			}
		}
	}


}
