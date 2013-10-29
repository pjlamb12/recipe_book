package prestonlamb.cs3200.recipe_book;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class AddIngredients extends Activity {

	Ingredients ingredients = new Ingredients();
	IngredientArrayAdapter adptr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_ingredients);
		// Show the Up button in the action bar.
		setupActionBar();
		ingredients.addSingleIngredient("There are no ingredients yet...");
		
		adptr = new IngredientArrayAdapter(this, R.id.ingredient_text_view, ingredients);
		ListView list = (ListView)findViewById(R.id.ingredient_list);
		list.setAdapter(adptr);
	}
	
	public void addIngredient(View v){
		if (ingredients.getIngredients().size() == 1){
			String theIngredient = ingredients.getSingleIngredient(0);
			if(theIngredient.contains("There are no ingredients")){
				ingredients.removeSingleIngredient(0);
			}
		}
		EditText enterIngredient = (EditText) findViewById(R.id.enter_ingredient);
		String ingredient = enterIngredient.getText().toString();
		System.out.println(ingredient);
		ingredients.addSingleIngredient(ingredient);
		adptr.notifyDataSetChanged();
		
		Toast.makeText(getApplicationContext(), "add ingredient", Toast.LENGTH_LONG).show();
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
		getMenuInflater().inflate(R.menu.add_ingredients, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
