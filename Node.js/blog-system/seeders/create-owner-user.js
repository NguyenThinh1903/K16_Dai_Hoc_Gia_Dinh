'use strict';

const bcrypt = require('bcrypt');

module.exports = {
  up: async (queryInterface, Sequelize) => {
    const hashedPassword = await bcrypt.hash('secure_password!?', 10); // Replace 'owner_password'

    return queryInterface.bulkInsert('users', [{
      email: 'thang@example.com', // Replace with the owner's email
      password_hash: hashedPassword,
      role: 'owner',
      created_at: new Date(),
      updated_at: new Date()
    }], {});
  },

  down: async (queryInterface, Sequelize) => {
    return queryInterface.bulkDelete('users', { email: 'owner@example.com' }, {});
  }
};