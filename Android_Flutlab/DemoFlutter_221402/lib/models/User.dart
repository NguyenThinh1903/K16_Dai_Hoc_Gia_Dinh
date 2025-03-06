class User {
  int id;
  String name;
  String email;
  String password;

  User(
      {this.id = 0,
      required this.name,
      this.email = "",
      required this.password});

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      id: json['id'],
      name: json['name'],
      email: json['email'],
      password: json['password'],
    );
  }

  factory User.fromJsonMocki(Map<String, dynamic> json) {
    return User(
      id: int.tryParse(json['id']?.toString() ?? "0") ??
          0, // Chuyển id từ String sang int
      name: json['name'],
      email: json['email'],
      password: json['password'],
    );
  }

  Map<String, dynamic> toJsonMocki() {
    return {
      'id': id.toString(), // Đổi ID thành String để phù hợp với JSON Mocki
      'name': name,
      'email': email,
      'password': password,
    };
  }

  // Tạo danh sách tĩnh để lưu người dùng
  static List<User> users = [];

  // Thêm người dùng mới vào danh sách
  static void addUser(User user) {
    users.add(user);
  }

  // Lấy danh sách tất cả người dùng
  static List<User> getUsers() {
    return users;
  }

  // Phương thức để xác thực người dùng
  static User? authenticate(String username, String password) {
    try {
      return users.firstWhere(
        (user) => user.name == username && user.password == password,
      );
    } catch (e) {
      return null;
    }
  }
}
