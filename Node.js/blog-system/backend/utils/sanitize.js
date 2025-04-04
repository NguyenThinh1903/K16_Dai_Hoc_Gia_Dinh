const sanitizeHtml = require('sanitize-html');

// Sanitize HTML content while allowing basic formatting
const sanitizeContent = (content) => {
  return sanitizeHtml(content, {
    allowedTags: [
      'b', 'i', 'em', 'strong', 'a', 'ul', 'ol', 'li', 
      'p', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'br'
    ],
    allowedAttributes: {
      'a': ['href', 'target']
    }
  });
};

// Sanitize user input for search queries
const sanitizeSearchQuery = (query) => {
  return query.replace(/[^a-zA-Z0-9 ]/g, '');
};

module.exports = {
  sanitizeContent,
  sanitizeSearchQuery
};