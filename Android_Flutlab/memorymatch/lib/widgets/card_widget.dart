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
        transitionBuilder: (Widget child, Animation<double> animation) {
          final rotateAnimation = Tween<double>(begin: 0.0, end: 3.14).animate(
            CurvedAnimation(parent: animation, curve: Curves.easeInOut),
          );
          return AnimatedBuilder(
            animation: rotateAnimation,
            builder: (context, child) {
              final isUnder = (rotateAnimation.value / 3.14).floor() % 2 == 0;
              return Transform(
                transform: Matrix4.rotationY(rotateAnimation.value),
                alignment: Alignment.center,
                child: Transform(
                  transform: Matrix4.rotationY(
                    rotateAnimation.value,
                  ), // Xoay ngược lại để bù
                  alignment: Alignment.center,
                  child:
                      isUnder
                          ? _buildBack()
                          : ScaleTransition(
                            scale: Tween<double>(begin: 0.95, end: 1.0).animate(
                              CurvedAnimation(
                                parent: animation,
                                curve: Curves.easeInOut,
                              ),
                            ),
                            child: _buildFront(),
                          ),
                ),
              );
            },
          );
        },
        switchInCurve: Curves.easeInOut,
        switchOutCurve: Curves.easeInOut,
        child: isFlipped ? _buildFront() : _buildBack(),
      ),
    );
  }

  Widget _buildFront() {
    return Container(
      key: const ValueKey(true),
      padding: const EdgeInsets.all(8.0),
      decoration: BoxDecoration(
        color: Colors.brown[50],
        borderRadius: BorderRadius.circular(12),
        border: Border.all(
          color: Colors.black54,
          width: 2,
          style: BorderStyle.solid,
        ),
        boxShadow: [
          BoxShadow(
            color: Colors.black26,
            blurRadius: 4,
            offset: const Offset(2, 2),
          ),
        ],
      ),
      child: Center(
        child: Text(
          text,
          style: const TextStyle(
            fontFamily: 'Amatic SC',
            fontSize: 24,
            color: Colors.black87,
            fontWeight: FontWeight.bold,
          ),
          textAlign: TextAlign.center,
        ),
      ),
    );
  }

  Widget _buildBack() {
    return Container(
      key: const ValueKey(false),
      padding: const EdgeInsets.all(8.0),
      decoration: BoxDecoration(
        color: Colors.brown[50],
        borderRadius: BorderRadius.circular(12),
        border: Border.all(
          color: Colors.black54,
          width: 2,
          style: BorderStyle.solid,
        ),
        boxShadow: [
          BoxShadow(
            color: Colors.black26,
            blurRadius: 4,
            offset: const Offset(2, 2),
          ),
        ],
      ),
      child: const Center(
        child: Text(
          '⭐',
          style: TextStyle(
            fontFamily: 'Amatic SC',
            fontSize: 24,
            color: Colors.black87,
            fontWeight: FontWeight.bold,
          ),
          textAlign: TextAlign.center,
        ),
      ),
    );
  }
}
