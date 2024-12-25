import android.graphics.Bitmap;
import java.util.ArrayList;

public class Categories {
    String name;
    ArrayList<Furniture> arrayList;
    Bitmap image;

    public Categories(String name, ArrayList<Furniture> arrayList) {
        this.name = name;
        this.arrayList = arrayList;
    }

    public Categories(String name, ArrayList<Furniture> arrayList, Bitmap image) {
        this.name = name;
        this.arrayList = arrayList;
        this.image = image;
    }

    // Getter và Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ArrayList<Furniture> getArrayList() { return arrayList; }
    public void setArrayList(ArrayList<Furniture> arrayList) { this.arrayList = arrayList; }
    public Bitmap getImage() { return image; }
    public void setImage(Bitmap image) { this.image = image; }
}
