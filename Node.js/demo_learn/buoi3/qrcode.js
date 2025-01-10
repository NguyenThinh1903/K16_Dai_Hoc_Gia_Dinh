const express = require('express');
const QRCode = require('qrcode'); // Thêm thư viện qrcode
const router = express.Router();

// Handle POST request to /qrcode
router.post('/qrcode', async (req, res) => {
  const urlToEncode = req.body.url;

  if (!urlToEncode) {
    return res.render('qrcode', { error: 'Vui lòng cung cấp URL', qrCode: null, url: null });
  }

  try {
    const qrCodeDataURL = await QRCode.toDataURL(urlToEncode);
    res.render('qrcode', { qrCode: qrCodeDataURL, url: urlToEncode, error: null });
  } catch (err) {
    console.error(err);
    res.render('qrcode', { error: 'Lỗi khi tạo mã QR', qrCode: null, url: urlToEncode });
  }
});

// Optional: Handle GET request to / to display the initial form
router.get('/', (req, res) => {
  res.render('qrcode', { qrCode: null, url: null, error: null });
});

module.exports = router;