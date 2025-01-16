import 'package:flutter/material.dart';
import 'models/Product.dart';

class ProductDetails extends StatefulWidget {
  Product product;

  ProductDetails({Key? key, required this.product}) : super(key: key);
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return TrangthaiSP();
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

class TrangthaiSP extends State<ProductDetails> {
  Product product = Product(image: '', title: '', description: '', size: 0);

  List<Product> relatedProducts = [];

  ColorLabel? selectedColor;
  final TextEditingController colorController = TextEditingController();

  @override
  void initState() {
    super.initState();
    product = widget.product;

    relatedProducts = products.where((p) => p.id != product.id).toList();
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return (Scaffold(
      appBar: AppBar(
          title: Center(
        child: Text(
          product.title ?? "No Title",
          style: TextStyle(
              color: selectedColor?.color,
              fontSize: 20,
              fontWeight: FontWeight.bold),
        ),
      )),
      body: SingleChildScrollView(
        child: Column(
          children: [
            Center(
              child: Hero(
                tag: product.id,
                child: Image.network(
                  product.image ?? 'assets/default_image.png',
                  width: 170,
                  fit: BoxFit.cover,
                ),
              ),
            ),
            SizedBox(
              height: 20,
            ),
            Text(
              product.title ?? "Chưa đặt tên",
              style: TextStyle(
                  fontSize: 22,
                  fontWeight: FontWeight.bold,
                  color: selectedColor?.color),
            ),
            SizedBox(height: 10),
            Text(
                product.price != null
                    ? "\$${product.price!.toStringAsFixed(2)}"
                    : "\$0.00",
                style: TextStyle(fontSize: 20, color: Colors.green)),
            SizedBox(height: 10),
            DropdownMenu<ColorLabel>(
              textStyle: TextStyle(
                  color: Colors.purple, backgroundColor: selectedColor?.color),
              initialSelection: ColorLabel.green,
              controller: colorController,
              // requestFocusOnTap is enabled/disabled by platforms when it is null.
              // On mobile platforms, this is false by default. Setting this to true will
              // trigger focus request on the text field and virtual keyboard will appear
              // afterward. On desktop platforms however, this defaults to true.
              requestFocusOnTap: true,
              label: const Text('Chọn màu sắc'),
              onSelected: (ColorLabel? color) {
                setState(() {
                  selectedColor = color;
                });
              },
              dropdownMenuEntries: ColorLabel.values
                  .map<DropdownMenuEntry<ColorLabel>>((ColorLabel color) {
                return DropdownMenuEntry<ColorLabel>(
                  value: color,
                  label: color.label,
                  enabled: color.label != 'Grey',
                  style: MenuItemButton.styleFrom(
                    foregroundColor: color.color,
                  ),
                );
              }).toList(),
            ),
            SizedBox(height: 10),
            Center(
              child: ElevatedButton.icon(
                onPressed: () {
                  // Thêm sản phẩm vào giỏ hàng
                  //Cart.addToCart(product);
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(
                      content: Text('${product.title} added to cart!'),
                    ),
                  );
                },
                icon: Icon(Icons.add_shopping_cart),
                label: Text("Add to Cart"),
                style: ElevatedButton.styleFrom(
                  backgroundColor: Color(0xff2196f3),
                  foregroundColor: Colors.white,
                  padding: EdgeInsets.symmetric(horizontal: 30, vertical: 15),
                  textStyle: TextStyle(fontSize: 16),
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                product.description ?? "",
                style: TextStyle(fontSize: 16, color: Colors.blueGrey),
                textAlign: TextAlign.justify,
              ),
            ),
            SizedBox(height: 10),
            // Hiển thị sản phẩm liên quan
            Container(
              height: 200, // Đặt chiều cao cho danh sách các sản phẩm liên quan
              child: ListView.builder(
                scrollDirection: Axis.horizontal,
                itemCount: relatedProducts.length,
                itemBuilder: (BuildContext context, int index) {
                  Product p = relatedProducts[index];
                  return GestureDetector(
                    onTap: () {
                      // Điều hướng sang trang chi tiết sản phẩm khi nhấn vào sản phẩm liên quan
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => ProductDetails(product: p),
                        ),
                      );
                    },
                    child: Container(
                      margin: EdgeInsets.symmetric(horizontal: 10),
                      width: 150,
                      decoration: BoxDecoration(
                        color: const Color(0xffebf0f2),
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
                                p.image ?? 'assets/default_image.png',
                                fit: BoxFit.cover,
                              ),
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Text(
                              p.title ?? "No Title",
                              textAlign: TextAlign.center,
                              style: TextStyle(
                                fontSize: 14,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  );
                },
              ),
            ),
            SizedBox(height: 20),
          ],
        ),
      ),
    ));
  }
}
