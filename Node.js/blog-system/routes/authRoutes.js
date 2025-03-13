const express = require('express');
const router = express.Router();
const { login, userLogin } = require('../controllers/authController');
const { loginValidation } = require('../utils/validation');
const { authLimiter } = require('../middlewares/rateLimiter');

// Login route
router.post('/login', authLimiter, loginValidation, login); // Owner login
//user login route
router.post('/user-login', authLimiter, loginValidation, userLogin); // User login

module.exports = router;