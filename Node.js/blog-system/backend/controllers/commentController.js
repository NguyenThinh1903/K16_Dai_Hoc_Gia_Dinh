const { Comment, Post, User, UserProfile } = require('../models');
const { validationResult } = require('express-validator');
const { sanitizeContent } = require('../utils/sanitize');
const { getUserIdOrGuestId } = require('../utils/user');
const { checkCommentPermission } = require('../utils/permissions');
const AppError = require('../utils/AppError'); // Import AppError
const logger = require('../utils/logger');     // Import logger
const { v4: uuidv4 } = require('uuid'); // Import uuidv4

// Get all comments for a post with pagination
exports.getComments = async (req, res, next) => {
  try {
    const postId = req.params.id;
    const page = parseInt(req.query.page) || 1;
    const limit = parseInt(req.query.limit) || 10;
    const offset = (page - 1) * limit;

    const post = await Post.findByPk(postId);
    if (!post) {
      return next(new AppError('Post not found', 404)); // Use AppError
    }

    // Inside commentController.js -> getComments
const { count, rows: comments } = await Comment.findAndCountAll({
  where: { post_id: postId },
  include: [
    {
      model: User,
      as: 'user', // This matches Comment.belongsTo(User, { as: 'user' }) - CORRECT
      attributes: ['id', 'role'],
      required: false, // Use LEFT JOIN - important if a user might be deleted
      include: [{
        model: UserProfile,
        // --- ADD THIS ALIAS ---
        as: 'profile', // Tell Sequelize to nest UserProfile data under the key 'profile'
        // -----------------------
        attributes: ['name', 'avatar'],
        required: false // Use LEFT JOIN - important if a profile might be missing
      }]
    }
  ],
  limit,
  offset,
  order: [['created_at', 'DESC']]
});

    res.json({
      status: 'success',
      data: {
        comments,
        totalComments: count,
        currentPage: page,
        totalPages: Math.ceil(count / limit)
      }
    });
  } catch (error) {
    logger.error('Error getting comments:', error); // Log the error
    next(error); // Pass to error handler
  }
};

// Get a single comment by ID
exports.getComment = async (req, res, next) => {
  try {
    const commentId = req.params.id;
    const comment = await Comment.findByPk(commentId);

    if (!comment) {
      return next(new AppError('Comment not found', 404)); // Use AppError
    }

    res.json({
      status: 'success',
      data: comment
    });
  } catch (error) {
    logger.error(`Error getting comment with ID ${req.params.id}:`, error);
    next(error);
  }
};

// Add a comment to a post
exports.addComment = async (req, res, next) => {
  try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
          return next(new AppError('Validation failed', 400, errors.array()));
      }

      const postId = req.params.id;
      const { content } = req.body;

      const { userId, visitorId } = getUserIdOrGuestId(req, res);

      const post = await Post.findByPk(postId);
      if (!post) {
          return next(new AppError('Post not found', 404));
      }

      let guestToken = null;
      if (!userId) { // It's a guest comment
          guestToken = uuidv4(); // Generate a unique token
      }

      const comment = await Comment.create({
          post_id: postId,
          user_id: userId,
          visitor_id: visitorId, // Still store this for display/filtering
          content: sanitizeContent(content),
          guest_token: guestToken // Store the token in the database
      });

      const responseData = {
        data: comment,
      }
      if (guestToken){
        responseData.guestToken = guestToken
      }
      res.status(201).json(responseData);

  } catch (error) {
      logger.error('Error adding comment:', error);
      next(error);
  }
};

const checkCommentPermissionUpdated = (req, comment) => {
  if (req.user) {
      return comment.user_id === req.user.id || ['admin', 'owner'].includes(req.user.role);
  } else {
      // User is a guest.  Check the guest_token.
      const providedGuestToken = req.body.guestToken; // Get token from request body
      return comment.guest_token && comment.guest_token === providedGuestToken;
  }
};

// Edit a comment
exports.updateComment = async (req, res, next) => {
  try {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return next(new AppError('Validation failed', 400, errors.array()));
      }

      const commentId = req.params.id;
      const { content } = req.body;

      const comment = await Comment.findByPk(commentId);
      if (!comment) {
          return next(new AppError('Comment not found', 404));
      }

      if (checkCommentPermissionUpdated(req, comment)) {
          comment.content = sanitizeContent(content);
          await comment.save();
          return res.json({ data: comment });
      } else {
          return next(new AppError('Forbidden', 403));
      }
  } catch (error) {
      logger.error(`Error updating comment with ID ${req.params.id}:`, error);
      next(error);
  }
};

// Delete a comment
exports.deleteComment = async (req, res, next) => {
  try {
      const commentId = req.params.id;

      const comment = await Comment.findByPk(commentId);
      if (!comment) {
          return next(new AppError('Comment not found', 404));
      }

      if (checkCommentPermissionUpdated(req, comment)) {
          await comment.destroy();
          return res.json({ message: 'Comment deleted' });
      } else {
          return next(new AppError('Forbidden', 403));
      }
  } catch (error) {
      logger.error(`Error deleting comment with ID ${req.params.id}:`, error);
      next(error);
  }
};