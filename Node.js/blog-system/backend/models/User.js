const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/db');

const User = sequelize.define('User', {
  id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true
  },
  email: {
    type: DataTypes.STRING,
    allowNull: false,
    unique: { // Added better unique
        args: true,
        msg: 'Email address already in use'
    },
    validate: {
      isEmail: {
        msg: 'Must be a valid email address.'
      },
      notEmpty: {
        msg: 'Email cannot be empty.'
      }
    }
  },
  password_hash: {
    type: DataTypes.STRING,
    allowNull: false,
     validate: { //Added validation
        notEmpty: {
            msg: 'Password hash cannot be empty.'
        }
    }
  },
  role: {
    type: DataTypes.ENUM('user', 'admin', 'owner'),
    defaultValue: 'user',
    validate: { // Added validation
        isIn: {
            args: [['user', 'admin', 'owner']],
            msg: 'Role must be user, admin or owner'
        }
    }
  }
}, {
  timestamps: true,
  createdAt: 'created_at',
  updatedAt: 'updated_at',
  tableName: 'users'
});

module.exports = User;