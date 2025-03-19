import 'package:flutter/material.dart';
import 'package:memorymatch/models/game_model.dart';
import 'package:memorymatch/controllers/game_controller.dart';

class GameOverDialog extends StatelessWidget {
  final BuildContext parentContext;
  final GameModel model;
  final GameController controller;
  final VoidCallback onDialogClosed;

  const GameOverDialog({
    super.key,
    required this.parentContext,
    required this.model,
    required this.controller,
    required this.onDialogClosed,
  });

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
              color: Color(0xFFE5989B),
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
                        color: Color(0xFFE5989B),
                        size: 50,
                      ),
                      const SizedBox(height: 10),
                      const Text(
                        'Game Over',
                        style: TextStyle(
                          fontFamily: 'Amatic SC',
                          fontSize: 30,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFFE5989B),
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
                          color: Color(0xFF3F4238),
                        ),
                      ),
                      Text(
                        'Level: ${model.level}',
                        style: const TextStyle(
                          fontFamily: 'Amatic SC',
                          fontSize: 24,
                          color: Color(0xFF3F4238),
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
                            backgroundColor: const Color(0xFFE5989B),
                            foregroundColor: const Color(0xFFC9787A),
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
                              color: Color(0xFF3F4238),
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
                            Navigator.pop(dialogContext);
                            await controller.goToLeaderboard(parentContext);
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
                              color: Color(0xFFF4A261),
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
                            model.timer?.cancel(); // Ensure timer is stopped
                            Navigator.pop(dialogContext);
                            await controller.goToHome(parentContext);
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
                              color: Color(0xFF9A9A9A),
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