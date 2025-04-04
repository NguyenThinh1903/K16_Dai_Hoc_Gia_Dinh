// C:\thangs\NODE_JS\blog-system_backup\blog-system\backend\controllers\postController.js
const { Post, Comment, Like } = require('../models');
const { validationResult } = require('express-validator');
const { sanitizeContent, sanitizeSearchQuery } = require('../utils/sanitize');
const { transformPost, transformPosts } = require('../utils/transformPosts');
const { Op } = require('sequelize');
const { deleteOldFile } = require('../middlewares/fileUpload');
const AppError = require('../utils/AppError');

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
    const postId = req.params.id;
    const visitorId = req.query.visitor_id;
    const userId = req.user ? req.user.id : null;

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


    if (!post) {
      return next(new AppError('Post not found', 404));
    }


    if (post.status === 'draft' && (!req.user || req.user.role !== 'owner')) {
      return next(new AppError('Post not found', 404));
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
    console.log('createPost controller called');
    console.log('File received:', req.file);
    console.log('Request body:', req.body);

    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      console.log('Validation errors:', errors.array());
      return next(new AppError('Validation failed', 400, errors.array()));
    }

    const { title, content, status = 'published' } = req.body;

    // Get image URL from the uploaded file
    let image_url = null;
    if (req.file) {
      console.log('Processing uploaded file:', req.file.filename);
      // Create URL path to the uploaded file - this should match your static file serving path
      image_url = `/uploads/${req.file.filename}`;
      console.log('Set image_url to:', image_url);
    }

    const sanitizedContent = sanitizeContent(content);

    console.log('Creating post with data:', {
      title,
      content: sanitizedContent ? sanitizedContent.substring(0, 20) + '...' : null,
      status,
      image_url
    });

    const post = await Post.create({
      title,
      content: sanitizedContent,
      status,
      image_url
    });

    console.log('Post created successfully:', post.id);
    res.status(201).json({
      status: 'success',
      data: post
    });
  } catch (error) {
    console.error('Error in createPost:', error);
    next(error);
  }
};

// Update a post (owner only)
// Helper function to update post fields
const updatePostFields = async (post, updateData, req) => {
  const oldImageUrl = post.image_url; // Store before any changes

  if (updateData.title !== undefined) post.title = updateData.title;
  if (updateData.content !== undefined) post.content = sanitizeContent(updateData.content);
  if (updateData.status !== undefined) post.status = updateData.status;

  if (req.file) {
    post.image_url = `/uploads/${req.file.filename}`;
  }

  await post.save();

  // Delete old image *after* successful save:
  if (req.file && oldImageUrl) {
    await req.deleteOldFile(oldImageUrl);
  }
  return post; // Return the updated post
};

exports.updatePost = async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return next(new AppError('Validation failed', 400, errors.array()));
    }

    const postId = req.params.id;
    const post = await Post.findByPk(postId);
    if (!post) {
      return next(new AppError('Post not found', 404));
    }

    // Use the helper function:
    const updatedPost = await updatePostFields(post, req.body, req);

    res.json({ status: 'success', data: updatedPost });

  } catch (error) {
    if (req.file) {
      await req.deleteOldFile(`/uploads/${req.file.filename}`);
    }
    next(error);
  }
};

// Delete a post (owner only)
exports.deletePost = async (req, res, next) => {
  try {
    const postId = req.params.id;

    const post = await Post.findByPk(postId);

    if (!post) {
      return next(new AppError('Post Not Found', 404));
    }
    const oldImageUrl = post.image_url;
    await post.destroy();
    if(oldImageUrl){
        await req.deleteOldFile(oldImageUrl); //delete image after deleting the post
    }
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
      return next(new AppError('Search query is required', 400));
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