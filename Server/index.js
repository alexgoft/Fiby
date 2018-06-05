const express = require('express');
const server = require('./server.js');
const app = express();

// Get user data from server
app.get('/get_user', function (req, res) {
    server.get_user(req, res)
});

// Set data to server.
app.get('/set_user', function (req, res) {
    server.set_user(req, res);
});

// Get item to server.
app.get('/items', function (req, res) {
    server.get_items(req, res);
});

// Get item to server.
app.get('/semi_items', function (req, res) {
    server.get_semi_items(req, res);
});

// Get item to server.
app.get('/history', function (req, res) {
    server.get_history(req, res);
});

// Get item to server.
app.get('/favourites', function (req, res) {
    server.get_favourites(req, res);
});

// Get item to server.
app.get('/update_favourites', function (req, res) {
    server.update_favourites(req, res);
});

// Get item to server.
app.get('/new_order', function (req, res) {
    server.add_order(req, res);
});

app.listen(80, function () {

    console.log('App listening on port 80!')
});