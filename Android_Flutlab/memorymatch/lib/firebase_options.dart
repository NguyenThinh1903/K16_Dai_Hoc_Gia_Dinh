import 'package:firebase_core/firebase_core.dart' show FirebaseOptions;
import 'package:flutter/foundation.dart'
    show defaultTargetPlatform, kIsWeb, TargetPlatform;

class DefaultFirebaseOptions {
  static FirebaseOptions get currentPlatform {
    if (kIsWeb) {
      return web;
    }
    throw UnsupportedError(
      'DefaultFirebaseOptions are not supported for this platform.',
    );
  }

  static const FirebaseOptions web = FirebaseOptions(
    apiKey: "AIzaSyAByh6MyW5u231DFjhdTGwcO36ubRNvrxc",
    authDomain: "guessthenumber-3ac61.firebaseapp.com",
    projectId: "guessthenumber-3ac61",
    storageBucket: "guessthenumber-3ac61.firebasestorage.app",
    messagingSenderId: "622610841946",
    appId: "1:622610841946:web:89875a338dc80652cfb363",
    measurementId: "G-52D187W7YF",
  );
}
