const { check, body } = require('express-validator'); // Import 'body' as well

const loginValidation = [
    check('email', 'Please include a valid email').isEmail(),
    check('password', 'Password is required').not().isEmpty()
  ];

  const registrationValidation = [
    check('email', 'Please include a valid email').isEmail(),
    check('password', 'Password must be at least 6 characters long').isLength({ min: 6 })
  ];
const postValidation = [
  check('title')
    .trim() // Remove leading/trailing whitespace
    .notEmpty().withMessage('Title is required')
    .isLength({ max: 100 }).withMessage('Title cannot exceed 100 characters')
    .escape(), // Sanitize HTML characters

  check('content')
    .trim()
    .notEmpty().withMessage('Content is required')
    .isLength({ max: 10000 }).withMessage('Content cannot exceed 10,000 characters'),

  check('status')
    .optional()
    .isIn(['draft', 'published']).withMessage('Status must be either draft or published'),

    // No need to validate image_url, as multer already does
];

const postUpdateValidation = [ //separate validations for update
    check('title')
    .optional()
    .trim()
    .notEmpty().withMessage('Title is required')
    .isLength({max: 100}).withMessage('Title cannot exceed 100 character')
    .escape(),
    check('content')
    .optional()
    .trim()
    .notEmpty().withMessage('Content is required')
    .isLength({max: 10000}).withMessage('content can not exceed 10,000 character'),
    check('status')
    .optional()
    .isIn(['draft', 'published']).withMessage('status must be either draft or published')
]
const profileValidation = [
  check('name')
      .optional()
      .trim()
      .isLength({ max: 255 }).withMessage('Name cannot exceed 255 characters')
      .escape(),
  check('bio')
      .optional() 
      .trim()
      .isLength({ max: 1000 }).withMessage('Bio cannot exceed 1000 characters')
      .escape()
];

// Comment validation
const commentValidation = [
    check('content')
      .not().isEmpty().withMessage('Comment content is required')
      .isLength({ max: 500 }).withMessage('Comment cannot exceed 500 characters'),
  ];
  const commentUpdateValidation = [
    ...commentValidation, 
    body('guestToken')
        .optional()
        .isString().withMessage('guestToken must be a string')
        .isUUID().withMessage('guestToken must be a valid UUID'), 
];
  // Like validation
const likeValidation = [
    check('like_type')
      .isIn(['like', 'dislike']).withMessage('Like type must be either like or dislike'),
  ];


module.exports = {
  registrationValidation,
  loginValidation,
  postValidation,
  commentValidation,
  commentUpdateValidation,
  likeValidation,
  profileValidation,
  registrationValidation,
  postUpdateValidation
};