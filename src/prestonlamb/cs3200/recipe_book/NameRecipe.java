package prestonlamb.cs3200.recipe_book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class NameRecipe extends Activity {

	Recipe recipe = new Recipe();
	int recipe_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_name_recipe);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent = getIntent();
		if(intent.hasExtra(Home.RECIPE_INTENT)){
			recipe = intent.getParcelableExtra(Home.RECIPE_INTENT);
			recipe_id = intent.getIntExtra(Home.RECIPE_ID_INTENT, -1);
			recipe.setId(recipe_id);
			EditText nameInput = (EditText)findViewById(R.id.enter_name);
			nameInput.setText(recipe.getRecipeName());
			String category = recipe.getCategory();
			RadioButton button;
			if(category.equals("Appetizer")){
				button = (RadioButton)findViewById(R.id.appetizer_radio);
				button.setChecked(true);
			} else if (category.equals("Entree")){
				button = (RadioButton)findViewById(R.id.entree_radio);
				button.setChecked(true);				
			} else if (category.equals("Soup")){
				button = (RadioButton)findViewById(R.id.soup_radio);
				button.setChecked(true);
			} else if (category.equals("Dessert")){
				button = (RadioButton)findViewById(R.id.dessert_radio);
				button.setChecked(true);
			}
		}
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
			intent.putExtra(Home.RECIPE_INTENT, (Parcelable)recipe);
			intent.putExtra(Home.RECIPE_ID_INTENT, recipe.getId());
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

		if(resultCode == RESULT_OK && requestCode == Home.INGREDIENTS_REQUEST){
			setResult(RESULT_OK);
		} else {
			setResult(RESULT_CANCELED);			
		}
		finish();
	}


}
