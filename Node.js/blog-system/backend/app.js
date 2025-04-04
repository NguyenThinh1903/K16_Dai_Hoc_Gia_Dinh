// C:\thangs\NODE_JS\blog-system_backup\blog-system\backend\app.js
const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const morgan = require('morgan');
const cookieParser = require('cookie-parser');
const path = require('path');
require('dotenv').config();

// Import routes - Do NOT use them yet.
const swaggerUi = require('swagger-ui-express');
const swaggerSpec = require('./swagger');
const adminRoutes = require('./routes/adminRoutes');
const authRoutes = require('./routes/authRoutes');
const postRoutes = require('./routes/postRoutes');
const commentRoutes = require('./routes/commentRoutes');
const likeRoutes = require('./routes/likeRoutes');
const userRoutes = require('./routes/userRoutes');

// Import middlewares
const { errorHandler, notFound } = require('./middlewares/errorHandler');
const { apiLimiter } = require('./middlewares/rateLimiter');
const fileUploadPromise = require('./middlewares/fileUpload'); // Get the promise

const app = express();

// CORS Configuration
const corsOptions = {
    origin: process.env.NODE_ENV === 'production'
        ? process.env.FRONTEND_URL
        : 'http://localhost:5173',
    credentials: true,
    methods: ['GET', 'POST', 'PUT', 'DELETE', 'PATCH', 'OPTIONS'],
    allowedHeaders: ['Content-Type', 'Authorization', 'x-visitor-id'],
};
app.use(cors(corsOptions));

// Middleware
app.use(helmet({
    crossOriginResourcePolicy: { policy: "cross-origin" }
}));
app.use(morgan('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(cookieParser());

// Serve static files from 'uploads'
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

// Rate limiting (apply to all /api routes)
app.use('/api', apiLimiter);

// Centralized Asynchronous Middleware Loading AND Route Setup
const startApp = async () => {
    try {
        // 1. WAIT for the file upload middleware to be ready:
        const { upload, deleteOldFile, logFileUploadRequest } = await fileUploadPromise;

        // 2. NOW set up your routes.  upload, etc., are DEFINITELY available.
        app.use('/api/auth', authRoutes);
        app.use('/api/posts', postRoutes(upload, logFileUploadRequest, deleteOldFile)); // Pass upload, etc.
        app.use('/api', commentRoutes);
        app.use('/api/', likeRoutes);
        app.use('/api/', userRoutes(upload, logFileUploadRequest)); // Pass upload, etc.
        app.use('/api/admin', adminRoutes);

        // Base route for API health check
        app.get('/api/health', (req, res) => {
            res.status(200).json({
                status: 'success',
                message: 'API is running',
                timestamp: new Date()
            });
        });

        // Swagger Documentation
        app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerSpec));

        // 3. Error Handling (Must be last)
        app.use(notFound);
        app.use(errorHandler);

        // Start the server
        const PORT = process.env.PORT || 3000;
        app.listen(PORT, () => {
            console.log(`Server is running on port ${PORT}`);
        });


    } catch (error) {
        console.error('Failed to initialize middleware or routes:', error);
        process.exit(1); // Exit if setup fails
    }
};


startApp();

module.exports = app;