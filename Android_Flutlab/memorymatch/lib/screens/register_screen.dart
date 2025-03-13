import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:memorymatch/services/auth_service.dart';
import 'package:memorymatch/screens/home_screen.dart';

class RegisterScreen extends StatefulWidget {
  const RegisterScreen({super.key});

  @override
  _RegisterScreenState createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final AuthService _auth = AuthService();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  bool _isLoading = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
            colors: [Color(0xFFF5F1E9), Color(0xFFE8D5C4)], // Gradient Kraft
          ),
        ),
        child: SafeArea(
          child: Center(
            child: SingleChildScrollView(
              padding: const EdgeInsets.symmetric(horizontal: 24.0),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Icon(
                    Icons.person_add,
                    size: 80,
                    color: Color(0xFF3F4238), // Dark Gray
                  ),
                  const SizedBox(height: 16),
                  const Text(
                    'Create Account',
                    style: TextStyle(
                      fontFamily: 'Amatic SC',
                      fontSize: 32,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF3F4238), // Dark Gray
                    ),
                  ),
                  const SizedBox(height: 48),
                  TextField(
                    controller: _emailController,
                    decoration: InputDecoration(
                      filled: true,
                      fillColor: Colors.white.withOpacity(0.9),
                      hintText: 'Email',
                      hintStyle: const TextStyle(
                        fontFamily: 'Amatic SC',
                        color: Color(0xFF9A9A9A), // Light Gray
                      ),
                      prefixIcon: const Icon(
                        Icons.email,
                        color: Color(0xFF3F4238), // Dark Gray
                      ),
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(12),
                        borderSide: BorderSide.none,
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  TextField(
                    controller: _passwordController,
                    obscureText: true,
                    decoration: InputDecoration(
                      filled: true,
                      fillColor: Colors.white.withOpacity(0.9),
                      hintText: 'Password',
                      hintStyle: const TextStyle(
                        fontFamily: 'Amatic SC',
                        color: Color(0xFF9A9A9A), // Light Gray
                      ),
                      prefixIcon: const Icon(
                        Icons.lock,
                        color: Color(0xFF3F4238), // Dark Gray
                      ),
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(12),
                        borderSide: BorderSide.none,
                      ),
                    ),
                  ),
                  const SizedBox(height: 24),
                  SizedBox(
                    width: double.infinity,
                    child: AnimatedScale(
                      scale: _isLoading ? 1.0 : 1.0,
                      duration: const Duration(milliseconds: 100),
                      child: ElevatedButton(
                        onPressed:
                            _isLoading
                                ? null
                                : () async {
                                  setState(() => _isLoading = true);
                                  try {
                                    User? user = await _auth.register(
                                      _emailController.text.trim(),
                                      _passwordController.text.trim(),
                                    );
                                    if (user != null && mounted) {
                                      Navigator.pushReplacement(
                                        context,
                                        MaterialPageRoute(
                                          builder:
                                              (context) => const HomeScreen(),
                                        ),
                                      );
                                    }
                                  } catch (e) {
                                    ScaffoldMessenger.of(context).showSnackBar(
                                      SnackBar(
                                        content: Text(
                                          e.toString().replaceFirst(
                                            'Exception: ',
                                            '',
                                          ),
                                          style: const TextStyle(
                                            fontFamily: 'Amatic SC',
                                            color: Colors.white,
                                          ),
                                        ),
                                        backgroundColor: const Color(
                                          0xFFE5989B,
                                        ), // Pastel Red
                                      ),
                                    );
                                  }
                                  setState(() => _isLoading = false);
                                },
                        onHover: (_) {
                          if (!_isLoading) {
                            setState(() {});
                          }
                        },
                        style: ElevatedButton.styleFrom(
                          backgroundColor: const Color(
                            0xFFF4A261,
                          ), // Pastel Orange
                          foregroundColor: const Color(
                            0xFFE76F51,
                          ), // Pastel Orange Dark
                          padding: const EdgeInsets.symmetric(vertical: 16),
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(12),
                            side: const BorderSide(
                              color: Color(0xFFE76F51),
                              width: 2,
                            ),
                          ),
                        ),
                        child:
                            _isLoading
                                ? const CircularProgressIndicator(
                                  color: Color(
                                    0xFFE76F51,
                                  ), // Pastel Orange Dark
                                )
                                : const Text(
                                  'Register',
                                  style: TextStyle(
                                    fontFamily: 'Amatic SC',
                                    fontSize: 18,
                                    color: Color(0xFF3F4238), // Dark Gray
                                  ),
                                ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  TextButton(
                    onPressed: () => Navigator.pop(context),
                    child: const Text(
                      'Already have an account? Login',
                      style: TextStyle(
                        fontFamily: 'Amatic SC',
                        color: Color(0xFF3F4238), // Dark Gray
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
