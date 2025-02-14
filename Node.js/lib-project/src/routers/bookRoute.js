const router = require('express').Router();
const bookController = require('../controllers/bookController');

router.get('/', bookController.getAllBooks);
router.post('/', bookController.createBook);
router.delete('/:id', bookController.deleteBook);

module.exports = router;