// routes/userRoutes.js
const express = require('express');
const router = express.Router();
const userController = require('../controllers/userController');
const { registrationValidation, profileValidation } = require('../utils/validation');
const { apiLimiter } = require('../middlewares/rateLimiter');
const { authenticateJWT, hasRole } = require('../middlewares/auth');
// Removed: const { upload, logFileUploadRequest } = require('../middlewares/fileUpload');
const logger = require('../utils/logger');

logger.info('userRoutes.js loaded');

// Asynchronously load the file upload middleware:
let upload, deleteOldFile, logFileUploadRequest;
const fileUploadPromise = require('../middlewares/fileUpload');

module.exports = (upload, logFileUploadRequest) => {
    const router = express.Router();

    // --- Route definitions (INSIDE the .then() block) ---
    /**
     * @swagger
     * /register:
     *   post:
     *     summary: Register a new user
     *     description: Register a new user account.
     *     tags: [Users]
     *     requestBody:
     *       required: true
     *       content:
     *         application/json:
     *           schema:
     *             type: object
     *             properties:
     *               email:
     *                 type: string
     *                 format: email
     *               password:
     *                 type: string
     *             required:
     *               - email
     *               - password
     *     responses:
     *       201:
     *         description: User registered successfully.
     *         content:
     *            application/json:
     *             schema:
     *               type: object
     *               properties:
     *                  status:
     *                      type: string
     *                      example: success
     *                  data:
     *                      $ref: '#/components/schemas/User'
     *       400:
     *         description: Bad request (validation error).
     *         content:
     *           application/json:
     *             schema:
     *               $ref: '#/components/schemas/Error'
     *       409:
     *         description: Conflict (email already registered).
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
    router.post('/register', apiLimiter, registrationValidation, userController.registerUser);

    /**
     * @swagger
     * /profile:
     *   get:
     *     summary: Get own user profile
     *     description: Retrieve the profile of the currently logged-in user.
     *     tags: [Users]
     *     security:
     *       - bearerAuth: []
     *     responses:
     *       200:
     *         description: The user's profile.
     *         content:
     *           application/json:
     *             schema:
     *               type: object
     *               properties:
     *                 data:
     *                     $ref: '#/components/schemas/UserProfile'
     *       401:
     *         description: Unauthorized (missing or invalid JWT).
     *         content:
     *           application/json:
     *             schema:
     *               $ref: '#/components/schemas/Error'
     *       404:
     *         description: User not found.
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
    router.get('/profile', apiLimiter, authenticateJWT, userController.getUserProfile);


    /**
     * @swagger
     * /users/{userId}/profile:
     *  get:
     *    summary: View any user's profile
     *    description: Retrieve the profile of any user by their ID.
     *    tags: [Users]
     *    parameters:
     *      - in: path
     *        name: userId
     *        required: true
     *        schema:
     *          type: integer
     *        description: The ID of the user.
     *    responses:
     *      200:
     *        description: The user's profile
     *        content:
     *          application/json:
     *            schema:
     *              type: object
     *              properties:
     *                data:
     *                  $ref: '#/components/schemas/UserProfile'
     *      404:
     *        description: User not found
     *        content:
     *           application/json:
     *             schema:
     *               $ref: '#/components/schemas/Error'
     *      500:
     *        description: Internal server error.
     *        content:
     *           application/json:
     *             schema:
     *               $ref: '#/components/schemas/Error'
     */
    router.get('/users/:userId/profile', apiLimiter, userController.viewAnyProfile);


    /**
     * @swagger
     * /profile:
     *   delete:
     *     summary: Delete own profile
     *     description: Delete the profile of the currently logged-in user.
     *     tags: [Users]
     *     security:
     *       - bearerAuth: []
     *     responses:
     *       200:
     *         description: User and profile deleted successfully
     *         content:
     *            application/json:
     *             schema:
     *               type: object
     *               properties:
     *                  status:
     *                      type: string
     *                      example: success
     *                  message:
     *                      type: string
     *                      example: User deleted
     *       401:
     *          description: Unauthorized
     *          content:
     *           application/json:
     *             schema:
     *               $ref: '#/components/schemas/Error'
     *       404:
     *         description: User not found.
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
    router.delete('/profile', apiLimiter, authenticateJWT, userController.deleteOwnProfile);


    /**
     * @swagger
     * /admin/users/{userId}/profile:
     *   delete:
     *     summary: Delete any user profile (Admin only)
     *     description: Delete the profile of any user by ID (requires admin/owner role).
     *     tags: [Users, Admin]
     *     security:
     *       - bearerAuth: []
     *     parameters:
     *       - in: path
     *         name: userId
     *         required: true
     *         schema:
     *           type: integer
     *         description: The ID of the user to delete.
     *     responses:
     *       200:
     *          description: User deleted
     *          content:
     *            application/json:
     *             schema:
     *               type: object
     *               properties:
     *                  status:
     *                      type: string
     *                      example: success
     *                  message:
     *                      type: string
     *                      example: User deleted
     *       401:
     *          description: Unauthorized
     *          content:
     *           application/json:
     *             schema:
     *               $ref: '#/components/schemas/Error'
     *       403:
     *         description: Forbidden (not an admin/owner).
     *         content:
     *           application/json:
     *             schema:
     *               $ref: '#/components/schemas/Error'
     *       404:
     *         description: User not found.
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
    router.delete('/admin/users/:userId/profile', apiLimiter, authenticateJWT, hasRole(['admin', 'owner']), userController.deleteAnyProfile);

    /**
     * @swagger
     * /profile:
     *   patch:
     *     summary: Update own user profile
     *     description: Update the profile of the currently logged-in user.
     *     tags: [Users]
     *     security:
     *       - bearerAuth: []
     *     requestBody:
     *       required: true
     *       content:
     *         multipart/form-data:
     *           schema:
     *             type: object
     *             properties:
     *               name:
     *                 type: string
     *                 description: The user's name.
     *               bio:
     *                 type: string
     *                 description: The user's bio.
     *               avatar:
     *                 type: string
     *                 format: binary
     *                 description: The user's avatar image.
     *     responses:
     *       200:
     *         description: Profile updated successfully.
     *         content:
     *           application/json:
     *             schema:
     *               type: object
     *               properties:
     *                 message:
     *                    type: string
     *                    example: Profile updated
     *                 data:
     *                   $ref: '#/components/schemas/UserProfile'
     *       400:
     *         description: Bad request (validation error).
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
     *       404:
     *         description: User profile not found.
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
    router.patch('/profile',
        apiLimiter,
        authenticateJWT,
        logFileUploadRequest, // Now defined
        upload.single('avatar'), // Now defined
        (req, res, next) => {
            logger.debug('After multer (update own profile):');
            logger.debug(`Received file: ${req.file ? req.file.filename : 'None'}`);
            logger.debug(`Body after multer: ${JSON.stringify(req.body)}`);
            next();
        },
        profileValidation,
        userController.updateUserProfile
    );

    /**
     * @swagger
     * /admin/users/{userId}/profile:
     *   put:
     *     summary: Update any user profile (Admin only)
     *     description: Update the profile of any user by ID (requires admin/owner role).
     *     tags: [Users, Admin]
     *     security:
     *       - bearerAuth: []
     *     parameters:
     *       - in: path
     *         name: userId
     *         required: true
     *         schema:
     *           type: integer
     *         description: The ID of the user whose profile is to be updated.
     *     requestBody:
     *       required: true
     *       content:
     *         multipart/form-data:
     *           schema:
     *             type: object
     *             properties:
     *               name:
     *                 type: string
     *               bio:
     *                 type: string
     *               avatar:
     *                 type: string
     *                 format: binary
     *     responses:
     *       200:
     *         description: Profile updated successfully.
     *         content:
     *           application/json:
     *              schema:
     *               type: object
     *               properties:
     *                 message:
     *                    type: string
     *                    example: Profile updated
     *                 data:
     *                   $ref: '#/components/schemas/UserProfile'
     *       400:
     *         description: Bad request (validation error).
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
     *         description: Forbidden (not an admin/owner).
     *         content:
     *           application/json:
     *             schema:
     *               $ref: '#/components/schemas/Error'
     *       404:
     *         description: User profile not found.
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
    router.put('/admin/users/:userId/profile',
        apiLimiter,
        authenticateJWT,
        hasRole(['admin', 'owner']),
        logFileUploadRequest, // Now Defined
        upload.single('avatar'), // Now defined
        (req, res, next) => {
            logger.debug('After multer (update any profile):');
            logger.debug(`Received file: ${req.file ? req.file.filename : 'None'}`);
            logger.debug(`Body after multer: ${JSON.stringify(req.body)}`);
            next();
        },
        profileValidation,
        userController.updateAnyProfile
    );
    return router;
}