import 'package:flutter/material.dart';
import 'package:memorymatch/screens/leaderboard_screen.dart';
import 'package:memorymatch/screens/home_screen.dart';

class GameController {
  Future<void> goToLeaderboard(BuildContext context) {
    return Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => LeaderboardScreen()),
    );
  }

  Future<void> goToHome(BuildContext context) async {
    await Navigator.pushReplacement(
      context,
      MaterialPageRoute(builder: (context) => const HomeScreen()),
    );
  }
}
