// migrations/2024XXXXXXXX-add-image-url-to-posts.js
'use strict';

module.exports = {
  async up (queryInterface, Sequelize) {
    await queryInterface.addColumn('Posts', 'image_url', {
      type: Sequelize.STRING, // Or Sequelize.TEXT if you might store very long URLs
      allowNull: true, // Images are optional
    });
  },

  async down (queryInterface, Sequelize) {
    await queryInterface.removeColumn('Posts', 'image_url');
  }
};