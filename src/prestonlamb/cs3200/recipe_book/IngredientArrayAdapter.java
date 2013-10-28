package prestonlamb.cs3200.recipe_book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IngredientArrayAdapter extends ArrayAdapter<Ingredients> {
	
	Context con;
	int resID;
	Ingredients ingredients;

	public IngredientArrayAdapter(Context context, int resource, Ingredients objects) {
		super(context, resource);
		
		con = context;
		resID = resource;
		ingredients = objects;
	}

	public View getView(int position, View convertView, ViewGroup parent){

		RelativeLayout layout;
		Ingredients selected_ingredient = getItem(position);
		
		if(convertView == null){
			layout = new RelativeLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
			vi.inflate(resID, layout, true);
		} else {
			layout = (RelativeLayout)convertView;
		}
		
		((TextView) layout.findViewById(R.id.ingredient_layout)).setText(selected_ingredient.toString());
		
		return layout;
	}
	
	
	
}
