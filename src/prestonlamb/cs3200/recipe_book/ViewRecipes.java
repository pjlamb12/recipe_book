package prestonlamb.cs3200.recipe_book;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class ViewRecipes extends Activity {

	List<Recipe> recipeList;
	public static final int DETAIL_REQUEST = 1;
	RecipeDbAdapter dbAdapter = null;
	RecipeListArrayAdapter adptr;
	boolean noRecipes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recipes);
		// Show the Up button in the action bar.
		setupActionBar();
		ListView listView = (ListView)findViewById(R.id.recipe_list);
		if(dbAdapter == null){
			dbAdapter = new RecipeDbAdapter(this);
		}
		recipeList = dbAdapter.retrieveAllRecipes();
		List<String> recipeNames = new ArrayList<String>();
		if(recipeList != null){
			noRecipes = false;
			for(Recipe recipe : recipeList){
				recipeNames.add(recipe.getRecipeName());
			}			
		} else {
			noRecipes = true;
			recipeNames.add("You have no recipes!");
		}
		adptr = new RecipeListArrayAdapter(this, R.layout.recipe_list_layout, recipeNames);
		listView.setAdapter(adptr);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long itemId) {
				
				if(noRecipes){
					
				}
				else{
					Recipe selectedRecipe = recipeList.get(position);
					Intent intent = new Intent(getApplicationContext(), RecipeDetails.class);
					intent.putExtra(Home.RECIPE_INTENT, (Parcelable)selectedRecipe);
					intent.putParcelableArrayListExtra(Home.RECIPE_LIST_INTENT, (ArrayList<? extends Parcelable>) recipeList);
					startActivityForResult(intent, 1);					
				}
			}
			
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long itemId) {
				if(noRecipes){
					
				}
				else{
					showDeletionDialog(position);					
				}
				return false;
			}
		});
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
				recipeList = dbAdapter.retrieveAllRecipes();
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
			Intent intent = NavUtils.getParentActivityIntent(this);
			intent.putParcelableArrayListExtra(Home.RECIPE_LIST_INTENT, (ArrayList<? extends Parcelable>) recipeList);
			NavUtils.navigateUpTo(this, intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){

		if(resultCode == Home.RESULT_OK && requestCode == DETAIL_REQUEST){
			if(data.hasExtra(Home.RECIPE_LIST_INTENT));{
				recipeList = data.getParcelableExtra(Home.RECIPE_LIST_INTENT);
			}
		}
	}


}
