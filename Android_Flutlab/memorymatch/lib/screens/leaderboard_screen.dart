import 'package:flutter/material.dart';
import 'package:memorymatch/services/firestore_service.dart';
import 'package:memorymatch/screens/home_screen.dart';

class LeaderboardScreen extends StatefulWidget {
  const LeaderboardScreen({super.key});

  @override
  _LeaderboardScreenState createState() => _LeaderboardScreenState();
}

class _LeaderboardScreenState extends State<LeaderboardScreen> {
  double _backScale = 1.0;

  @override
  Widget build(BuildContext context) {
    final FirestoreService firestore = FirestoreService();

    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'Leaderboard',
          style: TextStyle(
            fontFamily: 'Amatic SC',
            fontSize: 30,
            color: Color(0xFF3F4238), // Dark Gray
          ),
        ),
        backgroundColor: const Color(0xFFF5F1E9), // Kraft Light
        foregroundColor: const Color(0xFF3F4238), // Dark Gray
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.pushAndRemoveUntil(
              context,
              MaterialPageRoute(builder: (context) => const HomeScreen()),
              (route) => false,
            );
          },
        ),
      ),
      body: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [Color(0xFFF5F1E9), Color(0xFFE8D5C4)], // Gradient Kraft
          ),
        ),
        child: FutureBuilder<List<Map<String, dynamic>>>(
          future: firestore.getLeaderboard(),
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            }
            if (snapshot.hasError) {
              return Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      snapshot.error.toString().replaceFirst('Exception: ', ''),
                      style: const TextStyle(
                        fontFamily: 'Amatic SC',
                        color: Color(0xFFE5989B), // Pastel Red
                        fontSize: 16,
                      ),
                      textAlign: TextAlign.center,
                    ),
                    const SizedBox(height: 16),
                    AnimatedScale(
                      scale: _backScale,
                      duration: const Duration(milliseconds: 100),
                      child: ElevatedButton(
                        onPressed: () => Navigator.pop(context),
                        onHover: (hovered) {
                          setState(() {
                            _backScale = hovered ? 1.1 : 1.0;
                          });
                        },
                        style: ElevatedButton.styleFrom(
                          backgroundColor: const Color(
                            0xFFF4A261,
                          ), // Pastel Orange
                          foregroundColor: const Color(
                            0xFFE76F51,
                          ), // Pastel Orange Dark
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(12),
                            side: const BorderSide(
                              color: Color(0xFFE76F51),
                              width: 2,
                            ),
                          ),
                        ),
                        child: const Text(
                          'Quay lại',
                          style: TextStyle(
                            fontFamily: 'Amatic SC',
                            color: Color(0xFF3F4238), // Dark Gray
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              );
            }
            if (!snapshot.hasData || snapshot.data!.isEmpty) {
              return const Center(
                child: Text(
                  'Không có dữ liệu bảng xếp hạng',
                  style: TextStyle(
                    fontFamily: 'Amatic SC',
                    fontSize: 20,
                    color: Color(0xFF3F4238), // Dark Gray
                  ),
                ),
              );
            }
            final leaderboard = snapshot.data!;
            return ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: leaderboard.length,
              itemBuilder: (context, index) {
                final entry = leaderboard[index];
                return Card(
                  elevation: 4,
                  margin: const EdgeInsets.symmetric(vertical: 8),
                  color: const Color(0xFFE8D5C4), // Kraft Medium
                  child: ListTile(
                    leading: CircleAvatar(
                      backgroundColor: const Color(0xFFF4A261), // Pastel Orange
                      foregroundColor: const Color(0xFFF5F1E9),
                      child: Text(
                        '${index + 1}',
                        style: const TextStyle(
                          fontFamily: 'Amatic SC',
                          color: Color(0xFFF5F1E9), // Kraft Light
                        ),
                      ),
                    ),
                    title: Text(
                      entry['email'] ?? 'Unknown',
                      style: const TextStyle(
                        fontFamily: 'Amatic SC',
                        color: Color(0xFF3F4238), // Dark Gray
                      ),
                    ),
                    trailing: Text(
                      'Score: ${entry['score'] ?? 0}',
                      style: const TextStyle(
                        fontFamily: 'Amatic SC',
                        fontWeight: FontWeight.bold,
                        color: Color(0xFF3F4238), // Dark Gray
                      ),
                    ),
                  ),
                );
              },
            );
          },
        ),
      ),
    );
  }
}
