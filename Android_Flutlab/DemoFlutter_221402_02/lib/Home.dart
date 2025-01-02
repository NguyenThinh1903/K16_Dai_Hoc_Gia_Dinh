import 'package:flutter/material.dart';

class Home extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
        child: ListView(
      children: [
        Image.network(
            "https://giadinh.edu.vn/thumbs/1366x554x1/upload/photo/cover-nam-moi-fn-web-3375.jpg"),
        Text("Xin chào các bạn 221402"),
        Text("Hàng 1"),
        Text("Hàng 2"),
        Text("Hàng 3"),
      ],
    ));
  }
}
