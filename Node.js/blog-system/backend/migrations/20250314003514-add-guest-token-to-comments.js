// backend/migrations/2024XXXXXXXX-add-guest-token-to-comments.js (replace XXX with timestamp)
'use strict';

module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.addColumn('Comments', 'guest_token', {
      type: Sequelize.STRING,
      allowNull: true, // Only guest comments will have this.
      unique: true     // VERY IMPORTANT: Ensure uniqueness.
    });
  },

  async down(queryInterface, Sequelize) {
    await queryInterface.removeColumn('Comments', 'guest_token');
  }
};