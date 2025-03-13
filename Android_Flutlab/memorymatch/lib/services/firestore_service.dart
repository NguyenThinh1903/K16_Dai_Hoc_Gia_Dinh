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
      } catch (e) {
        throw Exception('Không thể cập nhật điểm số: $e');
      }
    } else {
      throw Exception('Vui lòng đăng nhập để cập nhật điểm số.');
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
      return data;
    } catch (e) {
      throw Exception('Không thể tải bảng xếp hạng: $e');
    }
  }
}
