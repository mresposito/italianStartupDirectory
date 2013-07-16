define ([
  "jquery",
  "underscore",
  "backbone",
  "views/Graph.html",
  "tableSort"
], function($, _, Backbone, GraphHTML, TableSorter) {

  return Backbone.View.extend({

    initialize: function(){
      self = this
      var series = this.model.get("series")

      this.loadHtml()
      this.table = $(this.el).find("table")

      var datasets = this.model.makeDataset(this.flattenData)

      if (this.model.hasPlot()) {
        this.plotDatasets(datasets)
      }
      this.resumeData(datasets)
      this.setGraphProperties()
      $(".sortable").tablesorter()
    },

    flattenData: function (data) {
      var el =  _.map(data, function(point) {
        return point.data
      });

      return _.sortBy(el, function(point) {
        return point[0]
      });
    },

    loadHtml: function() {
      $(this.el).html(GraphHTML)
    },


    resumeData: function (datasets) {
      var data = _.sortBy(datasets.none.data, function(el) { return el[1]; }).reverse()
      var table = this.table
      this.formatHead()

      _.map(data, function(point) {
        table.append(
          self.formatTable(point))
      })
    },

    setGraphProperties: function() {
      // set title
      $(this.el).prepend("<h1>" + this.model.get("graph").name + "</h1>")
      // set graph description
      var description = this.model.get("graph").description
      if (description.length > 5) {
        $(this.el).find(".graph-description").prepend("<h3>Description:</h3>")
        $(this.el).find(".graph-description p").html(description)
      }
    },

    formatHead: function() {
      var entries = [this.model.name(), this.model.getAggregateName()]
      console.log(this.model.options())
      var add = this.model.options().tableHeaders
      if(add != null) {
        var entries = add
      } 

      this.table.append(
        this.formatTableHeader(entries))
    },


    formatTable: function(array) {
      str = ""
      _.map(array, function(el) {
        str += "<td>" + el + "</td>"
      })
      return "<tr>" + str + "</tr>"
    },

    formatTableHeader: function(array) {
      return "<thead>" + this.formatTable(array).replace(/td/g,"th") + "</thead>"
    }
  });
});
