// backend/models/Comment.js
const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/db');
const User = require('./User');

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
    },
    validate: {
        notNull: {
            msg: 'Post ID cannot be null'
        }
    }
  },
  user_id: {
    type: DataTypes.INTEGER,
    allowNull: true,
    references: {
      model: User,
      key: 'id'
    }
  },
  visitor_id: {
    type: DataTypes.STRING,
    allowNull: true,
    validate: {
        len: {
            args: [0, 255],
            msg: 'Visitor ID must be within valid length'
        }
    }
  },
  content: {
    type: DataTypes.TEXT,
    allowNull: false,
    validate: {
      notEmpty: {
        msg: 'Comment content cannot be empty.'
      },
      len: {
        args: [1, 500],
        msg: 'Comment must be between 1 and 500 characters.'
      }
    }
  },
  guest_token: { // Add this block
    type: DataTypes.STRING,
    allowNull: true,
    unique: true,
    validate: {
        len: {
            args: [0, 255], // Ensure it's not excessively long.
            msg: 'Guest token must be a valid length.'
        }
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
      fields: ['user_id']
    },
    {
      fields: ['visitor_id']
    },
    { // Add this index for guest_token
      fields: ['guest_token'],
      unique: true, // Ensure uniqueness in the index as well
    }
  ]
});

Comment.belongsTo(User, { foreignKey: 'user_id', as: 'commentUser' });
module.exports = Comment;