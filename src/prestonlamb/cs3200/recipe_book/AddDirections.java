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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.text.InputType;

public class AddDirections extends Activity {

	Recipe recipe;
	DirectionArrayAdapter adptr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_directions);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent = getIntent();
		recipe = intent.getParcelableExtra(Home.RECIPE_INTENT);
		
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
				return false;
			}
			
		});

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
				recipe.deleteSingleDirection(listItem);
				recipe.addSingleDirection(editedDirection);
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
		Intent intent = new Intent(getApplicationContext(), Home.class);
		intent.putExtra(Home.RECIPE_INTENT, (Parcelable)recipe);
		setResult(Home.RESULT_OK, intent);
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
			NavUtils.navigateUpTo(this, intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
