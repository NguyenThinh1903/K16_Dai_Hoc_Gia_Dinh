const { sequelize, syncModels } = require('./models'); // Import sequelize directly
const logger = require('./utils/logger');
const app = require('./app'); // NOW it's safe to import app


const startServer = async () => {
  try {
    await sequelize.authenticate(); // This tests the connection.
    logger.info('Database connection has been established successfully.');
    const forceSync = process.env.DB_SYNC_FORCE === 'false';
    await syncModels(forceSync); // Consider making 'force' configurable via environment variable
    // Now that the DB is setup, start the app.
    //This will go inside the app.js and trigger the server start
  } catch (error) {
    logger.error('Failed to start server:', error);
    process.exit(1);
  }
};

process.on('unhandledRejection', (reason, promise) => {
  logger.error(`Unhandled Rejection at: ${promise}, reason: ${reason}`);
  process.exit(1); // Or consider a graceful shutdown
});

process.on('uncaughtException', (error) => {
  logger.error(`Uncaught Exception:`, error); // Log the FULL error
  process.exit(1); // Or consider a graceful shutdown
});

startServer(); 