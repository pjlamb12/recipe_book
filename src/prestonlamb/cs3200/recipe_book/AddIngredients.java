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

	Recipe recipe = new Recipe();
	IngredientArrayAdapter adptr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_ingredients);
		// Show the Up button in the action bar.
		setupActionBar();
		nameRecipeDialog();

		recipe.addSingleIngredient("There are no ingredients yet...");
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
	
	public void nameRecipeDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Name Recipe");
		final EditText editInput = new EditText(this);
		editInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		builder.setView(editInput);
		builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				recipe.setRecipeName(editInput.getText().toString());
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void editRecipeNameDialog(View v){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.name_recipe);
		final EditText editInput = new EditText(this);
		editInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		builder.setView(editInput);
		builder.setPositiveButton(R.string.continue_tag, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				recipe.setRecipeName(editInput.getText().toString());
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
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
		if(recipe.getRecipeName() == null){
			Toast.makeText(getApplicationContext(), R.string.name_required, Toast.LENGTH_LONG).show();
		} else {
			Intent intent = new Intent(getApplicationContext(), AddDirections.class);
			intent.putExtra("Recipe", (Parcelable)recipe);
			startActivity(intent);
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
