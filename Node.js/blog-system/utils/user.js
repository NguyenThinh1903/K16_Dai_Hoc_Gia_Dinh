const { getGuestId } = require('./guest');

exports.getUserIdOrGuestId = (req, res) => {
  let userId = null;
  let visitorId = null;

  if (req.user) {
    userId = req.user.id;
  } else {
    visitorId = getGuestId(req, res);
  }

  return { userId, visitorId };
};