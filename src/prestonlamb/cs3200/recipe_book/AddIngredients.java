package prestonlamb.cs3200.recipe_book;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class AddIngredients extends Activity {

	Recipe recipe = new Recipe();
	IngredientArrayAdapter adptr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_ingredients);
		// Show the Up button in the action bar.
		setupActionBar();

		recipe.addSingleIngredient("There are no ingredients yet...");
		adptr = new IngredientArrayAdapter(this, R.id.ingredient_text_view, recipe.getAllIngredients());
		ListView listView = (ListView)findViewById(R.id.ingredient_list);
		listView.setAdapter(adptr);
		
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				
			}
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				showDeletionDialog(position);
				return false;
			}
			
		});
		
	}
	
	public void showDeletionDialog(final int listItem){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.delete_ingredient_dialog_desc);
		builder.setTitle(R.string.delete_ingredient_dialog_title);
		builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				recipe.deleteSingleIngredient(listItem);
				adptr.notifyDataSetChanged();
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				Do nothing on the cancel
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}
	
	public void addIngredient(View v){
		if (recipe.getIngredientsSize() == 1){
			String theIngredient = recipe.getSingleIngredient(0);
			if(theIngredient.contains("There are no ingredients")){
				recipe.deleteSingleIngredient(0);
			}
		}
		EditText enterIngredient = (EditText) findViewById(R.id.enter_ingredient);
		String ingredient = enterIngredient.getText().toString();
		recipe.addSingleIngredient(ingredient);
		adptr.notifyDataSetChanged();
		enterIngredient.setText("");
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
