package prestonlamb.cs3200.recipe_book;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class IngredientArrayAdapter extends ArrayAdapter<String> {
	
//	private Context context;
//	private int resID;

	public IngredientArrayAdapter(Context context, int resource, List<String> objects) {
		super(context, resource, objects);
		
//		this.context = context;
//		resID = resource;
	}

	public View getView(int position, View convertView, ViewGroup parent){

		View view = convertView;
		String selected_ingredient = getItem(position);
		
		if(view == null){
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
			view = vi.inflate(R.layout.ingredient_layout, null);
		}
		TextView itemView = (TextView) view.findViewById(R.id.ingredient_text_view);
		itemView.setText(selected_ingredient);
		
		return view;
	}
	
	
	
}
