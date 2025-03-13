import 'dart:async';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:memorymatch/services/firestore_service.dart';

class GameModel extends ChangeNotifier {
  final FirestoreService _firestore = FirestoreService();
  int level = 1;
  List<String> cards = [];
  List<bool> flipped = [];
  List<bool> matched = [];
  int? firstCardIndex;
  int score = 0;
  int combo = 0;
  int timeLeft = 60;
  Timer? timer;
  int pairsFound = 0;
  bool isProcessing = false;
  String? currentUserId;

  GameModel() {
    _initializeWithAuthState();
    FirebaseAuth.instance.authStateChanges().listen((user) {
      if (user != null && user.uid != currentUserId) {
        currentUserId = user.uid;
        _loadGameState();
      } else if (user == null) {
        resetGame();
        currentUserId = null;
        notifyListeners(); // Cập nhật UI khi đăng xuất
      }
    });
  }

  void _initializeWithAuthState() {
    User? user = FirebaseAuth.instance.currentUser;
    if (user != null) {
      currentUserId = user.uid;
      _loadGameState();
    } else {
      initializeGame();
    }
  }

  Future<void> _loadGameState() async {
    if (currentUserId == null) return;
    DocumentSnapshot doc =
        await FirebaseFirestore.instance
            .collection('users')
            .doc(currentUserId)
            .get();
    if (doc.exists) {
      Map<String, dynamic> data = doc.data() as Map<String, dynamic>;
      level = data['level'] ?? 1;
      score = data['score'] ?? 0;
      timeLeft = data['timeLeft'] ?? 60;
    } else {
      level = 1;
      score = 0;
      timeLeft = 60;
    }
    initializeGame();
    startTimer();
    notifyListeners();
  }

  void initializeGame() {
    int cardCount = level * 4;
    cards =
        List.generate(cardCount ~/ 2, (i) => String.fromCharCode(65 + i))
          ..addAll(
            List.generate(cardCount ~/ 2, (i) => String.fromCharCode(65 + i)),
          )
          ..shuffle();
    flipped = List.filled(cardCount, false);
    matched = List.filled(cardCount, false);
    pairsFound = 0;
    firstCardIndex = null;
    isProcessing = false;
    notifyListeners();
  }

  void startTimer() {
    timer?.cancel(); // Hủy timer cũ
    timer = Timer.periodic(const Duration(seconds: 1), (t) {
      if (timeLeft > 0) {
        timeLeft--;
        notifyListeners();
      } else {
        timer?.cancel();
        notifyListeners();
      }
    });
  }

  void checkMatch(int index) async {
    if (isProcessing ||
        flipped[index] ||
        matched[index] ||
        firstCardIndex == index)
      return;

    flipped[index] = true;
    notifyListeners();

    if (firstCardIndex == null) {
      firstCardIndex = index;
    } else {
      isProcessing = true;
      notifyListeners();
      if (cards[firstCardIndex!] == cards[index]) {
        combo++;
        score += 10 + (combo * 5);
        pairsFound++;
        matched[firstCardIndex!] = true;
        matched[index] = true;
        await _saveGameState(); // Gộp updateScore vào đây
        if (pairsFound == cards.length ~/ 2) {
          timer?.cancel();
        }
      } else {
        combo = 0;
        await Future.delayed(const Duration(seconds: 1));
        flipped[firstCardIndex!] = false;
        flipped[index] = false;
      }
      firstCardIndex = null;
      isProcessing = false;
      notifyListeners();
    }
  }

  Future<void> _saveGameState() async {
    if (currentUserId == null) return;
    try {
      await FirebaseFirestore.instance
          .collection('users')
          .doc(currentUserId)
          .set({
            'score': score,
            'level': level,
            'timeLeft': timeLeft,
            'lastUpdated': FieldValue.serverTimestamp(),
            'email': FirebaseAuth.instance.currentUser?.email ?? 'Anonymous',
          }, SetOptions(merge: true));
    } catch (e) {
      throw Exception('Không thể lưu trạng thái trò chơi: $e');
    }
  }

  void resetGame() {
    level = 1;
    score = 0;
    combo = 0;
    timeLeft = 60;
    timer?.cancel(); // Hủy timer khi reset
    initializeGame();
    startTimer();
    _saveGameState();
    notifyListeners();
  }

  void nextLevel() {
    level++;
    timeLeft = 60;
    timer?.cancel();
    initializeGame();
    startTimer();
    notifyListeners();
  }

  @override
  void dispose() {
    timer?.cancel(); // Hủy timer khi dispose
    super.dispose();
  }
}
