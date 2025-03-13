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
        title: const Text('Memory Match - Game'),
        backgroundColor: Colors.blue.shade900,
        foregroundColor: Colors.white,
        actions: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text(
              'Score: ${model.score} | Time: ${model.timeLeft} | Level: ${model.level}',
            ),
          ),
        ],
      ),
      body: Container(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [Colors.blue.shade100, Colors.white],
          ),
        ),
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
                          : '?',
                  onTap: () => model.checkMatch(index),
                  isFlipped: model.flipped[index] || model.matched[index],
                );
              },
            ),
          ),
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
              side: const BorderSide(color: Colors.green, width: 3),
            ),
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 350),
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Column(
                      children: [
                        const Icon(Icons.star, color: Colors.yellow, size: 50),
                        const SizedBox(height: 10),
                        const Text(
                          'Level Complete!',
                          style: TextStyle(
                            fontSize: 24,
                            fontWeight: FontWeight.bold,
                            color: Colors.green,
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
                          style: const TextStyle(fontSize: 18),
                        ),
                        Text(
                          'Level: ${model.level}',
                          style: const TextStyle(fontSize: 18),
                        ),
                      ],
                    ),
                    const SizedBox(height: 20),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        ElevatedButton(
                          onPressed: () {
                            Navigator.pop(dialogContext); // Đóng dialog trước
                            model.nextLevel();
                            setState(() => _dialogShown = false);
                          },
                          style: ElevatedButton.styleFrom(
                            backgroundColor: Colors.green,
                            foregroundColor: Colors.white,
                            padding: const EdgeInsets.symmetric(
                              horizontal: 15,
                              vertical: 10,
                            ),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(10),
                            ),
                          ),
                          child: const Text(
                            'Next Level',
                            style: TextStyle(fontSize: 14),
                          ),
                        ),
                        const SizedBox(width: 8),
                        TextButton(
                          onPressed: () {
                            Navigator.pop(dialogContext); // Đóng dialog trước
                            Navigator.pushReplacement(
                              context,
                              MaterialPageRoute(
                                builder: (context) => const LeaderboardScreen(),
                              ),
                            ).then((_) {
                              // Đảm bảo cập nhật trạng thái sau khi điều hướng
                              if (mounted) {
                                setState(() => _dialogShown = false);
                              }
                            });
                          },
                          child: const Text(
                            'Leaderboard',
                            style: TextStyle(fontSize: 14, color: Colors.blue),
                          ),
                        ),
                        const SizedBox(width: 8),
                        TextButton(
                          onPressed: () {
                            model.timer?.cancel(); // Hủy timer khi thoát
                            Navigator.pop(dialogContext); // Đóng dialog trước
                            controller.goToHome(context);
                            setState(() => _dialogShown = false);
                          },
                          child: const Text(
                            'Exit',
                            style: TextStyle(fontSize: 14, color: Colors.grey),
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
              side: const BorderSide(color: Colors.red, width: 3),
            ),
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 350),
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Column(
                      children: [
                        const Icon(Icons.close, color: Colors.red, size: 50),
                        const SizedBox(height: 10),
                        const Text(
                          'Game Over',
                          style: TextStyle(
                            fontSize: 24,
                            fontWeight: FontWeight.bold,
                            color: Colors.red,
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
                          style: const TextStyle(fontSize: 18),
                        ),
                        Text(
                          'Level: ${model.level}',
                          style: const TextStyle(fontSize: 18),
                        ),
                      ],
                    ),
                    const SizedBox(height: 20),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        ElevatedButton(
                          onPressed: () {
                            Navigator.pop(dialogContext); // Đóng dialog trước
                            model.resetGame();
                            setState(() => _dialogShown = false);
                          },
                          style: ElevatedButton.styleFrom(
                            backgroundColor: Colors.red,
                            foregroundColor: Colors.white,
                            padding: const EdgeInsets.symmetric(
                              horizontal: 15,
                              vertical: 10,
                            ),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(10),
                            ),
                          ),
                          child: const Text(
                            'Restart',
                            style: TextStyle(fontSize: 14),
                          ),
                        ),
                        const SizedBox(width: 8),
                        TextButton(
                          onPressed: () {
                            Navigator.pop(dialogContext); // Đóng dialog trước
                            Navigator.pushReplacement(
                              context,
                              MaterialPageRoute(
                                builder: (context) => const LeaderboardScreen(),
                              ),
                            ).then((_) {
                              // Đảm bảo cập nhật trạng thái sau khi điều hướng
                              if (mounted) {
                                setState(() => _dialogShown = false);
                              }
                            });
                          },
                          child: const Text(
                            'Leaderboard',
                            style: TextStyle(fontSize: 14, color: Colors.blue),
                          ),
                        ),
                        const SizedBox(width: 8),
                        TextButton(
                          onPressed: () {
                            model.timer?.cancel(); // Hủy timer khi thoát
                            Navigator.pop(dialogContext); // Đóng dialog trước
                            controller.goToHome(context);
                            setState(() => _dialogShown = false);
                          },
                          child: const Text(
                            'Exit',
                            style: TextStyle(fontSize: 14, color: Colors.grey),
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
