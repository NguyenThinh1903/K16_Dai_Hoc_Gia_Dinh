// .\routes\commentRoutes.js
const express = require('express');
const router = express.Router();
const {
  getComments,
  getComment,
  addComment,
  updateComment,
  deleteComment
} = require('../controllers/commentController');
const { commentValidation } = require('../utils/validation');
const { apiLimiter } = require('../middlewares/rateLimiter');
const { authenticateJWT,optionalAuthJWT } = require('../middlewares/auth'); // Import authenticateJWT

// Get comments for a post
router.get('/posts/:id/comments', apiLimiter, getComments);

// Get a single comment
router.get('/comments/:id', apiLimiter, getComment);

//Add a comment to a post
router.post('/posts/:id/comments', apiLimiter, optionalAuthJWT, commentValidation, addComment);

// Update a comment - PROTECTED ROUTE
router.put('/comments/:id', optionalAuthJWT, apiLimiter, commentValidation, updateComment); // Added authenticateJWT

// Delete a comment - PROTECTED ROUTE
router.delete('/comments/:id', optionalAuthJWT, apiLimiter, deleteComment); // Added authenticateJWT

module.exports = router;