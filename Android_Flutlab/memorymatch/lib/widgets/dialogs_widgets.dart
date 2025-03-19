import 'package:flutter/material.dart';
import 'package:memorymatch/controllers/game_controller.dart';
import 'package:memorymatch/models/game_model.dart';

class LevelCompleteDialog extends StatelessWidget {
  final BuildContext parentContext;
  final GameModel model;
  final GameController controller;
  final VoidCallback onDialogClosed;

  const LevelCompleteDialog({
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
        double nextLevelScale = 1.0;
        double leaderboardScale = 1.0;
        double exitScale = 1.0;

        return Dialog(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(20),
            side: const BorderSide(
              color: Color(0xFFA9C5A0),
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
                        color: Color(0xFFF4A261),
                        size: 50,
                      ),
                      const SizedBox(height: 10),
                      const Text(
                        'Level Complete!',
                        style: TextStyle(
                          fontFamily: 'Amatic SC',
                          fontSize: 30,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFFA9C5A0),
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
                            backgroundColor: const Color(0xFFA9C5A0),
                            foregroundColor: const Color(0xFF829B7A),
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
