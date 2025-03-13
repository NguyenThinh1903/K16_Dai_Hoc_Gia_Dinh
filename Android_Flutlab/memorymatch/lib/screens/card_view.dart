import 'package:flutter/material.dart';

class CardView extends StatelessWidget {
  final String displayText;
  final bool isFlipped;
  final VoidCallback onTap;

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
          final scale = Tween<double>(begin: 1.0, end: 1.1).animate(
            CurvedAnimation(parent: animation, curve: Curves.easeInOut),
          );
          return AnimatedBuilder(
            animation: animation,
            builder: (context, child) {
              final isBack = rotate.value <= 0.5;
              final angle =
                  isBack ? rotate.value * 3.14 : (1 - rotate.value) * 3.14;
              return Transform(
                transform: Matrix4.rotationY(angle),
                alignment: Alignment.center,
                child: ScaleTransition(
                  scale: scale,
                  child: isBack ? _buildBack() : _buildFront(),
                ),
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
        color: const Color(0xFFE8D5C4), // Kraft Medium
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: const Color(0xFF3F4238), width: 2),
        boxShadow: [
          BoxShadow(
            color: const Color(0xFF3F4238).withOpacity(0.2),
            blurRadius: 6,
            offset: const Offset(0, 3),
          ),
        ],
      ),
      child: Center(
        child: Text(
          displayText,
          style: const TextStyle(
            fontFamily: 'Amatic SC',
            fontSize: 24,
            color: Color(0xFF3F4238), // Dark Gray
          ),
        ),
      ),
    );
  }

  Widget _buildBack() {
    return Container(
      key: const ValueKey(false),
      decoration: BoxDecoration(
        color: const Color(0xFFF5F1E9), // Kraft Light
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: const Color(0xFF3F4238), width: 2),
        boxShadow: [
          BoxShadow(
            color: const Color(0xFF3F4238).withOpacity(0.2),
            blurRadius: 6,
            offset: const Offset(0, 3),
          ),
        ],
      ),
      child: const Center(
        child: Text(
          '⭐',
          style: TextStyle(
            fontFamily: 'Amatic SC',
            fontSize: 24,
            color: Color(0xFF3F4238), // Dark Gray
          ),
        ),
      ),
    );
  }
}
