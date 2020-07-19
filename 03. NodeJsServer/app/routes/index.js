const usersRoutes = require('./users_routes');
const requestRoutes = require('./request_routes');
module.exports = function(app) {
    usersRoutes(app);
    requestRoutes(app);
};
