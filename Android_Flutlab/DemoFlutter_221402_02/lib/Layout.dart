import 'Page1.dart';
import 'package:flutter/material.dart';
import 'Home.dart';
import 'Page2.dart';

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

  final List<Widget> _pages = [Home(), Page1(), Page2()];

  // Function to handle tab switching
  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
            title: Image.network(
                "https://giadinh.edu.vn/upload/photo/logogdu-02-5690.png")),
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
