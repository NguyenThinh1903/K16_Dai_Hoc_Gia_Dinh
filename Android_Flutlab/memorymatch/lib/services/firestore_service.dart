import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';

class FirestoreService {
  final CollectionReference _users = FirebaseFirestore.instance.collection(
    'users',
  );

  Future<void> updateScore(int score) async {
    User? user = FirebaseAuth.instance.currentUser;
    if (user != null) {
      await _users.doc(user.uid).set({
        'score': score,
        'lastUpdated': FieldValue.serverTimestamp(),
      }, SetOptions(merge: true));
    }
  }
}
