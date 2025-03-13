const { Like, Post } = require('../models');
const { validationResult } = require('express-validator');
const { getUserIdOrGuestId } = require('../utils/user');

// Add or update a like/dislike
  exports.updateLike = async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({ errors: errors.array() });
    }

    const postId = req.params.id;
    const { like_type } = req.body;

    const post = await Post.findByPk(postId);
    if (!post) {
      return res.status(404).json({ message: 'Post not found' });
    }

    const { userId, visitorId } = getUserIdOrGuestId(req, res);
    
    // Create appropriate where clause
    const whereClause = {
      post_id: postId
    };
    
    // Prepare like data
    const likeData = {
      post_id: postId,
      like_type
    };
    
    if (userId) {
      whereClause.user_id = userId;
      likeData.user_id = userId;
      likeData.visitor_id = null;
    } else {
      whereClause.visitor_id = visitorId;
      likeData.visitor_id = visitorId;
      likeData.user_id = null;
    }

    // First check if a like already exists
    const existingLike = await Like.findOne({ where: whereClause });
    
    if (existingLike) {
      // Update existing like
      existingLike.like_type = like_type;
      await existingLike.save();
      return res.json({ 
        message: `Like updated to ${like_type}`, 
        data: existingLike 
      });
    } else {
      // Create new like
      const newLike = await Like.create(likeData);
      return res.status(201).json({ 
        message: `Like created: ${like_type}`, 
        data: newLike 
      });
    }
  } catch (error) {
    // Handle specific constraint errors
    if (error.name === 'SequelizeUniqueConstraintError') {
      return res.status(409).json({ message: 'You have already liked/disliked this post.' });
    }
    next(error);
  }
};
// Remove a like/dislike
exports.deleteLike = async (req, res, next) => {
  try {
    const postId = req.params.id;

    const post = await Post.findByPk(postId);
    if (!post) {
      return res.status(404).json({ message: 'Post not found' });
    }

    // Get user identification
    const { userId, visitorId } = getUserIdOrGuestId(req, res);

    // Build the where clause for the specific like
    const whereClause = {
      post_id: postId
    };

    if (userId) {
      whereClause.user_id = userId;
    } else if (visitorId) {
      whereClause.visitor_id = visitorId;
    } else {
      return res.status(403).json({ message: 'Authentication required' });
    }

    // Find the like
    const like = await Like.findOne({ where: whereClause });

    // Return 404 if like doesn't exist
    if (!like) {
      return res.status(404).json({ message: 'Like not found' });
    }

    // Check if the user is the owner of the like (this should always be true based on our where clause)
    // But we'll keep it as a safety check
    const isOwner = (userId && like.user_id === userId) || (visitorId && like.visitor_id === visitorId);
    
    // If admin or owner, allow deletion
    const isAdmin = req.user && req.user.role === 'admin';
    
    if (isOwner || isAdmin) {
      await like.destroy();
      return res.status(200).json({ message: 'Like removed' });
    } else {
      // This should rarely happen due to our where clause, but just in case
      return res.status(403).json({ message: 'Forbidden' });
    }
  } catch (error) {
    next(error);
  }
};