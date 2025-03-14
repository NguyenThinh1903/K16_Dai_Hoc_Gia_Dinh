// lib/widgets/dialogs_widgets.dart
import 'package:flutter/material.dart';
import 'package:memorymatch/controllers/game_controller.dart';
import 'package:memorymatch/models/game_model.dart';

class LevelCompleteDialog extends StatelessWidget {
  final BuildContext parentContext;
  final GameModel model;
  final GameController controller;
  final VoidCallback onDialogClosed;

  const LevelCompleteDialog({
    Key? key,
    required this.parentContext,
    required this.model,
    required this.controller,
    required this.onDialogClosed,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return StatefulBuilder(
      builder: (dialogContext, setDialogState) {
        double nextLevelScale = 1.0;
        double leaderboardScale = 1.0;
        double exitScale = 1.0;

        return Dialog(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(20),
            side: const BorderSide(
              color: Color(0xFFA9C5A0), // Pastel Green
              width: 2,
              style: BorderStyle.solid,
            ),
          ),
          backgroundColor: Colors.white,
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 350),
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Column(
                    children: [
                      const Icon(
                        Icons.star,
                        color: Color(0xFFF4A261), // Pastel Orange
                        size: 50,
                      ),
                      const SizedBox(height: 10),
                      const Text(
                        'Level Complete!',
                        style: TextStyle(
                          fontFamily: 'Amatic SC',
                          fontSize: 30,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFFA9C5A0), // Pastel Green
                        ),
                        textAlign: TextAlign.center,
                      ),
                    ],
                  ),
                  const SizedBox(height: 10),
                  Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Text(
                        'Score: ${model.score}',
                        style: const TextStyle(
                          fontFamily: 'Amatic SC',
                          fontSize: 24,
                          color: Color(0xFF3F4238), // Dark Gray
                        ),
                      ),
                      Text(
                        'Level: ${model.level}',
                        style: const TextStyle(
                          fontFamily: 'Amatic SC',
                          fontSize: 24,
                          color: Color(0xFF3F4238), // Dark Gray
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 20),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      AnimatedScale(
                        scale: nextLevelScale,
                        duration: const Duration(milliseconds: 100),
                        child: ElevatedButton(
                          onPressed: () {
                            Navigator.pop(dialogContext);
                            model.nextLevel();
                            onDialogClosed();
                          },
                          onHover: (hovered) {
                            setDialogState(() {
                              nextLevelScale = hovered ? 1.1 : 1.0;
                            });
                          },
                          style: ElevatedButton.styleFrom(
                            backgroundColor: const Color(
                              0xFFA9C5A0,
                            ), // Pastel Green
                            foregroundColor: const Color(
                              0xFF829B7A,
                            ), // Pastel Green Dark
                            padding: const EdgeInsets.symmetric(
                              horizontal: 15,
                              vertical: 10,
                            ),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(10),
                              side: const BorderSide(
                                color: Color(0xFF829B7A),
                                width: 2,
                              ),
                            ),
                          ),
                          child: const Text(
                            'Next Level',
                            style: TextStyle(
                              fontFamily: 'Amatic SC',
                              fontSize: 20,
                              color: Color(0xFF3F4238), // Dark Gray
                            ),
                          ),
                        ),
                      ),
                      const SizedBox(width: 8),
                      AnimatedScale(
                        scale: leaderboardScale,
                        duration: const Duration(milliseconds: 100),
                        child: TextButton(
                          onPressed: () async {
                            Navigator.pop(dialogContext); // Đóng dialog trước
                            await controller.goToLeaderboard(
                              parentContext,
                            ); // Chờ điều hướng
                            onDialogClosed();
                          },
                          onHover: (hovered) {
                            setDialogState(() {
                              leaderboardScale = hovered ? 1.1 : 1.0;
                            });
                          },
                          child: const Text(
                            'Leaderboard',
                            style: TextStyle(
                              fontFamily: 'Amatic SC',
                              fontSize: 20,
                              color: Color(0xFFF4A261), // Pastel Orange
                            ),
                          ),
                        ),
                      ),
                      const SizedBox(width: 8),
                      AnimatedScale(
                        scale: exitScale,
                        duration: const Duration(milliseconds: 100),
                        child: TextButton(
                          onPressed: () async {
                            model.timer?.cancel();
                            Navigator.pop(dialogContext); // Đóng dialog trước
                            await controller.goToHome(
                              parentContext,
                            ); // Chờ điều hướng
                            onDialogClosed();
                          },
                          onHover: (hovered) {
                            setDialogState(() {
                              exitScale = hovered ? 1.1 : 1.0;
                            });
                          },
                          child: const Text(
                            'Exit',
                            style: TextStyle(
                              fontFamily: 'Amatic SC',
                              fontSize: 20,
                              color: Color(0xFF9A9A9A), // Light Gray
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),
        );
      },
    );
  }
}

class GameOverDialog extends StatelessWidget {
  final BuildContext parentContext;
  final GameModel model;
  final GameController controller;
  final VoidCallback onDialogClosed;

