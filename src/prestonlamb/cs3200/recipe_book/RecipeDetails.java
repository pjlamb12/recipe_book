package prestonlamb.cs3200.recipe_book;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class RecipeDetails extends Activity {

	Recipe recipe = new Recipe();
	int recipe_id;
	RecipeDbAdapter dbAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_details);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent = getIntent();
		recipe = intent.getParcelableExtra(Home.RECIPE_INTENT);
		recipe_id = intent.getIntExtra(Home.RECIPE_ID_INTENT, -1);
		recipe.setId(recipe_id);
		
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
			NavUtils.navigateUpTo(this, intent);
			return true;
		case R.id.email_recipe:
			emailRecipe();
			return true;
		case R.id.update_recipe:
			updateRecipe();
			return true;
		case R.id.export_text:
			exportAsText();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void updateRecipe(){
		Intent intent = new Intent(getApplicationContext(), NameRecipe.class);
		intent.putExtra(Home.RECIPE_INTENT, (Parcelable)recipe);
		intent.putExtra(Home.RECIPE_ID_INTENT, recipe.getId());
		startActivityForResult(intent, Home.NAME_REQUEST);
	}
	
	public void emailRecipe(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.email_recipe);
		builder.setMessage(R.string.email_recipe_desc);
		builder.setPositiveButton(R.string.email_as_db, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String fileName = "recipes.db";
				String path = makeDbFile(fileName);
				sendEmail(path, "Add this to your Recipe Book!", "Here, add this to your recipe book by importing it in the Recipe Book app!", "Send email...");
				dialog.dismiss();
			}
		});
		builder.setNeutralButton(R.string.email_as_text, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String fileName = "recipes.txt";
				String path = makeDbFile(fileName);
				sendEmail(path, "Here's the recipe", "Here's the recipe you asked me for. Enjoy!", "Send email...");
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
		
	public String makeDbFile(String fileName){
		try {
			FileOutputStream fileOut = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + File.separator + fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			List<Recipe> recipeList = new ArrayList<Recipe>();
			recipeList.add(recipe);
			out.writeObject(recipeList);
			out.close();
			fileOut.close();
			return Environment.getExternalStorageDirectory().toString() + File.separator + fileName;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	public String makeTextFile(String fileName){
		try {
			FileOutputStream fileOut = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + File.separator + fileName);
			@SuppressWarnings("resource")
			PrintStream out = new PrintStream(fileOut);
			
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
			return Environment.getExternalStorageDirectory().toString() + File.separator + fileName;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public void sendEmail(String path, String subject, String body, String hint){
		File file = new File(path);
		if (!file.exists() || !file.canRead()){
			Toast.makeText(getApplicationContext(), "Error attaching file", Toast.LENGTH_LONG).show();
		} else{
			Uri uri = Uri.parse("file://" + path);
			Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.fromParts("mailto", "", null));
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
			emailIntent.putExtra(Intent.EXTRA_TEXT, body);
			emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
			emailIntent.setType("plain/text");
			PackageManager manager = getApplicationContext().getPackageManager();
			List<ResolveInfo> list = manager.queryIntentActivities(emailIntent, 0);
			if(list != null && list.size() > 0){
				startActivity(Intent.createChooser(emailIntent, hint));							
			} else {
				Toast.makeText(getApplicationContext(), R.string.no_email_handler, Toast.LENGTH_LONG).show();
			}
		}
		
	}

	
	public void exportAsText(){
		try {
			String destination = Environment.getExternalStorageDirectory().toString() + File.separator + recipe.getRecipeName() + ".txt";
			FileOutputStream fileOut = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + File.separator + recipe.getRecipeName() + ".txt");
			PrintStream out = new PrintStream(fileOut);
			
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

			out.print(recipeOut.toString());
			
			out.close();
			fileOut.close();
			Toast.makeText(this, "File written out to " + destination, Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}

}
