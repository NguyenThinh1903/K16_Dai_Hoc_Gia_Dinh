import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class Furniture implements Serializable {
    String name;
    String description;
    Bitmap image;

    public Furniture(String name, String description, Bitmap image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    // Getter và Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Bitmap getImage() { return image; }
    public void setImage(Bitmap image) { this.image = image; }

    public static Bitmap convertStringToBitmapFromAccess(Context context, String filename) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open(filename);
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
