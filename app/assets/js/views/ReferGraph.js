define ([
  "jquery",
  "underscore",
  "backbone",
  "flot",
  "flotTime",
  "flotSelect",
  "views/LineGraph"
], function($, _, Backbone, Flot, FlotTime, FlotSelect, LineGraph) {

  return LineGraph.extend({

    loadCounterModel: function() { // little hack, remove hard coding
      var activation = "Activation from Referrer"
      var refer = "Referrer Count"
      if(this.model.name() === activation) {
        return this.collection.findByName(refer)
      } else if (this.model.name() === refer){
        return this.collection.findByName(activation)
      } else {
        return null
      }
    },

    formatHead: function() {
      var ratio = ""
      var counterColumn = ""
      if(this.model.name() === "Activation from Referrer") {
        ratio = "Activation/Refer"
        counterColumn = "Referrer Count" 
      } else {
        ratio = "Refer/Activation"
        counterColumn = "Activation from Referrer" 
      }
      this.table.append(
          this.formatTableHeader(["Name of referrer", "Count", counterColumn, ratio]))
    },

    resumeData: function(datasets) {
      function showPercent(numerand, dividend) {
        if(dividend != null && numerand != null)
          return (((numerand*1.0)/dividend)*100).toFixed(2) + "%"
        return "0%"
      }
      self = this      
      var counterModel = this.loadCounterModel()
      var counterDataset = counterModel.makeDataset(this.flattenData)
      this.formatHead()
      
      _.map(datasets, function(series) {
        var tot = self.model.totalBySeries(series)
        var counterTot = counterModel.findTotalSeries(counterDataset, series.label)
        var perCent = null
        if(self.model.name() === "Activation from Referrer") {
          perCent = showPercent(tot, counterTot)
        } else {
          perCent = showPercent(counterTot, tot)
        }
        if( counterTot == null) {
          counterTot = 0
        }
        self.table.append(
          self.formatTable([series.label, tot, counterTot, perCent]))
      })
    }
  });
});
