const { Like, Post } = require('../models');
const { validationResult } = require('express-validator');
const { getUserIdOrGuestId } = require('../utils/user');
const AppError = require('../utils/AppError');
const logger = require('../utils/logger');

exports.updateLike = async (req, res, next) => {
    try {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return next(new AppError('Validation failed', 400, errors.array()));
        }

        const postId = req.params.id;
        const { like_type } = req.body;

        const post = await Post.findByPk(postId);
        if (!post) {
            return next(new AppError('Post not found', 404));
        }

        const { userId, visitorId } = getUserIdOrGuestId(req, res);

        const whereClause = {
            post_id: postId
        };

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

        const existingLike = await Like.findOne({ where: whereClause });

        if (existingLike) {
            existingLike.like_type = like_type;
            await existingLike.save();
            return res.json({
                message: `Like updated to ${like_type}`,
                data: existingLike
            });
        } else {
            const newLike = await Like.create(likeData);
            return res.status(201).json({
                message: `Like created: ${like_type}`,
                data: newLike
            });
        }
    } catch (error) {
        if (error.name === 'SequelizeUniqueConstraintError') {
            return next(new AppError('You have already liked/disliked this post.', 409));
          }
        logger.error('Error updating like:', error);
        next(error);
    }
};

exports.deleteLike = async (req, res, next) => {
    try {
        const postId = req.params.id;

        const post = await Post.findByPk(postId);
        if (!post) {
            return next(new AppError('Post not found', 404));
        }

        const { userId, visitorId } = getUserIdOrGuestId(req, res);

        const whereClause = {
            post_id: postId
        };

        if (userId) {
            whereClause.user_id = userId;
        } else if (visitorId) {
            whereClause.visitor_id = visitorId;
        } else {
            return next(new AppError('Authentication required', 403));
        }

        const like = await Like.findOne({ where: whereClause });

        if (!like) {
            return next(new AppError('Like not found', 404));
        }

        const isOwner = (userId && like.user_id === userId) || (visitorId && like.visitor_id === visitorId);
        const isAdmin = req.user && req.user.role === 'admin';

        if (isOwner || isAdmin) {
            await like.destroy();
            return res.status(200).json({ message: 'Like removed' });
        } else {
            return next(new AppError('Forbidden', 403));
        }
    } catch (error) {
        logger.error('Error deleting like:', error);
        next(error);
    }
};