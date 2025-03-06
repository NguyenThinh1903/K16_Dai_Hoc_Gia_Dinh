import 'package:flutter/material.dart';
import 'models/MyCrypto.dart';

class TestHashing extends StatelessWidget {
  TextEditingController txt1 = TextEditingController();
  TextEditingController txt2 = TextEditingController();

  void Hasing() {
    String data_input = txt1.text.toString();
    String data_output = MyCrypto.hashText(data_input);
    print(data_output);
    txt2.text = data_output;
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
      appBar: AppBar(
          title: Center(
        child: Text(
          "TEST HÀM BĂM",
          style:
              TextStyle(color: Colors.deepOrange, fontWeight: FontWeight.bold),
        ),
      )),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: txt1,
              decoration: InputDecoration(hintText: "Dữ liệu cần băm"),
              style: TextStyle(fontSize: 20),
            ),
            SizedBox(
              height: 10,
            ),
            TextField(
              controller: txt2,
              decoration: InputDecoration(hintText: "Kết quả băm"),
              style: TextStyle(fontSize: 20),
            ),
            SizedBox(
              height: 10,
            ),
            ElevatedButton(
                onPressed: Hasing,
                child: Text(
                  "Thực hiện băm",
                  style: TextStyle(fontSize: 20),
                ))
          ],
        ),
      ),
    );
  }
}
