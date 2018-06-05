ADODB = require('node-adodb');

const CONNECTION_STRING = "Provider=Microsoft.ACE.OLEDB.12.0;Data Source=db\\db.accdb;Persist Security Info=False;";

module.exports = {
    get_user: function (req, res) {

        let data_type = req.query.type;
        let sql_command = "";

        switch (data_type) {
            case "login":
                let email = req.query.email;
                let password = req.query.password;

                sql_commad = "SELECT * FROM users WHERE email = '" + email + "' and password = '" + password + "';";

                // run query
                run_sql_query(sql_commad, function (data) {
                    res.header("Content-Type", "application/json; charset=utf-8");
                    console.log(data.records);
                    res.end(JSON.stringify(data.records, null, 4));
                });
                break;

            case "user":
                let user_id = req.query.user_id;
                // search by user_name
                if (user_id === undefined) {
                    let username = req.query.username;
                    sql_commad = "SELECT * FROM [users] WHERE username = '" + username + "';";
                }else {
                    sql_commnad = "SELECT * FROM [users] WHERE user_id = " + user_id + ";";
                }

                // run query
                run_sql_query(sql_commad, function (data) {
                    res.header("Content-Type", "application/json; charset=utf-8");
                    res.end(JSON.stringify(data.records, null, 4));
                });
                break;

        }
    },

    get_items: function (req, res) {

        let sql_command = "";

        sql_commad = "SELECT * FROM items;";

        // run query
        run_sql_query(sql_commad, function (data) {
            res.header("Content-Type", "application/json; charset=utf-8");
            console.log(data.records);
            res.end(JSON.stringify(data.records, null, 4));
        });

    },

    get_semi_items: function (req, res) {

        let sql_command = "";

        sql_commad = "SELECT * FROM semi_items;";

        // run query
        run_sql_query(sql_commad, function (data) {
            res.header("Content-Type", "application/json; charset=utf-8");
            console.log(data.records);
            res.end(JSON.stringify(data.records, null, 4));
        });

    },

    get_history: function (req, res) {

        let sql_command = "";

        let user_id = req.query.user_id;

        sql_commad = "SELECT history.order_id AS order_id, date, order_items.order_item_id AS order_item, item_id, semi_item_id FROM (history INNER JOIN order_items ON history.order_id=order_items.order_id) LEFT JOIN order_semi ON order_semi.order_item_id=order_items.order_item_id WHERE user_id = " + user_id + ";";

        // run query
        run_sql_query(sql_commad, function (data) {
            res.header("Content-Type", "application/json; charset=utf-8");
            console.log(data.records);
            res.end(JSON.stringify(data.records, null, 4));
        });

    },

    get_favourites: function (req, res) {
        let user_id = req.query.user_id;

        let sql_commad = "SELECT favourites.fav_id AS favourite_id, item_id, semi_id FROM favourites LEFT JOIN fav_semi ON favourites.fav_id=fav_semi.fav_id WHERE favourites.user_id = " + user_id + " ;";

        // run query
        run_sql_query(sql_commad, function (data) {
            res.header("Content-Type", "application/json; charset=utf-8");
            console.log(data.records);
            res.end(JSON.stringify(data.records, null, 4));
        });
    },

    set_user: function (req, res) {
        let data_type = req.query.type;
        let sql_commad = "";

        switch (data_type) {
            // Set Users
            case "user":
                let first_name = req.query.first_name;
                let last_name = req.query.last_name;
                let password = req.query.password;
                let email = req.query.email;

                select_sql_commad = "SELECT * FROM [users] WHERE email = '" + email + "';";

                // run query - Check if user with given username exists.
                run_sql_query(select_sql_commad, function (data) {


                    if (data.records.length == 0) {

                        insert_sql_commad = `INSERT INTO [users] (first_name, last_name, [password], email) ` +
                                            `VALUES ('${first_name}', '${last_name}', '${password}', '${email}');`;

                        // run query - No user with given username found - will be added to database.
                        run_sql_query(insert_sql_commad, function (data) {},  function () {

                            // run query
                            run_sql_query(select_sql_commad, function (data) {
                                res.header("Content-Type", "application/json; charset=utf-8");
                                res.end(JSON.stringify(data.records, null, 4));
                            });
                        }, debug=false);
                    }
                    else {
                        res.header("Content-Type", "application/json; charset=utf-8");
                        res.end(JSON.stringify([], null, 4));
                    }
                });
                break;
        }
    },

    update_favourites: function (req, res) {

        let user_id = req.query.user_id;
        let item_id = req.query.item_id;
        let type = req.query.type;
        let semi_items = req.query.semi_items;
        let fav_id;

        switch (type) {
            // Example URL: http://localhost/update_favourites?user_id=1&type=add&item_id=3&semi_items=1_2_3
            case "add":

                insert_sql_commad = `INSERT INTO [favourites] (user_id, item_id)  ` + `VALUES ('${user_id}', '${item_id}');`;

                // run query - No user with given username found - will be added to database.
                run_sql_query(insert_sql_commad, function (data) {},
                    function () {

                        select_sql_commad = 'SELECT MAX(fav_id) FROM [favourites] WHERE user_id=' + user_id + ' AND item_id=' + item_id +';';

                        // run query
                        run_sql_query(select_sql_commad, function (data) {

                            fav_id = data.records[0]["Expr1000"];

                            if(semi_items != -1){
                                semi_items = semi_items.split("_");

                                for(let i = 0; i < semi_items.length; i++){
                                    insert_sql_commad = `INSERT INTO [fav_semi] (fav_id, semi_id)  ` + `VALUES ('${fav_id}', '${semi_items[i]}');`;

                                    // run query - No user with given username found - will be added to database.
                                    run_sql_query(insert_sql_commad, function (data) {},
                                        function () {

                                            select_sql_commad = 'SELECT * FROM [fav_semi] WHERE fav_id=' + fav_id + ' AND semi_id=' + semi_items[i] +';';

                                            // run query
                                            run_sql_query(select_sql_commad, function (data) {});
                                        }, debug = false);
                                }
                            } else {
                                console.log("here")
                            }
                        });
                    }, debug = false);

                res.end(JSON.stringify(user_id, null, 4));
                break;

            case "remove":
                // Example URL: http://localhost/update_favourites?fav_id=56&type=remove
                fav_id  = req.query.fav_id;

                delete_sql_query = "DELETE FROM [favourites] WHERE fav_id=" + fav_id + ';';
                run_sql_query(delete_sql_query, function() {}, function (data) {

                    select_query = "SELECT * FROM [fav_semi] WHERE fav_id=" + fav_id + ';';
                    run_sql_query(select_query, function (data) {

                        if (data.records.length != 0) {

                            delete_query = "DELETE fav_id FROM [fav_semi] WHERE fav_id=" + fav_id + ';';
                            run_sql_query(delete_query, function() {}, function (data) {
                                res.header("Content-Type", "application/json; charset=utf-8");
                                res.end(JSON.stringify("removed", null, 4));
                            }, debug=false);
                        }
                    });
                }, debug=false);

        }

    },

    add_order: function (req, res) {
       let user_id = req.query.user_id;
        let item_id = req.query.item_id;
        let semi_items = req.query.semi_items;
        let fav_id;

        insert_sql_commad = `INSERT INTO [favourites] (user_id, item_id)  ` + `VALUES ('${user_id}', '${item_id}');`;

        // run query - No user with given username found - will be added to database.
        run_sql_query(insert_sql_commad, function (data) {},
            function () {

                select_sql_commad = 'SELECT MAX(fav_id) FROM [favourites] WHERE user_id=' + user_id + ' AND item_id=' + item_id +';';

                // run query
                run_sql_query(select_sql_commad, function (data) {

                    fav_id = data.records[0]["Expr1000"];

                    if(semi_items !== -1){
                        semi_items = semi_items.split("_");

                        for(let i = 0; i < semi_items.length; i++){
                            insert_sql_commad = `INSERT INTO [fav_semi] (fav_id, semi_id)  ` + `VALUES ('${fav_id}', '${semi_items[i]}');`;

                            // run query - No user with given username found - will be added to database.
                            run_sql_query(insert_sql_commad, function (data) {},
                                function () {

                                    select_sql_commad = 'SELECT * FROM [fav_semi] WHERE fav_id=' + fav_id + ' AND semi_id=' + semi_items[i] +';';

                                    // run query
                                    run_sql_query(select_sql_commad, function (data) {});
                                }, debug = false);

                        }
                    }
                });
            }, debug = false);

        res.end(JSON.stringify("add to fav is OK, user_id: " + user_id, null, 4));
    }
};

// =====================================================================
// =========================   Utils     ===============================
// =====================================================================
/**
 * Runs SQL queries.
 * ATM working with Access DB.
 *
 * @param query
 * @param success
 * @param failure
 * @param debug
 */
function run_sql_query(query, success, failure, debug = true) {

    // if no failure was given, use the default one
    if (failure === undefined) {
        failure = default_failure_callback;
    }

    // Log adodb errors
    ADODB.debug = debug;

    // Connect to the MS Access DB
    let connection = ADODB.open(CONNECTION_STRING);

    // Query the DB
    connection.query(query).on('done', success).on('fail', failure);
}

/**
 * Runs SQL queries.
 * ATM working with Access DB.
 *
 * @param query
 * @param success
 * @param failure
 */
function run_insert_sql_query(query, success, failure) {

    // if no failure was given, use the default one
    if (failure === undefined) {
        failure = default_failure_callback;
    }

    // Log adodb errors
    ADODB.debug = true;

    // Connect to the MS Access DB
    let connection = ADODB.open(CONNECTION_STRING);

    // Query the DB
    connection.query(query).on('done', success).on('fail', failure);
}


/**
 * Default failure callback
 */
function default_failure_callback(data) {
    console.log("failed to execute sql command")
}

