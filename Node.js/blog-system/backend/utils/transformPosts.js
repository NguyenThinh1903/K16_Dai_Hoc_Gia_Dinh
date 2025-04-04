/**
 * Transform post data to include comment count and like statistics
 * @param {Object} post - Post model instance
 * @param {Object} options - Additional options for transformation
 * @returns {Object} Transformed post object
 */
exports.transformPost = (post, options) => {
  const { visitorId, userId, includeUserLikeStatus = false } = options || {}; // Provide default values
  const postData = post.get({ plain: true });

  // Calculate comment count if Comments are included
  if (postData.Comments) {
    postData.comment_count = postData.Comments.length;
    delete postData.Comments;
  }

  // Calculate likes and dislikes if Likes are included
  if (postData.Likes) {
    let likes = 0;
    let dislikes = 0;
    let userLikeStatus = null;

    for (const like of postData.Likes) {
      if (like.like_type === 'like') {
        likes++;
      } else if (like.like_type === 'dislike') {
        dislikes++;
      }

      // Check if the current like belongs to the visitor or logged-in user
      if (visitorId && like.visitor_id === visitorId) {
        userLikeStatus = like.like_type;
      } else if (userId && like.user_id === userId) {
        userLikeStatus = like.like_type;
      }
    }

    postData.likes = likes;
    postData.dislikes = dislikes;
    postData.image_url = post.image_url;

    // Add userLikeStatus only if requested
    if (includeUserLikeStatus) {
      postData.userLikeStatus = userLikeStatus;
    }

    delete postData.Likes;
  }

  return postData;
};

/**
 * Transform an array of posts
 * @param {Array} posts - Array of Post model instances
 * @param {Object} options - Additional options for transformation
 * @returns {Array} Array of transformed post objects
 */
exports.transformPosts = (posts, options) => {
  const { includeUserLikeStatus = false } = options || {}; // Provide default values
  return posts.map(post => exports.transformPost(post, { ...options, includeUserLikeStatus }));
};