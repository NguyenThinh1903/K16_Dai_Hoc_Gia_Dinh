// routes/postRoutes.js
const express = require('express');
const {
    getPosts,
    getPost,
    createPost,
    updatePost,
    deletePost,
    searchPosts
} = require('../controllers/postController');
const { authenticateJWT, hasRole, optionalAuthJWT } = require('../middlewares/auth');
const { postValidation, postUpdateValidation } = require('../utils/validation');
const { apiLimiter, postManagementLimiter } = require('../middlewares/rateLimiter');
const logger = require('../utils/logger'); // Import Logger

logger.info('postRoutes.js loaded'); // Log route file load

// Export a function that takes the middleware as arguments
module.exports = (upload, logFileUploadRequest, deleteOldFile) => {
  const router = express.Router();


  // --- Route definitions (INSIDE the .then() block) ---

  /**
    * @swagger
    * /posts:
    *   get:
    *     summary: Get all published posts
    *     description: Retrieve a list of all published blog posts with pagination.  Supports optional search.
    *     tags: [Posts]
    *     parameters:
    *       - in: query
    *         name: page
    *         schema:
    *           type: integer
    *         description: The page number
    *       - in: query
    *         name: limit
    *         schema:
    *           type: integer
    *         description: The number of items per page
    *       - in: query
    *         name: search
    *         schema:
    *           type: string
    *         description: Search term for title and content
    *     responses:
    *       200:
    *         description: A list of posts.
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
    *                     posts:
    *                       type: array
    *                       items:
    *                         $ref: '#/components/schemas/Post'
    *                     totalPosts:
    *                       type: integer
    *                       example: 10
    *                     currentPage:
    *                       type: integer
    *                       example: 1
    *                     totalPages:
    *                       type: integer
    *                       example: 2
    *       400:
    *          description: Bad request
    *          content:
    *           application/json:
    *             schema:
    *               $ref: '#/components/schemas/Error'
    *       500:
    *         description: Internal server error
    *         content:
    *           application/json:
    *             schema:
    *               $ref: '#/components/schemas/Error'
    */
  router.get('/', apiLimiter, optionalAuthJWT, getPosts);

  /**
    * @swagger
    * /posts/search:
    *   get:
    *     summary: Search posts
    *     description: Search for posts by title or content.
    *     tags: [Posts]
    *     parameters:
    *       - in: query
    *         name: q
    *         required: true
    *         schema:
    *           type: string
    *         description: The search query.
    *       - in: query
    *         name: page
    *         schema:
    *           type: integer
    *         description: The page number.
    *       - in: query
    *         name: limit
    *         schema:
    *           type: integer
    *         description: The number of items per page.
    *     responses:
    *       200:
    *         description: A list of matching posts.
    *         content:
    *           application/json:
    *             schema:
    *               type: object
    *               properties:
    *                 status:
    *                   type: string
    *                 data:
    *                   type: object
    *                   properties:
    *                     posts:
    *                       type: array
    *                       items:
    *                         $ref: '#/components/schemas/Post'
    *                     totalPosts:
    *                       type: integer
    *                     currentPage:
    *                       type: integer
    *                     totalPages:
    *                       type: integer
    *       400:
    *         description: Bad request (e.g., missing search query).
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
  router.get('/search', apiLimiter, optionalAuthJWT, searchPosts);

  /**
    * @swagger
    * /posts/{id}:
    *   get:
    *     summary: Get a single post by ID
    *     description: Retrieve a single blog post by its ID.  Returns 404 if the post is not found or is a draft and the user is not the owner.
    *     tags: [Posts]
    *     parameters:
    *       - in: path
    *         name: id
    *         required: true
    *         schema:
    *           type: integer
    *         description: The post ID
    *       - in: query
    *         name: visitor_id  # Add visitor_id as a query parameter
    *         schema:
    *           type: string
    *         description: The visitor ID (for guest users)
    *     responses:
    *       200:
    *         description: The requested post.
    *         content:
    *           application/json:
    *             schema:
    *               $ref: '#/components/schemas/Post'
    *       404:
    *         description: Post not found
    *         content:
    *           application/json:
    *             schema:
    *               $ref: '#/components/schemas/Error'
    *       500:
    *         description: Internal server error
    *         content:
    *           application/json:
    *             schema:
    *               $ref: '#/components/schemas/Error'
    */
  router.get('/:id', apiLimiter, optionalAuthJWT, getPost);

  /**
    * @swagger
    * /posts:
    *   post:
    *     summary: Create a new post
    *     description: Create a new blog post (requires owner role).
    *     tags: [Posts]
    *     security:
    *       - bearerAuth: []
    *     requestBody:
    *       required: true
    *       content:
    *         multipart/form-data:  # Use multipart/form-data for file uploads
    *           schema:
    *             type: object
    *             properties:
    *               title:
    *                 type: string
    *                 description: The title of the post
    *               content:
    *                 type: string
    *                 description: The content of the post
    *               status:
    *                 type: string
    *                 enum: [draft, published]
    *                 description: The status of the post (default is published)
    *               image: # Add the image field
    *                 type: string
    *                 format: binary
    *                 description: The image file to upload
    *             required:
    *               - title
    *               - content
    *     responses:
    *       201:
    *         description: The created post.
    *         content:
    *           application/json:
    *             schema:
    *               $ref: '#/components/schemas/Post'
    *       400:
    *         description: Bad request (e.g., validation error)
    *         content:
    *           application/json:
    *             schema:
    *               $ref: '#/components/schemas/Error'
    *       401:
    *         description: Unauthorized (missing or invalid JWT)
    *         content:
    *           application/json:
    *             schema:
    *               $ref: '#/components/schemas/Error'
    *       403:
    *         description: Forbidden (not an owner)
    *         content:
    *           application/json:
    *             schema:
    *               $ref: '#/components/schemas/Error'
    *       500:
    *         description: Internal server error
    *         content:
    *           application/json:
    *             schema:
    *               $ref: '#/components/schemas/Error'
    */
  router.post('/',
    authenticateJWT,
    hasRole(['owner']),
    postManagementLimiter,
    logFileUploadRequest, // Now defined
    upload.single('image'), // Now defined
    (req, res, next) => {
    logger.debug('After multer (createPost):');
    logger.debug(`Received file: ${req.file ? req.file.filename : 'None'}`);
    logger.debug(`Body after multer: ${JSON.stringify(req.body)}`);
    next();
    },
    postValidation,
    createPost
);

  /**
    * @swagger
    * /posts/{id}:
    *   put:
    *     summary: Update a post
    *     description: Update an existing blog post (requires owner role).
    *     tags: [Posts]
    *     security:
    *       - bearerAuth: []
    *     parameters:
    *       - in: path
    *         name: id
    *         required: true
    *         schema:
    *           type: integer
    *         description: The ID of the post to update.
    *     requestBody:
    *       required: true
    *       content:
    *         multipart/form-data:
    *           schema:
    *             type: object
    *             properties:
    *               title:
    *                 type: string
    *                 description: The updated title of the post.
    *               content:
    *                 type: string
    *                 description: The updated content of the post.
    *               status:
    *                 type: string
    *                 enum: [draft, published]
    *                 description: The updated status of the post.
    *               image:
    *                 type: string
    *                 format: binary
    *                 description: The updated image file for the post.
    *     responses:
    *       200:
    *         description: The updated post.
    *         content:
    *           application/json:
    *             schema:
    *               $ref: '#/components/schemas/Post'
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
    *         description: Forbidden (not an owner).
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
  router.put('/:id',
    authenticateJWT,
    hasRole(['owner']),
    postManagementLimiter,
    logFileUploadRequest, // Add request logging and now defined
    upload.single('image'), // now defined
    (req, res, next) => {
    logger.debug('After multer (updatePost):');
    logger.debug(`Received file: ${req.file ? req.file.filename : 'None'}`);
    logger.debug(`Body after multer: ${JSON.stringify(req.body)}`);
    next();
    },
    postUpdateValidation,
    (req, res, next) => { // <--- Modify to pass deleteOldFile
      req.deleteOldFile = deleteOldFile; // <--- Attach deleteOldFile to req object
      return updatePost(req, res, next); // Call original controller function
  }
);

  /**
    * @swagger
    * /posts/{id}:
    *   delete:
    *     summary: Delete a post
    *     description: Delete a blog post (requires owner role).
    *     tags: [Posts]
    *     security:
    *       - bearerAuth: []
    *     parameters:
    *       - in: path
    *         name: id
    *         required: true
    *         schema:
    *           type: integer
    *         description: The ID of the post to delete.
    *     responses:
    *       200:
    *         description: Post deleted successfully.
    *         content:
    *           application/json:
    *             schema:
    *               type: object
    *               properties:
    *                 status:
    *                   type: string
    *                   example: success
    *                 message:
    *                   type: string
    *                   example: Post deleted successfully
    *       401:
    *         description: Unauthorized (missing or invalid JWT).
    *         content:
    *           application/json:
    *             schema:
    *               $ref: '#/components/schemas/Error'
    *       403:
    *         description: Forbidden (not an owner).
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
  router.delete('/:id', authenticateJWT, hasRole(['owner']), postManagementLimiter, (req, res, next) => { // <--- Modify to pass deleteOldFile
    req.deleteOldFile = deleteOldFile; // <--- Attach deleteOldFile to req object
    return deletePost(req, res, next); // Call original controller function
});
  return router;
};