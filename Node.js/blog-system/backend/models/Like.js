const { DataTypes, Op } = require('sequelize');
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
    },
     validate: { // Added validation
        notNull: {
            msg: 'Post ID cannot be null.'
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
     validate: { // Added validation
        len: {
            args: [0, 255], // Assuming UUIDs or similar
            msg: 'Visitor ID must be a valid length'
        }
    }
  },
  like_type: {
    type: DataTypes.ENUM('like', 'dislike'),
    allowNull: false,
    validate: { // Added validation
        isIn: {
             args: [['like', 'dislike']],
            msg: 'Like type must be either like or dislike.'
        },
        notNull: {
            msg: 'Like type cannot be null.'
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
      fields: ['visitor_id']
    },
    {
      fields: ['user_id']
    },
    {
      unique: true,
      fields: ['post_id', 'user_id'],
      where: {
        user_id: {
          [Op.not]: null
        }
      }
    },
    {
      unique: true,
      fields: ['post_id', 'visitor_id'],
      where: {
        visitor_id: {
          [Op.not]: null
        }
      }
    }
  ]
});

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