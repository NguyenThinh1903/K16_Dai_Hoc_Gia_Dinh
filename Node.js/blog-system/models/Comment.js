const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/db');
const User = require('./User'); // Import the User model

const Comment = sequelize.define('Comment', {
  id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true
  },
  post_id: {
    type: DataTypes.INTEGER,
    allowNull: false,
    references: {
      model: 'Posts',
      key: 'id'
    }
  },
  user_id: { // New foreign key for registered users
    type: DataTypes.INTEGER,
    allowNull: true, // Allow null for guest comments
    references: {
      model: User, // Reference the User model
      key: 'id'
    }
  },
  visitor_id: {
    type: DataTypes.STRING,
    allowNull: true // Make visitor_id optional (for registered users)
  },
  content: {
    type: DataTypes.TEXT,
    allowNull: false,
    validate: {
      notEmpty: true,
      len: [1, 500]
    }
  }
}, {
  timestamps: true,
  createdAt: 'created_at',
  updatedAt: 'updated_at',
  indexes: [
    {
      fields: ['post_id']
    },
    {
      fields: ['user_id'] // Index user_id
    },
    {
      fields: ['visitor_id'] // Keep index on visitor_id
    }
  ]
});

// Correct the association definition
Comment.belongsTo(User, { foreignKey: 'user_id', as: 'commentUser' }); // Use 'commentUser' as alias
module.exports = Comment;