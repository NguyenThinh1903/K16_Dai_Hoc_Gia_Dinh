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
