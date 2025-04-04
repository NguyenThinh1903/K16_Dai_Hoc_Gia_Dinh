// C:\thangs\NODE_JS\blog-system_backup\blog-system\backend\middlewares\errorHandler.js
const notFound = (req, res, next) => {
  const error = new Error(`Not Found - ${req.originalUrl}`);
  res.status(404);
  next(error); // Pass the error to the error handler
};

const errorHandler = (err, req, res, next) => {
console.error(err.stack);

let status = err.statusCode || 500;
let message = err.message || 'Something went wrong on the server';

  if (err.name === 'SequelizeValidationError') {
    status = 400; // Bad Request
    message = err.errors.map(e => e.message).join(', '); // Collect all validation error messages
  } else if (err.name === 'SequelizeUniqueConstraintError'){
      status = 409;
      message = err.errors.map(e => e.message).join(', '); // Collect all validation error messages
  }

const response = {
  status: 'error',
  message,
};

if (process.env.NODE_ENV === 'development') {
  response.stack = err.stack;
}

res.status(status).json(response);
};

module.exports = { errorHandler, notFound };