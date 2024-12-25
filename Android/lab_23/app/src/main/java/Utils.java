import java.util.ArrayList;

public class Utils {
    static ArrayList<Furniture> furnitureHistory = new ArrayList<>();

    public void addFurnitureHistory(Furniture furniture) {
        if (!furnitureHistory.contains(furniture)) {
            this.furnitureHistory.add(0, furniture);
        }
    }

    public ArrayList<Furniture> getFurnitureHistory() { return this.furnitureHistory; }
}
