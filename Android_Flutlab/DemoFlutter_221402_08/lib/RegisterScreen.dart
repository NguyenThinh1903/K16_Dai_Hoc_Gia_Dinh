import 'package:flutter/material.dart';
import 'models/User.dart';
import 'models/MyCrypto.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'LoginScreen.dart';

class RegisterScreen extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return Trangthai_RegisterScreen();
  }
}

class Trangthai_RegisterScreen extends State<RegisterScreen> {
  final _formKey = GlobalKey<FormState>();
  String name = '';
  String email = '';
  String password = '';
  String repassword = '';
  String errorMessage = '';

  Future<void> register() async {
    if (name.isEmpty || email.isEmpty || password.isEmpty) {
      setState(() {
        errorMessage = 'Vui lòng điền đầy đủ thông tin';
      });
      return;
    }

    try {
      final response = await http.post(
        Uri.parse('https://6731c05f7aaf2a9aff11dd05.mockapi.io/users'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'name': name,
          'email': email,
          'password': MyCrypto.hashText(password),
        }),
      );

      if (response.statusCode == 201) {
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(builder: (context) => LoginScreen()),
        );
      } else {
        setState(() {
          errorMessage = 'Đăng ký thất bại. Vui lòng thử lại!';
        });
      }
    } catch (error) {
      setState(() {
        errorMessage = 'Lỗi kết nối. Vui lòng kiểm tra lại mạng!';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
      appBar: AppBar(
          title: Center(
        child: Text('ĐĂNG KÝ',
            style: TextStyle(
                color: Colors.deepOrange, fontWeight: FontWeight.bold)),
      )),
      body: Form(
          key: _formKey,
          child: Padding(
            padding: const EdgeInsets.all(16),
            child: Column(children: [
              TextFormField(
                style: TextStyle(fontSize: 20),
                decoration: InputDecoration(labelText: 'Tên đăng nhập'),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Tên không được để trống';
                  }
                  return null;
                },
                onChanged: (value) {
                  setState(() {
                    name = value;
                  });
                },
              ),
              TextFormField(
                style: TextStyle(fontSize: 20),
                decoration: InputDecoration(labelText: 'Email'),
                keyboardType: TextInputType.emailAddress,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Email không hợp lệ';
                  } else if (!RegExp(r"^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]+")
                      .hasMatch(value)) {
                    return 'Hãy nhập email';
                  }
                  return null;
                },
                onChanged: (value) {
                  setState(() {
                    email = value;
                  });
                },
              ),
              TextFormField(
                style: TextStyle(fontSize: 20),
                decoration: InputDecoration(labelText: 'Mật khẩu'),
                keyboardType: TextInputType.visiblePassword,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Hãy nhập mật khẩu';
                  }
                  return null;
                },
                onChanged: (value) {
                  setState(() {
                    password = value;
                  });
                },
              ),
              TextFormField(
                style: TextStyle(fontSize: 20),
                decoration: InputDecoration(labelText: 'Mật khẩu nhắc lại'),
                keyboardType: TextInputType.visiblePassword,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Hãy nhập mật khẩu nhắc lại';
                  } else if (value != password) {
                    return 'Mật khẩu nhắc lại không khớp';
                  }
                  return null;
                },
              ),
              if (errorMessage.isNotEmpty)
                Text(errorMessage, style: TextStyle(color: Colors.red)),
              SizedBox(height: 20),
              ElevatedButton(
                style: ElevatedButton.styleFrom(
                    backgroundColor: Color(0xffdfef86),
                    padding:
                        EdgeInsets.symmetric(horizontal: 50, vertical: 20)),
                onPressed: () {
                  if (_formKey.currentState?.validate() ?? false) {
                    register();
                    ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                        content: Text('Đăng ký thành công người dùng')));
                  }
                },
                child: Text(
                  'Đăng ký',
                  style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                ),
              ),
            ]),
          )),
    );
  }
}
