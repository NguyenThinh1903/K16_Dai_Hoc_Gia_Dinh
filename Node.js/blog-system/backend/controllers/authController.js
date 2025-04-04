const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const { validationResult } = require('express-validator');
const { User } = require('../models');
const AppError = require('../utils/AppError');
const logger = require('../utils/logger'); // Import the logger

exports.login = async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return next(new AppError('Validation failed', 400, errors.array()));
    }

    const { email, password } = req.body;
    const user = await User.findOne({ where: { email, role: 'owner' } });

    if (!user) {
      return next(new AppError('Invalid credentials', 401));
    }

    const isMatch = await bcrypt.compare(password, user.password_hash);
    if (!isMatch) {
      return next(new AppError('Invalid credentials', 401));
    }

    const payload = { user: { id: user.id, role: user.role } };
    const token = jwt.sign(payload, process.env.JWT_SECRET, { expiresIn: process.env.JWT_EXPIRES_IN });
    res.json({ status: 'success', token });

  } catch (error) {
    logger.error('Error in login:', error); // Log the error
    next(error);
  }
};

exports.userLogin = async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return next(new AppError('Validation failed', 400, errors.array()));
    }

    const { email, password } = req.body;
    const user = await User.findOne({
      where: {
        email,
        role: { [require('sequelize').Op.ne]: 'owner' } 
      }
    });

    if (!user) {
      return next(new AppError('Invalid credentials', 401)); 
    }

    const isMatch = await bcrypt.compare(password, user.password_hash);
    if (!isMatch) {
      return next(new AppError('Invalid credentials', 401)); 
    }

    const payload = {
      user: {
        id: user.id,
        role: user.role
      }
    };

    jwt.sign(
      payload,
      process.env.JWT_SECRET,
      { expiresIn: process.env.JWT_EXPIRES_IN },
      (err, token) => {
        if (err) return next(err); // Pass JWT errors to error handler
        res.json({
          status: 'success',
          token
        });
      }
    );

  } catch (error) {
    logger.error('Error in userLogin:', error); // Log the error
    next(error);
  }
};