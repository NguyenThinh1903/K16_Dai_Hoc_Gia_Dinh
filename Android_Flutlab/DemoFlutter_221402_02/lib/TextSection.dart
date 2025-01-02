import 'package:flutter/cupertino.dart';

import 'package:flutter/material.dart';

class TextSection extends StatelessWidget {
  const TextSection({
    super.key,
    required this.description,
  });

  final String description;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(32),
      child: Text(
        description,
        style: TextStyle(color: Colors.green, fontSize: 20),
        softWrap: true,
      ),
    );
  }
}
