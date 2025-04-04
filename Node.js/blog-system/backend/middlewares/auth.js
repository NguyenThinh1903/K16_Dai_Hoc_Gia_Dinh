const jwt = require('jsonwebtoken');

const authenticateJWT = (req, res, next) => {
  const authHeader = req.headers.authorization;
  console.log('Auth Header:', authHeader); // Log the entire header

  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return res.status(401).json({
      status: 'error',
      message: 'Unauthorized: No token provided'
    });
  }

  const token = authHeader.split(' ')[1];
  console.log('Extracted Token:', token); // Log the extracted token

  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    console.log('Decoded Payload:', decoded); // Log the decoded payload
    req.user = decoded.user;
    next();
  } catch (error) {
    console.error('JWT Verification Error:', error); // Log any errors
    return res.status(401).json({
      status: 'error',
      message: 'Unauthorized: Invalid token'
    });
  }
};

// Create a new middleware in your auth.js file
const optionalAuthJWT = (req, res, next) => {
  const authHeader = req.headers.authorization;
 
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    // No token, proceed as guest
    return next();
  }
 
  const token = authHeader.split(' ')[1];
 
  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    req.user = decoded.user;
    next();
  } catch (error) {
     logger.warn('Invalid token provided, proceeding as guest');
    next(); 
  }
 };


const hasRole = (allowedRoles) => {
  return (req, res, next) => {
    if (req.user && allowedRoles.includes(req.user.role)) {
      next();
    } else {
      return res.status(403).json({
        status: 'error',
        message: `Forbidden: This action requires one of these roles: ${allowedRoles.join(', ')}`
      });
    }
  };
};

module.exports = {
  authenticateJWT,
  optionalAuthJWT,
  hasRole
};
