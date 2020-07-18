module.exports = function (app) {
    app.get('/users', (request, response) => {
        var result = [
            {
                "name": "Роман",
                "surname": "Леонтьев",
                "address": "Казань",
                "phoneNumber": 89196411599,
                "best-skill": "Умею драться клюшкой на коньках",
                "e-mail": "974078rl@gmail.com",
                "telegram": "@romych13",
                "information": "Всем привет! Меня зовут Роман Леонтьев мне 18 лет. " +
                    "Я начинающий java-разработчик, закончил первый курс в ВШ ИТИС"
            }
        ];
        response.send(JSON.stringify(result));
    });
};
