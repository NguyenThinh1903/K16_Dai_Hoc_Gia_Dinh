const express = require('express');
const router = express.Router();
const {
  getPosts,
  getPost,
  createPost,
  updatePost,
  deletePost,
  searchPosts
} = require('../controllers/postController');
const { authenticateJWT, hasRole, optionalAuthJWT } = require('../middlewares/auth'); // isOwner removed from import
const { postValidation, postUpdateValidation } = require('../utils/validation');
const { apiLimiter, postManagementLimiter } = require('../middlewares/rateLimiter');

// Public routes
router.get('/', apiLimiter, optionalAuthJWT, getPosts);
router.get('/search', apiLimiter,optionalAuthJWT, searchPosts);
router.get('/:id', apiLimiter, optionalAuthJWT, getPost);

// Protected routes - only blog owner can create, update, delete posts
router.post('/', authenticateJWT, hasRole(['owner']), postManagementLimiter, postValidation, createPost); // isOwner replaced with hasRole(['owner'])
router.put('/:id', authenticateJWT, hasRole(['owner']), postManagementLimiter, postUpdateValidation, updatePost); // isOwner replaced with hasRole(['owner'])
router.delete('/:id', authenticateJWT, hasRole(['owner']), postManagementLimiter, deletePost); // isOwner replaced with hasRole(['owner'])

module.exports = router;