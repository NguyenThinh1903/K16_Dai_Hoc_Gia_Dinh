const { User, UserProfile } = require('../models');
const bcrypt = require('bcrypt');
const { validationResult } = require('express-validator');
const AppError = require('../utils/AppError');
const logger = require('../utils/logger');

exports.registerUser = async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return next(new AppError('Validation failed', 400, errors.array()));
    }

    const { email, password } = req.body;
    const existingUser = await User.findOne({ where: { email } });
    if (existingUser) {
      return next(new AppError('Email already registered', 409));
    }

    const salt = await bcrypt.genSalt(10);
    const password_hash = await bcrypt.hash(password, salt);
    const role = req.originalUrl.includes('/admin/register') ? 'admin' : 'user';

    const newUser = await User.create({
      email,
      password_hash,
      role: role
    });

    const newProfile = await UserProfile.create({
      user_id: newUser.id,
    });

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
    logger.error('Error in registerUser:', error);
    next(error);
  }
};

exports.getAllUsers = async (req, res, next) => {
  try {
    const users = await User.findAll({
      include: [{
        model: UserProfile,
        attributes: ['name', 'avatar', 'bio']
      }],
      attributes: { exclude: ['password_hash'] }
    });

    res.json({
      status: 'success',
      data: {
        users
      }
    });

  } catch (error) {
    logger.error('Error in getAllUsers:', error);
    next(error);
  }
};

exports.getUserProfile = async (req, res, next) => {
  try {
    const user = await User.findByPk(req.user.id, {
      include: [{
        model: UserProfile,
        attributes: ['name', 'avatar', 'bio']
      }],
      attributes: { exclude: ['password_hash'] }
    });

    if (!user) {
      return next(new AppError('User not found', 404));
    }

    res.json({ data: user });
  } catch (error) {
    logger.error('Error in getUserProfile:', error);
    next(error);
  }
};
// userController.js
const updateUserProfileFields = async (userProfile, updateData, req) => {
  const oldAvatar = userProfile.avatar; // Store old avatar before update

  let changesMade = false; // Flag to track if DB needs saving

  // Update name conditionally
  if (updateData.name !== undefined) { // Check if 'name' was provided in the request
      const trimmedName = updateData.name.trim();
      // Allow update if new name is non-empty OR if current DB value is already null/empty
      if (trimmedName !== "" || !userProfile.name) {
          if (userProfile.name !== trimmedName) { // Only update if value is actually different
              userProfile.name = trimmedName;
              changesMade = true;
          }
      } else {
          // If name has a value in DB and "" was sent, log and ignore
          logger.debug(`Ignoring empty 'name' update for profile ${userProfile.id} because database value is not empty.`);
      }
  }

  // Update bio similarly
  if (updateData.bio !== undefined) { // Check if 'bio' was provided
      const trimmedBio = updateData.bio.trim();
      // Allow update if new bio is non-empty OR if current DB value is already null/empty
      if (trimmedBio !== "" || !userProfile.bio) {
           if (userProfile.bio !== trimmedBio) { // Only update if value is actually different
              userProfile.bio = trimmedBio;
              changesMade = true;
          }
      } else {
           // If bio has a value in DB and "" was sent, log and ignore
          logger.debug(`Ignoring empty 'bio' update for profile ${userProfile.id} because database value is not empty.`);
      }
  }

  // Handle avatar upload
  if (req.file) {
      const newAvatarPath = `/uploads/${req.file.filename}`;
      if (userProfile.avatar !== newAvatarPath) { // Only update if path is different
           userProfile.avatar = newAvatarPath;
           changesMade = true;
      }
  }

  // Only save if there are actual changes
  if (changesMade) {
      await userProfile.save();
      logger.info(`User profile ${userProfile.id} updated.`);

      // Delete old avatar file *after* successful save, only if it changed
      if (req.file && oldAvatar && oldAvatar !== userProfile.avatar) {
           // Make sure req.deleteOldFile exists before calling
           if (req.deleteOldFile && typeof req.deleteOldFile === 'function') {
               await req.deleteOldFile(oldAvatar);
               logger.info(`Deleted old avatar ${oldAvatar} for profile ${userProfile.id}.`);
           } else {
               logger.warn(`req.deleteOldFile function not available when trying to delete old avatar for profile ${userProfile.id}.`);
           }
      }
  } else {
       logger.info(`User profile ${userProfile.id} update request received, but no actual changes detected.`);
       // If only an identical file was uploaded, multer saves it, but we might want to delete it if no DB changes occurred.
       if (req.file && oldAvatar === userProfile.avatar) {
           logger.info(`Uploaded avatar for profile ${userProfile.id} is identical to the old one. Deleting the newly uploaded file.`);
           if (req.deleteOldFile && typeof req.deleteOldFile === 'function') {
               await req.deleteOldFile(`/uploads/${req.file.filename}`);
           }
       }
  }

  return userProfile;
}


