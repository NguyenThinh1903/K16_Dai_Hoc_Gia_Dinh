const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const morgan = require('morgan');
const cookieParser = require('cookie-parser'); 
require('dotenv').config();

// Import routes
const adminRoutes = require('./routes/adminRoutes');
const authRoutes = require('./routes/authRoutes');
const postRoutes = require('./routes/postRoutes');
const commentRoutes = require('./routes/commentRoutes');
const likeRoutes = require('./routes/likeRoutes');
const userRoutes = require('./routes/userRoutes');


// Import middlewares
const { errorHandler, notFound } = require('./middlewares/errorHandler');
const { apiLimiter } = require('./middlewares/rateLimiter');

// Create Express app
const app = express();

// Middleware
app.use(helmet()); // Set security-related HTTP headers
app.use(morgan('dev')); // HTTP request logger
app.use(cors()); // Enable CORS for all routes
app.use(express.json()); // Parse JSON request bodies
app.use(express.urlencoded({ extended: true })); // Parse URL-encoded request bodies
app.use(cookieParser());

// Rate limiting
app.use('/api', apiLimiter);

// Routes
// Authentication
app.use('/api/auth', authRoutes);

// Posts & Comments
app.use('/api/posts', postRoutes);  // Handles /api/posts
app.use('/api', commentRoutes);    // Handles /api/posts/:id/comments and /api/comments/:id

// Likes
app.use('/api', likeRoutes); // Handles /api/posts/:id/likes

// Users & Profiles
app.use('/api', userRoutes);     // Handles user registration, profile access
app.use('/api/admin', adminRoutes); // Admin-specific routes

// Base route for API health check
app.get('/api/health', (req, res) => {
  res.status(200).json({
    status: 'success',
    message: 'API is running',
    timestamp: new Date()
  });
});

// Error handling
app.use(notFound);
app.use(errorHandler);

module.exports = app;