import 'package:flutter/material.dart';
import 'package:memorymatch/models/game_model.dart';
import 'package:memorymatch/controllers/game_controller.dart';
import 'package:memorymatch/widgets/card_widget.dart';
import 'package:memorymatch/widgets/level_complete_dialog.dart';
import 'package:memorymatch/widgets/game_over_dialog.dart';
import 'package:memorymatch/widgets/resume_game_dialog.dart';
import 'package:provider/provider.dart';

class GameScreen extends StatefulWidget {
  const GameScreen({super.key});

  @override
  GameScreenState createState() => GameScreenState();
}

class GameScreenState extends State<GameScreen> {
  bool _dialogShown = false;

  @override
  void initState() {
    super.initState();
    _checkForInitialGameState();
  }

  Future<void> _checkForInitialGameState() async {
    final model = Provider.of<GameModel>(context, listen: false);
    if (!model.isNewGame && model.timeLeft > 0 && !_dialogShown) {
          _dialogShown = true;
          WidgetsBinding.instance.addPostFrameCallback((_) {
            showDialog(
                context: context,
                barrierDismissible: false,
                builder: (dialogContext) => ResumeGameDialog(
                    parentContext: context,
                    model: model,
                    onResume: (){
                        model.resumeGame();
                        setState(()=> _dialogShown = false);
                    },
                    onReset: (){
                         setState(()=> _dialogShown = false);
                    }
                )
              ).then((value) {
                 if (model.timer == null || !model.timer!.isActive) {
                    model.startTimer();
                  }
              });
          });
    } else {
      //model.startTimer(); //Start the timer if it a new game
      //Avoid start multiple times
      model.isNewGame = false;
    }
  }



  Future<bool> _onWillPop() async {
    final model = Provider.of<GameModel>(context, listen: false);
    final controller = Provider.of<GameController>(context, listen: false);

    model.pauseGame();
    await model.saveGameState();
    if (mounted) {
      await controller.goToHome(context);
    }
    return false;
  }

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<GameController>(context);
    final model = Provider.of<GameModel>(context);

    // Start or resume timer if not active
    if (model.timer == null || !model.timer!.isActive) {
        model.startTimer();
    }

      // Check for game completion or game over *within the build method*
    if (model.timeLeft <= 0 && !_dialogShown) {
      model.pauseGame();
      _dialogShown = true;
      WidgetsBinding.instance.addPostFrameCallback((_) {
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (dialogContext) => GameOverDialog(
            parentContext: context,
            model: model,
            controller: controller,
            onDialogClosed: () {
               if (mounted) {
                    setState(() => _dialogShown = false);
                  }
            },
          ),
        );
      });
    } else if (model.pairsFound == model.cards.length ~/ 2 &&
        !_dialogShown) {
      model.pauseGame();
      _dialogShown = true;

      WidgetsBinding.instance.addPostFrameCallback((_) {
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (dialogContext) => LevelCompleteDialog(
            parentContext: context,
            model: model,
            controller: controller,
             onDialogClosed: () {
               if (mounted) {
                    setState(() => _dialogShown = false);
                  }
            },
          ),
        );
      });
    }



    return PopScope(
      canPop: false,
      onPopInvoked: (didPop) async {
        if (didPop) return;
        await _onWillPop();
      },
      child: Scaffold(
        appBar: AppBar(
          title: const Text(
            'Memory Match',
            style: TextStyle(
              fontFamily: 'Amatic SC',
              fontSize: 30,
              color: Color(0xFF3F4238),
            ),
          ),
          backgroundColor: const Color(0xFFF5F1E9),
          elevation: 0,
          leading: IconButton(
            icon: const Icon(Icons.arrow_back, color: Color(0xFF3F4238)),
            onPressed: () async {
              await _onWillPop();
            },
          ),
          actions: [
            IconButton(
              icon: const Icon(Icons.exit_to_app, color: Color(0xFF3F4238)),
              onPressed: () async {
                final model = Provider.of<GameModel>(context, listen: false);
                final controller = Provider.of<GameController>(
                  context,
                  listen: false,
                );
                model.timer?.cancel();
                await controller.goToHome(context);
              },
            ),
          ],
        ),
        body: Container(
          color: const Color(0xFFF5F1E9),
          child: Column(
            children: [
              Container(
                margin: const EdgeInsets.symmetric(
                  horizontal: 20,
                  vertical: 10,
                ),
                padding: const EdgeInsets.all(10),
                decoration: BoxDecoration(
                  color: Colors.white,
                  border: Border.all(
                    color: const Color(0xFF3F4238),
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
                        color: Color(0xFF3F4238),
                      ),
                    ),
                    Text(
                      'Time: ${model.timeLeft}',
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
              ),
              Expanded(
                child: Center(
                  child: ConstrainedBox(
                    constraints: const BoxConstraints(maxWidth: 400),
                    child: GridView.builder(
                      padding: const EdgeInsets.all(16),
                      shrinkWrap: true,
                      gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                        crossAxisCount: model.level + 1,
                        crossAxisSpacing: 10,
                        mainAxisSpacing: 10,
                        childAspectRatio: 1,
                      ),
                      itemCount: model.cards.length,
                      itemBuilder: (context, index) {
                        return CardWidget(
                          text: model.flipped[index] || model.matched[index]
                              ? (model.cards[index] ?? '⭐')
                              : '⭐',
                          onTap: () => model.checkMatch(index),
                          isFlipped:
                              model.flipped[index] || model.matched[index],
                        );
                      },
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}