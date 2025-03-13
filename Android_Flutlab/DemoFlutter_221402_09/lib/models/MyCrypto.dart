import 'package:crypto/crypto.dart';
import 'dart:convert'; // for the utf8.encode method

class MyCrypto {
  static String hashText(String inputstr) {
    var bytes = utf8.encode(inputstr); // data being hashed

    var digest = sha256.convert(bytes);
    var base64 = base64UrlEncode(utf8.encode(digest.toString()));
    return base64;
  }
}
