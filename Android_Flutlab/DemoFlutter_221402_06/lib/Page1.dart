import 'package:flutter/material.dart';
import 'TextSection.dart';
import 'ImageSection.dart';
import 'TitleSection.dart';
import 'ButtonSection.dart';

class Test {
  final int a;
  final int b;
  const Test(this.a, this.b);
}

class Page1 extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    print("----------------------------");
    Test obj1 = const Test(1, 2);
    Test obj2 = const Test(1, 2);
    Test obj3 = const Test(1, 2);

    if ((obj1 == obj2) && (obj2 == obj3))
      print("Các object là một");
    else
      print("Các object là KHÁC NHAU");

    // TODO: implement build
    return ListView(children: [
      const ImageSection(
          image:
              "https://i1-thethao.vnecdn.net/2025/01/02/viet-nam-thai-lan-giao-huu-202-1128-6035-1735760624.jpg?w=1020&h=0&q=100&dpr=1&fit=crop&s=KbviV-lc-M8d76F_shaQ3w"),
      const TitleSection(
        name: 'SVĐ Việt Trì',
        location: 'Việt trì, Phú Thọ',
      ),
      const ButtonSection(),
      const Center(
        child: TextSection(
            description:
                "Có một mùa hoa cải.\nNở rộ trên bến sông.\nEm đang thì con gái.\nĐợi anh chưa lấy chồng"),
      ),
    ]);
  }
}
