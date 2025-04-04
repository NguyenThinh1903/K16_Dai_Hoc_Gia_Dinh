// routes/commentRoutes.js
const express = require('express');
const router = express.Router();
const {
  getComments,
  getComment,
  addComment,
  updateComment,
  deleteComment
} = require('../controllers/commentController');
const { commentValidation,commentUpdateValidation } = require('../utils/validation');
const { apiLimiter } = require('../middlewares/rateLimiter');
const { optionalAuthJWT } = require('../middlewares/auth');
const logger = require('../utils/logger'); // Import Logger

logger.info('commentRoutes.js loaded'); // Log route file load

/**
 * @swagger
 * /posts/{postId}/comments:
 *   get:
 *     summary: Get comments for a post
 *     description: Retrieve a paginated list of comments for a specific post.
 *     tags: [Comments]
 *     parameters:
 *       - in: path
 *         name: postId
 *         required: true
 *         schema:
 *           type: integer
 *         description: The ID of the post.
 *       - in: query
 *         name: page
 *         schema:
 *           type: integer
 *         description: The page number.
 *       - in: query
 *         name: limit
 *         schema:
 *           type: integer
 *         description: The number of comments per page.
 *     responses:
 *       200:
 *         description: A list of comments.
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 status:
 *                   type: string
 *                   example: success
 *                 data:
 *                   type: object
 *                   properties:
 *                     comments:
 *                       type: array
 *                       items:
 *                         $ref: '#/components/schemas/Comment'
 *                     totalComments:
 *                       type: integer
 *                     currentPage:
 *                       type: integer
 *                     totalPages:
 *                       type: integer
 *       404:
 *         description: Post not found.
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
router.get('/posts/:id/comments', apiLimiter, getComments);

/**
 * @swagger
 * /comments/{id}:
 *   get:
 *     summary: Get a single comment
 *     description: Retrieve a single comment by its ID.
 *     tags: [Comments]
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: The ID of the comment.
 *     responses:
 *       200:
 *         description: The requested comment.
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Comment'
 *       404:
 *         description: Comment not found.
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
router.get('/comments/:id', apiLimiter, getComment);

/**
 * @swagger
 * /posts/{postId}/comments:
 *   post:
 *     summary: Add a comment to a post
 *     description: Add a new comment to a specific post.  Works for both registered users and guests.
 *     tags: [Comments]
 *     parameters:
 *       - in: path
 *         name: postId
 *         required: true
 *         schema:
 *           type: integer
 *         description: The ID of the post to add the comment to.
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               content:
 *                 type: string
 *                 description: The content of the comment.
 *             required:
 *               - content
 *     responses:
 *       201:
 *         description: Comment added successfully.
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                  status:
 *                      type: string
 *                      example: success
 *                  data:
 *                      $ref: '#/components/schemas/Comment'
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
 *       500:
 *         description: Internal server error.
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Error'
 */
router.post('/posts/:id/comments', apiLimiter, optionalAuthJWT, commentValidation, addComment);

/**
 * @swagger
 * /comments/{id}:
 *   put:
 *     summary: Update a comment
 *     description: Update an existing comment. Requires authentication and ownership/admin privileges.
 *     tags: [Comments]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: The ID of the comment to update.
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               content:
 *                 type: string
 *                 description: The updated content of the comment.
 *               guestToken: 
 *                 type: string
 *                 description: The guest token.  Required for guest comments, ignored for logged-in users.
 *     responses:
 *       200:
 *         description: Comment updated successfully.
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                  status:
 *                      type: string
 *                      example: success
 *                  data:
 *                      $ref: '#/components/schemas/Comment'
 *       400:
 *         description: Bad request (e.g., validation error).
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Error'
 *       401:
 *         description: Unauthorized (missing or invalid JWT).
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Error'
 *       403:
 *         description: Forbidden (not the owner of the comment or an admin/owner).
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Error'
 *       404:
 *         description: Comment not found.
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
router.put('/comments/:id', optionalAuthJWT, apiLimiter, commentUpdateValidation, updateComment);

/**
 * @swagger
 * /comments/{id}:
 *   delete:
 *     summary: Delete a comment
 *     description: Delete a comment. Requires authentication and ownership/admin privileges.
 *     tags: [Comments]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: The ID of the comment to delete.
 *     requestBody:  # <-- Add this!
 *       required: true # Make the requestBody required
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               guestToken:
 *                 type: string
 *                 description: The guest token. Required for guest comments, ignored for logged-in users.
 *     responses:
 *       200:
 *         description: Comment deleted successfully.
 *         content:
 *           application/json:
 *             schema:
 *                type: object
 *                properties:
 *                  status:
 *                    type: string
 *                    example: success
 *                  message:
 *                    type: string
 *                    example: Comment deleted
 *       401:
 *         description: Unauthorized (missing or invalid JWT).
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Error'
 *       403:
 *         description: Forbidden (not the owner of the comment or an admin/owner).
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Error'
 *       404:
 *         description: Comment not found.
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
router.delete('/comments/:id', optionalAuthJWT, apiLimiter, deleteComment);

module.exports = router;