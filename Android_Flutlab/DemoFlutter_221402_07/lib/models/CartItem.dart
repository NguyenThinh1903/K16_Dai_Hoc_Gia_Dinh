import 'Product.dart';

class CartItem /* Định nghĩa model cho Item trong giỏ hàng */
{
  Product product;
  int quantity;
  double? current_price = 0; /*Giá thực tế nếu có*/
  double? ratio_sale_off = 0;

  CartItem({required this.product, this.quantity = 1});
}
