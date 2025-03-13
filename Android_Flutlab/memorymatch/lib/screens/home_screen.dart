import 'package:flutter/material.dart';
import 'package:memorymatch/models/game_model.dart';
import 'package:memorymatch/screens/game_screen.dart';
import 'package:memorymatch/controllers/game_controller.dart';
import 'package:provider/provider.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<GameController>(context);
    final gameModel = Provider.of<GameModel>(context, listen: false);

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
              ElevatedButton(
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => const GameScreen()),
                  );
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
                    side: const BorderSide(color: Color(0xFFE76F51), width: 2),
                  ),
                ),
                child: const Text(
                  'Continue Game',
                  style: TextStyle(
                    fontFamily: 'Amatic SC',
                    fontSize: 20,
                    color: Color(0xFF3F4238), // Dark Gray
                  ),
                ),
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: () {
                  gameModel.resetGame();
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => const GameScreen()),
                  );
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
                    side: const BorderSide(color: Color(0xFFE76F51), width: 2),
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
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: () => controller.goToLeaderboard(context),
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
                    side: const BorderSide(color: Color(0xFFE76F51), width: 2),
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
            ],
          ),
        ),
      ),
    );
  }
}
