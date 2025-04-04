
const express = require('express');
const router = express.Router();
const userController = require('../controllers/userController');
const { registrationValidation } = require('../utils/validation');
const { authenticateJWT, hasRole } = require('../middlewares/auth');
const logger = require('../utils/logger'); 

logger.info('adminRoutes.js loaded'); 

/**
 * @swagger
 * /admin/register:
 *   post:
 *     summary: Register a new admin user (Owner only)
 *     description: Register a new admin user (requires owner role).
 *     tags: [Admin]
 *     security:
 *       - bearerAuth: []
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
 *         description: Admin user registered successfully.
 *         content:
 *           application/json:
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
 *       409:
 *         description: Conflict (email already registered).
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
router.post('/register', authenticateJWT, hasRole(['owner']), registrationValidation, userController.registerUser);

/**
 * @swagger
 * /admin/users:
 *   get:
 *     summary: Get all users (Admin/Owner only)
 *     description: Retrieve a list of all users (requires admin or owner role).
 *     tags: [Admin]
 *     security:
 *      - bearerAuth: []
 *     responses:
 *       200:
 *         description: A list of users.
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
 *                     users:
 *                       type: array
 *                       items:
 *                         $ref: '#/components/schemas/User'
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
 *       500:
 *          description: Internal Server Error
 *          content:
 *            application/json:
 *              schema:
 *                $ref: '#/components/schemas/Error'
 */
router.get('/users', authenticateJWT, hasRole(['admin', 'owner']), userController.getAllUsers);

module.exports = router;