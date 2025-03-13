// Global error handling middleware
const errorHandler = (err, req, res, next) => {
    console.error(err.stack);
    
    // Default error status and message
    const status = err.statusCode || 500;
    const message = err.message || 'Something went wrong on the server';
    
    res.status(status).json({
      status: 'error',
      message,
      ...(process.env.NODE_ENV === 'development' && { stack: err.stack })
    });
  };
  
  // Not found middleware
  const notFound = (req, res, next) => {
    const error = new Error(`Not Found - ${req.originalUrl}`);
    error.statusCode = 404;
    next(error);
  };
  
  module.exports = {
    errorHandler,
    notFound
  };