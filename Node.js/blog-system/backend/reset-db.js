const { sequelize, syncModels } = require('./models');
const readline = require('readline');
const { exec } = require('child_process');
const util = require('util');
const execPromise = util.promisify(exec);

// Create readline interface for user prompts
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

// Promisify the readline question method
const question = (query) => new Promise((resolve) => rl.question(query, resolve));

async function resetDatabase(environment, options = {}) {
  try {
    // Set the environment based on input or default to development
    process.env.NODE_ENV = environment || process.env.NODE_ENV || 'development';
    
    console.log(`\n==== Database Reset Tool ====`);
    console.log(`Target environment: ${process.env.NODE_ENV}`);
    
    // Confirm action for production or staging environment to prevent accidents
    if (['production', 'staging'].includes(process.env.NODE_ENV) && !options.force) {
      console.log('\n⚠️  WARNING: You are about to reset a sensitive database environment!');
      console.log('   This will DELETE ALL DATA. This action CANNOT be undone.\n');
      
      const confirmation = await question(`Type "${process.env.NODE_ENV}" to confirm, or anything else to cancel: `);
      
      if (confirmation.toLowerCase() !== process.env.NODE_ENV.toLowerCase()) {
        console.log('\n❌ Operation cancelled. No changes were made.');
        return false;
      }
      
      console.log('\n⚠️  You have confirmed a potentially destructive operation.');
      
      if (process.env.NODE_ENV === 'production' && !options.forceProduction) {
        const doubleConfirmation = await question(`For PRODUCTION, please type "YES I UNDERSTAND THE CONSEQUENCES" to proceed: `);
        
        if (doubleConfirmation !== 'YES I UNDERSTAND THE CONSEQUENCES') {
          console.log('\n❌ Operation cancelled. No changes were made.');
          return false;
        }
      }
    }

    // Connect to the database
    console.log('\n🔄 Connecting to database...');
    await sequelize.authenticate();
    console.log(`✅ Connected to the ${process.env.NODE_ENV} database.`);

    // Drop and recreate all tables in the correct order
    console.log('\n🔄 Dropping existing tables...');
    
    if (sequelize.options.dialect === 'mysql') {
      await sequelize.query('SET FOREIGN_KEY_CHECKS = 0');
    } else if (sequelize.options.dialect === 'postgres') {
      // For PostgreSQL
      await sequelize.query('SET session_replication_role = replica;');
    }
    
    await sequelize.drop(); 
    
    if (sequelize.options.dialect === 'mysql') {
      await sequelize.query('SET FOREIGN_KEY_CHECKS = 1');
    } else if (sequelize.options.dialect === 'postgres') {
      // For PostgreSQL
      await sequelize.query('SET session_replication_role = default;');
    }
    
    console.log('✅ All tables dropped successfully.');

    // Sync models to recreate tables
    console.log('\n🔄 Recreating database schema...');
    await syncModels(true);
    console.log('✅ Database schema recreated successfully.');

    // Run seeders if requested
    if (options.seed) {
      try {
        console.log('\n🔄 Running database seeders...');
        const command = `npx sequelize-cli db:seed:all --env ${process.env.NODE_ENV}`;
        
        const { stdout, stderr } = await execPromise(command);
        
        if (stdout) console.log(`Seeder output: ${stdout}`);
        if (stderr && !stderr.includes('Deprecation Warning')) console.error(`Seeder warnings: ${stderr}`);
        
        console.log('✅ Database seeded successfully.');
      } catch (error) {
        console.error('\n❌ Error running seeders:');
        if (error.stdout) console.log(error.stdout);
        if (error.stderr) console.error(error.stderr);
        throw new Error('Seeding failed');
      }
    }

    console.log('\n✅ Database reset complete!');
    return true;

  } catch (error) {
    console.error('\n❌ Error during database reset:');
    console.error(error);
    return false;
  } finally {
    // Close the database connection
    if (sequelize) {
      await sequelize.close();
      console.log('📝 Database connection closed.');
    }
    
    // Close readline interface
    rl.close();
  }
}

// Process command line arguments
if (require.main === module) {
  // Only parse args when running directly (not when imported)
  const args = process.argv.slice(2);
  const options = {
    seed: args.includes('--seed'),
    force: args.includes('--force'),
    forceProduction: args.includes('--force-production')
  };
  
  // Environment is the first argument that doesn't start with --
  const environment = args.find(arg => !arg.startsWith('--'));
  
  resetDatabase(environment, options)
    .then(success => {
      process.exit(success ? 0 : 1);
    })
    .catch(err => {
      console.error('Unhandled error:', err);
      process.exit(1);
    });
} else {
  // Export the function when this file is imported
  module.exports = resetDatabase;
}