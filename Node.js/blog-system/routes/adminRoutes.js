// routes/adminRoutes.js
const express = require('express');
const router = express.Router();
const userController = require('../controllers/userController');
const { registrationValidation } = require('../utils/validation');
const { authenticateJWT, hasRole } = require('../middlewares/auth');

// Admin Registration Route (owner only)
router.post('/register', authenticateJWT, hasRole(['owner']), registrationValidation, userController.registerUser);

// Get All Users
router.get('/users', authenticateJWT, hasRole(['admin', 'owner']), userController.getAllUsers);

module.exports = router;