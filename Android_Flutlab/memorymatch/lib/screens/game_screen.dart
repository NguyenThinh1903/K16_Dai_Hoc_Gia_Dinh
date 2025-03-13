import 'package:flutter/material.dart';
import 'package:memorymatch/models/game_model.dart';
import 'package:memorymatch/controllers/game_controller.dart';
import 'package:memorymatch/screens/leaderboard_screen.dart';
import 'package:memorymatch/screens/home_screen.dart';
import 'package:memorymatch/widgets/card_widget.dart';
import 'package:provider/provider.dart';

class GameScreen extends StatefulWidget {
  const GameScreen({super.key});

  @override
  _GameScreenState createState() => _GameScreenState();
}

class _GameScreenState extends State<GameScreen> {
  bool _dialogShown = false;

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<GameController>(context);
    final model = Provider.of<GameModel>(context);

    if (model.timeLeft <= 0 && !_dialogShown) {
      _dialogShown = true;
      WidgetsBinding.instance.addPostFrameCallback((_) {
        _showGameOverDialog(context, model, controller);
      });
    } else if (model.pairsFound == model.cards.length ~/ 2 && !_dialogShown) {
      _dialogShown = true;
      WidgetsBinding.instance.addPostFrameCallback((_) {
        _showLevelCompleteDialog(context, model, controller);
      });
    }

    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'Memory Match',
          style: TextStyle(
            fontFamily: 'Amatic SC',
            fontSize: 30,
            color: Color(0xFF3F4238), // Dark Gray
          ),
        ),
        backgroundColor: const Color(0xFFF5F1E9), // Kraft Light
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(
              Icons.exit_to_app,
              color: Color(0xFF3F4238), // Dark Gray
            ),
            onPressed: () {
              model.timer?.cancel();
              controller.goToHome(context);
            },
          ),
        ],
      ),
      body: Container(
        color: const Color(0xFFF5F1E9), // Kraft Light
        child: Column(
          children: [
            Container(
              margin: const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
              padding: const EdgeInsets.all(10),
              decoration: BoxDecoration(
                color: Colors.white,
                border: Border.all(
                  color: const Color(0xFF3F4238), // Dark Gray
                  width: 2,
                  style: BorderStyle.solid,
                ),
                borderRadius: BorderRadius.circular(10),
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
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
                    'Time: ${model.timeLeft}',
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
            ),
            Expanded(
              child: Center(
                child: ConstrainedBox(
                  constraints: const BoxConstraints(maxWidth: 400),
                  child: GridView.builder(
                    padding: const EdgeInsets.all(16),
                    shrinkWrap: true,
                    gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: model.level <= 2 ? 4 : 6,
                      crossAxisSpacing: 10,
                      mainAxisSpacing: 10,
                      childAspectRatio: 1,
                    ),
                    itemCount: model.cards.length,
                    itemBuilder: (context, index) {
                      return CardWidget(
                        text:
                            model.flipped[index] || model.matched[index]
                                ? model.cards[index]
                                : '⭐',
                        onTap: () => model.checkMatch(index),
                        isFlipped: model.flipped[index] || model.matched[index],
                      );
                    },
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  void _showLevelCompleteDialog(
    BuildContext context,
    GameModel model,
    GameController controller,
  ) {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder:
          (dialogContext) => Dialog(
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
                        ElevatedButton(
                          onPressed: () {
                            Navigator.pop(dialogContext);
                            model.nextLevel();
                            setState(() => _dialogShown = false);
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
                        const SizedBox(width: 8),
                        TextButton(
                          onPressed: () {
                            Navigator.pop(dialogContext);
                            Navigator.pushReplacement(
                              context,
                              MaterialPageRoute(
                                builder: (context) => const LeaderboardScreen(),
                              ),
                            ).then((_) {
                              if (mounted) {
                                setState(() => _dialogShown = false);
                              }
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
                        const SizedBox(width: 8),
                        TextButton(
                          onPressed: () {
                            model.timer?.cancel();
                            Navigator.pop(dialogContext);
                            controller.goToHome(context);
                            setState(() => _dialogShown = false);
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
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ),
    );
  }

  void _showGameOverDialog(
    BuildContext context,
    GameModel model,
    GameController controller,
  ) {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder:
          (dialogContext) => Dialog(
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
                        ElevatedButton(
                          onPressed: () {
                            Navigator.pop(dialogContext);
                            model.resetGame();
                            setState(() => _dialogShown = false);
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
                        const SizedBox(width: 8),
                        TextButton(
                          onPressed: () {
                            Navigator.pop(dialogContext);
                            Navigator.pushReplacement(
                              context,
                              MaterialPageRoute(
                                builder: (context) => const LeaderboardScreen(),
                              ),
                            ).then((_) {
                              if (mounted) {
                                setState(() => _dialogShown = false);
                              }
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
                        const SizedBox(width: 8),
                        TextButton(
                          onPressed: () {
                            model.timer?.cancel();
                            Navigator.pop(dialogContext);
                            controller.goToHome(context);
                            setState(() => _dialogShown = false);
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
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ),
    );
  }
}
