define ([
  "jquery",
  "underscore",
  "backbone",
  "/assets/js/bootstrap.min.js",
  "text!/crea/persona",
  "text!/crea/startup",
  "text!/crea/investitore"
], function($, _, Backbone, Typeahead, PersonaTemplate, StartupTemplate, InvestitoreTemplate) {

  return Backbone.View.extend({

    events: {
      "click .btnStep1Controller": "goToStep2",
      "click .btnControl" : "avantiIndietro"
    },

    initialize: function() {
      this.local = true;
      this.step1Template = this.body().clone()
    
      var step2 = this.getParameterByName("type")

      if(step2 == null) {
        this.step = 1
      } else {
        this.loadStep2(step2)
      }
    },

    body: function() {
      return $(this.el).find(".createBody")
    },

    avantiIndietro: function(event) {
      var av = $(event.target).data().control
      if(av === "indietro") {
        this.indietro()
      } else {
        this.avanti()
      }
    },

    indietro: function() {

      // we are going back to the first step
      this.step = 1
      this.step2Template = this.body().clone()
      this.setUrl("/crea")
      this.body().html(this.step1Template)
    }, 

    avanti: function () {

      if(this.step == 2) {
        this.sendForm()
      } else {
        this.step = 2
        this.setUrlWithParam(this.step2Name)
        this.body().html(this.step2Template)
      }
    },

    getParameterByName: function(name) {
        name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
        return results == null ? null : decodeURIComponent(results[1].replace(/\+/g, " "));
    },

    goToStep2: function(event) {
      var $target = $(event.target)
      var btn = $target
      if(btn.is("h3")) {
        btn = btn.parent()
      }
      var step = $(btn).data().step.toLowerCase()
      /* update url with new state */
      this.setUrlWithParam(step)
      // upload new template
      if(this.step2Template && this.step2Name == step) {
        // means we have pressed the same  button
        // as before, so just load it again.
        this.body().html(this.step2Template)
      } else {
        this.loadStep2(step)
      }
    },

    setUrlWithParam: function(param) {
      var pathname = window.document.location.pathname
      var newLocation = pathname + "?type=" + param
      this.setUrl(newLocation)
    },

    setUrl: function(newLocation) {
      window.history.pushState("", window.document.title,  newLocation)
    },
    
    loadStep2: function(type) {
      // set local state parameters
      this.step2Name = type
      this.step = 2
      
      var Template = null
      if(type === "persona") {
        Template = PersonaTemplate
      } else if (type === "startup") {
        Template = StartupTemplate
      }  else if (type === "investitore") {
        Template = InvestitoreTemplate
      } else {
        throw "type not found: " + type
      }

      if(Template !== null) {
        $(this.el).html(Template)
        $(this.el).find(".createControllers").show()
        $(this.el).find(".inputLavoro").typeahead()
      }
    }
  });
});
