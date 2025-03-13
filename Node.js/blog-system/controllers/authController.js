const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const { validationResult } = require('express-validator');
const { User } = require('../models'); 

// Function to login the owner (now using database lookup)
exports.login = async (req, res, next) => { // Add 'next' to handle errors
  try {
    // Validate request (you likely already have this)
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        status: 'error',
        errors: errors.array()
      });
    }

    const { email, password } = req.body;

    // 1. Find the owner user by email and role 'owner' in the database
    const user = await User.findOne({ where: { email, role: 'owner' } });

    if (!user) {
      return res.status(401).json({ // 401 Unauthorized for invalid credentials
        status: 'error',
        message: 'Invalid credentials'
      });
    }

    // 2. Compare the provided password with the stored password hash
    const isMatch = await bcrypt.compare(password, user.password_hash);

    if (!isMatch) {
      return res.status(401).json({
        status: 'error',
        message: 'Invalid credentials'
      });
    }

    // 3. Create JWT payload (same as before, but now using user from database)
    const payload = {
      user: {
        id: user.id, // Include user ID
        role: user.role // Include user role ('owner')
      }
    };

    // 4. Sign and return the JWT (same as before)
    jwt.sign(
      payload,
      process.env.JWT_SECRET,
      { expiresIn: process.env.JWT_EXPIRES_IN },
      (err, token) => {
        if (err) throw err;
        res.json({
          status: 'success',
          token
        });
      }
    );

  } catch (error) {
    console.error('Owner login error:', error);
    next(error); // Pass errors to error handler
  }
};

// Function to login a regular user
exports.userLogin = async (req, res, next) => {
  try {
    // 1. Validate request
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        status: 'error',
        errors: errors.array()
      });
    }

    const { email, password } = req.body;

    // 2. Find the user by email
    const user = await User.findOne({ where: { email } });
    if (!user) {
      return res.status(401).json({ // 401 Unauthorized
        status: 'error',
        message: 'Invalid credentials'
      });
    }

    // 3. Compare the provided password with the stored hash
    const isMatch = await bcrypt.compare(password, user.password_hash);
    if (!isMatch) {
      return res.status(401).json({
        status: 'error',
        message: 'Invalid credentials'
      });
    }

    // 4. Create JWT payload
    const payload = {
      user: {
        id: user.id, // Use the user's ID
        role: user.role // Include the user's role
      }
    };

    // 5. Sign and return the JWT
    jwt.sign(
      payload,
      process.env.JWT_SECRET,
      { expiresIn: process.env.JWT_EXPIRES_IN },
      (err, token) => {
        if (err) throw err;
        res.json({
          status: 'success',
          token
        });
      }
    );

  } catch (error) {
    console.error('User login error:', error);
    next(error); // Pass errors to the global error handler
  }
};

// Helper function to generate a hash for the owner's password (for setup)
exports.generateHash = async (password) => {
  const salt = await bcrypt.genSalt(10);
  const hash = await bcrypt.hash(password, salt);
  return hash;
};