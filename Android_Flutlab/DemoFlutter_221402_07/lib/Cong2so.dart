import 'package:flutter/material.dart'; /*Thư viện giao diện Android*/
import 'package:flutter/cupertino.dart'; /*Thư viện giao diện iOS*/

class Cong2so extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return TrangthaiCong();
  }
}

class TrangthaiCong extends State<Cong2so> {
  TextEditingController txt1 = TextEditingController();
  TextEditingController txt2 = TextEditingController();

  double ketqua = 0;
  String message = "";
  bool? namnu = false;

  void congso() {
    try {
      double a = double.parse(txt1.text);
      double b = double.parse(txt2.text);
      double c = a + b;
      setState(() {
        ketqua = c;
        message = "";
      });
    } catch (e) {
      setState(() {
        ketqua = 0;
        message = e.toString();
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return (ListView(
      children: [
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text("CỘNG 2 SỐ",
              style: TextStyle(
                  fontFamily: "Tahoma",
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                  color: Colors.blue)),
        ),
        Padding(
          padding: const EdgeInsets.all(10),
          child: TextField(
            controller: txt1,
            decoration: InputDecoration(hintText: "Số thứ nhất"),
            style: TextStyle(fontSize: 20),
          ),
        ),
        Padding(
          padding: const EdgeInsets.all(10),
          child: TextField(
            controller: txt2,
            decoration: InputDecoration(hintText: "Số thứ hai"),
            style: TextStyle(fontSize: 20),
          ),
        ),
        SizedBox(height: 10),
        Text(ketqua.toString(), style: TextStyle(fontSize: 20)),
        Text(message, style: TextStyle(fontSize: 18, color: Colors.red)),
        SizedBox(height: 10),
        Checkbox(
          isError: true,
          tristate: true,
          value: namnu,
          onChanged: (bool? value) {
            setState(() {
              namnu = value;
            });
          },
        ),
        (namnu == true)
            ? Container(
                width: 100,
                height: 100,
                child: AspectRatio(
                  aspectRatio: 1,
                  child: Image.network(
                      fit: BoxFit.contain,
                      "https://cdn-icons-png.flaticon.com/512/2042/2042895.png"),
                ),
              )
            : Container(
                width: 100,
                height: 100,
                child: Image.network(
                    fit: BoxFit.contain,
                    "https://as1.ftcdn.net/v2/jpg/01/54/63/22/1000_F_154632264_mzeBfVl6QdkDLhVKWOAkTZvTIgICwfwZ.jpg"),
              ),
        SizedBox(height: 20),
        ElevatedButton(
          onPressed: congso,
          child: Text("Cộng 2 số", style: TextStyle(fontSize: 20)),
          style: ElevatedButton.styleFrom(backgroundColor: Color(0xffb0ab7e)),
        )
      ],
    ));
  }
}
