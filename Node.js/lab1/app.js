const express = require('express');
const path = require('path'); // Đảm bảo đã import path
const app = express();
const port = 3000;

// Định nghĩa route để phục vụ file index.html
app.get('/index.html', function(req, res) {
    res.sendFile(path.join(__dirname, 'index.html')); // Sử dụng path.join để lấy đường dẫn chính xác
});

// Route chính
app.get('/', function(req, res) {
    res.send("Hello World");
});

// Xử lý GET request từ form
app.get('/process_get', function(req, res) {
    const response = { // Đảm bảo biến response được định nghĩa
        first_name: req.query.first_name,
        last_name: req.query.last_name
    };
    console.log(response);
    res.end(JSON.stringify(response));
});

// Khởi động server
app.listen(port, function() {
    console.log("Your app running on port " + port);
});