  const GameOverDialog({
    Key? key,
    required this.parentContext,
    required this.model,
    required this.controller,
    required this.onDialogClosed,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return StatefulBuilder(
      builder: (dialogContext, setDialogState) {
        double restartScale = 1.0;
        double leaderboardScale = 1.0;
        double exitScale = 1.0;

        return Dialog(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(20),
            side: const BorderSide(
              color: Color(0xFFE5989B), // Pastel Red
              width: 2,
              style: BorderStyle.solid,
            ),
          ),
          backgroundColor: Colors.white,
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 350),
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Column(
                    children: [
                      const Icon(
                        Icons.close,
                        color: Color(0xFFE5989B), // Pastel Red
                        size: 50,
                      ),
                      const SizedBox(height: 10),
                      const Text(
                        'Game Over',
                        style: TextStyle(
                          fontFamily: 'Amatic SC',
                          fontSize: 30,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFFE5989B), // Pastel Red
                        ),
                        textAlign: TextAlign.center,
                      ),
                    ],
                  ),
                  const SizedBox(height: 10),
                  Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Text(
                        'Score: ${model.score}',
                        style: const TextStyle(
                          fontFamily: 'Amatic SC',
                          fontSize: 24,
                          color: Color(0xFF3F4238), // Dark Gray
                        ),
                      ),
                      Text(
                        'Level: ${model.level}',
                        style: const TextStyle(
                          fontFamily: 'Amatic SC',
                          fontSize: 24,
                          color: Color(0xFF3F4238), // Dark Gray
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 20),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      AnimatedScale(
                        scale: restartScale,
                        duration: const Duration(milliseconds: 100),
                        child: ElevatedButton(
                          onPressed: () {
                            Navigator.pop(dialogContext);
                            model.resetGame();
                            onDialogClosed();
                          },
                          onHover: (hovered) {
                            setDialogState(() {
                              restartScale = hovered ? 1.1 : 1.0;
                            });
                          },
                          style: ElevatedButton.styleFrom(
                            backgroundColor: const Color(
                              0xFFE5989B,
                            ), // Pastel Red
                            foregroundColor: const Color(
                              0xFFC9787A,
                            ), // Pastel Red Dark
                            padding: const EdgeInsets.symmetric(
                              horizontal: 15,
                              vertical: 10,
                            ),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(10),
                              side: const BorderSide(
                                color: Color(0xFFC9787A),
                                width: 2,
                              ),
                            ),
                          ),
                          child: const Text(
                            'Restart',
                            style: TextStyle(
                              fontFamily: 'Amatic SC',
                              fontSize: 20,
                              color: Color(0xFF3F4238), // Dark Gray
                            ),
                          ),
                        ),
                      ),
                      const SizedBox(width: 8),
                      AnimatedScale(
                        scale: leaderboardScale,
                        duration: const Duration(milliseconds: 100),
                        child: TextButton(
                          onPressed: () async {
                            Navigator.pop(dialogContext); // Đóng dialog trước
                            await controller.goToLeaderboard(
                              parentContext,
                            ); // Chờ điều hướng
                            onDialogClosed();
                          },
                          onHover: (hovered) {
                            setDialogState(() {
                              leaderboardScale = hovered ? 1.1 : 1.0;
                            });
                          },
                          child: const Text(
                            'Leaderboard',
                            style: TextStyle(
                              fontFamily: 'Amatic SC',
                              fontSize: 20,
                              color: Color(0xFFF4A261), // Pastel Orange
                            ),
                          ),
                        ),
                      ),
                      const SizedBox(width: 8),
                      AnimatedScale(
                        scale: exitScale,
                        duration: const Duration(milliseconds: 100),
                        child: TextButton(
                          onPressed: () async {
                            model.timer?.cancel();
                            Navigator.pop(dialogContext); // Đóng dialog trước
                            await controller.goToHome(
                              parentContext,
                            ); // Chờ điều hướng
                            onDialogClosed();
                          },
                          onHover: (hovered) {
                            setDialogState(() {
                              exitScale = hovered ? 1.1 : 1.0;
                            });
                          },
                          child: const Text(
                            'Exit',
                            style: TextStyle(
                              fontFamily: 'Amatic SC',
                              fontSize: 20,
                              color: Color(0xFF9A9A9A), // Light Gray
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),
        );
      },
    );
  }
}

// Dialog mới: ResumeGameDialog
class ResumeGameDialog extends StatelessWidget {
  final BuildContext parentContext;
  final GameModel model;
  final VoidCallback onResume;
  final VoidCallback onReset;

