package prestonlamb.cs3200.recipe_book;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Home extends Activity {

	RecipeDbAdapter dbAdapter = null;
	public static final int RESULT_BAD = 0;
	public static final int RESULT_OK = 1;
	public static final int NAME_REQUEST = 1;
	public static final int INGREDIENTS_REQUEST = 2;
	public static final int DIRECTIONS_REQUEST = 3;
	public static final int RECIPES_REQUEST = 4;
	public static final int IMPORT_DB_REQUEST = 5;
	public static final String RECIPE_LIST_INTENT = "RecipeList";
	public static final String RECIPE_INTENT = "Recipe";
	public static final String RECIPE_ID_INTENT = "RecipeId";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}
	
	public void newRecipe(View v){
		Intent intent = new Intent(getApplicationContext(), NameRecipe.class);
		startActivityForResult(intent, NAME_REQUEST);
	}
	
	public void viewRecipes(View v){
		Intent intent = new Intent(getApplicationContext(), ViewRecipes.class);
		startActivity(intent);		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){

		if(resultCode == RESULT_OK && requestCode == NAME_REQUEST){
			if(data.hasExtra(Home.RECIPE_INTENT));{
				Recipe newRecipe = data.getParcelableExtra(Home.RECIPE_INTENT);
				int recipe_id = data.getIntExtra(RECIPE_ID_INTENT, -1);
				newRecipe.setId(recipe_id);
				if(newRecipe != null){
					if(dbAdapter == null){
						dbAdapter = new RecipeDbAdapter(this);
					}
					dbAdapter.open();
					if(newRecipe.getId() == -1){
						dbAdapter.insertRecipe(newRecipe);						
					}else{
						dbAdapter.updateRecipeWhereID(newRecipe);
					}
					dbAdapter.close();
				}
			}
		} else if(resultCode == -1 && requestCode == IMPORT_DB_REQUEST){
			Uri uri = data.getData();
			String filePath = uri.getPath().toString();
			readObjectsFromFile(filePath);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.email_database:
			emailDatabase();
			return true;
		case R.id.email_recipes:
			emailRecipes();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void emailDatabase(){
		String fileName = "recipes.db";
		String path = makeDbFile(fileName);
		String subject = "Here's my Recipe Book Database";
		String body = "Here's my Recipe Book Database. I hope you like the recipes!";
		String hint = "Email recipe database...";
		sendEmail(path, subject, body, hint);
	}
	
	public String makeDbFile(String fileName){
		try {
			FileOutputStream fileOut = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + File.separator + fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			if(dbAdapter == null){
				dbAdapter = new RecipeDbAdapter(this);
			}
			dbAdapter.open();
			List<Recipe> recipeList = dbAdapter.retrieveAllRecipes();
			dbAdapter.close();
			out.writeObject(recipeList);
			out.close();
			fileOut.close();
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
	
	public String makeTextFile(String fileName){
		try {
			FileOutputStream fileOut = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + File.separator + fileName);
			@SuppressWarnings("resource")
			PrintStream out = new PrintStream(fileOut);
			if(dbAdapter == null){
				dbAdapter = new RecipeDbAdapter(this);
			}
			dbAdapter.open();
			List<Recipe> recipeList = dbAdapter.retrieveAllRecipes();
			dbAdapter.close();
			
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
			return Environment.getExternalStorageDirectory().toString() + File.separator + fileName;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	public void emailRecipes(){
		String fileName = "recipes.txt";
		String path = makeTextFile(fileName);
		String subject = "My Recipe Book";
		String body = "Here's my Recipe Book. Hope you like them!";
		String hint = "Email Recipe Book...";
		sendEmail(path, subject, body, hint);
	}
	
	@SuppressWarnings("unchecked")
	public void readObjectsFromFile(String path){
		try{
			FileInputStream fileIn = new FileInputStream(path);
			@SuppressWarnings("resource")
			ObjectInputStream in = new ObjectInputStream(fileIn);
			ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
			recipeList = (ArrayList<Recipe>) in.readObject();
			replaceDatabaseDialog(recipeList);
		} catch (IOException e){
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), R.string.error_opening_file, Toast.LENGTH_LONG).show();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), R.string.error_opening_file, Toast.LENGTH_LONG).show();
		}
	}
	
	public void replaceDatabaseDialog(final ArrayList<Recipe> recipeList){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.import_database_dialog_title);
		builder.setMessage(R.string.import_database_dialog_desc);
		builder.setPositiveButton(R.string.replace, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				replaceDatabase(recipeList);
				dialog.dismiss();
			}
		});
		builder.setNeutralButton(R.string.add_to, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				addToDatabase(recipeList);
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
	
	public void replaceDatabase(ArrayList<Recipe> recipeList){
		if(dbAdapter == null){
			dbAdapter = new RecipeDbAdapter(this);
		}
		dbAdapter.open();
		dbAdapter.deleteAllRecipes();
		for(Recipe recipe : recipeList){
			dbAdapter.insertRecipe(recipe);
		}
		dbAdapter.close();
		Toast.makeText(getApplicationContext(), R.string.recipes_imported, Toast.LENGTH_LONG).show();
	}
	
	public void addToDatabase(ArrayList<Recipe> recipeList){
		if(dbAdapter == null){
			dbAdapter = new RecipeDbAdapter(this);
		}
		dbAdapter.open();
		for(Recipe recipe : recipeList){
			dbAdapter.insertRecipe(recipe);
		}
		dbAdapter.close();
		Toast.makeText(getApplicationContext(), R.string.recipes_imported, Toast.LENGTH_LONG).show();
	}

	public void exportDatabase(View v){
		String fileName = "recipes.db";
		String path = makeDbFile(fileName);
		Toast.makeText(this, "File written out to " + path, Toast.LENGTH_LONG).show();
	}
	
	public void importDatabase(View v){
		Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
		fileIntent.setType("file/*");
		PackageManager manager = getApplicationContext().getPackageManager();
		List<ResolveInfo> list = manager.queryIntentActivities(fileIntent, 0);
		if(list != null && list.size() > 0){
			startActivityForResult(Intent.createChooser(fileIntent, "Select database..."), IMPORT_DB_REQUEST);			
		} else {
			Toast.makeText(getApplicationContext(), R.string.no_file_handler, Toast.LENGTH_LONG).show();
		}

	}
}
