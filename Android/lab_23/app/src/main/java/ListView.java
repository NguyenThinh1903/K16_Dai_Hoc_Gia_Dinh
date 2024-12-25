import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapp.R; // Chắc chắn rằng import là đúng


import java.util.ArrayList;

public class FurnitureAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Furniture> furnitureList;

    public FurnitureAdapter(Context context, ArrayList<Furniture> furnitureList) {
        this.context = context;
        this.furnitureList = furnitureList;
    }

    @Override
    public int getCount() {
        return furnitureList.size();
    }

    @Override
    public Object getItem(int position) {
        return furnitureList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_furniture, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            viewHolder.nameTextView = convertView.findViewById(R.id.nameTextView);
            viewHolder.descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Furniture furniture = furnitureList.get(position);
        viewHolder.nameTextView.setText(furniture.getName());
        viewHolder.descriptionTextView.setText(furniture.getDescription());
        viewHolder.imageView.setImageBitmap(furniture.getImage());

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView descriptionTextView;
    }
}
