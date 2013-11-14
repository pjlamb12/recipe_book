package prestonlamb.cs3200.recipe_book;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class NameRecipe extends Activity {

	Recipe recipe = new Recipe();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name_recipe);
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	@SuppressWarnings("unused")
	public void nextIngredients(View v){
		EditText nameInput = (EditText)findViewById(R.id.enter_name);
		String name = nameInput.getText().toString();
		if(name != null || name != ""){
			recipe.setRecipeName(name);
			RadioGroup categoryGroup = (RadioGroup) findViewById(R.id.category_group);
			int selectedRadio = categoryGroup.getCheckedRadioButtonId();
			switch(selectedRadio){
				case R.id.appetizer_radio:
					recipe.setCategory("Appetizer");
					break;
				case R.id.entree_radio:
					recipe.setCategory("Entree");
					break;
				case R.id.soup_radio:
					recipe.setCategory("Soup");
					break;
				case R.id.dessert_radio:
					recipe.setCategory("Dessert");
					break;
				default:
					recipe.setCategory("");
					break;
			}
			
			Intent intent = new Intent(getApplicationContext(), AddIngredients.class);
			intent.putExtra("Recipe", (Parcelable)recipe);
			startActivityForResult(intent, Home.INGREDIENTS_REQUEST);
		} else {
			Toast.makeText(getApplicationContext(), R.string.name_required, Toast.LENGTH_LONG).show();			
		}
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
		getMenuInflater().inflate(R.menu.name_recipe, menu);
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
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){

		if(resultCode == Home.RESULT_OK && requestCode == Home.INGREDIENTS_REQUEST){
			setResult(Home.RESULT_OK, data);
			finish();
		}
	}


}
