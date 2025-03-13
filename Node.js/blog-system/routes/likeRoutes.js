const express = require('express');
const router = express.Router();
const { updateLike, deleteLike } = require('../controllers/likeController');
const { likeValidation } = require('../utils/validation');
const { apiLimiter } = require('../middlewares/rateLimiter');
const { optionalAuthJWT, authJWT } = require('../middlewares/auth');

// Like or dislike a post (works for both registered users and guests)
router.put('/posts/:id/likes', apiLimiter, optionalAuthJWT, likeValidation, updateLike);

// Remove a like/dislike (works for both registered users and guests)
router.delete('/posts/:id/likes', apiLimiter, optionalAuthJWT, deleteLike);

module.exports = router;