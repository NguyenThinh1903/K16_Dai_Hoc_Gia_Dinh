'use strict';
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('Posts', { // Sequelize pluralizes by default
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      title: {
        type: Sequelize.STRING(100),
        allowNull: false
      },
      content: {
        type: Sequelize.TEXT,
        allowNull: false
      },
      status: {
        type: Sequelize.ENUM('draft', 'published'),
        defaultValue: 'published'
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
    // Add FULLTEXT index (for searching)
    await queryInterface.addIndex('Posts', ['title', 'content'], {
        type: 'FULLTEXT',
        name: 'title_content_fulltext' // Optional name
    });
  },
  async down(queryInterface, Sequelize) {
      await queryInterface.removeIndex('Posts', 'title_content_fulltext');
    await queryInterface.dropTable('Posts');
  }
};