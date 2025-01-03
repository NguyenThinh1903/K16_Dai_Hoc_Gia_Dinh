const express = require('express');
const { console } = require('inspector');
const path = require('path');
const bodyParser = require('body-parser');
const app = express();
const port = 3000;


app.use(express.static('public'));
app.use(bodyParser.urlencoded({ extended: false }));
//middleware cho static files (CSS,JS, hình ảnh)
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
app.listen(port, () => {
    console.log(`Server listening on port ${port}`);
})