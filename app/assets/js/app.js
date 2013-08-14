require.config({
  paths: {
    jquery: "/assets/js/jquery-1.9.0.min",
    underscore: "/assets/js/underscore-min",
    backbone: "/assets/js/backbone-min"
  },
  shim: {
    jquery: {
      exports: "$"
    },
    underscore: {
      exports: "_"
    },
    backbone: {
      deps: ["underscore"],
      exports: "Backbone"
    }
  }
});

require ([
  "jquery",
  "underscore",
  "backbone",
  "router",
  "views/index",
  "views/create"
], function($, _, Backbone, Router, Index, Create) {
  // init router
  var router = new Router;
  Backbone.history.start();
  // index login logic
  new Index({
    el: $("#bodyWrap")
  });

  var pathname = window.document.location.pathname;
  if(pathname === "/crea") {
    new Create({
      el: $(".createContainer")
    })
  }
});
