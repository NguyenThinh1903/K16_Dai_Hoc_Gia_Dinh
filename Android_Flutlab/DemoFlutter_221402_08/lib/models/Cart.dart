import 'Product.dart';
import 'CartItem.dart';

class Cart {
  static List<CartItem> cartItems = [];

  static void addToCart(Product product) {
    // Kiểm tra nếu sản phẩm đã có trong giỏ hàng
    int index = cartItems.indexWhere((item) => item.product.id == product.id);
    if (index != -1) {
      cartItems[index].quantity++; // Tăng số lượng nếu sản phẩm đã có
    } else {
      cartItems.add(CartItem(product: product, quantity: 1));
    }
  }

  static void removeFromCart(int productID) {
    int index = cartItems.indexWhere((item) => item.product.id == productID);
    if (index != -1) {
      cartItems.remove(cartItems[index]);
    }
  }

  static double getTotalPrice() {
    double total = 0;
    for (var item in cartItems) {
      total += (item.product.price ?? 0) *
          item.quantity; // Tính tổng dựa trên số lượng
    }
    return total;
  }
}
