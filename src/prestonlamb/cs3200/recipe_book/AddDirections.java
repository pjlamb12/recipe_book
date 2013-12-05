package prestonlamb.cs3200.recipe_book;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AddDirections extends Activity {

	Recipe recipe;
	int recipe_id;
	DirectionArrayAdapter adptr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_directions);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent = getIntent();
		recipe = intent.getParcelableExtra(Home.RECIPE_INTENT);
		recipe_id = intent.getIntExtra(Home.RECIPE_ID_INTENT, -1);
		recipe.setId(recipe_id);
		
		if(recipe.getDirectionsSize() == 0){
			recipe.addSingleDirection("There are no directions yet...");
		}
		
		ListView listView = (ListView)findViewById(R.id.direcion_list);
		adptr = new DirectionArrayAdapter(this, R.id.ingredient_text_view, recipe.getAllDirections());
		listView.setAdapter(adptr);
		
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showEditDirectionDialog(position);
			}
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				showDeletionDialog(position);
				return true;
			}
			
		});

	}

	public void startVoiceInput(View v){
		PackageManager manager = getPackageManager();
		Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		List<ResolveInfo> activities = manager.queryIntentActivities(voiceIntent, 0);
		if(activities.size() == 0){
			
		}else{
			voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Ingredient");
			voiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
			startActivityForResult(voiceIntent, Home.SPEECH_REQUEST);
		}
	}

	
	public void showEditDirectionDialog(final int listItem){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.edit_direction);
		final EditText editInput = new EditText(this);
		editInput.setText(recipe.getSingleDirection(listItem));
		editInput.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(editInput);
		builder.setPositiveButton(R.string.Update, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String editedDirection = editInput.getText().toString();
				List<String> allDirections = new ArrayList<String>();
				allDirections = recipe.getAllDirections();
				allDirections.remove(listItem);
				allDirections.add(listItem, editedDirection);
				recipe.setDirections(allDirections);
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
		builder.setMessage(R.string.delete_direction_dialog_desc);
		builder.setTitle(R.string.delete_direction_dialog_title);
		builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				recipe.deleteSingleDirection(listItem);
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
	
	public void addDirection(View v){
		EditText enterDirection = (EditText) findViewById(R.id.enter_direction);
		String direction = enterDirection.getText().toString();
		if(direction.length() != 0){			
			if (recipe.getDirectionsSize() == 1){
				String firstDirection = recipe.getSingleDirection(0);
				if(firstDirection.contains("There are no directions")){
					recipe.deleteSingleDirection(0);
				}
			}
			recipe.addSingleDirection(direction);
			adptr.notifyDataSetChanged();
			enterDirection.setText("");
		} else {
			Toast.makeText(getApplicationContext(), R.string.direction_required, Toast.LENGTH_LONG).show();
		}

	}
	
	public void finish(View v){
		RecipeDbAdapter dbAdapter = new RecipeDbAdapter(this);
		dbAdapter.open();
		if(recipe.getId() == -1){
			dbAdapter.insertRecipe(recipe);						
		}else{
			dbAdapter.updateRecipeWhereID(recipe);
		}
		dbAdapter.close();
		Toast.makeText(getApplicationContext(), "Your recipe was successfully saved.", Toast.LENGTH_LONG).show();
		setResult(RESULT_OK);
		finish();
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
		getMenuInflater().inflate(R.menu.add_directions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = NavUtils.getParentActivityIntent(this);
			intent.putExtra(Home.RECIPE_INTENT, (Parcelable)recipe);
			intent.putExtra(Home.RECIPE_ID_INTENT, recipe.getId());
			setResult(RESULT_OK);
			NavUtils.navigateUpTo(this, intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		Intent intent = NavUtils.getParentActivityIntent(this);
		intent.putExtra(Home.RECIPE_INTENT, (Parcelable)recipe);
		intent.putExtra(Home.RECIPE_ID_INTENT, recipe.getId());
		setResult(RESULT_OK);
		NavUtils.navigateUpTo(this, intent);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if( requestCode == Home.SPEECH_REQUEST && resultCode == RESULT_OK){
			ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			StringBuilder sb = new StringBuilder();
			for(String match : matches){
				sb.append(match);
				sb.append(" ");
			}
			EditText enterDirection = (EditText) findViewById(R.id.enter_direction);
			enterDirection.setText(sb.toString());
		} else {
			finish();			
		}
	}

}
