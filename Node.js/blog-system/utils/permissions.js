// utils/permissions.js
const checkCommentPermission = (req, comment) => {
    if (req.user) {
      return comment.user_id === req.user.id || ['admin', 'owner'].includes(req.user.role);
    } else {
      // Since we're using cookies, get the guest ID here
      const guestVisitorId = req.cookies.guestId; // Directly use req.cookies
      return comment.visitor_id === guestVisitorId;
    }
  };
  
  module.exports = { checkCommentPermission };