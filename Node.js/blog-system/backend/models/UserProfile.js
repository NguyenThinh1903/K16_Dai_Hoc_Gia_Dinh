const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/db');
const User = require('./User');

const UserProfile = sequelize.define('UserProfile', {
  id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true
  },
  user_id: {
    type: DataTypes.INTEGER,
    allowNull: false,
    unique: true,
    references: {
      model: User,
      key: 'id'
    },
     validate: { // Added validation
        notNull: {
            msg: 'User ID cannot be null'
        }
    }
  },
  name: {
    type: DataTypes.STRING,
    allowNull: true,
     validate: { // Added validation
        len: {
            args: [0, 255],
            msg: 'Name must be less than 255 character'
        }
    }
  },
  avatar: {
    type: DataTypes.STRING,
    allowNull: true,
    // validate: { // Added validation
    //     isUrl: {
    //         msg: 'Avatar must be a valid URL'
    //     }
    // }
  },
  bio: {
    type: DataTypes.TEXT,
    allowNull: true,
    validate: { // Added validation
        len: {
            args: [0, 1000],
            msg: 'Bio must be less than 1000 character'
        }
    }
  }
}, {
  timestamps: true,
  createdAt: 'created_at',
  updatedAt: 'updated_at',
  tableName: 'user_profiles'
});

User.hasOne(UserProfile, { foreignKey: 'user_id', as: 'profile' });
UserProfile.belongsTo(User, { foreignKey: 'user_id' }); 

module.exports = UserProfile;