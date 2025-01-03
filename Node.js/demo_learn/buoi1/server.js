const http = require('http');

const server = http.createServer((req, res) =>{
    res.writeHead(200, {'Content-Type': 'text/plain; charset=utf-8'});
    res.end('Hello world - GIA DINH University');
});

server.listen(3000, ()=>{
    console.log('Server is running at http://localhost:3000/');
})