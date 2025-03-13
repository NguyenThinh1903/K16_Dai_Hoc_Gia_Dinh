const { check } = require('express-validator');

// Auth validation
const loginValidation = [
  check('email', 'Please include a valid email').isEmail(),
  check('password', 'Password is required').not().isEmpty()
];

const registrationValidation = [
  check('email', 'Please include a valid email').isEmail(),
  check('password', 'Password must be at least 6 characters long').isLength({ min: 6 })
];
// Post validation
const postValidation = [
  check('title')
    .not().isEmpty().withMessage('Title is required')
    .isLength({ max: 100 }).withMessage('Title cannot exceed 100 characters'),
  check('content')
    .not().isEmpty().withMessage('Content is required')
    .isLength({ max: 10000 }).withMessage('Content cannot exceed 10,000 characters'),
  check('status')
    .optional()
    .isIn(['draft', 'published']).withMessage('Status must be either draft or published')
];

const postUpdateValidation = [
  check('title')
    .optional()
    .not().isEmpty().withMessage('Title is required when provided')
    .isLength({ max: 100 }).withMessage('Title cannot exceed 100 characters'),
  check('content')
    .optional()
    .not().isEmpty().withMessage('Content is required when provided')
    .isLength({ max: 10000 }).withMessage('Content cannot exceed 10,000 characters'),
  check('status')
    .optional()
    .isIn(['draft', 'published']).withMessage('Status must be either draft or published')
];

// Comment validation
const commentValidation = [
  check('content')
    .not().isEmpty().withMessage('Comment content is required')
    .isLength({ max: 500 }).withMessage('Comment cannot exceed 500 characters'),
];

// Like validation
const likeValidation = [
  check('like_type')
    .isIn(['like', 'dislike']).withMessage('Like type must be either like or dislike'),
];

const profileValidation = [
  check('name')
      .optional()
      .isLength({min: 1, max: 255}).withMessage('Name must be between 1 and 255 character'),
  check('avatar')
      .optional()
      .isURL().withMessage('Avatar must be a URL'),
  check('bio')
      .optional()
      .isLength({max: 1000}).withMessage('Bio can not exceed 1000 character')
]

module.exports = {
  registrationValidation,
  loginValidation,
  postValidation,
  commentValidation,
  likeValidation,
  profileValidation,
  registrationValidation,
  postUpdateValidation
};