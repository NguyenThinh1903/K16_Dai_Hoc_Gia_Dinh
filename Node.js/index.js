const express = require('express'); // Nhập express, npm install express
const bodyParser = require('body-parser'); // Nếu sử dụng body-parser, npm install body-parser
const path = require('path');

const app = express();

app.use(bodyParser.json());
app.use(function (req, res, next) {
    console.log('Time: ', Date.now());
    next();
});

app.get('/', function (req, res) {
    res.send('WELCOME TO MY APPLICATION WEB');
});

app.get('/login', function (req, res) {
    res.sendFile(path.join(__dirname, 'login2.html'));
});

app.get('/checkId', function (req, res) {
    const id = req.query.id;
    if (id == 3) {
        res.json({ id: id, status: 'ID received' });
    } else {
        res.status(400).json({ error: "ID not provided" });
    }
});

// use Postman resquest name and email
app.post('/', function (req, res) {
    const { name, email } = req.body;
    console.log(req.body);
    //validate incoming data
    if (!name || !email) {
        return res.status(400).json({ message: 'Name and email are required!' });
    }

    //send a response
    res.status(200).json({
        message: 'Data received successfully!',
        data: { name, email }
    });
});

app.listen(3000, function () {
    console.log('Server is running at http://localhost:3000');
    // console.log('Example app listening on port 3000!')
});
