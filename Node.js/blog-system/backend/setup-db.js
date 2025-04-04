const { exec } = require('child_process');
const util = require('util');
const readline = require('readline');
require('dotenv').config(); // Load .env for environment detection

const execPromise = util.promisify(exec);

// Create readline interface for user prompts
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

// Promisify the readline question method
const question = (query) => new Promise((resolve) => rl.question(query, resolve));

async function runCommand(command, description) {
  console.log(`\n🔄 ${description}...`);
  console.log(`   Running: ${command}`);
  try {
    const { stdout, stderr } = await execPromise(command);
    if (stdout) console.log(`   Output:\n${stdout}`);
    // Ignore common non-error stderr messages from sequelize-cli if needed
    if (stderr && !stderr.includes('Loaded configuration') && !stderr.includes('Using environment')) {
        console.warn(`   Warnings/Info:\n${stderr}`);
    }
    console.log(`✅ ${description} completed.`);
    return true;
  } catch (error) {
    console.error(`\n❌ Error during: ${description}`);
    if (error.stdout) console.error(`   STDOUT:\n${error.stdout}`);
    if (error.stderr) console.error(`   STDERR:\n${error.stderr}`);
    // Optionally check for specific errors like "database exists" and treat as success
    if (error.stderr && error.stderr.includes('database exists') && description.includes('Creating database')) {
        console.warn('   Database already exists, continuing setup.');
        return true; // Treat as success if database already exists
    }
    console.error('   Setup failed.');
    return false;
  }
}

async function setupDatabase(environment) {
    try {
        process.env.NODE_ENV = environment || process.env.NODE_ENV || 'development';
        const nodeEnv = process.env.NODE_ENV;

        console.log(`\n==== Database Setup Tool ====`);
        console.log(`Target environment: ${nodeEnv}`);

        if (['production', 'staging'].includes(nodeEnv)) {
            console.log('\n⚠️  WARNING: You are setting up a sensitive database environment!');
            const confirmation = await question(`Type "${nodeEnv}" to confirm, or anything else to cancel: `);
            if (confirmation.toLowerCase() !== nodeEnv.toLowerCase()) {
                console.log('\n❌ Operation cancelled.');
                return false;
            }
        }

        // 1. Create Database (Optional check - sequelize-cli handles existence)
        if (!await runCommand(`npx sequelize-cli db:create --env ${nodeEnv}`, 'Creating database')) {
             // If db:create failed for reasons other than "already exists", stop.
             if (!console.output?.includes('already exists')) return false; // Rough check, improve if needed
        }


        // 2. Run Migrations
        if (!await runCommand(`npx sequelize-cli db:migrate --env ${nodeEnv}`, 'Running migrations')) {
            return false;
        }

        // 3. Run Seeders (Optional - prompt user?)
        const runSeeds = await question(`\nDo you want to run seeders to add initial data (e.g., owner user)? (yes/no): `);
        if (runSeeds.toLowerCase() === 'yes' || runSeeds.toLowerCase() === 'y') {
             if (!await runCommand(`npx sequelize-cli db:seed:all --env ${nodeEnv}`, 'Running seeders')) {
                 // Decide if failing seeds should stop the whole process
                 console.warn('⚠️ Seeding failed, but setup might be partially complete.');
                 // return false; // Uncomment if seed failure should halt everything
             }
        } else {
            console.log('\nSkipping seeders.');
        }

        console.log(`\n✅ Database setup for environment '${nodeEnv}' complete!`);
        return true;

    } catch (error) {
        console.error('\n❌ An unexpected error occurred during setup:');
        console.error(error);
        return false;
    } finally {
        rl.close();
    }
}

// --- Script Execution ---
if (require.main === module) {
    const args = process.argv.slice(2);
    const environment = args.find(arg => !arg.startsWith('--')); // Basic arg parsing

    setupDatabase(environment)
        .then(success => {
            process.exit(success ? 0 : 1);
        })
        .catch(err => {
            console.error('Unhandled setup error:', err);
            process.exit(1);
        });
} else {
    module.exports = setupDatabase; // Export if needed elsewhere
}