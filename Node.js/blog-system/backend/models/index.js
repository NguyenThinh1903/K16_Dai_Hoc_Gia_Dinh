
const { sequelize } = require('../config/db');
const Post = require('./Post');
const Comment = require('./Comment');
const Like = require('./Like');
const User = require('./User');
const UserProfile = require('./UserProfile');
const logger = require('../utils/logger'); // Import the logger

// Set up associations
Post.hasMany(Comment, { foreignKey: 'post_id', onDelete: 'CASCADE' });
Comment.belongsTo(Post, { foreignKey: 'post_id' });
Comment.belongsTo(User, { foreignKey: 'user_id', as: 'user' });
Like.belongsTo(User, { foreignKey: 'user_id', as: 'user' });


Post.hasMany(Like, { foreignKey: 'post_id', onDelete: 'CASCADE' });
Like.belongsTo(Post, { foreignKey: 'post_id' });

User.hasOne(UserProfile, { foreignKey: 'user_id' });
UserProfile.belongsTo(User, { foreignKey: 'user_id' });

const syncModels = async (force = false) => {
  try {
    // Sync in dependency order:
    await User.sync({ force });
    await UserProfile.sync({ force }); // Depends on User
    await Post.sync({ force });
    await Comment.sync({ force }); // Depends on Post and User
    await Like.sync({ force });   // Depends on Post and User

    logger.info('All Models synced successfully');
  } catch (error) {
    logger.error('Failed to sync models:', error);
    throw error; // Re-throw the error!
  }
};

module.exports = {
  sequelize,
  Post,
  Comment,
  Like,
  User,
  UserProfile,
  syncModels
};