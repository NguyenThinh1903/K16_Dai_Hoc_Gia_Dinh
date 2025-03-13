// routes/userRoutes.js
const express = require('express');
const router = express.Router();
const userController = require('../controllers/userController');
const { registrationValidation, profileValidation } = require('../utils/validation');
const { apiLimiter } = require('../middlewares/rateLimiter');
const { authenticateJWT, hasRole } = require('../middlewares/auth');

// User Registration Route (for regular users - public)
router.post('/register', apiLimiter, registrationValidation, userController.registerUser);

// Get own profile (protected)
router.get('/users/profile', apiLimiter, authenticateJWT, userController.getUserProfile);

// Update own profile (protected)
router.put('/users/profile', apiLimiter, authenticateJWT, profileValidation, userController.updateUserProfile);

//view any profile (public)
router.get('/users/:userId/profile', apiLimiter, userController.viewAnyProfile);

//update any profile (admin)
router.put('/admin/users/:userId/profile', apiLimiter,authenticateJWT, hasRole(['admin', 'owner']), profileValidation, userController.updateAnyProfile);

//delete own profile
router.delete('/users/profile', apiLimiter, authenticateJWT, userController.deleteOwnProfile);

//delete any profile (admin)
router.delete('/admin/users/:userId/profile', apiLimiter, authenticateJWT, hasRole(['admin', 'owner']), userController.deleteAnyProfile);
module.exports = router;