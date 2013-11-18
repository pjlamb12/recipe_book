package prestonlamb.cs3200.recipe_book;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.text.InputType;

public class AddIngredients extends Activity {

	Recipe recipe;
	int recipe_id;
	IngredientArrayAdapter adptr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_ingredients);
		// Show the Up button in the action bar.
		setupActionBar();

		Intent intent = getIntent();
		recipe = intent.getParcelableExtra(Home.RECIPE_INTENT);
		recipe_id = intent.getIntExtra(Home.RECIPE_ID_INTENT, -1);
		recipe.setId(recipe_id);
		
		if(recipe.getIngredientsSize() == 0){
			recipe.addSingleIngredient("There are no ingredients yet...");			
		}
		adptr = new IngredientArrayAdapter(this, R.id.ingredient_text_view, recipe.getAllIngredients());
		ListView listView = (ListView)findViewById(R.id.ingredient_list);
		listView.setAdapter(adptr);
		
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showEditIngredientDialog(position);
			}
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				showDeletionDialog(position);
				return false;
			}
			
		});
		
	}
	
	public void showEditIngredientDialog(final int listItem){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.edit_ingredient);
		final EditText editInput = new EditText(this);
		editInput.setText(recipe.getSingleIngredient(listItem));
		editInput.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(editInput);
		builder.setPositiveButton(R.string.Update, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String editedIngredient = editInput.getText().toString();
				recipe.deleteSingleIngredient(listItem);
				recipe.addSingleIngredient(editedIngredient);
				adptr.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				Do nothing on the cancel
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
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
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				Do nothing on the cancel
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}
	
	public void addIngredient(View v){
		EditText enterIngredient = (EditText) findViewById(R.id.enter_ingredient);
		String ingredient = enterIngredient.getText().toString();
		if(ingredient.length() != 0){			
			if (recipe.getIngredientsSize() == 1){
				String theIngredient = recipe.getSingleIngredient(0);
				if(theIngredient.contains("There are no ingredients")){
					recipe.deleteSingleIngredient(0);
				}
			}
			recipe.addSingleIngredient(ingredient);
			adptr.notifyDataSetChanged();
			enterIngredient.setText("");
		} else {
			Toast.makeText(getApplicationContext(), R.string.ingredient_required, Toast.LENGTH_LONG).show();
		}
	}

	
	public void nextDirections(View v){
		Intent intent = new Intent(getApplicationContext(), AddDirections.class);
		intent.putExtra(Home.RECIPE_INTENT, (Parcelable)recipe);
		intent.putExtra(Home.RECIPE_ID_INTENT, recipe.getId());
		startActivityForResult(intent, Home.DIRECTIONS_REQUEST);
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
			Intent intent = NavUtils.getParentActivityIntent(this);
			intent.putExtra(Home.RECIPE_INTENT, (Parcelable)recipe);
			NavUtils.navigateUpTo(this, intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){

		if(resultCode == Home.RESULT_OK && requestCode == Home.DIRECTIONS_REQUEST){
			setResult(Home.RESULT_OK, data);
			finish();
		}
	}


}
