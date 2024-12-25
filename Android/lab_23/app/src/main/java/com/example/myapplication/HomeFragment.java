import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ListView listView;
    ArrayList<Furniture> arrayList;
    FurnitureAdapter furnitureAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.listView);
        arrayList = getMockData();
        furnitureAdapter = new FurnitureAdapter(getContext(), arrayList);
        listView.setAdapter(furnitureAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Furniture selectedFurniture = arrayList.get(position);
                Toast.makeText(getContext(), "Selected: " + selectedFurniture.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<Furniture> getMockData() {
        ArrayList<Furniture> furnitureList = new ArrayList<>();
        // Thêm dữ liệu mẫu
        furnitureList.add(new Furniture("Sofa", "Comfortable sofa", Furniture.convertStringToBitmapFromAccess(getContext(), "sofa.jpg")));
        furnitureList.add(new Furniture("Table", "Wooden table", Furniture.convertStringToBitmapFromAccess(getContext(), "table.jpg")));
        return furnitureList;
    }
}
