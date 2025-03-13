import 'package:flutter/material.dart';
import 'package:memorymatch/models/game_model.dart';
import 'package:memorymatch/controllers/game_controller.dart';
import 'package:memorymatch/screens/leaderboard_screen.dart';
import 'package:memorymatch/screens/home_screen.dart';
import 'package:memorymatch/widgets/card_widget.dart';
import 'package:provider/provider.dart';

class GameScreen extends StatelessWidget {
  const GameScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<GameController>(context);
    final model = Provider.of<GameModel>(context);

    if (model.timeLeft <= 0) {
      WidgetsBinding.instance.addPostFrameCallback((_) {
        _showGameOverDialog(context, model, controller);
      });
    } else if (model.pairsFound == model.cards.length ~/ 2) {
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
        child: GridView.builder(
          padding: const EdgeInsets.all(16),
          gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: model.level == 1 ? 4 : 6,
            crossAxisSpacing: 10,
            mainAxisSpacing: 10,
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
          (context) => AlertDialog(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(16),
            ),
            title: const Text('Level Complete!'),
            content: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text('Score: ${model.score}'),
                Text('Level: ${model.level}'),
              ],
            ),
            actions: [
              TextButton(
                onPressed: () {
                  Navigator.pop(context);
                  model.nextLevel();
                },
                child: const Text('Next Level'),
              ),
              TextButton(
                onPressed: () {
                  Navigator.pop(context);
                  controller.goToLeaderboard(context).then((_) {
                    controller.goToHome(context);
                  });
                },
                child: const Text('View Leaderboard'),
              ),
              TextButton(
                onPressed: () {
                  Navigator.pop(context);
                  controller.goToHome(context);
                },
                child: const Text('Exit'),
              ),
            ],
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
          (context) => AlertDialog(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(16),
            ),
            title: const Text('Game Over'),
            content: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text('Score: ${model.score}'),
                Text('Level: ${model.level}'),
              ],
            ),
            actions: [
              TextButton(
                onPressed: () {
                  Navigator.pop(context);
                  model.resetGame();
                },
                child: const Text('Restart'),
              ),
              TextButton(
                onPressed: () {
                  Navigator.pop(context);
                  controller.goToLeaderboard(context).then((_) {
                    controller.goToHome(context);
                  });
                },
                child: const Text('View Leaderboard'),
              ),
              TextButton(
                onPressed: () {
                  Navigator.pop(context);
                  controller.goToHome(context);
                },
                child: const Text('Exit'),
              ),
            ],
          ),
    );
  }
}
