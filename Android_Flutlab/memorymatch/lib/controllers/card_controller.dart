import 'package:memorymatch/models/card_model.dart';

class CardController {
  final CardModel cardModel;
  final Function(int) onCardTapped; // Callback để báo cho GameModel
  final int index; // Vị trí của thẻ trong danh sách

  CardController({
    required this.cardModel,
    required this.onCardTapped,
    required this.index,
  });

  void handleTap() {
    if (!cardModel.isFlipped && !cardModel.isMatched) {
      onCardTapped(index); // Báo cho GameModel xử lý logic ghép đôi
    }
  }

  String getDisplayText() {
    return cardModel.isFlipped || cardModel.isMatched ? cardModel.content : '?';
  }

  bool getIsFlipped() {
    return cardModel.isFlipped || cardModel.isMatched;
  }
}
