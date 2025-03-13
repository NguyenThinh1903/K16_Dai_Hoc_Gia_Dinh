import 'package:flutter/material.dart';
import 'package:carousel_slider/carousel_slider.dart';
// import 'ListProducts.dart';
import 'CartScreen.dart';
import 'ListProducts_Admin.dart';
import 'ListProductsAPI.dart';
import 'ListProductsFirebase.dart';
import 'chat_screen.dart';

//import 'package:flutter/cupertino.dart';

class Layout extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return Layout_State();
  }
}

class Layout_State extends State<Layout> {
  int _selectedIndex = 0;

  final List<Widget> _pages = [
    ListProductsAPI(),
    ListProductsFirebase(),
    CartScreen()
  ];

  final List<String> _carouselImages = [
    'https://giadinh.edu.vn/upload/photo/logogdu-02-5690.png',
    'https://giadinh.edu.vn/thumbs/1366x554x1/upload/photo/ma-dan-qr-in-web-77490.jpg',
    'https://giadinh.edu.vn/thumbs/1366x554x1/upload/photo/cover-nam-moi-fn-web-3375.jpg',
  ];

  // Function to handle tab switching
  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: PreferredSize(
          preferredSize: Size.fromHeight(150), // Adjust app bar height
          child: AppBar(
            toolbarHeight: 150, // Set the height of the toolbar
            title: CarouselSlider(
              items: _carouselImages
                  .map((imageUrl) => ClipRRect(
                        borderRadius: BorderRadius.circular(5.0),
                        child: AspectRatio(
                          aspectRatio:
                              16 / 7, // Keep the aspect ratio consistent
                          child: Image.network(
                            imageUrl,
                            fit: BoxFit
                                .cover, // Fit the image to cover the width
                          ),
                        ),
                      ))
                  .toList(),
              options: CarouselOptions(
                height: 150, // Set carousel height to match the app bar
                autoPlay: true,
                autoPlayInterval: Duration(seconds: 3),
                enlargeCenterPage: true,
                viewportFraction: 1.0,
              ),
            ),
            actions: [
              PopupMenuButton<String>(
                onSelected: (value) {
                  if (value == 'products') {
                    // ScaffoldMessenger.of(context).showSnackBar(
                    //     SnackBar(content: Text('Chuyển đến Quản lý sản phẩm')));
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => ListProducts_Admin()),
                    );

                    // Điều hướng sang trang Quản lý sản phẩm nếu có
                  } else if (value == 'categories') {
                    ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('Chuyển đến Quản lý danh mục')));
                    // Điều hướng sang trang Quản lý danh mục nếu có
                  } else if (value == 'chatbot') {
                    // ScaffoldMessenger.of(context).showSnackBar(
                    //     SnackBar(content: Text('Chuyển đến Quản lý danh mục')));
                    // Điều hướng sang trang Quản lý danh mục nếu có
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => ChatScreen()),
                    );
                  }
                },
                itemBuilder: (BuildContext context) => <PopupMenuEntry<String>>[
                  PopupMenuItem<String>(
                    value: 'products',
                    child: Text('Quản lý sản phẩm'),
                  ),
                  PopupMenuItem<String>(
                    value: 'categories',
                    child: Text('Quản lý danh mục'),
                  ),
                  PopupMenuItem<String>(
                    value: 'chatbot',
                    child: Text('Chat với AI'),
                  ),
                ],
              ),
            ],
          ),
        ),
        body: _pages[_selectedIndex],
        bottomNavigationBar: BottomNavigationBar(
          items: const <BottomNavigationBarItem>[
            BottomNavigationBarItem(
              icon: Icon(Icons.home),
              label: 'Home',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.list),
              label: 'Products',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.shopping_cart),
              label: 'Cart',
            ),
          ],
          currentIndex: _selectedIndex,
          selectedItemColor: Colors.redAccent,
          onTap: _onItemTapped,
        ));
  }
}
