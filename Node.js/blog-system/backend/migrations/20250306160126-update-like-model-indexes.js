'use strict';
const { Op } = require('sequelize');  // Import Op directly

module.exports = {
  up: async (queryInterface, Sequelize) => {
    // First, remove the existing composite index
    await queryInterface.removeIndex('Likes', ['post_id', 'visitor_id', 'user_id']);
    
    // Add the new separate indexes
    await queryInterface.addIndex('Likes', ['post_id', 'user_id'], {
      unique: true,
      name: 'likes_post_user_unique',
      where: {
        user_id: {
          [Op.not]: null  // Use Op directly, not Sequelize.Op
        }
      }
    });
    
    await queryInterface.addIndex('Likes', ['post_id', 'visitor_id'], {
      unique: true,
      name: 'likes_post_visitor_unique',
      where: {
        visitor_id: {
          [Op.not]: null  // Use Op directly, not Sequelize.Op
        }
      }
    });
  },

  down: async (queryInterface, Sequelize) => {
    // Revert changes - remove new indexes
    await queryInterface.removeIndex('Likes', 'likes_post_user_unique');
    await queryInterface.removeIndex('Likes', 'likes_post_visitor_unique');
    
    // Restore original index
    await queryInterface.addIndex('Likes', ['post_id', 'visitor_id', 'user_id'], {
      unique: true,
      name: 'likes_composite_unique'
    });
  }
};