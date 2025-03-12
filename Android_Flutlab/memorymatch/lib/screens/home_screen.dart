import 'package:flutter/material.dart';
import 'package:memorymatch/widgets/card_widget.dart'; // Sửa từ memory_match thành memorymatch
import 'package:memorymatch/services/firestore_service.dart'; // Sửa từ memory_match thành memorymatch

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final FirestoreService _firestore = FirestoreService();
  List<String> cards = ['A', 'A', 'B', 'B', 'C', 'C', 'D', 'D']..shuffle();
  List<bool> flipped = List.filled(8, false);
  int? firstCardIndex;
  int score = 0;

  void checkMatch(int index) async {
    if (flipped[index] || firstCardIndex == index) return;

    setState(() {
      flipped[index] = true;
    });

    if (firstCardIndex == null) {
      firstCardIndex = index;
    } else {
      if (cards[firstCardIndex!] == cards[index]) {
        score += 10;
        await _firestore.updateScore(score);
      } else {
        Future.delayed(const Duration(seconds: 1), () {
          setState(() {
            flipped[firstCardIndex!] = false;
            flipped[index] = false;
          });
        });
      }
      firstCardIndex = null;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Memory Match')),
      body: GridView.builder(
        padding: const EdgeInsets.all(16),
        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 4,
          crossAxisSpacing: 10,
          mainAxisSpacing: 10,
        ),
        itemCount: cards.length,
        itemBuilder: (context, index) {
          return CardWidget(
            text: flipped[index] ? cards[index] : '?',
            onTap: () => checkMatch(index),
          );
        },
      ),
    );
  }
}
