define ([
  "jquery",
  "underscore",
  "backbone"
], function($, _, Backbone) {

  return Backbone.View.extend({

    events: {
      "click .fbLogin": "fbLogin",
      "click .fbLogout": "fbLogout"
    },

    initialize: function() {
      this.local = true;
    },

    fbLogin:  function(event) {
      var self = this;

      FB.login(function(response) {
        if (response.authResponse) {
          access_token = response.authResponse.accessToken; //get access token
          user_id = response.authResponse.userID; //get FB UID

          FB.api('/me', function(response) {
            self.toServer("/login", response);
          });

        } else {
          //user hit cancel button
          console.log('User cancelled login or did not fully authorize.');
        }
      }, {
        scope: 'email'
      });
    },

    fbLogout:  function(event) {
      var self = this;

      FB.logout(function(response) {
        self.toServer("/logout", response) 
      });
    },

    toServer: function(path, data) {
      $.ajax({
        url: path,
        data: data
      }).done(function() {
        window.location.reload(true);
      });
    }
  });
});
