import 'package:flutter/material.dart';
import 'models/Product.dart';
import 'models/Cart.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class ProductDetailsAPI extends StatefulWidget {
  Product product;

  ProductDetailsAPI({Key? key, required this.product}) : super(key: key);
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return TrangthaiSPAPI();
  }
}

enum ColorLabel {
  blue('Blue', Colors.blue),
  pink('Pink', Colors.pink),
  green('Green', Colors.green),
  yellow('Orange', Colors.orange),
  grey('Grey', Colors.grey);

  const ColorLabel(this.label, this.color);
  final String label;
  final Color color;
}

class TrangthaiSPAPI extends State<ProductDetailsAPI> {
  late Product product;
  late Future<List<Product>> relatedProducts;

  //List<Product> relatedProducts = [];

  ColorLabel? selectedColor;
  final TextEditingController colorController = TextEditingController();

  Future<List<Product>> LayDulieutuBackend() async {
    final response = await http
        .get(Uri.parse("https://6731c05f7aaf2a9aff11dd05.mockapi.io/products"));

    if (response.statusCode == 200) {
      List<dynamic> jsonData = json.decode(response.body);
      List<Product> allProducts =
          jsonData.map((item) => Product.fromJsonMocki(item)).toList();

      // Lọc danh sách sản phẩm liên quan
      List<Product> filteredProducts = allProducts
          .where((p) => p.category == product.category && p.id != product.id)
          .toList();

      return filteredProducts;
    } else {
      throw Exception('Không đọc được sản phẩm từ backend');
    }
  }

  @override
  void initState() {
    super.initState();
    product = widget.product;
    relatedProducts = LayDulieutuBackend();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(product.title ?? "No Title")),
      body: SingleChildScrollView(
        padding: EdgeInsets.all(16.0),
        child: Column(
          children: [
            Hero(
              tag: product.id,
              child: Image.network(
                product.image ?? 'assets/default_image.png',
                height: 250,
                fit: BoxFit.cover,
              ),
            ),
            CircularProgressIndicator(),
            SizedBox(height: 20),
            Text(
              product.title ?? "No Title",
              style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 10),
            Text(
              product.price != null
                  ? "\$${product.price!.toStringAsFixed(2)}"
                  : "\$0.00",
              style: TextStyle(fontSize: 20, color: Colors.green),
            ),
            SizedBox(height: 20),
            ElevatedButton.icon(
              onPressed: () {
                Cart.addToCart(product);
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text('${product.title} added to cart!')),
                );
              },
              icon: Icon(Icons.add_shopping_cart),
              label: Text("Add to Cart"),
            ),
            SizedBox(height: 30),
            Text("Sản phẩm liên quan:",
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
            SizedBox(height: 10),
            Container(
              height: 200,
              child: FutureBuilder<List<Product>>(
                future: relatedProducts,
                builder: (context, snapshot) {
                  if (snapshot.connectionState == ConnectionState.waiting) {
                    return Center(child: CircularProgressIndicator());
                  } else if (snapshot.hasError) {
                    return Center(child: Text('Lỗi: ${snapshot.error}'));
                  } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
                    return Center(child: Text('Không có dữ liệu'));
                  } else {
                    return ListView.builder(
                      scrollDirection: Axis.horizontal,
                      itemCount: snapshot.data!.length,
                      itemBuilder: (context, index) {
                        Product relatedProduct = snapshot.data![index];
                        return GestureDetector(
                          onTap: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) =>
                                    ProductDetailsAPI(product: relatedProduct),
                              ),
                            );
                          },
                          child: Container(
                            margin: EdgeInsets.symmetric(horizontal: 10),
                            width: 150,
                            decoration: BoxDecoration(
                              color: Color(0xffebf0f2),
                              borderRadius: BorderRadius.circular(15.0),
                              boxShadow: [
                                BoxShadow(
                                  color: Colors.grey.withOpacity(0.5),
                                  spreadRadius: 2,
                                  blurRadius: 5,
                                  offset: Offset(0, 3),
                                ),
                              ],
                            ),
                            child: Column(
                              children: [
                                Expanded(
                                  child: ClipRRect(
                                    borderRadius: BorderRadius.vertical(
                                        top: Radius.circular(15.0)),
                                    child: Image.network(
                                      relatedProduct.image ??
                                          'assets/default_image.png',
                                      fit: BoxFit.cover,
                                    ),
                                  ),
                                ),
                                Padding(
                                  padding: EdgeInsets.all(8.0),
                                  child: Text(
                                    relatedProduct.title ?? "No Title",
                                    textAlign: TextAlign.center,
                                    style: TextStyle(
                                        fontSize: 14,
                                        fontWeight: FontWeight.bold),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        );
                      },
                    );
                  }
                },
              ),
            ),
            SizedBox(height: 20),
          ],
        ),
      ),
    );
  }
}
