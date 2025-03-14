import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:memorymatch/services/firestore_service.dart';

class GameModel extends ChangeNotifier {
  List<String> cards = [];
  List<bool> flipped = [];
  List<bool> matched = [];
  int pairsFound = 0;
  int score = 0;
  int level = 1;
  int timeLeft = 60;
  Timer? timer;
  bool isNewGame = true;
  bool justCompletedLevel = false;

  final FirestoreService _firestoreService = FirestoreService();

  GameModel() {
    _initializeGame();
  }

  void _initializeGame() {
    level = 1;
    score = 0;
    timeLeft = _calculateTimeForLevel(level);
    pairsFound = 0;
    isNewGame = true;
    justCompletedLevel = false;
    _generateCards();
  }

  int _calculateTimeForLevel(int level) {
    int baseTime = 60;
    int additionalTime = (level - 1) * 15;
    int maxTime = 120;
    return (baseTime + additionalTime).clamp(60, maxTime);
  }

  void _generateCards() {
    cards.clear();
    flipped.clear();
    matched.clear();

    int gridSize = (level + 1) * level;
    int pairs = gridSize ~/ 2;
    List<String> letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');
    List<String> selectedLetters = letters.take(pairs).toList();

    cards = [...selectedLetters, ...selectedLetters]..shuffle();

    flipped = List.generate(gridSize, (index) => false);
    matched = List.generate(gridSize, (index) => false);
    notifyListeners();
  }

  void resetCurrentLevel() {
    timer?.cancel();
    pairsFound = 0;
    timeLeft = _calculateTimeForLevel(level);
    _generateCards();
    startTimer();
    notifyListeners();
  }

  void startTimer() {
    timer?.cancel();
    timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (timeLeft > 0) {
        timeLeft--;
        notifyListeners();
      } else {
        timer.cancel();
      }
    });
  }

  void pauseGame() {
    timer?.cancel();
    notifyListeners();
  }

  void resumeGame() {
    startTimer();
    notifyListeners();
  }

  void resetGame() {
    timer?.cancel();
    _initializeGame();
    notifyListeners();
  }

  void nextLevel() {
    timer?.cancel();
    level++;
    score += 10 * level;
    timeLeft = _calculateTimeForLevel(level);
    pairsFound = 0;
    isNewGame = false;
    justCompletedLevel = true;
    _generateCards();
    startTimer();
    notifyListeners();
  }

  void checkMatch(int index) {
    if (flipped[index] || matched[index]) return;

    flipped[index] = true;
    notifyListeners();

    List<int> flippedIndices = [];
    for (int i = 0; i < flipped.length; i++) {
      if (flipped[i] && !matched[i]) {
        flippedIndices.add(i);
      }
    }

    if (flippedIndices.length == 2) {
      int firstIndex = flippedIndices[0];
      int secondIndex = flippedIndices[1];

      if (cards[firstIndex] == cards[secondIndex] &&
          cards[firstIndex].isNotEmpty) {
        matched[firstIndex] = true;
        matched[secondIndex] = true;
        pairsFound++;
        score += 10;
        notifyListeners();
      } else {
        Future.delayed(const Duration(milliseconds: 1000), () {
          flipped[firstIndex] = false;
          flipped[secondIndex] = false;
          notifyListeners();
        });
      }
    }
  }

  Future<void> saveGameState() async {
    try {
      await _firestoreService.saveGameState({
        'level': level,
        'score': score,
        'timeLeft': timeLeft,
        'cards': cards,
        'flipped': flipped,
        'matched': matched,
        'pairsFound': pairsFound,
      });
      await _firestoreService.updateUserScore(score);
    } catch (e) {
      debugPrint('Error saving game state: $e');
    }
  }

  Future<void> loadGameState() async {
    try {
      final data = await _firestoreService.loadGameState();
      if (data != null) {
        level = data['level'] ?? 1;
        score = data['score'] ?? 0;
        timeLeft = data['timeLeft'] ?? _calculateTimeForLevel(level);
        cards = List<String>.from(data['cards'] ?? []);
        flipped = List<bool>.from(data['flipped'] ?? []);
        matched = List<bool>.from(data['matched'] ?? []);
        pairsFound = data['pairsFound'] ?? 0;
        isNewGame = false;
        justCompletedLevel = false;
        notifyListeners();
      } else {
        _initializeGame();
      }
    } catch (e) {
      debugPrint('Error loading game state: $e');
      _initializeGame();
    }
  }

  @override
  void dispose() {
    timer?.cancel();
    super.dispose();
  }
}
