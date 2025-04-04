require('dotenv').config();
const { User } = require('../models');
const bcrypt = require('bcrypt');

async function createOwner() {
  try {
    const email = 'thang@example.com'; // Change this to your desired owner email
    const password = 'secure_password!?'; // Change this to your desired owner password

    // Check if user exists
    let user = await User.findOne({ where: { email } });

    if (user) {
      // Update existing user to owner
      await user.update({ role: 'owner' });
      console.log(`User ${email} updated to owner role`);
    } else {
      // Create new owner
      const salt = await bcrypt.genSalt(10);
      const password_hash = await bcrypt.hash(password, salt);

      user = await User.create({
        email,
        password_hash,
        role: 'owner'
      });

      console.log(`Owner created with email: ${email}`);
    }
  } catch (error) {
    console.error('Error creating owner:', error);
  }
}

createOwner()
  .then(() => process.exit(0))
  .catch(err => {
    console.error(err);
    process.exit(1);
  });