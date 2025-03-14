import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';

class FirestoreService {
  final FirebaseFirestore _db = FirebaseFirestore.instance;
  final FirebaseAuth _auth = FirebaseAuth.instance;

  // Lấy danh sách leaderboard (đã có trong code của bạn)
  Future<List<Map<String, dynamic>>> getLeaderboard() async {
    try {
      QuerySnapshot snapshot =
          await _db
              .collection('users')
              .orderBy('score', descending: true)
              .limit(10)
              .get();

      return snapshot.docs.map((doc) {
        return {'email': doc['email'] ?? 'Unknown', 'score': doc['score'] ?? 0};
      }).toList();
    } catch (e) {
      throw Exception('Error fetching leaderboard: $e');
    }
  }

  // Lưu trạng thái game vào Firestore
  Future<void> saveGameState(Map<String, dynamic> gameState) async {
    try {
      User? user = _auth.currentUser;
      if (user != null) {
        await _db.collection('game_states').doc(user.uid).set(gameState);
      } else {
        throw Exception('User not logged in');
      }
    } catch (e) {
      throw Exception('Error saving game state: $e');
    }
  }

  // Cập nhật điểm số của người dùng
  Future<void> updateUserScore(int score) async {
    try {
      User? user = _auth.currentUser;
      if (user != null) {
        DocumentReference userRef = _db.collection('users').doc(user.uid);
        DocumentSnapshot doc = await userRef.get();

        if (doc.exists) {
          int currentScore = doc['score'] ?? 0;
          if (score > currentScore) {
            await userRef.update({'score': score, 'email': user.email});
          }
        } else {
          await userRef.set({'score': score, 'email': user.email});
        }
      } else {
        throw Exception('User not logged in');
      }
    } catch (e) {
      throw Exception('Error updating user score: $e');
    }
  }

  // Tải trạng thái game từ Firestore
  Future<Map<String, dynamic>?> loadGameState() async {
    try {
      User? user = _auth.currentUser;
      if (user != null) {
        DocumentSnapshot doc =
            await _db.collection('game_states').doc(user.uid).get();
        if (doc.exists) {
          return doc.data() as Map<String, dynamic>;
        }
        return null;
      } else {
        throw Exception('User not logged in');
      }
    } catch (e) {
      throw Exception('Error loading game state: $e');
    }
  }
}
