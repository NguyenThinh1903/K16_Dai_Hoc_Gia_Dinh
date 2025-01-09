import 'package:flutter/material.dart';

class Home extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
        child: ListView(
      children: [
        GestureDetector(
          onTap: () {
            print("Sự kiện nhấn vào ảnh");
          },
          child: Image.network(
              "https://giadinh.edu.vn/upload/elfinder/Chi%E1%BA%BFn%202024/Mammathon/DSC_0054.jpg"),
        ),
        Text("Xin chào các bạn 221402"),
        Text("Hàng 1"),
        Text("Hàng 2"),
        Text("Hàng 3"),
      ],
    ));
  }
}
