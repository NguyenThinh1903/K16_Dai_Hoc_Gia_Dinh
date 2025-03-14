import 'package:flutter/material.dart';
import 'package:memorymatch/models/game_model.dart';
import 'package:memorymatch/screens/game_screen.dart';
import 'package:memorymatch/controllers/game_controller.dart';
import 'package:provider/provider.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  double _continueScale = 1.0;
  double _newGameScale = 1.0;
  double _leaderboardScale = 1.0;

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<GameController>(context);
    final gameModel = Provider.of<GameModel>(context);

    bool isGameCompleted = gameModel.pairsFound == gameModel.cards.length ~/ 2;

    return Scaffold(
      body: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [Color(0xFFF5F1E9), Color(0xFFE8D5C4)], // Gradient Kraft
          ),
        ),
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Icon(
                Icons.memory,
                size: 100,
                color: Color(0xFF3F4238), // Dark Gray
              ),
              const SizedBox(height: 20),
              const Text(
                'Memory Match',
                style: TextStyle(
                  fontFamily: 'Amatic SC',
                  fontSize: 40,
                  fontWeight: FontWeight.bold,
                  color: Color(0xFF3F4238), // Dark Gray
                ),
              ),
              const SizedBox(height: 40),
              AnimatedScale(
                scale: _continueScale,
                duration: const Duration(milliseconds: 100),
                child: ElevatedButton(
                  onPressed: () {
                    if (isGameCompleted) {
                      gameModel.nextLevel(); // Chuyển sang level tiếp theo
                    } else {
                      gameModel.isNewGame =
                          false; // Đánh dấu không phải game mới khi tiếp tục
                    }
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => const GameScreen(),
                      ),
                    );
                  },
                  onHover: (hovered) {
                    setState(() {
                      _continueScale = hovered ? 1.1 : 1.0;
                    });
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor:
                        isGameCompleted
                            ? const Color(
                              0xFFA9C5A0,
                            ) // Pastel Green nếu đã hoàn thành
                            : const Color(
                              0xFFF4A261,
                            ), // Pastel Orange nếu chưa hoàn thành
                    foregroundColor:
                        isGameCompleted
                            ? const Color(0xFF829B7A) // Pastel Green Dark
                            : const Color(0xFFE76F51), // Pastel Orange Dark
                    padding: const EdgeInsets.symmetric(
                      horizontal: 40,
                      vertical: 20,
                    ),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                      side: BorderSide(
                        color:
                            isGameCompleted
                                ? const Color(0xFF829B7A)
                                : const Color(0xFFE76F51),
                        width: 2,
                      ),
                    ),
                  ),
                  child: Text(
                    isGameCompleted ? 'Next Level' : 'Continue Game',
                    style: const TextStyle(
                      fontFamily: 'Amatic SC',
                      fontSize: 20,
                      color: Color(0xFF3F4238), // Dark Gray
                    ),
                  ),
                ),
              ),
              const SizedBox(height: 20),
              AnimatedScale(
                scale: _newGameScale,
                duration: const Duration(milliseconds: 100),
                child: ElevatedButton(
                  onPressed: () {
                    gameModel.resetGame();
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => const GameScreen(),
                      ),
                    );
                  },
                  onHover: (hovered) {
                    setState(() {
                      _newGameScale = hovered ? 1.1 : 1.0;
                    });
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: const Color(0xFFF4A261), // Pastel Orange
                    foregroundColor: const Color(
                      0xFFE76F51,
                    ), // Pastel Orange Dark
                    padding: const EdgeInsets.symmetric(
                      horizontal: 40,
                      vertical: 20,
                    ),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                      side: const BorderSide(
                        color: Color(0xFFE76F51),
                        width: 2,
                      ),
                    ),
                  ),
                  child: const Text(
                    'New Game',
                    style: TextStyle(
                      fontFamily: 'Amatic SC',
                      fontSize: 20,
                      color: Color(0xFF3F4238), // Dark Gray
                    ),
                  ),
                ),
              ),
              const SizedBox(height: 20),
              AnimatedScale(
                scale: _leaderboardScale,
                duration: const Duration(milliseconds: 100),
                child: ElevatedButton(
                  onPressed: () => controller.goToLeaderboard(context),
                  onHover: (hovered) {
                    setState(() {
                      _leaderboardScale = hovered ? 1.1 : 1.0;
                    });
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: const Color(0xFFF4A261), // Pastel Orange
                    foregroundColor: const Color(
                      0xFFE76F51,
                    ), // Pastel Orange Dark
                    padding: const EdgeInsets.symmetric(
                      horizontal: 40,
                      vertical: 20,
                    ),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                      side: const BorderSide(
                        color: Color(0xFFE76F51),
                        width: 2,
                      ),
                    ),
                  ),
                  child: const Text(
                    'Leaderboard',
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
        ),
      ),
    );
  }
}
