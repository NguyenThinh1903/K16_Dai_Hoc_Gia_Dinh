// models/index.js
const { sequelize } = require('../config/db');
const Post = require('./Post');
const Comment = require('./Comment');
const Like = require('./Like');
const User = require('./User');
const UserProfile = require('./UserProfile');
const { testConnection } = require('../config/db');

// Set up associations
Post.hasMany(Comment, { foreignKey: 'post_id', onDelete: 'CASCADE' });
Comment.belongsTo(Post, { foreignKey: 'post_id' });
// Ensure this line is present and correct after modifying Comment model:
Comment.belongsTo(User, { foreignKey: 'user_id', as: 'user' }); // <---- Verify this line is here and correct
Like.belongsTo(User, { foreignKey: 'user_id', as: 'user' });


Post.hasMany(Like, { foreignKey: 'post_id', onDelete: 'CASCADE' });
Like.belongsTo(Post, { foreignKey: 'post_id' });

// Define User and UserProfile associations here
User.hasOne(UserProfile, { foreignKey: 'user_id' });
UserProfile.belongsTo(User, { foreignKey: 'user_id' });


// Function to sync all models with the database
const syncModels = async (force = false) => {
  try {
    // Sync User FIRST
    await User.sync({ force });
    console.log('User model synced successfully');

    // Then sync UserProfile
    await UserProfile.sync({ force });
    console.log('UserProfile model synced successfully');

    // Then sync the rest
    await Post.sync({ force });
    console.log('Post model synced successfully');

    await Comment.sync({ force });
    console.log('Comment model synced successfully');

    await Like.sync({ force });
    console.log('Like model synced successfully');

    console.log('All Models synced successfully');
  } catch (error) {
    console.error('Failed to sync models:', error);
  }
};

module.exports = {
  sequelize,
  Post,
  Comment,
  Like,
  User,
  UserProfile,
  syncModels,
  testConnection
};