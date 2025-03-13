class CardModel {
  final String content; // Nội dung thật của thẻ (ví dụ: "A", "B")
  bool isFlipped; // Trạng thái lật (true: đã lật, false: chưa lật)
  bool isMatched; // Trạng thái ghép đôi (true: đã khớp, false: chưa khớp)

  CardModel({
    required this.content,
    this.isFlipped = false,
    this.isMatched = false,
  });
}
