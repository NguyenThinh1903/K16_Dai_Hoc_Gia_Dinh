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
        transitionBuilder:
            (child, animation) => RotationTransition(
              turns: Tween<double>(begin: 0, end: 0.5).animate(animation),
              child: ScaleTransition(scale: animation, child: child),
            ),
        child: Container(
          key: ValueKey(isFlipped),
          decoration: BoxDecoration(
            color: isFlipped ? Colors.blue : Colors.grey.shade400,
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
              text,
              style: const TextStyle(fontSize: 24, color: Colors.white),
            ),
          ),
        ),
      ),
    );
  }
}
