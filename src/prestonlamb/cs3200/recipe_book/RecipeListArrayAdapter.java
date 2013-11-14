package prestonlamb.cs3200.recipe_book;

import android.widget.ArrayAdapter;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecipeListArrayAdapter extends ArrayAdapter<String> {

		public RecipeListArrayAdapter(Context context, int resource, List<String> objects) {
			super(context, resource, objects);
		}

		public View getView(int position, View convertView, ViewGroup parent){

			View view = convertView;
			String selected_recipe = getItem(position);
			
			if(view == null){
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
				view = vi.inflate(R.layout.ingredient_layout, null);
			}
			TextView itemView = (TextView) view.findViewById(R.id.ingredient_text_view);
			itemView.setText(selected_recipe);
			
			return view;
		}

}
