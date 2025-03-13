class Product {
  int id = 0;
  String? title = "", description = "", category = "", image = "";
  double? price = 0;
  int? size = 0;

  Product(
      {this.id = 0,
      this.title,
      this.description,
      this.category,
      this.image,
      this.price,
      this.size});

  factory Product.fromJson(Map<String, dynamic> json) {
    return Product(
      id: json['id'],
      title: json['title'],
      category: json['category'],
      description: json['description'],
      size: 0,
      price: json['price'],
      image: json['image'],
    );
  }

  factory Product.fromJsonMocki(Map<String, dynamic> json) {
    return Product(
      id: int.tryParse(json['id']?.toString() ?? "0") ??
          0, // Chuyển id từ String sang int
      title: json['title'] ?? "",
      description: json['description'] ?? "",
      category: json['category'] ?? "",
      image: json['image'] ?? "",
      price: (json['price'] ?? 0).toDouble(), // Đảm bảo price luôn là double
      size:
          json['size'] != null ? int.tryParse(json['size'].toString()) ?? 0 : 0,
    );
  }

  Map<String, dynamic> toJsonMocki() {
    return {
      'id': id.toString(), // Đổi ID thành String để phù hợp với JSON Mocki
      'title': title,
      'description': description,
      'category': category,
      'image': image,
      'price': price,
      'size': size?.toString() ?? "0" // Chuyển size thành String
    };
  }

  //------------------------------------Hàm chuyển đổi cho Firebase
  //
  /// 🟢 Chuyển đổi từ Firebase JSON sang Object Product
  factory Product.fromFireBase(Map<String, dynamic> json) {
    final fields = json['fields'] as Map<String, dynamic>;

    return Product(
      id: int.tryParse(fields['id']?['integerValue']?.toString() ?? '0') ?? 0,
      title: fields['title']?['stringValue'] ?? '',
      description: fields['description']?['stringValue'] ?? '',
      image: fields['image']?['stringValue'] ?? '',
      price: double.tryParse(fields['price']?['doubleValue']?.toString() ??
              fields['price']?['doubleValue']?.toString() ??
              '0') ??
          0.0,
      size: fields['size']?['integerValue'] != null
          ? int.tryParse(fields['size']!['integerValue'].toString()) ?? 0
          : null,
    );
  }

  /// 🔵 Chuyển đổi Object Product thành JSON để lưu vào Firebase
  Map<String, dynamic> toFirebase() {
    return {
      "fields": {
        "id": {"integerValue": id},
        "title": {"stringValue": title},
        "description": {"stringValue": description},
        "image": {"stringValue": image},
        "price": {"stringValue": price.toString()},
        if (size != null) "size": {"integerValue": size},
      }
    };
  }
}

List<Product> products = [
  Product(
    title: "Office Code",
    id: 1,
    price: 234,
    size: 12,
    description: dummyText,
    image: "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
  ),
  Product(
    id: 2,
    title: "Belt Bag",
    price: 234,
    size: 8,
    description: dummyText,
    image:
        "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg",
  ),
  Product(
    id: 3,
    title: "Hang Top",
    price: 234,
    size: 10,
    description: dummyText,
    image: "https://fakestoreapi.com/img/71YXzeOuslL._AC_UY879_.jpg",
  ),
  Product(
    id: 4,
    title: "Old Fashion",
    price: 234,
    size: 11,
    description: dummyText,
    image: "https://fakestoreapi.com/img/71pWzhdJNwL._AC_UL640_QL65_ML3_.jpg",
  ),
  Product(
    id: 5,
    title: "Office Code",
    price: 234,
    size: 12,
    description: dummyText,
    image: "https://fakestoreapi.com/img/71YAIFU48IL._AC_UL640_QL65_ML3_.jpg",
  ),
  Product(
    id: 6,
    title: "Office Code",
    price: 234,
    size: 12,
    description: dummyText,
    image: "https://fakestoreapi.com/img/61IBBVJvSDL._AC_SY879_.jpg",
  ),
  Product(
    id: 7,
    title: "Túi vải bố",
    price: 234,
    size: 12,
    description: dummyText,
    image: "https://fakestoreapi.com/img/61U7T1koQqL._AC_SX679_.jpg",
  ),
  Product(
    id: 8,
    title: "Túi vải",
    price: 234,
    size: 12,
    description: dummyText,
    image: "https://fakestoreapi.com/img/71kWymZ+c+L._AC_SX679_.jpg",
  ),
  Product(
    id: 9,
    title: "Túi đeo nữ Venu",
    price: 234,
    size: 12,
    description: dummyText,
    image: "https://fakestoreapi.com/img/61mtL65D4cL._AC_SX679_.jpg",
  ),
  Product(
    id: 10,
    title: "Túi đeo nữ Xì trum",
    price: 234,
    size: 12,
    description: dummyText,
    image: "https://fakestoreapi.com/img/81QpkIctqPL._AC_SX679_.jpg",
  ),
  Product(
    id: 11,
    title: "Túi đeo nữ Việt Tiến",
    price: 234,
    size: 12,
    description: dummyText,
    image: "https://fakestoreapi.com/img/81Zt42ioCgL._AC_SX679_.jpg",
  ),
  Product(
    id: 12,
    title: "Túi đeo nữ Coop",
    price: 234,
    size: 12,
    description: dummyText,
    image: "https://fakestoreapi.com/img/81XH0e8fefL._AC_UY879_.jpg",
  ),
];

String dummyText = "Đây là sản phẩm tuyệt vời. Không thể tin được";
