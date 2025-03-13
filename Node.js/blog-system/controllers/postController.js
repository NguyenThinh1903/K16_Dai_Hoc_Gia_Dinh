const { Post, Comment, Like } = require('../models');
const { validationResult } = require('express-validator');
const { sanitizeContent, sanitizeSearchQuery } = require('../utils/sanitize');
const { transformPost, transformPosts } = require('../utils/transformPosts');
const { Op } = require('sequelize');

// Get all posts with pagination
exports.getPosts = async (req, res, next) => {
  try {
    const page = parseInt(req.query.page) || 1;
    const limit = parseInt(req.query.limit) || 10;
    const offset = (page - 1) * limit;
    
    const search = req.query.search ? sanitizeSearchQuery(req.query.search) : null;
    
    let whereClause = {
      status: 'published'
    };
    
    // Add search condition if search query is provided
    if (search) {
      whereClause = {
        ...whereClause,
        [Op.or]: [
          { title: { [Op.like]: `%${search}%` } },
          { content: { [Op.like]: `%${search}%` } }
        ]
      };
    }
    
    // If the user is the owner, also show draft posts
    if (req.user && req.user.role === 'owner') {
      delete whereClause.status;
    }
    
    const { count, rows: posts } = await Post.findAndCountAll({
      where: whereClause,
      limit,
      offset,
      order: [['created_at', 'DESC']],
      include: [
        {
          model: Comment,
          attributes: ['id'],
          required: false
        },
        {
          model: Like,
          attributes: ['like_type'],
          required: false
        }
      ]
    });
    
    // Use the helper to transform posts
    const transformedPosts = transformPosts(posts);
    
    res.json({
      status: 'success',
      data: {
        posts: transformedPosts,
        totalPosts: count,
        currentPage: page,
        totalPages: Math.ceil(count / limit)
      }
    });
  } catch (error) {
    next(error);
  }
};

// Get a single post by ID
exports.getPost = async (req, res, next) => {
  try {
    console.log('Full req.user object:', req.user); // Add this line
    const postId = req.params.id;
    const visitorId = req.query.visitor_id;
    const userId = req.user ? req.user.id : null;
    
    console.log('Debug getPost:', {
      postId,
      visitorId,
      userId,
      userRole: req.user?.role,
      isAuthenticated: !!req.user
    });
    
    const post = await Post.findByPk(postId, {
      include: [
        {
          model: Comment,
          attributes: ['id', 'visitor_id', 'content', 'created_at', 'updated_at'],
          limit: 10,
          order: [['created_at', 'DESC']]
        },
        {
          model: Like,
          attributes: ['visitor_id', 'like_type', 'user_id']
        }
      ]
    });
    
    console.log('Found post:', post ? {
      id: post.id,
      status: post.status,
      title: post.title
    } : 'null');

    if (!post) {
      return res.status(404).json({
        status: 'error',
        message: 'Post not found'
      });
    }
    
    console.log('Access check:', {
      postStatus: post.status,
      userRole: req.user?.role,
      hasAccess: post.status !== 'draft' || (req.user && req.user.role === 'owner')
    });

    if (post.status === 'draft' && (!req.user || req.user.role !== 'owner')) {
      return res.status(404).json({
        status: 'error',
        message: 'Post not found'
      });
    }
    
    const postData = transformPost(post, {
      visitorId,
      userId,
      includeUserLikeStatus: true
    });
    
    res.json({
      status: 'success',
      data: postData
    });
  } catch (error) {
    console.error('Error in getPost:', error);
    next(error);
  }
};


// Create a new post (owner only)
exports.createPost = async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        status: 'error',
        errors: errors.array()
      });
    }
    
    const { title, content, status = 'published' } = req.body;
    
    // Sanitize the content
    const sanitizedContent = sanitizeContent(content);
    
    const post = await Post.create({
      title,
      content: sanitizedContent,
      status
    });
    
    res.status(201).json({
      status: 'success',
      data: post
    });
  } catch (error) {
    next(error);
  }
};

// Update a post (owner only)
exports.updatePost = async (req, res, next) => {
  try {
    // Check validation errors
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        status: 'error',
        errors: errors.array()
      });
    }
    
    const postId = req.params.id;
    const { title, content, status } = req.body;
    
    // Ensure at least one field is provided for the update
    if (!title && !content && !status) {
      return res.status(400).json({
        status: 'error',
        message: 'At least one field (title, content, or status) must be provided for the update'
      });
    }
    
    const post = await Post.findByPk(postId);
    
    if (!post) {
      return res.status(404).json({
        status: 'error',
        message: 'Post not found'
      });
    }
    
    // Create an update object with only the provided fields
    const updateData = {};
    
    if (title !== undefined) updateData.title = title;
    if (content !== undefined) updateData.content = sanitizeContent(content);
    if (status !== undefined) updateData.status = status;
    
    // Update the post with the fields that were provided
    await post.update(updateData);
    
    // Fetch the updated post to return the complete updated object
    const updatedPost = await Post.findByPk(postId);
    
    res.json({
      status: 'success',
      data: updatedPost
    });
  } catch (error) {
    next(error);
  }
};

// Delete a post (owner only)
exports.deletePost = async (req, res, next) => {
  try {
    const postId = req.params.id;
    
    const post = await Post.findByPk(postId);
    
    if (!post) {
      return res.status(404).json({
        status: 'error',
        message: 'Post not found'
      });
    }
    
    await post.destroy();
    
    res.json({
      status: 'success',
      message: 'Post deleted successfully'
    });
  } catch (error) {
    next(error);
  }
};

// Search posts
exports.searchPosts = async (req, res, next) => {
  try {
    const searchQuery = sanitizeSearchQuery(req.query.q);
    const page = parseInt(req.query.page) || 1;
    const limit = parseInt(req.query.limit) || 10;
    const offset = (page - 1) * limit;
    
    if (!searchQuery) {
      return res.status(400).json({
        status: 'error',
        message: 'Search query is required'
      });
    }
    
    let whereClause = {
      status: 'published',
      [Op.or]: [
        { title: { [Op.like]: `%${searchQuery}%` } },
        { content: { [Op.like]: `%${searchQuery}%` } }
      ]
    };
    
    // If the user is the owner, also search in draft posts
    if (req.user && req.user.role === 'owner') {
      delete whereClause.status;
    }
    
    const { count, rows: posts } = await Post.findAndCountAll({
      where: whereClause,
      limit,
      offset,
      order: [['created_at', 'DESC']]
    });
    
    // Use the helper to transform posts
    const transformedPosts = transformPosts(posts);
    
    res.json({
      status: 'success',
      data: {
        posts: transformedPosts,
        totalPosts: count,
        currentPage: page,
        totalPages: Math.ceil(count / limit)
      }
    });
  } catch (error) {
    next(error);
  }
};