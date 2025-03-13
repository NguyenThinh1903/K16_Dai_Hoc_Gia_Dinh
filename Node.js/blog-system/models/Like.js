const { DataTypes, Op } = require('sequelize');  // Add Op to imports
const { sequelize } = require('../config/db');
const User = require('./User');

const Like = sequelize.define('Like', {
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
    allowNull: true
  },
  like_type: {
    type: DataTypes.ENUM('like', 'dislike'),
    allowNull: false
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
      fields: ['visitor_id']
    },
    {
      fields: ['user_id']
    },
    // The unique constraint should account for one of either user_id OR visitor_id being null
    // Using a partial index or custom validation would be better, but this is a common workaround:
    {
      unique: true,
      fields: ['post_id', 'user_id'],
      where: {
        user_id: {
          [Op.not]: null  // Use Op directly, not sequelize.Op
        }
      }
    },
    {
      unique: true,
      fields: ['post_id', 'visitor_id'],
      where: {
        visitor_id: {
          [Op.not]: null  // Use Op directly, not sequelize.Op
        }
      }
    }
  ]
});

// Add a class method to help with finding likes
Like.findByUserOrVisitor = async function(postId, userId, visitorId) {
  const whereClause = { post_id: postId };
  
  if (userId) {
    whereClause.user_id = userId;
  } else if (visitorId) {
    whereClause.visitor_id = visitorId;
  }
  
  return await this.findOne({ where: whereClause });
};

Like.belongsTo(User, { foreignKey: 'user_id', as: 'likeUser' });

module.exports = Like;