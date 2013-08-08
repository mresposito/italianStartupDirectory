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
  "views/indexNoConnection"
], function($, _, Backbone, index) {
  new index({
    el: $("#bodyWrap")
  }); 
});