  const ResumeGameDialog({
    Key? key,
    required this.parentContext,
    required this.model,
    required this.onResume,
    required this.onReset,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return StatefulBuilder(
      builder: (dialogContext, setDialogState) {
        double resumeScale = 1.0;
        double resetScale = 1.0;

        return Dialog(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(20),
            side: const BorderSide(
              color: Color(0xFFF4A261), // Pastel Orange
              width: 2,
              style: BorderStyle.solid,
            ),
          ),
          backgroundColor: Colors.white,
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 350),
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Column(
                    children: [
                      const Icon(
                        Icons.pause,
                        color: Color(0xFFF4A261), // Pastel Orange
                        size: 50,
                      ),
                      const SizedBox(height: 10),
                      const Text(
                        'Game Paused',
                        style: TextStyle(
                          fontFamily: 'Amatic SC',
                          fontSize: 30,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFFF4A261), // Pastel Orange
                        ),
                        textAlign: TextAlign.center,
                      ),
                    ],
                  ),
                  const SizedBox(height: 10),
                  Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Text(
                        'Score: ${model.score}',
                        style: const TextStyle(
                          fontFamily: 'Amatic SC',
                          fontSize: 24,
                          color: Color(0xFF3F4238), // Dark Gray
                        ),
                      ),
                      Text(
                        'Level: ${model.level}',
                        style: const TextStyle(
                          fontFamily: 'Amatic SC',
                          fontSize: 24,
                          color: Color(0xFF3F4238), // Dark Gray
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 20),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      AnimatedScale(
                        scale: resumeScale,
                        duration: const Duration(milliseconds: 100),
                        child: ElevatedButton(
                          onPressed: () {
                            Navigator.pop(dialogContext);
                            onResume();
                          },
                          onHover: (hovered) {
                            setDialogState(() {
                              resumeScale = hovered ? 1.1 : 1.0;
                            });
                          },
                          style: ElevatedButton.styleFrom(
                            backgroundColor: const Color(
                              0xFFA9C5A0,
                            ), // Pastel Green
                            foregroundColor: const Color(
                              0xFF829B7A,
                            ), // Pastel Green Dark
                            padding: const EdgeInsets.symmetric(
                              horizontal: 15,
                              vertical: 10,
                            ),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(10),
                              side: const BorderSide(
                                color: Color(0xFF829B7A),
                                width: 2,
                              ),
                            ),
                          ),
                          child: const Text(
                            'Continue',
                            style: TextStyle(
                              fontFamily: 'Amatic SC',
                              fontSize: 20,
                              color: Color(0xFF3F4238), // Dark Gray
                            ),
                          ),
                        ),
                      ),
                      const SizedBox(width: 8),
                      AnimatedScale(
                        scale: resetScale,
                        duration: const Duration(milliseconds: 100),
                        child: ElevatedButton(
                          onPressed: () {
                            Navigator.pop(dialogContext);
                            onReset();
                          },
                          onHover: (hovered) {
                            setDialogState(() {
                              resetScale = hovered ? 1.1 : 1.0;
                            });
                          },
                          style: ElevatedButton.styleFrom(
                            backgroundColor: const Color(
                              0xFFE5989B,
                            ), // Pastel Red
                            foregroundColor: const Color(
                              0xFFC9787A,
                            ), // Pastel Red Dark
                            padding: const EdgeInsets.symmetric(
                              horizontal: 15,
                              vertical: 10,
                            ),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(10),
                              side: const BorderSide(
                                color: Color(0xFFC9787A),
                                width: 2,
                              ),
                            ),
                          ),
                          child: const Text(
                            'Reset',
                            style: TextStyle(
                              fontFamily: 'Amatic SC',
                              fontSize: 20,
                              color: Color(0xFF3F4238), // Dark Gray
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),
        );
      },
    );
  }
}
