import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';

class FirestoreService {
  final CollectionReference _users = FirebaseFirestore.instance.collection(
    'users',
  );

  Future<void> updateScore(int score) async {
    User? user = FirebaseAuth.instance.currentUser;
    if (user != null) {
      try {
        await _users.doc(user.uid).set({
          'score': score,
          'lastUpdated': FieldValue.serverTimestamp(),
          'email': user.email ?? 'Anonymous',
        }, SetOptions(merge: true));
        print('Score updated for ${user.email}: $score'); // Log để kiểm tra
      } catch (e) {
        print('Error updating score: $e');
      }
    } else {
      print('No user logged in');
    }
  }

  Future<List<Map<String, dynamic>>> getLeaderboard() async {
    try {
      QuerySnapshot snapshot =
          await _users.orderBy('score', descending: true).limit(10).get();
      var data =
          snapshot.docs
              .map((doc) => doc.data() as Map<String, dynamic>)
              .toList();
      print('Leaderboard data: $data'); // Log để kiểm tra
      return data;
    } catch (e) {
      print('Error fetching leaderboard: $e');
      return [];
    }
  }
}
