// Simple in-memory rate limiter
const rateLimit = require('express-rate-limit');

const createRateLimiter = (windowMs, max, message) => {
  return rateLimit({
    windowMs: windowMs || parseInt(process.env.RATE_LIMIT_WINDOW) * 60 * 1000, // Default to env config in minutes
    max: max || parseInt(process.env.RATE_LIMIT_MAX_REQUESTS), // Default to env config
    message: {
      status: 'error',
      message: message || 'Too many requests, please try again later'
    },
    standardHeaders: true,
    legacyHeaders: false
  });
};

// General API rate limiter
const apiLimiter = createRateLimiter();

// Stricter limiter for post management (create/edit/delete)
const postManagementLimiter = createRateLimiter(
  15 * 60 * 1000, // 15 minutes
  150, // 5 requests per window
  'Too many post management requests, please try again later'
);

// Limiter for authentication attempts
const authLimiter = createRateLimiter(
  60 * 60 * 1000, // 1 hour
  150, // 10 requests per window
  'Too many login attempts, please try again later'
);

module.exports = {
  apiLimiter,
  postManagementLimiter,
  authLimiter
};