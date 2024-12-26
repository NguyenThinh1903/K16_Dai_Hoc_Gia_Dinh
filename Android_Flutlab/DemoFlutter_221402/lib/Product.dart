class Product {
  int id = 0;
  String title = "", description = "", category = "", image = "";
  double price = 0;
  int size = 0;

  Product(
      {this.id = 0,
      this.title = "",
      this.description = "",
      this.category = "",
      this.image = "",
      this.price = 0,
      this.size = 0});
}

List<Product> dssanpham = [
  Product(
    title: "Sản phẩm may đo",
    id: 1,
    description: motasanpham,
    category: "Dệt may",
    image: "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
  ),
  Product(
    title: "Sản phẩm áo thun",
    id: 1,
    description: motasanpham,
    category: "Dệt may",
    image:
        "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg",
  ),
  Product(
    id: 3,
    title: "Mens Cotton Jacket",
    price: 55.99,
    description:
        "great outerwear jackets for Spring/Autumn/Winter, suitable for many occasions, such as working, hiking, camping, mountain/rock climbing, cycling, traveling or other outdoors. Good gift choice for you or your family member. A warm hearted love to Father, husband or son in this thanksgiving or Christmas Day.",
    category: "men's clothing",
    image: "https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg",
  ),
  Product(
    id: 4,
    title: "Old Fashion",
    price: 234,
    size: 11,
    description: motasanpham,
    image: "https://fakestoreapi.com/img/71pWzhdJNwL._AC_UL640_QL65_ML3_.jpg",
  ),
  Product(
    id: 5,
    title: "Office Code",
    price: 234,
    size: 12,
    description: motasanpham,
    image: "https://fakestoreapi.com/img/71YAIFU48IL._AC_UL640_QL65_ML3_.jpg",
  ),
  Product(
    id: 6,
    title: "Office Code",
    price: 234,
    size: 12,
    description: motasanpham,
    image: "https://fakestoreapi.com/img/61IBBVJvSDL._AC_SY879_.jpg",
  ),
  Product(
    id: 7,
    title: "Túi vải bố",
    price: 234,
    size: 12,
    description: motasanpham,
    image: "https://fakestoreapi.com/img/61U7T1koQqL._AC_SX679_.jpg",
  ),
  Product(
    id: 8,
    title: "Túi vải",
    price: 234,
    size: 12,
    description: motasanpham,
    image: "https://fakestoreapi.com/img/71kWymZ+c+L._AC_SX679_.jpg",
  ),
  Product(
    id: 9,
    title: "Túi đeo nữ Venu",
    price: 234,
    size: 12,
    description: motasanpham,
    image: "https://fakestoreapi.com/img/61mtL65D4cL._AC_SX679_.jpg",
  ),
  Product(
    id: 10,
    title: "Túi đeo nữ Xì trum",
    price: 234,
    size: 12,
    description: motasanpham,
    image: "https://fakestoreapi.com/img/81QpkIctqPL._AC_SX679_.jpg",
  ),
  Product(
    id: 11,
    title: "Túi đeo nữ Việt Tiến",
    price: 234,
    size: 12,
    description: motasanpham,
    image: "https://fakestoreapi.com/img/81Zt42ioCgL._AC_SX679_.jpg",
  ),
  Product(
    id: 12,
    title: "Túi đeo nữ Coop",
    price: 234,
    size: 12,
    description: motasanpham,
    image: "https://fakestoreapi.com/img/81XH0e8fefL._AC_UY879_.jpg",
  ),
];

String motasanpham = "Sản phẩm tuyệt vời, không thể tin được";
