// server.js

const http = require('http');

// Tạo server
const server = http.createServer((req, res) => {
  res.writeHead(200, { 'Content-Type': 'text/plain' });
  res.end('Hello World - GIA DINH University');
});

// Lắng nghe tại cổng 3000
server.listen(3000, () => {
  console.log('Server is running on http://localhost:3000');
  // console.log('Example app listening on port 3000!')
});