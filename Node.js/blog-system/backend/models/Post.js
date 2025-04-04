const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/db');

const Post = sequelize.define('Post', {
  id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true
  },
  title: {
    type: DataTypes.STRING(100),
    allowNull: false,
    validate: {
      notEmpty: {
        msg: 'Title cannot be empty.'
      },
      len: {
        args: [1, 100],
        msg: 'Title must be between 1 and 100 characters.'
      }
    }
  },
  content: {
    type: DataTypes.TEXT,
    allowNull: false,
    validate: {
      notEmpty: {
        msg: 'Content cannot be empty.'
      },
      len: { //Added min length
        args: [1, 10000],
        msg: 'Content must be between 1 and 10,000 characters'
      }
    }
  },
  status: {
    type: DataTypes.ENUM('draft', 'published'),
    defaultValue: 'published',
    validate: { // Added validation
        isIn: {
            args: [['draft', 'published']],
            msg: 'Status must be either draft or published'
        }
    }
  },
  image_url: {
    type: DataTypes.STRING,
    allowNull: true,
    // validate: { // Added validation
    //     isUrl: {
    //         msg: 'Image URL must be a valid URL'
    //     }
    // }
  },
},
{
  timestamps: true,
  createdAt: 'created_at',
  updatedAt: 'updated_at',
  indexes: [
    {
      type: 'FULLTEXT',
      fields: ['title', 'content']
    }
  ]
});

module.exports = Post;