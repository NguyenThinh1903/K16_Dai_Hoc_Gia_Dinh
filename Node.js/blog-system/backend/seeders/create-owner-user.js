'use strict';

const bcrypt = require('bcrypt');

module.exports = {
  up: async (queryInterface, Sequelize) => {
    const ownerEmail = 'thang@example.com'; // Use a variable for clarity
    const hashedPassword = await bcrypt.hash('secure_password!?', 10); // Replace 'secure_password!?' if needed

    // --- 1. Insert the User ---
    try {
      await queryInterface.bulkInsert('users', [{
        email: ownerEmail,
        password_hash: hashedPassword,
        role: 'owner',
        created_at: new Date(),
        updated_at: new Date()
      }], {});
      console.log(`User ${ownerEmail} inserted.`); // Log success
    } catch (error) {
      console.error(`Error inserting user ${ownerEmail}:`, error);
      throw error; // Re-throw to stop the migration/seeder
    }

    // --- 2. Find the newly created User's ID ---
    let ownerUserId;
    try {
        const users = await queryInterface.sequelize.query(
          `SELECT id FROM users WHERE email = :email`,
          {
            replacements: { email: ownerEmail }, // Use replacements to prevent SQL injection
            type: queryInterface.sequelize.QueryTypes.SELECT // Specify query type
          }
        );

        if (!users || users.length === 0) {
          // This should ideally not happen if the insert above succeeded
          throw new Error(`Could not find user with email ${ownerEmail} immediately after insertion.`);
        }
        ownerUserId = users[0].id;
        console.log(`Found user ID ${ownerUserId} for email ${ownerEmail}.`); // Log success

    } catch(error) {
         console.error(`Error finding user ID for ${ownerEmail}:`, error);
         throw error; // Re-throw
    }


    // --- 3. Insert the corresponding UserProfile ---
    try {
      await queryInterface.bulkInsert('user_profiles', [{
        user_id: ownerUserId, // Use the retrieved ID
        name: null,           // Default to null or set an initial name
        avatar: null,         // Default to null
        bio: null,            // Default to null
        created_at: new Date(),
        updated_at: new Date()
      }], {});
      console.log(`UserProfile for user ID ${ownerUserId} inserted.`); // Log success
    } catch(error){
        console.error(`Error inserting profile for user ID ${ownerUserId}:`, error);
        // Optionally: attempt to clean up the user if profile creation fails?
        // await queryInterface.bulkDelete('users', { id: ownerUserId }, {});
        throw error; // Re-throw
    }
  },

  down: async (queryInterface, Sequelize) => {
    const ownerEmail = 'thang@example.com'; // Make sure this matches the email used in 'up'

    console.log(`Attempting to remove user ${ownerEmail} and associated profile.`);

    // Find the user ID to ensure we delete the correct profile (safer than just relying on email for profile)
    const users = await queryInterface.sequelize.query(
      `SELECT id FROM users WHERE email = :email`,
      {
        replacements: { email: ownerEmail },
        type: queryInterface.sequelize.QueryTypes.SELECT
      }
    );

    if (users && users.length > 0) {
        const ownerUserId = users[0].id;
        // It's generally safer to delete the referencing row (profile) first,
        // although CASCADE should handle it if migrations are correct.
        await queryInterface.bulkDelete('user_profiles', { user_id: ownerUserId }, {});
        console.log(`UserProfile for user ID ${ownerUserId} deleted.`);
    } else {
        console.log(`User ${ownerEmail} not found, skipping profile deletion.`);
    }

    // Then delete the user
    await queryInterface.bulkDelete('users', { email: ownerEmail }, {});
    console.log(`User ${ownerEmail} deleted.`);
  }
};