import 'models/Product.dart';
import 'models/CartItem.dart';
import 'models/Cart.dart';
import 'package:flutter/material.dart';

class CartScreen extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return Trangthai_CartScreen();
  }
}

class Trangthai_CartScreen extends State<CartScreen> {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
      appBar: AppBar(
        title: ListTile(
          leading: Icon(
            Icons.shopping_cart,
            color: Colors.blueAccent,
          ),
          title: Text("Giỏ của bạn",
              style: TextStyle(
                  fontSize: 18,
                  color: Colors.blueAccent,
                  fontWeight: FontWeight.bold)),
        ),
      ),
      body: (Cart.cartItems.isEmpty)
          ? Center(child: Text("Giỏ của bạn còn trống"))
          : Column(
              children: [
                Expanded(
                    child: ListView.builder(
                  itemCount: Cart.cartItems.length,
                  itemBuilder: (context, index) {
                    CartItem item = Cart.cartItems[index];
                    Product product = item.product;

                    return ListTile(
                      leading: Image.network(
                        product.image ?? "",
                        width: 50,
                        height: 50,
                      ),
                      title: Text(product.title ?? "Chưa rõ"),
                      subtitle: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Row(
                              children: [
                                IconButton(
                                    onPressed: () {
                                      setState(() {
                                        if (item.quantity > 1) {
                                          item.quantity--;
                                        } else {
                                          Cart.removeFromCart(product.id);
                                        }
                                      });
                                    },
                                    icon: Icon(Icons.remove)),
                                Text(item.quantity.toString()),
                                IconButton(
                                    onPressed: () {
                                      setState(() {
                                        item.quantity++;
                                      });
                                    },
                                    icon: Icon(Icons.add))
                              ],
                            ),
                          ]),
                      trailing: IconButton(
                        icon: Icon(Icons.delete),
                        onPressed: () {
                          setState(() {
                            Cart.removeFromCart(product.id);
                          });

                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(
                              content: Text('${product.title} Đã Xóa Khỏi Giỏ'),
                            ),
                          );
                        },
                      ),
                    );
                  },
                )),
                Padding(
                  padding: const EdgeInsets.all(16),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text("\$ " + Cart.getTotalPrice().toStringAsFixed(2),
                          style: TextStyle(
                              fontWeight: FontWeight.bold,
                              fontSize: 18,
                              color: Colors.red)),
                      ElevatedButton(
                        onPressed: () {},
                        child: Text("Thanh toán"),
                        style: ElevatedButton.styleFrom(
                            backgroundColor: Colors.green),
                      )
                    ],
                  ),
                )
              ],
            ),
    );
  }
}
