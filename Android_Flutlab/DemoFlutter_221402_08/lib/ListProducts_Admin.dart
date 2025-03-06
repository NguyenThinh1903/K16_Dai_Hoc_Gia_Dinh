import 'package:flutter/material.dart';
import 'models/Product.dart';
import 'ProductDetails.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'ProductEdit.dart';

class ListProducts_Admin extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return TrangthaiListProduct_Admin();
  }
}

class TrangthaiListProduct_Admin extends State<ListProducts_Admin> {
  //Bỏ ds sản phẩm lấy từ mảng
  //List<Product> lstproducts = products;
  //
  late Future<List<Product>> lstproducts;
  Product newproduct = Product();
  /*Khai báo lstproducts sử dụng trong tương lai*/

  Future<List<Product>> LayDssanphamtuBackend() async {
    final response = await http
        .get(Uri.parse('https://6731c05f7aaf2a9aff11dd05.mockapi.io/products'));

    if (response.statusCode == 200) {
      // Chuyển đổi JSON sang danh sách các đối tượng Product

      List<dynamic> jsonData = json.decode(response.body);

      return jsonData.map((item) => Product.fromJsonMocki(item)).toList();
    } else {
      throw Exception('Không đọc được sản phẩm từ backend');
    }
  }

  @override
  void initState() {
    super.initState();
    lstproducts = LayDssanphamtuBackend();
  }

  void ConfirmDelete(BuildContext context, Product product) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text("Xác nhận xóa"),
          content: Text(
              "Bạn có chắc chắn muốn xóa sản phẩm '${product.title}' không?"),
          actions: <Widget>[
            TextButton(
              onPressed: () {
                Navigator.of(context).pop(); // Đóng hộp thoại
              },
              child: Text("Hủy", style: TextStyle(color: Colors.grey)),
            ),
            TextButton(
              onPressed: () async {
                Navigator.of(context).pop(); // Đóng hộp thoại
                await _deleteProduct(product);
              },
              child: Text("Có", style: TextStyle(color: Colors.red)),
            ),
          ],
        );
      },
    );
  }

  Future<void> _deleteProduct(Product product) async {
    print("Đang xóa sản phẩm có ID: ${product.id}");
    final response = await http.delete(
      Uri.parse('https://6731c05f7aaf2a9aff11dd05.mockapi.io/products/' +
          product.id.toString()),
    );

    print("Mã trạng thái HTTP: ${response.statusCode}");
    print("Phản hồi từ server: ${response.body}");

    if (response.statusCode == 200 || response.statusCode == 201) {
      setState(() {
        lstproducts = LayDssanphamtuBackend(); // Cập nhật danh sách sau khi xóa
      });
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text("Đã xóa sản phẩm thành công")),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
            content: Text("Lỗi khi xóa sản phẩm:" +
                response.statusCode.toString() +
                "," +
                product.id.toString())),
      );
    }
  }

  Future<void> SaveProduct(Product p) async {
    if (p.id == 0) {
      // Trường hợp thêm mới sản phẩm
      final response = await http.post(
        Uri.parse('https://6731c05f7aaf2a9aff11dd05.mockapi.io/products'),
        headers: {'Content-Type': 'application/json'},
        body: json.encode(p.toJsonMocki()),
      );

      if (response.statusCode == 200 || response.statusCode == 201) {
        setState(() {
          lstproducts =
              LayDssanphamtuBackend(); // Cập nhật danh sách sau khi thêm mới
        });
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text("Thêm sản phẩm thành công")),
        );
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
              content: Text("Lỗi khi thêm sản phẩm: ${response.statusCode}")),
        );
      }
    } else {
      // Trường hợp cập nhật sản phẩm
      final response = await http.put(
        Uri.parse(
            'https://6731c05f7aaf2a9aff11dd05.mockapi.io/products/${p.id}'),
        headers: {'Content-Type': 'application/json'},
        body: json.encode(p.toJsonMocki()),
      );

      if (response.statusCode == 200) {
        setState(() {
          lstproducts =
              LayDssanphamtuBackend(); // Cập nhật danh sách sau khi chỉnh sửa
        });
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text("Cập nhật sản phẩm thành công")),
        );
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
              content:
                  Text("Lỗi khi cập nhật sản phẩm: ${response.statusCode}")),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
        appBar: AppBar(
          title: Text("QUẢN TRỊ SẢN PHẨM",
              style: TextStyle(
                  color: Colors.deepOrange, fontWeight: FontWeight.bold)),
        ),
        body: FutureBuilder(
          future: lstproducts,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return Center(
                child: CircularProgressIndicator(),
              );
            } else if (snapshot.hasError) {
              return Center(
                child: Text('Lỗi:' + snapshot.error.toString()),
              );
            } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
              return Center(
                child: Text('Không có dữ liệu'),
              );
            } else {
              List<Product> lst = snapshot.data!;

              return Container(
                margin: EdgeInsets.all(10.0),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(20.0),
                  color: Color(0xff7f98e7),
                ),
                child: ListView.builder(
                  itemCount: products.length,
                  itemBuilder: (context, index) {
                    final product = lst[index];
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
                          title: Text(
                            product.title ?? "Chưa đặt tên",
                            maxLines: 2,
                            overflow: TextOverflow.ellipsis,
                            style: TextStyle(fontWeight: FontWeight.bold),
                          ),
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
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                        builder: (context) => ProductEdit(
                                              product: newproduct,
                                              onSave: SaveProduct,
                                            )),
                                  );
                                },
                              ),
                              IconButton(
                                icon: Icon(Icons.edit),
                                onPressed: () {
                                  // Chức năng sửa
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                        builder: (context) => ProductEdit(
                                              product: product,
                                              onSave: SaveProduct,
                                            )),
                                  );
                                },
                              ),
                              IconButton(
                                icon: Icon(Icons.delete),
                                onPressed: () {
                                  ConfirmDelete(context, product);
                                  // Chức năng xóa
                                },
                              ),
                            ],
                          )),
                    );
                  },
                ),
              );
            }
          },
        ));
  }
}
