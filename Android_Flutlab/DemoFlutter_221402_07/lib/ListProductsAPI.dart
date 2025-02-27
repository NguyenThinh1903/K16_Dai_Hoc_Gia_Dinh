import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'models/Product.dart';
import 'ProductDetailsAPI.dart';

class ListProductsAPI extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return Trangthai_ListProductsAPI();
  }
}

class Trangthai_ListProductsAPI extends State<ListProductsAPI> {
  late Future<List<Product>> lstproducts;

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

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
      appBar: AppBar(title: Text("Danh sách sản phẩm từ API")),
      body: FutureBuilder(
        future: lstproducts,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Text("Em đang tải dữ liệu. Các anh chờ em xíu!");
          } else if (snapshot.hasError) {
            return Text("Em bị lỗi:" + snapshot.error.toString());
          } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return Text("Không có dữ liệu");
          } else {
            List<Product> lst = snapshot.data!;
            return Container(
              margin: EdgeInsets.all(10.0),
              height: 500,
              width: 400,
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(20.0),
                color: Color(0xffcf83a9),
              ),
              child: GridView.builder(
                itemCount: lst.length,
                gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 2,
                    childAspectRatio: 0.75,
                    crossAxisSpacing: 10,
                    mainAxisSpacing: 10),
                itemBuilder: (context, index) {
                  Product product = lst[index];
                  return Card(
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(15.0),
                      ),
                      elevation: 5.0, // Hiệu ứng đổ bóng cho sản phẩm
                      child: Column(
                        children: [
                          Expanded(
                              child: Container(
                            padding: EdgeInsets.all(5.0),
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(10.0),
                            ),
                            child: GestureDetector(
                              onTap: () {
                                Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                        builder: (_) => ProductDetailsAPI(
                                            product: lst[index])));
                              },
                              child: Image.network(product.image ?? "",
                                  fit: BoxFit.fill),
                            ),
                          )),
                          ListTile(
                            title: Text(
                              product.title ?? "",
                              maxLines: 1,
                              overflow: TextOverflow.ellipsis,
                              style: TextStyle(
                                  fontWeight: FontWeight.bold, fontSize: 16),
                            ),
                            subtitle: Text(
                              "\$ " + (product.price ?? 0).toStringAsFixed(2),
                              style: TextStyle(color: Colors.red),
                            ),
                            trailing: IconButton(
                                onPressed: () {},
                                icon: Icon(Icons.shopping_cart_outlined)),
                          ),
                        ],
                      ));
                },
              ),
            );
          }
        },
      ),
    );
  }
}
