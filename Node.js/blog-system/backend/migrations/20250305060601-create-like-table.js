'use strict';
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('Likes', {
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      post_id: {
        type: Sequelize.INTEGER,
        allowNull: false,
        references: {
          model: 'Posts',
          key: 'id'
        },
        onDelete: 'CASCADE' // If a post is deleted, delete its likes
      },
      user_id: {
        type: Sequelize.INTEGER,
        allowNull: true,
        references: {
          model: 'users',
          key: 'id'
        },
        onDelete: 'SET NULL'
      },
      visitor_id: {
        type: Sequelize.STRING,
        allowNull: true
      },
      like_type: {
        type: Sequelize.ENUM('like', 'dislike'),
        allowNull: false
      },
      created_at: {
        allowNull: false,
        type: Sequelize.DATE
      },
      updated_at: {
        allowNull: false,
        type: Sequelize.DATE
      }
    });
    // Add unique constraint to prevent duplicate likes
    await queryInterface.addConstraint('Likes', {
        fields: ['post_id', 'visitor_id', 'user_id'],
        type: 'unique',
        name: 'unique_like_constraint'
    });
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.removeConstraint('Likes', 'unique_like_constraint');
    await queryInterface.dropTable('Likes');
  }
};