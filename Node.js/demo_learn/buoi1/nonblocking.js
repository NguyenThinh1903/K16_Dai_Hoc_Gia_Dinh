const fs = require('fs');
fs.readFile('file.txt','utf8', (err, data) =>{
    if (err) throw err;
    console.log(data);
});

console.log('Read file started');