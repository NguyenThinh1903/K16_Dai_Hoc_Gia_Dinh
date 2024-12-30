//use get
var express = require('express'); 
var app = express(); 

app.use(express.static('public')); 

app.get('/index.htm', function (req, res) { 
    res.sendFile(__dirname + "/" + "index.html"); 
}); 

app.get('/process_get', function (req, res) { 
    // Chuẩn bị output trong định dạng JSON
    let response = { 
        first_name: req.query.first_name, 
        last_name: req.query.last_name
    }; 
    console.log(response); 
    res.end(JSON.stringify(response)); 
}); 

var server = app.listen(8081, function () { 
    var host = server.address().address; 
    var port = server.address().port; 
    console.log("Ứng dụng Node.js đang lắng nghe tại địa chỉ: http://%s:%s", host, port); 
});


//use post
// var express = require('express'); 
// var bodyParser = require('body-parser');
// var app = express(); 


// app.use(express.static('public')); 


// app.use(bodyParser.urlencoded({ extended: true }));
// app.use(bodyParser.json());

// app.get('/index.htm', function (req, res) { 
//     res.sendFile(__dirname + "/" + "index.html"); 
// }); 

// app.post('/process_post', function (req, res) { 
//     // Chuẩn bị output trong định dạng JSON
//     let response = { 
//         first_name: req.body.first_name, 
//         last_name: req.body.last_name
//     }; 
//     console.log(response); 
//     res.end(JSON.stringify(response)); 
// }); 

// var server = app.listen(8081, function () { 
//     var host = server.address().address; 
//     var port = server.address().port; 
//     console.log("Ứng dụng Node.js đang lắng nghe tại địa chỉ: http://%s:%s", host, port); 
// });
