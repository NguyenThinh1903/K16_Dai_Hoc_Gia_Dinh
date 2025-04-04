const express = require('express');
const router = express.Router();
const { updateLike, deleteLike } = require('../controllers/likeController');
const { likeValidation } = require('../utils/validation');
const { apiLimiter } = require('../middlewares/rateLimiter');
const { optionalAuthJWT } = require('../middlewares/auth');
const logger = require('../utils/logger');

logger.info('likeRoutes.js loaded');


/**
 * @swagger
 * /posts/{postId}/likes:
 *   put:
 *     summary: Update a like/dislike on a post
 *     description:  Allows users (both registered and guests) to like or dislike a post.  If a like/dislike already exists, it updates the type.  If it doesn't exist, it creates a new one.
 *     tags: [Likes]
 *     parameters:
 *       - in: path
 *         name: postId
 *         required: true
 *         schema:
 *           type: integer
 *         description: The ID of the post.
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               like_type:
 *                 type: string
 *                 enum: [like, dislike]
 *                 description: The type of like ('like' or 'dislike').
 *             required:
 *               - like_type
 *     responses:
 *       200:
 *         description: Like updated successfully.
 *         content:
 *            application/json:
 *             schema:
 *               type: object
 *               properties:
 *                  status:
 *                      type: string
 *                      example: success
 *                  data:
 *                      $ref: '#/components/schemas/Like'
 *       201:
 *         description: Like created successfully
 *         content:
 *           application/json:
 *             schema:
 *                type: object
 *                properties:
 *                  status:
 *                    type: string
 *                    example: success
 *                  data:
 *                   $ref: '#/components/schemas/Like'
 *       400:
 *         description: Bad request (e.g., validation error).
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Error'
 *       404:
 *         description: Post not found.
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Error'
 *       409:
 *         description: Conflict. You have already liked/dislike this post
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Error'
 *       500:
 *          description: Internal Server Error
 *          content:
 *            application/json:
 *              schema:
 *                $ref: '#/components/schemas/Error'
 */
router.put('/posts/:id/likes', apiLimiter, optionalAuthJWT, likeValidation, updateLike);

/**
 * @swagger
 * /posts/{postId}/likes:
 *   delete:
 *     summary: Remove a like/dislike from a post
 *     description: Allows users (both registered and guests) to remove their like or dislike from a post. Requires authentication if the user is registered.
 *     tags: [Likes]
 *     parameters:
 *       - in: path
 *         name: postId
 *         required: true
 *         schema:
 *           type: integer
 *         description: The ID of the post.
 *     responses:
 *       200:
 *         description: Like removed successfully.
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 message:
 *                   type: string
 *                   example: Like removed
 *       401:
 *         description: Unauthorized (missing or invalid JWT for registered users).
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Error'
 *       403:
 *         description: Forbidden (attempting to remove a like that doesn't belong to the user).
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Error'
 *       404:
 *         description: Like or Post not found.
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Error'
 *       500:
 *         description: Internal server error.
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Error'
 */
router.delete('/posts/:id/likes', apiLimiter, optionalAuthJWT, deleteLike);

module.exports = router;