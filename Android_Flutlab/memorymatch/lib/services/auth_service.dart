import 'package:firebase_auth/firebase_auth.dart';

class AuthService {
  final FirebaseAuth _auth = FirebaseAuth.instance;

  Future<User?> register(String email, String password) async {
    try {
      UserCredential result = await _auth.createUserWithEmailAndPassword(
        email: email,
        password: password,
      );
      return result.user;
    } on FirebaseAuthException catch (e) {
      // Ném lỗi với thông điệp cụ thể
      switch (e.code) {
        case 'email-already-in-use':
          throw Exception('Email đã được sử dụng. Vui lòng chọn email khác.');
        case 'invalid-email':
          throw Exception('Email không hợp lệ. Vui lòng kiểm tra lại.');
        case 'weak-password':
          throw Exception('Mật khẩu quá yếu. Hãy chọn mật khẩu mạnh hơn.');
        default:
          throw Exception('Đăng ký thất bại: ${e.message}');
      }
    } catch (e) {
      throw Exception('Đã xảy ra lỗi không xác định: $e');
    }
  }

  Future<User?> login(String email, String password) async {
    try {
      UserCredential result = await _auth.signInWithEmailAndPassword(
        email: email,
        password: password,
      );
      return result.user;
    } on FirebaseAuthException catch (e) {
      // Ném lỗi với thông điệp cụ thể
      switch (e.code) {
        case 'user-not-found':
          throw Exception('Không tìm thấy tài khoản với email này.');
        case 'wrong-password':
          throw Exception('Mật khẩu không đúng. Vui lòng thử lại.');
        case 'invalid-email':
          throw Exception('Email không hợp lệ. Vui lòng kiểm tra lại.');
        default:
          throw Exception('Đăng nhập thất bại: ${e.message}');
      }
    } catch (e) {
      throw Exception('Đã xảy ra lỗi không xác định: $e');
    }
  }

  Future<void> logout() async {
    try {
      await _auth.signOut();
    } catch (e) {
      throw Exception('Đăng xuất thất bại: $e');
    }
  }

  User? getCurrentUser() {
    return _auth.currentUser;
  }
}
