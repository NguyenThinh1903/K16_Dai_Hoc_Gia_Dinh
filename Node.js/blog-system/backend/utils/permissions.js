const checkCommentPermission = (req, comment) => {
  if (req.user) {
    return comment.user_id === req.user.id || ['admin', 'owner'].includes(req.user.role);
  } else {
    // Check for guest_token in the request body
    const providedGuestToken = req.body.guestToken;  // Get from body
    return comment.guest_token && comment.guest_token === providedGuestToken;
  }
};

module.exports = { checkCommentPermission };