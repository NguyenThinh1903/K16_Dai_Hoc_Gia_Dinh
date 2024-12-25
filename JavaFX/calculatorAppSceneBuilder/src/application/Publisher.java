package application;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private List<Subscriber> subscribers = new ArrayList<>(); // Danh sách các người đăng ký

    // Thêm người đăng ký vào danh sách
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    // Xóa người đăng ký khỏi danh sách
    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    // Thông báo cho tất cả người đăng ký
    public void notifySubscribers(String message) {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(message); // Gọi phương thức update trên mỗi người đăng ký
        }
    }
}
