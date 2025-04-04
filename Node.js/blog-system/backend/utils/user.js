exports.getUserIdOrGuestId = (req, res) => {
  let userId = null;
  let visitorId = null;

  if (req.user) {
    userId = req.user.id;
  } else {
    // visitorId = getGuestId(req, res); // No longer need
    visitorId = req.cookies.guestId ? req.cookies.guestId : null; // Send visitorId only for display purposes.
  }

  return { userId, visitorId };
};