window.fbAsyncInit = function() {
    FB.init({
        appId      : '282861061882459',
        cookie     : true,  // enable cookies to allow the server to access the session
        xfbml      : true,  // parse social plugins on this page
        version    : 'v2.0' // use version 2.0
    });
    
    // Now that we've initialized the JavaScript SDK, we call
    // FB.getLoginStatus().  This function gets the state of the
    // person visiting this page and can return one of three states to
    // the callback you provide.  They can be:
    //
    // 1. Logged into your app ('connected')
    // 2. Logged into Facebook, but not your app ('not_authorized')
    // 3. Not logged into Facebook and can't tell if they are logged into
    //    your app or not.
    //
    // These three cases are handled in the callback function.
    FB.getLoginStatus(function(response) {
        frandom.login.statusChangeCallback(response);
    });
};

// Load the SDK asynchronously
(function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

frandom.logout = function() {
    FB.logout(function(response) {
        console.log(response);
        location.reload();
    });
}


frandom.login = {
    statusChangeCallback : function (response) {
        // This is called with the results from from FB.getLoginStatus().
        console.log('statusChangeCallback');
        console.log(response);
        // The response object is returned with a status field that lets the
        // app know the current login status of the person.
        // Full docs on the response object can be found in the documentation
        // for FB.getLoginStatus().
        if (response.status === 'connected') {
            // Logged into your app and Facebook.
            frandom.login.testAPI();
            $("#login-out-button-container").html('<button onclick="frandom.logout();">Log out</button>');
        } else if (response.status === 'not_authorized') {
            // The person is logged into Facebook, but not your app.
            document.getElementById('status').innerHTML = 'Please log ' +
            'into this app.';
        } else {
            // The person is not logged into Facebook, so we're not sure if
            // they are logged into this app or not.
            document.getElementById('status').innerHTML = 'Please log ' +
            'into Facebook.';
        }
    },

    checkLoginState : function () {
        // This function is called when someone finishes with the Login Button.
        //See the onlogin handler attached to it in the sample code below.
        FB.getLoginStatus(function(response) {
            frandom.login.statusChangeCallback(response);
        });
    },

    // Here we run a very simple test of the Graph API after login is
    // successful.  See statusChangeCallback() for when this call is made.
    testAPI : function() {
        console.log('Welcome!  Fetching your information.... ');
        var data = {};
        FB.api('/me', function(response) {
            console.log('Good to see you, ' + response.name + '.');
            document.getElementById('status').innerHTML = 'Good to see you, ' +
            response.name;
            data["fbUserID"] = response.id;
            data["fbUserName"] = response.name;
            FB.api('/me/friends', function(response) {
                console.log(response);
                data["fbFriends"] = [];
                for (var i = 0; i < response.data.length; i++) {
                    data["fbFriends"].push(response.data[i].id);
                }
                console.log(data);
                var socket = new WebSocket("ws://localhost:8080/frandom-1.0.0-SNAPSHOT/chat");
                socket.onopen = function(event) {
                    console.log("opened");
                    socket.send(JSON.stringify(data));
                }
                socket.onmessage = function(event) {
                    console.log("message:" + event.data);
                }

                /*$.ajax({
                    type: "POST",
                    data: data,
                    url: "chat",
                    success: function(res) {
                        alert(res);
                    }
                });*/
            });
        });
    }        
};