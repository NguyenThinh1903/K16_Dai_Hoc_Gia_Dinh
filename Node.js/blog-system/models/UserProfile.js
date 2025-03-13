// models/UserProfile.js
const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/db');
const User = require('./User'); // Import the User model

const UserProfile = sequelize.define('UserProfile', {
  id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true
  },
  user_id: { // Foreign key to link to User model
    type: DataTypes.INTEGER,
    allowNull: false,
    unique: true, // One-to-one relationship, each profile belongs to one user
    references: {
      model: User, // Reference the User model directly
      key: 'id'
    }
  },
  name: {
    type: DataTypes.STRING,
    allowNull: true // Name is optional
  },
  avatar: {
    type: DataTypes.STRING,
    allowNull: true // Avatar URL or path is optional
  },
  bio: {
    type: DataTypes.TEXT,
    allowNull: true // Bio is optional
  }
}, {
  timestamps: true,
  createdAt: 'created_at',
  updatedAt: 'updated_at',
  tableName: 'user_profiles' // Explicitly set table name
});

UserProfile.belongsTo(User, { foreignKey: 'user_id' }); // Define the one-to-one relationship
User.hasOne(UserProfile, { foreignKey: 'user_id' });      // Define the one-to-one relationship

module.exports = UserProfile;