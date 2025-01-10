const express = require('express');
const { console } = require('inspector');
const path = require('path');
const bodyParser = require('body-parser');
const multer = require('multer');
const crypto = require('crypto');
const QRCode = require('qrcode'); // Import thư viện qrcode
const app = express();
const port = 3000;

// Cấu hình body-parser (phải đặt trước các route)
app.use(bodyParser.urlencoded({ extended: true }));

// --- Code xử lý QR Code ---
app.post('/qrcode', async (req, res) => {
  console.log("Request body:", req.body); // Kiểm tra dữ liệu nhận được
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
app.get('/qrcode', (req, res) => {
  res.render('qrcode', { qrCode: null, url: null, error: null });
});
// --- Kết thúc code xử lý QR Code ---

// --- Code xử lý Todo (nếu bạn có) ---
// ... (Thêm code xử lý Todo của bạn ở đây) ...
// --- Kết thúc code xử lý Todo ---

app.use(express.static('public'));

app.use(express.static(path.join(__dirname, 'public')));

//Cấu hình view engine là Pug
app.set('view engine', 'pug');
app.set('views', path.join(__dirname, 'views'));

app.get('/', (req, res) => {
    res.render('index', { title: 'Hey', message: 'Xin chao cac bạn Truong Dai hoc Gia Dinh' })
});

app.get('/login', (req, res) => {
    res.render('login');
});

app.get('/register', (req, res) => {
    res.render('register');
});

app.get('/users', (req, res) => {
    console.log(req.query);
    res.send('Get user');
});

app.post('/login', (req, res) => {
    const { username, password } = req.body;
    if (username === 'admin' && password === '123') {
        // res.send('Login success');
        // res.render('index', { title: 'Hey', message: 'Login success' });
        res.redirect('/');
    } else {
        res.send('Login fail');
    }
})

app.get('/upload', (req, res) => {
    res.render('upload');
});

const storage = multer.diskStorage({
    destination: (req, file, cb) => {
      cb(null, 'uploads/'); 
    },
    filename: (req, file, cb) => {
        const randomName = crypto.randomBytes(10).toString('hex'); 
        const extendName = path.extname(file.originalname); 
    //   cb(null, file.originalname); // giữ nguyên tên file gốc
        cb(null, randomName + extendName); 
    },
});
  
  const upload = multer({ storage: storage });

   
app.post('/upload', upload.single('file'), (req, res) => {
        const {username, password} = req.body;
        const file = req.file;

        res.send(`
            <h1>Upload file success</h1>
            <p>Username: ${username}</p>
            <p>Password: ${password}</p>
            <p>Uploaded file: ${file.originalname}</p>
            <p>Saved at: ${file.path}</p>
        `);
});

app.get('/books/:bookid', (req, res) =>{
    console.log(req.params);
    const bookId = req.params.bookid;
    res.send(`Book ID: ${bookId}`);
});

app.listen(port, () => {
    console.log(`Server listening on port ${port}`);
})