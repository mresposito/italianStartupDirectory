function fbLogin(){
  FB.login(function(response) {
    if (response.authResponse) {
      access_token = response.authResponse.accessToken; //get access token
      user_id = response.authResponse.userID; //get FB UID

      FB.api('/me', function(response) {
        makeLogout();
      });

    } else {
      //user hit cancel button
      console.log('User cancelled login or did not fully authorize.');
    }
  }, {
      scope: 'email'
  });
}

var makeLogout = function (response) {
  setAttrLogControl("Logout");
}

var makeLogin = function (response) {
  setAttrLogControl("Login");
}

var setAttrLogControl = function(log) {
  $(".logControl").text(log);
  $(".logControl").attr("onClick","fb" + log + "()");
}

var fbLogout = function () {
  FB.logout(function(response) {
    makeLogin();
  });
}