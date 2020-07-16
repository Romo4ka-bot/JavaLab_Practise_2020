// используем библиотеку express
const express = require('express');
// создаем объект express
const app = express();
// говорим, что мы раздаем папку public
app.use(express.static('public'));
// говорим, что запускаемся на порту 80
app.listen(80);
console.log("Server started at 80");
