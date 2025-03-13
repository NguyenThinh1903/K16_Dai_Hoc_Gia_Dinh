import 'dart:convert';
import 'package:http/http.dart' as http;

class ChatService {
  final String apiKey =
      "AIzaSyCDQ05OxjdlqU71p9u6VuKLvGFIpADR4Rg"; // Thay bằng API key của bạn
  final String apiUrl =
      "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

  Future<String> sendMessage(String message) async {
    try {
      final response = await http.post(
        Uri.parse("$apiUrl?key=$apiKey"),
        headers: {"Content-Type": "application/json"},
        body: jsonEncode({
          "contents": [
            {
              "parts": [
                {"text": message}
              ]
            }
          ]
        }),
      );

      if (response.statusCode == 200) {
        final jsonResponse = jsonDecode(response.body);
        if (jsonResponse.containsKey('candidates') &&
            jsonResponse['candidates'].isNotEmpty &&
            jsonResponse['candidates'][0]['content']['parts'].isNotEmpty) {
          return jsonResponse['candidates'][0]['content']['parts'][0]['text'];
        } else {
          return "Lỗi: Không tìm thấy phản hồi từ AI.";
        }
      } else {
        return "Lỗi API (${response.statusCode}): ${response.body}";
      }
    } catch (e) {
      return "Lỗi kết nối: $e";
    }
  }
}