exports.updateUserProfile = async (req, res, next) => {
  try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
          // If validation fails now, it's likely due to max length or invalid chars
          return next(new AppError('Validation failed', 400, errors.array()));
      }

      logger.debug(`Looking up profile for user ID: ${req.user.id}`);
      const userProfile = await UserProfile.findOne({ where: { user_id: req.user.id }});

      if (!userProfile) {
          logger.error(`No profile found for user ID: ${req.user.id}`);
          // Send a 404 if the profile itself doesn't exist
          return next(new AppError('User profile not found', 404));
      }

      // Call the helper, passing 'req' so it can access req.deleteOldFile
      const updatedUserProfile = await updateUserProfileFields(userProfile, req.body, req);

      res.json({ message: 'Profile update processed', data: updatedUserProfile }); // Changed message slightly

  } catch (error) {
      logger.error('Error in updateUserProfile:', error);
      // IMPORTANT: If save fails, delete the *newly uploaded* file
      if (req.file) {
          logger.warn(`Rolling back file upload due to error: deleting ${req.file.filename}`);
          // Use the attached deleteOldFile function (it works for any file path)
          if (req.deleteOldFile && typeof req.deleteOldFile === 'function') {
              await req.deleteOldFile(`/uploads/${req.file.filename}`).catch(delErr => {
                  logger.error(`Failed to delete newly uploaded file ${req.file.filename} during error handling:`, delErr);
              });
          } else {
               logger.error("deleteOldFile function not found on request object during error handling.");
          }
      }
      next(error);
  }
};

// --- MODIFIED Controller Endpoint ---
exports.updateAnyProfile = async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return next(new AppError('Validation failed', 400, errors.array()));
    }
    const userId = req.params.userId;

    const userProfile = await UserProfile.findOne({ where: { user_id: userId} });

    if (!userProfile) {
      return next(new AppError('User profile not found', 404));
    }

    // Call the helper, passing 'req'
    const updatedUserProfile = await updateUserProfileFields(userProfile, req.body, req);

    res.json({ message: 'Profile update processed', data: updatedUserProfile });
  } catch (error) {
      logger.error("Error updating any profile:", error);
      // IMPORTANT: If save fails, delete the *newly uploaded* file
       if (req.file) {
          logger.warn(`Rolling back file upload due to error: deleting ${req.file.filename}`);
          if (req.deleteOldFile && typeof req.deleteOldFile === 'function') {
               await req.deleteOldFile(`/uploads/${req.file.filename}`).catch(delErr => {
                  logger.error(`Failed to delete newly uploaded file ${req.file.filename} during error handling:`, delErr);
              });
          } else {
               logger.error("deleteOldFile function not found on request object during error handling.");
          }
       }
    next(error);
  }
};


exports.viewAnyProfile = async(req, res, next) => {
    try{
        const userId = req.params.userId;
        const user = await User.findByPk(userId, {
          include: [{
            model: UserProfile,
            attributes: ['name', 'avatar', 'bio']
          }],
          attributes: { exclude: ['password_hash', 'role'] }
        });

        if (!user) {
          return next(new AppError('User not found', 404));
        }

        res.json({ data: user });
    }
    catch(error){
        logger.error("Error viewing any profile", error);
        next(error);
    }
};

exports.deleteOwnProfile = async (req, res, next) => {
    try {
        const user = await User.findByPk(req.user.id);
        const userProfile = await UserProfile.findOne({where : {user_id: req.user.id}});
        if (!user || !userProfile) {
          return next(new AppError('User not found', 404));
        }
        await userProfile.destroy();
        await user.destroy();
        res.status(200).json({message: 'User deleted'});
    } catch (error) {
        logger.error("Error deleting own profile", error);
        next(error)
    }
};

exports.deleteAnyProfile = async(req, res, next) => {
    try {
        const userId = req.params.userId;
        const user = await User.findByPk(userId);
        const userProfile = await UserProfile.findOne({where: {user_id: userId}});
        if(!user || !userProfile){
            return next(new AppError('User not found', 404));
        }
        await userProfile.destroy();
        await user.destroy();
        res.status(200).json({message: 'User deleted'});
    } catch (error) {
        logger.error("Error deleting any profile", error);
        next(error)
    }
};