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
  bool isNewGame = true; // Thêm trạng thái để theo dõi trò chơi mới

  GameModel() {
    _initializeWithAuthState();
    FirebaseAuth.instance.authStateChanges().listen((user) {
      if (user != null && user.uid != currentUserId) {
        currentUserId = user.uid;
        _loadGameState();
      } else if (user == null) {
        resetGame();
        currentUserId = null;
        notifyListeners();
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
      cards = List<String>.from(data['cards'] ?? []);
      flipped = List<bool>.from(data['flipped'] ?? []);
      matched = List<bool>.from(data['matched'] ?? []);
      pairsFound = data['pairsFound'] ?? 0;
      firstCardIndex = data['firstCardIndex'];
      combo = data['combo'] ?? 0;
      isNewGame = false; // Khi load từ Firestore, không phải trò chơi mới
    } else {
      level = 1;
      score = 0;
      timeLeft = 60;
      isNewGame = true;
      initializeGame();
    }
    startTimer();
    notifyListeners();
  }

  void initializeGame() {
    int gridSize = level + 1; // Level 1 -> 2x2, Level 2 -> 3x3, Level 3 -> 4x4
    int cardCount = gridSize * gridSize;
    List<String> cardPairs = List.generate(
      cardCount ~/ 2,
      (i) => String.fromCharCode(65 + i),
    );
    cards = [...cardPairs, ...cardPairs]..shuffle();
    flipped = List.filled(cardCount, false);
    matched = List.filled(cardCount, false);
    pairsFound = 0;
    firstCardIndex = null;
    combo = 0;
    isProcessing = false;
    notifyListeners();
  }

  void startTimer() {
    timer?.cancel();
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

  void pauseGame() {
    timer?.cancel();
    isNewGame = false; // Đánh dấu không phải trò chơi mới khi tạm dừng
    notifyListeners();
  }

  void resumeGame() {
    startTimer();
    notifyListeners();
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
        await saveGameState();
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

  Future<void> saveGameState() async {
    if (currentUserId == null) return;
    try {
      await FirebaseFirestore.instance
          .collection('users')
          .doc(currentUserId)
          .set({
            'score': score,
            'level': level,
            'timeLeft': timeLeft,
            'cards': cards,
            'flipped': flipped,
            'matched': matched,
            'pairsFound': pairsFound,
            'firstCardIndex': firstCardIndex,
            'combo': combo,
            'isNewGame': isNewGame, // Lưu trạng thái isNewGame
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
    timer?.cancel();
    isNewGame = true; // Đánh dấu là trò chơi mới
    initializeGame();
    startTimer();
    saveGameState();
    notifyListeners();
  }

  void nextLevel() {
    level++;
    timeLeft = 60;
    timer?.cancel();
    isNewGame = false; // Khi chuyển level, không phải trò chơi mới
    initializeGame();
    startTimer();
    notifyListeners();
  }

  @override
  void dispose() {
    timer?.cancel();
    super.dispose();
  }
}
