import 'package:flutter/material.dart';
import 'Product.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
          appBar: AppBar(
              title: Image.network(
                  "https://giadinh.edu.vn/upload/photo/logogdu-02-5690.png")),
          body: ListView(
            children: [
              Text("Xin chào các bạn 221402"),
              Image.network(
                  "https://giadinh.edu.vn/upload/elfinder/2024/Chon%20Nghe%20Nganh%20Truong%20cung%202k7-WEB.jpg"),
              Container(
                height: MediaQuery.of(context)
                    .size
                    .height, // Đảm bảo chiều cao cố định
                child: Scaffold(
                  appBar: AppBar(
                    title: Text("Trình bày sản phẩm"),
                  ),
                  body: ListView.builder(
                      itemCount: dssanpham.length,
                      itemBuilder: (BuildContext context, int chiso) {
                        return Column(
                          children: [
                            Text(dssanpham[chiso].title.toString()),
                            Container(
                                width: 140,
                                child: Image.network(
                                    dssanpham[chiso].image.toString())),
                            Text(dssanpham[chiso].description.toString())
                          ],
                        );
                      }),
                ),
              )
            ],
          )),
    );
  }
}
