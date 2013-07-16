define ([
  "underscore",
  "backbone"
], function(_, Backbone) {

  return Backbone.Model.extend({
    defaults: {
      options: {}
    },
    urlRoot: "/v1/data/",

    /**
     * takes a series 
     * and calculates the total of the y axis
     */
    totalBySeries: function (series) {
      var aggregateSeries = this.options().aggregate
      var valueType = this.options().valueType
      var sum = _.reduce(series.data, function(sum, el) {
        return el[1] + sum
      }, 0);

      console.log(this.get("options"))
      if(aggregateSeries == null) {
        return sum
      } else if (aggregateSeries === "average") {
        var avg = ((sum * 1.0)/series.data.length).toFixed(2)
        if(valueType === "percentage") {
          return avg + "%"
        } else {
          return avg
        }
      }
      return sum    
    },

    options: function() {
      return this.get("graph").options
    },
    name: function() {
      return this.get("graph").name
    },

    findSeries: function(datasets, name) {
      return _.find(datasets, function(series) {
        return series.label === name
      });
    },

    findTotalSeries: function(datasets, name) {
      var series = this.findSeries(datasets, name)
      if(series !== undefined) {
        return this.totalBySeries(series)
      } else {
        return null
      }
    },

    makeDataset: function(flattenData) {
      var datasets = {}

      _.map(this.get("series"), function(data) {
        label = data.name
        datasets[label] = {
          label: label,
          data : flattenData(data.data)
        }
      });

      return datasets
    },

    getAggregateName: function() {
      var aggregateSeries = this.options().aggregate

      if(aggregateSeries == "average") {
        return "Average"
      }
      return "Total"
    },

    hasPlot: function() {
      return this.get("graph").render !== "table"
    }
  });
});
