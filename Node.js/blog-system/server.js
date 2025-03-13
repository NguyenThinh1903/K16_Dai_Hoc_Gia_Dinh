const app = require('./app');
const { testConnection, syncModels } = require('./models');

// Set the port
const PORT = process.env.PORT || 3000;

// Start the server
const startServer = async () => {
  try {
    // Test database connection
    await testConnection();
    
    // Sync models with database
    await syncModels(false); // Set to true to force recreate tables (use carefully)
    
    // Start the Express server
    app.listen(PORT, () => {
      console.log(`Server running on port ${PORT}`);
      console.log(`Environment: ${process.env.NODE_ENV || 'development'}`);
    });
  } catch (error) {
    console.error('Failed to start server:', error);
    process.exit(1);
  }
};

// Handle unhandled promise rejections
process.on('unhandledRejection', (reason, promise) => {
  console.error('UNHANDLED REJECTION at:', promise, 'reason:', reason);
  process.exit(1);
});

// Start the server
startServer();