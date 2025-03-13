import 'package:flutter/material.dart';

class CardWidget extends StatelessWidget {
  final String text;
  final VoidCallback onTap;
  final bool isFlipped;

  const CardWidget({
    super.key,
    required this.text,
    required this.onTap,
    required this.isFlipped,
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
              final isBack = rotate.value <= 0.5;
              final angle =
                  isBack ? rotate.value * 3.14 : (1 - rotate.value) * 3.14;
              return Transform(
                transform: Matrix4.rotationY(angle),
                alignment: Alignment.center,
                child: ScaleTransition(
                  scale: Tween<double>(begin: 1.0, end: 1.1).animate(animation),
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
        color: Colors.brown[50],
        borderRadius: BorderRadius.circular(12),
        border: Border.all(
          color: Colors.black54,
          width: 2,
          style: BorderStyle.solid,
        ),
      ),
      child: Center(
        child: Text(
          text,
          style: const TextStyle(
            fontFamily: 'Amatic SC',
            fontSize: 24,
            color: Colors.black87,
          ),
        ),
      ),
    );
  }

  Widget _buildBack() {
    return Container(
      key: const ValueKey(false),
      decoration: BoxDecoration(
        color: Colors.brown[50],
        borderRadius: BorderRadius.circular(12),
        border: Border.all(
          color: Colors.black54,
          width: 2,
          style: BorderStyle.solid,
        ),
      ),
      child: const Center(
        child: Text(
          '⭐',
          style: TextStyle(
            fontFamily: 'Amatic SC',
            fontSize: 24,
            color: Colors.black87,
          ),
        ),
      ),
    );
  }
}
