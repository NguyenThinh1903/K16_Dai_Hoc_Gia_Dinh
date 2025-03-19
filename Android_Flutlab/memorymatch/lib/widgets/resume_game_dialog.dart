import 'package:flutter/material.dart';
import 'package:memorymatch/models/game_model.dart';

class ResumeGameDialog extends StatelessWidget {
  final BuildContext parentContext;
  final GameModel model;
  final VoidCallback onResume;
  final VoidCallback onReset;

  const ResumeGameDialog({
    super.key,
    required this.parentContext,
    required this.model,
    required this.onResume,
    required this.onReset,
  });

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
              color: Color(0xFFF4A261),
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
                        color: Color(0xFFF4A261),
                        size: 50,
                      ),
                      const SizedBox(height: 10),
                      const Text(
                        'Game Paused',
                        style: TextStyle(
                          fontFamily: 'Amatic SC',
                          fontSize: 30,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFFF4A261),
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
                            'Continue',
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
                        scale: resetScale,
                        duration: const Duration(milliseconds: 100),
                        child: ElevatedButton(
                          onPressed: () {
                            Navigator.pop(dialogContext);
                            model.resetCurrentLevel(); // Call resetCurrentLevel
                            onReset();
                          },
                          onHover: (hovered) {
                            setDialogState(() {
                              resetScale = hovered ? 1.1 : 1.0;
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
                            'Reset',
                            style: TextStyle(
                              fontFamily: 'Amatic SC',
                              fontSize: 20,
                              color: Color(0xFF3F4238),
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