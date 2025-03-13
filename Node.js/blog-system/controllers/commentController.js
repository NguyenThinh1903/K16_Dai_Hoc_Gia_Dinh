const { Comment, Post } = require('../models');
const { validationResult } = require('express-validator');
const { sanitizeContent } = require('../utils/sanitize');
const { getUserIdOrGuestId } = require('../utils/user');
const { checkCommentPermission } = require('../utils/permissions');

// Get all comments for a post with pagination
exports.getComments = async (req, res, next) => {
  try {
    const postId = req.params.id;
    const page = parseInt(req.query.page) || 1;
    const limit = parseInt(req.query.limit) || 10;
    const offset = (page - 1) * limit;
    
    // Check if post exists
    const post = await Post.findByPk(postId);
    if (!post) {
      return res.status(404).json({
        status: 'error',
        message: 'Post not found'
      });
    }
    
    // Get comments with pagination
    const { count, rows: comments } = await Comment.findAndCountAll({
      where: { post_id: postId },
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
    next(error);
  }
};

// Get a single comment by ID
exports.getComment = async (req, res, next) => {
  try {
    const commentId = req.params.id;
    
    const comment = await Comment.findByPk(commentId);
    
    if (!comment) {
      return res.status(404).json({
        status: 'error',
        message: 'Comment not found'
      });
    }
    
    res.json({
      status: 'success',
      data: comment
    });
  } catch (error) {
    next(error);
  }
};

// Add a comment to a post
exports.addComment = async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({ errors: errors.array() });
    }

    const postId = req.params.id;
    const { content } = req.body; // Only content is needed

    // Use the helper
    const { userId, visitorId } = getUserIdOrGuestId(req, res);

    const post = await Post.findByPk(postId);
    if (!post) {
      return res.status(404).json({ message: 'Post not found' });
    }

    const comment = await Comment.create({
      post_id: postId,
      user_id: userId,
      visitor_id: visitorId,
      content: sanitizeContent(content)
    });

    res.status(201).json({ data: comment });
  } catch (error) {
    next(error);
  }
};

// Edit a comment (using the helper function)
exports.updateComment = async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({ errors: errors.array() });
    }

    const commentId = req.params.id;
    const { content } = req.body;

    const comment = await Comment.findByPk(commentId);
    if (!comment) {
      return res.status(404).json({ message: 'Comment not found' });
    }

    if (checkCommentPermission(req, comment)) {
      comment.content = sanitizeContent(content);
      await comment.save();
      return res.json({ data: comment });
    } else {
      return res.status(403).json({ message: 'Forbidden' });
    }
  } catch (error) {
    next(error);
  }
};

// Delete a comment (using the helper function)
exports.deleteComment = async (req, res, next) => {
  try {
    const commentId = req.params.id;

    const comment = await Comment.findByPk(commentId);
    if (!comment) {
      return res.status(404).json({ message: 'Comment not found' });
    }

    if (checkCommentPermission(req, comment)) {
      await comment.destroy();
      return res.json({ message: 'Comment deleted' });
    } else {
      return res.status(403).json({ message: 'Forbidden' });
    }
  } catch (error) {
    next(error);
  }
};