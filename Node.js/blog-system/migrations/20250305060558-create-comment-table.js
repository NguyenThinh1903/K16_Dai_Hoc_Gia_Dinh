'use strict';
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('Comments', {
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
          model: 'Posts', // Table name (usually plural)
          key: 'id'
        },
        onDelete: 'CASCADE' // If a post is deleted, delete its comments
      },
      user_id: {
        type: Sequelize.INTEGER,
        allowNull: true, // Allow null for guest comments
        references: {
          model: 'users',
          key: 'id'
        },
        onDelete: 'SET NULL' // If a user is deleted, set user_id to NULL
      },
      visitor_id: {
        type: Sequelize.STRING,
        allowNull: true // Allow registered users to not have a visitor_id
      },
      content: {
        type: Sequelize.TEXT,
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
  },
  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable('Comments');
  }
};