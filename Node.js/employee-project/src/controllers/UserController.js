const User = require('../models/userModel');
const bcrypt = require('bcrypt');

class UserController {
    async getAllUsers(req, res) {
        try {
            const users = await User.getAll();
            res.status(200).json(users);
        } catch (error) {
            console.log(error);
            res.status(500).json(error);
        }

    }

    async createUser(req, res) {
        try {
            const newUser = req.body;
            const user = await User.getUser(newUser.username);
            if (user.length > 0) {
                return res.status(400).json("User already exists");
            }
            newUser.password = await bcrypt.hash(newUser.password, 10);
            await User.create(newUser);
            res.status(201).json("User created successfully");
        } catch (error) {
            console.log(error);
            res.status(500).json(error);
        }
    }

    async login (req, res) {
        const {username, password} = req.body;
        const user = await User.getUser(username);
        if (user.length === 0) {
            return res.status(400).json("User not found");
        }
        const isMatch = await bcrypt.compare(password, user[0].PASSWORD);
        if (!isMatch) {
            return res.status(400).json("Incorrect password");
        }
        return res.status(200).json("Login successfully");
    }

}

module.exports = new UserController;