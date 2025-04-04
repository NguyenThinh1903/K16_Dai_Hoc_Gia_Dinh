// swagger.js
const swaggerJSDoc = require('swagger-jsdoc');

const options = {
  definition: {
    openapi: '3.0.0',
    info: {
      title: 'Blog System API',
      version: '1.0.0',
      description: 'API documentation for the blog system',
    },
    servers: [
      {
        url: 'http://localhost:3000/api',
        description: 'Development server',
      },
      // Add other servers (staging, production) as needed
    ],
    components: {
      securitySchemes: {
        bearerAuth: {
          type: 'http',
          scheme: 'bearer',
          bearerFormat: 'JWT',
        },
      },
      schemas: {
        User: {
          type: 'object',
          properties: {
            id: { type: 'integer', description: 'The user ID' },
            email: { type: 'string', format: 'email', description: 'The user\'s email address' },
            role: { type: 'string', enum: ['user', 'admin', 'owner'], description: 'The user\'s role' },
            created_at: { type: 'string', format: 'date-time' },
            updated_at: { type: 'string', format: 'date-time' },
          },
        },
        UserProfile: {
          type: 'object',
          properties: {
            id: { type: 'integer' },
            user_id: { type: 'integer' },
            name: { type: 'string' },
            avatar: { type: 'string', format: 'url' },
            bio: { type: 'string' },
            created_at: { type: 'string', format: 'date-time' },
            updated_at: { type: 'string', format: 'date-time' },
          },
        },
        Post: {
          type: 'object',
          properties: {
            id: { type: 'integer', description: 'The post ID' },
            title: { type: 'string', description: 'The post title' },
            content: { type: 'string', description: 'The post content' },
            status: { type: 'string', enum: ['draft', 'published'], description: 'The post status' },
            image_url: { type: 'string', format: 'url', description: 'URL of the post image' },
            created_at: { type: 'string', format: 'date-time', description: 'Creation timestamp' },
            updated_at: { type: 'string', format: 'date-time', description: 'Last update timestamp' },
            comment_count: { type: 'integer', description: 'Number of comments', default: 0 },
            likes: { type: 'integer', description: 'Number of likes', default: 0 },
            dislikes: { type: 'integer', description: 'Number of dislikes', default: 0 },
            userLikeStatus: { type: 'string', enum: ['like', 'dislike', null], description: 'User\'s like status (if applicable)' },
          },
        },
        Comment: {
          type: 'object',
          properties: {
            id: { type: 'integer', description: 'The comment ID' },
            post_id: { type: 'integer', description: 'The ID of the post the comment belongs to' },
            user_id: { type: 'integer', nullable: true, description: 'The ID of the user who wrote the comment (null for guests)' },
            visitor_id: { type: 'string', nullable: true, description: 'The visitor ID for guest comments (null for registered users)' },
            content: { type: 'string', description: 'The comment content' },
            created_at: { type: 'string', format: 'date-time', description: 'Creation timestamp' },
            updated_at: { type: 'string', format: 'date-time', description: 'Last update timestamp' },
          },
        },
        Like: {
          type: 'object',
          properties: {
            id: { type: 'integer' },
            post_id: { type: 'integer' },
            user_id: { type: 'integer', nullable: true },
            visitor_id: { type: 'string', nullable: true },
            like_type: { type: 'string', enum: ['like', 'dislike'] },
            created_at: { type: 'string', format: 'date-time' },
            updated_at: { type: 'string', format: 'date-time' },
          }
        },
        Error: {
          type: 'object',
          properties: {
            status: { type: 'string', example: 'error' },
            message: { type: 'string' },
          },
        },
      },
    },
    security: [{
      bearerAuth: [] // Apply bearerAuth globally (can be overridden per route)
    }]
  },
  apis: ['./routes/*.js'], // Path to the API routes
};

const swaggerSpec = swaggerJSDoc(options);

module.exports = swaggerSpec;