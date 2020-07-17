// используем библиотеку express
const express = require('express');
const bodyParser = require('body-parser');
// создаем объект express
const app = express();
app.use(bodyParser.urlencoded({ extended: true }));
require('./app/routes')(app);
// говорим, что мы раздаем папку public
app.use(express.static('public/siteSummary'));
// говорим, что запускаемся на порту 80
app.listen(80);
console.log("Server started at 80");

