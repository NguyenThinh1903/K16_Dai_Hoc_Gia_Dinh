import 'package:flutter/material.dart';
import 'models/Product.dart';
import 'ProductDetails.dart';

class ListProducts_Admin extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return TrangthaiListProduct_Admin();
  }
}

class TrangthaiListProduct_Admin extends State<ListProducts_Admin> {
  List<Product> lstproducts = products;
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
      appBar: AppBar(
        title: Text("QUẢN TRỊ SẢN PHẨM"),
      ),
      body: Container(
        margin: EdgeInsets.all(10.0),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(20.0),
          color: Color(0xff7f98e7),
        ),
        child: ListView.builder(
          itemCount: products.length,
          itemBuilder: (context, index) {
            final product = lstproducts[index];
            return Card(
              color: Color(0xfff5f8e7),
              child: ListTile(
                  leading: Image.network(
                    product.image ?? "",
                    width: 50,
                    height: 50,
                    fit: BoxFit.cover,
                    errorBuilder: (context, error, stackTrace) {
                      return Icon(Icons.error, size: 50);
                    },
                  ),
                  title: Text(product.title ?? "Chưa đặt tên"),
                  subtitle: Text(
                    (product.price ?? 0).toStringAsFixed(2),
                    style: TextStyle(color: Colors.redAccent),
                  ),
                  trailing: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      IconButton(
                        icon: Icon(Icons.add),
                        onPressed: () {
                          // Chức năng thêm
                        },
                      ),
                      IconButton(
                        icon: Icon(Icons.edit),
                        onPressed: () {
                          // Chức năng sửa
                        },
                      ),
                      IconButton(
                        icon: Icon(Icons.delete),
                        onPressed: () {
                          // Chức năng xóa
                        },
                      ),
                    ],
                  )),
            );
          },
        ),
      ),
    );
  }
}
