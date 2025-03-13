import 'package:flutter/material.dart';

class CardView extends StatelessWidget {
  final String displayText; // Nội dung hiển thị (content hoặc "?")
  final bool isFlipped; // Trạng thái để quyết định mặt trước/mặt sau
  final VoidCallback onTap; // Sự kiện nhấn

  const CardView({
    super.key,
    required this.displayText,
    required this.isFlipped,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: AnimatedSwitcher(
        duration: const Duration(milliseconds: 400),
        transitionBuilder: (child, animation) {
          final rotate = Tween<double>(begin: 0, end: 1).animate(animation);
          return AnimatedBuilder(
            animation: rotate,
            builder: (context, child) {
              final isBack = rotate.value <= 0.5; // Đổi tên để rõ ràng hơn
              final angle =
                  isBack ? rotate.value * 3.14 : (1 - rotate.value) * 3.14;
              return Transform(
                transform: Matrix4.rotationY(angle),
                alignment: Alignment.center,
                child: isBack ? _buildBack() : _buildFront(),
              );
            },
            child: child,
          );
        },
        child: isFlipped ? _buildFront() : _buildBack(),
      ),
    );
  }

  Widget _buildFront() {
    return Container(
      key: const ValueKey(true),
      decoration: BoxDecoration(
        color: Colors.blue,
        borderRadius: BorderRadius.circular(12),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.2),
            blurRadius: 6,
            offset: const Offset(0, 3),
          ),
        ],
      ),
      child: Center(
        child: Text(
          displayText,
          style: const TextStyle(fontSize: 24, color: Colors.white),
        ),
      ),
    );
  }

  Widget _buildBack() {
    return Container(
      key: const ValueKey(false),
      decoration: BoxDecoration(
        color: Colors.grey.shade400,
        borderRadius: BorderRadius.circular(12),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.2),
            blurRadius: 6,
            offset: const Offset(0, 3),
          ),
        ],
      ),
      child: const Center(
        child: Text('?', style: TextStyle(fontSize: 24, color: Colors.white)),
      ),
    );
  }
}
