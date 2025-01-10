const express = require('express');
const fs = require('fs');
const router = express.Router();

// Route này xử lý khi người dùng truy cập vào đường dẫn gốc (path '/') của router này.
router.get('/', (req, res)=>{
    // Sử dụng fs.readFile để đọc file 'data.json' với mã hóa ký tự 'utf8'.
    fs.readFile('data.json', 'utf8', (err, data)=>{
        // Nếu có lỗi xảy ra trong quá trình đọc file (ví dụ: file không tồn tại),
        if(err){
            // In lỗi ra console.
            console.log(err);
            // Có thể xử lý lỗi tốt hơn ở đây, ví dụ: gửi thông báo lỗi cho người dùng hoặc render một trang lỗi.
            // Hiện tại, chỉ log lỗi và không làm gì khác.
        } else {
            // Nếu đọc file thành công, 'data' sẽ là một chuỗi JSON.
            // Chuyển đổi chuỗi JSON này thành một đối tượng JavaScript (mảng todos).
            const todos = JSON.parse(data);
            // Render file 'data.pug' và truyền mảng 'todos' vào template với tên biến là 'todos'.
            res.render('data', {todos: todos});
        }
    });
})

module.exports = router;