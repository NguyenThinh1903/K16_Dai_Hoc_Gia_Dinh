import 'package:flutter/material.dart';
import 'models/MyCrypto.dart';
import 'package:http/http.dart' as http;
import 'models/User.dart';
import 'dart:convert';
import 'TestHashing.dart';
import 'Layout.dart';
// import 'RegisterScreen.dart';

class LoginScreen extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return Trangthai_LoginScreen();
  }
}

class Trangthai_LoginScreen extends State<LoginScreen> {
  final TextEditingController usernameController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();
  String errorMessage = '';

  late Future<List<User>> lstusers;

  Future<List<User>> LayDsUsertuBackend() async {
    final response = await http
        .get(Uri.parse('https://6731c05f7aaf2a9aff11dd05.mockapi.io/users'));

    if (response.statusCode == 200) {
      List<dynamic> jsonData = json.decode(response.body);
      return jsonData.map((item) => User.fromJsonMocki(item)).toList();
    } else {
      throw Exception('Không đọc được danh sách người dùng từ backend');
    }
  }

  @override
  void initState() {
    super.initState();
    lstusers = LayDsUsertuBackend();
  }

  void Dangnhap() async {
    String username = usernameController.text.trim();
    String password = MyCrypto.hashText(passwordController.text.trim());

    if (username.isEmpty || password.isEmpty) {
      setState(() {
        errorMessage = "Vui lòng nhập đầy đủ thông tin đăng nhập";
      });
      return;
    }

    try {
      List<User> users = await lstusers;
      User? foundUser = users.firstWhere(
        (user) => user.name == username && user.password == password,
        orElse: () => User(name: "", password: ""),
      );

      if (foundUser.name.isNotEmpty) {
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(builder: (context) => Layout()),
        );
      } else {
        setState(() {
          errorMessage = "Tên đăng nhập hoặc mật khẩu không chính xác";
        });
      }
    } catch (e) {
      setState(() {
        errorMessage = "Lỗi kết nối đến máy chủ. Vui lòng thử lại sau";
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Center(
          child: Text(
            "ĐĂNG NHẬP HỆ THỐNG",
            style: TextStyle(
                color: Colors.deepOrange, fontWeight: FontWeight.bold),
          ),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            Image.network(
              "https://thumbs.dreamstime.com/b/login-icon-button-vector-illustration-isolated-white-background-126997728.jpg",
              width: 150,
            ),
            TextField(
              controller: usernameController,
              style: TextStyle(fontSize: 16),
              decoration: InputDecoration(labelText: 'Tên đăng nhập'),
            ),
            TextField(
              style: TextStyle(fontSize: 16),
              controller: passwordController,
              decoration: InputDecoration(labelText: 'Mật khẩu sử dụng'),
              obscureText: true,
            ),
            if (errorMessage.isNotEmpty)
              Text(errorMessage, style: TextStyle(color: Colors.red)),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: Dangnhap,
              child: Padding(
                padding: const EdgeInsets.all(10),
                child: const Text('Đăng nhập'),
              ),
              style: ElevatedButton.styleFrom(
                backgroundColor: Color(0xff9fd59f),
                textStyle: TextStyle(fontSize: 18),
              ),
            ),
            SizedBox(height: 10),
            TextButton(
              onPressed: () {
                // Navigator.push(
                //   context,
                //   MaterialPageRoute(builder: (context) => RegisterScreen()),
                // );
              },
              child: Text(
                'Chưa có tài khoản? Đăng ký ngay!',
                style: TextStyle(fontSize: 17, color: Colors.deepOrange),
              ),
            ),
            const SizedBox(height: 50),
            TextButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => TestHashing()),
                );
              },
              child: Text('Test Hàm băm'),
            ),
          ],
        ),
      ),
    );
  }
}
