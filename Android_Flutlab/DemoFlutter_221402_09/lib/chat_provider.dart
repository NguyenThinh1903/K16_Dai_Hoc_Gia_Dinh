import 'package:flutter/material.dart';
import 'ChatService.dart';

class ChatProvider with ChangeNotifier {
  final ChatService chatService = ChatService();
  List<Map<String, String>> messages = [];

  Future<void> sendMessage(String message) async {
    messages.add({"role": "user", "content": message});
    notifyListeners();

    String response = await chatService.sendMessage(message);
    messages.add({"role": "bot", "content": response});
    notifyListeners();
  }
}
