const express = require('express');
const router = express.Router();
const userController = require('../controllers/UserController');

// [GET] localhost:3000/users/
router.get('/', userController.getAllUsers);

// [POST] localhost:3000/users/
router.post('/', userController.createUser);

// [POST] localhost:3000/users/login
router.post('/login', userController.login);

module.exports = router;