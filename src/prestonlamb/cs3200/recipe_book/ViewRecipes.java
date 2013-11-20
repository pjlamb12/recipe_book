package prestonlamb.cs3200.recipe_book;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ViewRecipes extends Activity {

	List<Recipe> recipeList = new ArrayList<Recipe>();
	List<String> recipeNames = new ArrayList<String>();
	public static final int DETAIL_REQUEST = 1;
	RecipeDbAdapter dbAdapter = null;
	RecipeListArrayAdapter adptr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recipes);
		// Show the Up button in the action bar.
		setupActionBar();
		ListView listView = (ListView)findViewById(R.id.recipe_list);
		setRecipes();
		adptr = new RecipeListArrayAdapter(this, R.layout.recipe_list_layout, recipeNames);
		listView.setAdapter(adptr);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long itemId) {
				Recipe selectedRecipe = recipeList.get(position);
				Intent intent = new Intent(getApplicationContext(), RecipeDetails.class);
				intent.putExtra(Home.RECIPE_INTENT, (Parcelable)selectedRecipe);
				intent.putExtra(Home.RECIPE_ID_INTENT, selectedRecipe.getId());
				intent.putParcelableArrayListExtra(Home.RECIPE_LIST_INTENT, (ArrayList<? extends Parcelable>) recipeList);
				startActivityForResult(intent, 1);					
			}
			
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long itemId) {
				showDeletionDialog(position);					
				return false;
			}
		});
		
		Spinner filter = (Spinner) findViewById(R.id.filter_spinner);
		ArrayAdapter<CharSequence> spinAdptr = ArrayAdapter.createFromResource(this, R.array.categories_array, R.layout.spinner_item_layout);
		filter.setAdapter(spinAdptr);
		
		filter.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long itemId) {
				if(dbAdapter == null){
					dbAdapter = new RecipeDbAdapter(getApplicationContext());
				}
				dbAdapter.open();
				switch (position) {
					case 0:	//all
						setRecipes();
						break;
					case 1: //Appetizers
						recipeList = dbAdapter.getRecipesByCategory("Appetizer");
						setRecipeNames();
						break;
					case 2: //Entrees
						recipeList = dbAdapter.getRecipesByCategory("Entree");
						setRecipeNames();
						break;
					case 3: //Soups
						recipeList = dbAdapter.getRecipesByCategory("Soup");
						setRecipeNames();
						break;
					case 4: //Desserts
						recipeList = dbAdapter.getRecipesByCategory("Dessert");
						setRecipeNames();
						break;
					default:
						setRecipes();
						break;
				}
				dbAdapter.close();
				adptr.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	}
	
	public void setRecipes(){
		if(dbAdapter == null){
			dbAdapter = new RecipeDbAdapter(this);
		}
		dbAdapter.open();
		recipeList = dbAdapter.retrieveAllRecipes();
		dbAdapter.close();
		setRecipeNames();
	}
	
	public void setRecipeNames(){
		recipeNames.clear();
		if(recipeList != null){
			for(Recipe recipe : recipeList){
				recipeNames.add(recipe.getRecipeName());
			}			
		}
	}
	
	public void showDeletionDialog(final int listItem){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.delete_recipe_dialog_desc);
		builder.setTitle(R.string.delete_recipe_dialog_title);
		builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Recipe recipe = recipeList.get(listItem);
				int id = recipe.getId();
				if(dbAdapter == null){
					dbAdapter = new RecipeDbAdapter(getApplicationContext());
				}
				dbAdapter.deleteRecipeWhereId(id);
				setRecipes();
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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.export_recipes:
			exportAllAsText();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void exportAllAsText(){
		try {
			String destination = Environment.getExternalStorageDirectory().toString() + File.separator + "recipes.txt";
			FileOutputStream fileOut = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + File.separator + "recipes.txt");
			PrintStream out = new PrintStream(fileOut);
			
			for(Recipe recipe : recipeList){
				StringBuffer recipeOut = new StringBuffer();
				recipeOut.append(recipe.getRecipeName() + "\n\n");
				recipeOut.append("Ingredients\n\n");
				for(String ingredient : recipe.getAllIngredients()){
					recipeOut.append(ingredient + "\n");
				}
				recipeOut.append("\nDirections\n\n");
				for(String direction : recipe.getAllDirections()){
					recipeOut.append(direction + "\n");
				}
				recipeOut.append("\n\n\n\n\n");
				
				out.print(recipeOut.toString());				
			}
			
			out.close();
			fileOut.close();
			Toast.makeText(this, "File written out to " + destination, Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){

		if(resultCode == RESULT_OK && requestCode == DETAIL_REQUEST){
			setRecipes();
		} else {
			setRecipes();			
		}
		adptr.notifyDataSetChanged();
	}


}
