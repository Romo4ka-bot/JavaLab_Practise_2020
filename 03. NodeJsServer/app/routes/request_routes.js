bodyParser = require('body-parser').json();

module.exports = function (app) {
    app.post('/request', bodyParser, (request, response) => {
        const {Client} = require('pg');
        const client = new Client({
            user: 'postgres',
            host: 'localhost',
            database: 'postgres',
            password: '111',
        });

        client.connect();

        let back = '<br><a href="/summary.html"><button>Назад</button></a>';
        let name = `${request.body.name}`;
        let email = `${request.body.email}`;
        let text = `${request.body.text}`;
        let query = 'insert into postgres.public.request (name, email, message) values ($1,$2,$3);';
        client.query(query, [name, email, text], (err) => {

            if (err != null)
                response.send("Incorrect data. Please try again." + back);

            else
                response.send("Thank you for your message, you will get a response from me soon." + back);

            client.end();
        });
    });

    app.get('/requests', (request, response) => {
        const {Client} = require('pg');

        const client = new Client({
            user: 'postgres',
            host: 'localhost',
            database: 'postgres',
            password: '111',
        });

        client.connect();
        client.query('select * from postgres.public.request', (err, res) => {

            if (err != null)
                response.send("Your request was rejected, please check the correct values. Incorrect data. Please try again.");

            response.send(JSON.stringify(res.rows));
            client.end();
        });
    })
};