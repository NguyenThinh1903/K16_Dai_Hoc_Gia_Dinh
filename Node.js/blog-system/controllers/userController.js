// controllers/userController.js
const { User, UserProfile } = require('../models');
const bcrypt = require('bcrypt');
const { validationResult } = require('express-validator');

// controllers/userController.js - Updated registerUser function
exports.registerUser = async (req, res, next) => {
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

    // 2. Check if user already exists
    const existingUser = await User.findOne({ where: { email } });
    if (existingUser) {
      return res.status(409).json({
        status: 'error',
        message: 'Email already registered'
      });
    }

    // 3. Hash the password
    const salt = await bcrypt.genSalt(10);
    const password_hash = await bcrypt.hash(password, salt);

    // 4. Determine the role based on the route
    // If this is accessed through the admin registration route, create an admin user
    // Otherwise, create a regular user
    const role = req.originalUrl.includes('/admin/register') ? 'admin' : 'user';

    // 5. Create the user with the appropriate role
    const newUser = await User.create({
      email,
      password_hash,
      role: role
    });

    // 6. Create the user profile
    const newProfile = await UserProfile.create({
      user_id: newUser.id,
    });

    // 7. Return success response
    res.status(201).json({
      status: 'success',
      message: `${role.charAt(0).toUpperCase() + role.slice(1)} registered successfully`,
      data: {
        user: {
          id: newUser.id,
          role: newUser.role
        }
      }
    });

  } catch (error) {
    next(error);
  }
};

exports.getAllUsers = async (req, res, next) => {
  try {
    // Fetch all users with their associated profiles
    const users = await User.findAll({
      include: [{
        model: UserProfile,
        attributes: ['name', 'avatar', 'bio'] // Include only specific profile attributes
      }],
      attributes: { exclude: ['password_hash'] } // Exclude sensitive data
    });

    res.json({
      status: 'success',
      data: {
        users
      }
    });

  } catch (error) {
    next(error);
  }
};

// Get own profile (protected)
exports.getUserProfile = async (req, res, next) => {
  try {
    const user = await User.findByPk(req.user.id, {
      include: [{
        model: UserProfile,
        attributes: ['name', 'avatar', 'bio'] // Get profile data
      }],
      attributes: { exclude: ['password_hash'] } // Exclude password
    });

    if (!user) {
      return res.status(404).json({ message: 'User not found' });
    }

    res.json({ data: user });
  } catch (error) {
    next(error);
  }
};


// Update own profile (protected)
exports.updateUserProfile = async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({ errors: errors.array() });
    }

    const { name, avatar, bio } = req.body;

    const userProfile = await UserProfile.findOne({ where: { user_id: req.user.id } });

    if (!userProfile) {
      return res.status(404).json({ message: 'User profile not found' });
    }

    // Update fields if provided
    if (name) userProfile.name = name;
    if (avatar) userProfile.avatar = avatar;
    if (bio) userProfile.bio = bio;

    await userProfile.save();

    res.json({ message: 'Profile updated', data: userProfile });
  } catch (error) {
    next(error);
  }
};

//view any profile
exports.viewAnyProfile = async(req, res, next) => {
    try{
        const userId = req.params.userId;
        const user = await User.findByPk(userId, {
          include: [{
            model: UserProfile,
            attributes: ['name', 'avatar', 'bio'] // Get profile data
          }],
          attributes: { exclude: ['password_hash', 'role'] } // Exclude password and role
        });

        if (!user) {
          return res.status(404).json({ message: 'User not found' });
        }

        res.json({ data: user });
    }
    catch(error){
        next(error);
    }
};

//update any profile (admin)
exports.updateAnyProfile = async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({ errors: errors.array() });
    }
    const userId = req.params.userId;
    const { name, avatar, bio } = req.body;

    const userProfile = await UserProfile.findOne({ where: { user_id: userId} });

    if (!userProfile) {
      return res.status(404).json({ message: 'User profile not found' });
    }

    // Update fields if provided
    if (name) userProfile.name = name;
    if (avatar) userProfile.avatar = avatar;
    if (bio) userProfile.bio = bio;

    await userProfile.save();

    res.json({ message: 'Profile updated', data: userProfile });
  } catch (error) {
    next(error);
  }
};

//delete own profile
exports.deleteOwnProfile = async (req, res, next) => {
    try {
        const user = await User.findByPk(req.user.id);
        const userProfile = await UserProfile.findOne({where : {user_id: req.user.id}});
        if (!user || !userProfile) {
          return res.status(404).json({ message: 'User not found' });
        }
        await userProfile.destroy();
        await user.destroy();
        res.status(200).json({message: 'User deleted'});
    } catch (error) {
        next(error)
    }
};

//delete any profile
exports.deleteAnyProfile = async(req, res, next) => {
    try {
        const userId = req.params.userId;
        const user = await User.findByPk(userId);
        const userProfile = await UserProfile.findOne({where: {user_id: userId}});
        if(!user || !userProfile){
            return res.status(404).json({message: 'User not found'});
        }
        await userProfile.destroy();
        await user.destroy();
        res.status(200).json({message: 'User deleted'});
    } catch (error) {
        next(error)
    }
};