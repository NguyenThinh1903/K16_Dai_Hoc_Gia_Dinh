// utils/guest.js
const { v4: uuidv4 } = require('uuid');

const getGuestId = (req, res) => {
  let guestId = req.cookies.guestId;
  if (!guestId) {
    guestId = uuidv4();
    res.cookie('guestId', guestId, { maxAge: 31536000000, httpOnly: true, sameSite: 'strict' }); // 1 year, httpOnly, sameSite
  }
  return guestId;
};

module.exports = { getGuestId };