package prestonlamb.cs3200.recipe_book;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RecipeDbAdapter {
	
	public static final String ADPTR_LOGTAG = RecipeDbAdapter.class.getSimpleName() + "_TAG";
	public static final String QUERY_LOGTAG = "QUERY_TAG";
	
	public static final String DB_NAME = "recipe_book.db";
	public static final int DB_VERSION = 1;
	public static final String RECIPE_TABLE = "recipe";
	
	public static final String RECIPE_ID_COL_NAME = "id";
	public static final String RECIPE_NAME_COL_NAME = "name";
	public static final String RECIPE_CATEGORY_COL_NAME = "category";
	public static final String RECIPE_INGREDIENTS_COL_NAME = "ingredients";
	public static final String RECIPE_DIRECTIONS_COL_NAME = "directions";
	
	public static final int RECIPE_ID_COL_NUM = 0;
	public static final int RECIPE_NAME_COL_NUM = 1;
	public static final int RECIPE_CATEGORY_COL_NUM = 2;
	public static final int RECIPE_INGREDIENTS_COL_NUM = 3;
	public static final int RECIPE_DIRECTIONS_COL_NUM = 4;
	
	private SQLiteDatabase db = null;
	private RecipeDBOpenHelper dbHelper = null;
	
	private static class RecipeDBOpenHelper extends SQLiteOpenHelper {
		static final String HELPER_LOGTAG = RecipeDBOpenHelper.class.getSimpleName() + "_TAG";
		
		public RecipeDBOpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		static final String RECIPE_TABLE_CREATE = "create table " + RECIPE_TABLE
				+ " (" + RECIPE_ID_COL_NAME + " integer primary key autoincrement, " +
				RECIPE_NAME_COL_NAME + " text not null, " + 
				RECIPE_CATEGORY_COL_NAME + " text not null, " +
				RECIPE_INGREDIENTS_COL_NAME + " text not null, " +
				RECIPE_DIRECTIONS_COL_NAME + " text not null " + ");";
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(HELPER_LOGTAG, RECIPE_TABLE_CREATE);
			db.execSQL(RECIPE_TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(ADPTR_LOGTAG, "Upgrading from version " + oldVersion + " to " + newVersion + ", which will destroy all old data.");
			db.execSQL("DROP TABLE IF EXISTS " + RECIPE_TABLE);
			onCreate(db);
		}
	} // end of RecipeDBOpenHelper class
	
	public RecipeDbAdapter(Context context){
		dbHelper = new RecipeDBOpenHelper(context, DB_NAME, null, DB_VERSION);
	}
	
	public void open() throws SQLiteException{
		try{
			Log.d(ADPTR_LOGTAG, "WRITEABLE DB CREATED");
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException ex){
			Log.d(ADPTR_LOGTAG, "READABLE DB CREATED");
			db = dbHelper.getReadableDatabase();
		}
	}
	
	public SQLiteDatabase getReadableDatabase(){
		try{
			return dbHelper.getReadableDatabase();
		} catch (SQLiteException ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	public boolean isCreated(){
		if(db == null){
			return false;
		}else{
			return true;
		}
	}
	
	public void close(){
		db.close();
	}
	
	public long insertRecipe(Recipe recipe){
		ContentValues newRecipe = new ContentValues();
		String ingredients = escapeString(recipe.getAllIngredients());
		String directions = escapeString(recipe.getAllDirections());
		
		newRecipe.put(RECIPE_NAME_COL_NAME, recipe.getRecipeName());
		newRecipe.put(RECIPE_CATEGORY_COL_NAME, recipe.getCategory());
		newRecipe.put(RECIPE_INGREDIENTS_COL_NAME, ingredients);
		newRecipe.put(RECIPE_DIRECTIONS_COL_NAME, directions);
		long insertedRowIndex = db.insertWithOnConflict(RECIPE_TABLE, null, newRecipe, SQLiteDatabase.CONFLICT_REPLACE);
		Log.d(ADPTR_LOGTAG, "Inserted recipe record: " + insertedRowIndex);
		return insertedRowIndex;
	}
	
	private String escapeString(List<String> list){
		StringBuilder sb = new StringBuilder();
		for(String item : list){
			sb.append(item);
			sb.append("/");
		}
		return sb.toString();
	}
	
	private ArrayList<String> unescapeString(String fullString){
		String[] parts = fullString.split("/");
		ArrayList<String> list = new ArrayList<String>();
		for(String part : parts){
			list.add(part);
		}
		return list;
	}
	
	static final String GET_ALL_RECIPES_QUERY = "SELECT * FROM " + RECIPE_TABLE;
	public List<Recipe> retrieveAllRecipes(){
		SQLiteDatabase db = this.getReadableDatabase();
		Log.d(ADPTR_LOGTAG, "GET_ALL_RECIPES_QUERY Readable DB opened");
		
		Cursor cursor = db.query(RECIPE_TABLE, null, null, null, null, null, null);
		List<Recipe> recipeList= new ArrayList<Recipe>();
		try{
			
			if (cursor.getCount() != 0){
				cursor.moveToFirst();
				while(cursor.isAfterLast() == false){
					Recipe recipe = new Recipe();
					int recipeId = cursor.getInt(cursor.getColumnIndex(RECIPE_ID_COL_NAME));
					recipe.setId(recipeId);
					String recipeName = cursor.getString(cursor.getColumnIndex(RECIPE_NAME_COL_NAME));
					recipe.setRecipeName(recipeName);
					String category = cursor.getString(cursor.getColumnIndex(RECIPE_CATEGORY_COL_NAME));
					recipe.setCategory(category);
					String ingredients = cursor.getString(cursor.getColumnIndex(RECIPE_INGREDIENTS_COL_NAME));
					List<String> ingredientList = unescapeString(ingredients);
					recipe.setAllIngredients(ingredientList);
					String directions = cursor.getString(cursor.getColumnIndex(RECIPE_DIRECTIONS_COL_NAME));
					List<String> directionList = unescapeString(directions);
					recipe.setDirections(directionList);
					recipeList.add(recipe);
					cursor.moveToNext();
				}
			} else {
				recipeList = null;
			}
			cursor.close();
			db.close();
			Log.d(ADPTR_LOGTAG, "GET_ALL_RECIPES_QUERY Readable DB closed...");
			return recipeList;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	static final String GET_RECIPE_BY_ID = "SELECT * FROM " + RECIPE_TABLE + " WHERE id=?";
	static final String RECIPE_BY_ID_WHERE = "id = ?";
	public Recipe getRecipeByID(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Log.d(ADPTR_LOGTAG, "GET_ALL_RECIPES_QUERY Readable DB opened");
		String[] args = new String[1];
		args[0] = Integer.toString(id);
		Cursor cursor = db.query(RECIPE_TABLE, null, RECIPE_BY_ID_WHERE, args, null, null, null);
		Recipe recipe = new Recipe();
		try{
			if (cursor.getCount() != 0){
				cursor.moveToFirst();
				int recipeId = cursor.getInt(cursor.getColumnIndex(RECIPE_ID_COL_NAME));
				recipe.setId(recipeId);
				String recipeName = cursor.getString(cursor.getColumnIndex(RECIPE_NAME_COL_NAME));
				recipe.setRecipeName(recipeName);
				String category = cursor.getString(cursor.getColumnIndex(RECIPE_CATEGORY_COL_NAME));
				recipe.setCategory(category);
				String ingredients = cursor.getString(cursor.getColumnIndex(RECIPE_INGREDIENTS_COL_NAME));
				List<String> ingredientList = unescapeString(ingredients);
				recipe.setAllIngredients(ingredientList);
				String directions = cursor.getString(cursor.getColumnIndex(RECIPE_DIRECTIONS_COL_NAME));
				List<String> directionList = unescapeString(directions);
				recipe.setDirections(directionList);
			} else {
				recipe = null;
			}
			return recipe;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public void deleteRecipeWhereId(int id){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String DELETE_RECIPE_WHERE_ID = "id = ?";
		String args[] = new String[1];
		args[0] = Integer.toString(id);
		try{
			db.delete(RECIPE_TABLE, DELETE_RECIPE_WHERE_ID, args);
		} catch (Exception e){
			e.printStackTrace();
		}
		db.close();
	}
	
	public int numberRecipesInDb(){
		SQLiteDatabase db = this.getReadableDatabase();
		Log.d(ADPTR_LOGTAG, "GET_ALL_RECIPES_QUERY Readable DB opened");
		
		Cursor cursor = db.query(RECIPE_TABLE, null, null, null, null, null, null);
		int count = cursor.getCount();
		return count;
	}
}
