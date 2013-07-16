define ([
  "jquery",
  "underscore",
  "backbone",
  "flot",
  "flotTime",
  "flotSelect",
  "views/GraphSuper"
], function($, _, Backbone, Flot, FlotTime, FlotSelect, GraphSuper) {

  return GraphSuper.extend({

    /**
     * Create the table
     */ 
    resumeData: function(datasets) {
      self = this      
      this.formatHead()
      
      _.map(datasets, function(series) {
        var tot = self.model.totalBySeries(series)
        self.table.append(
          self.formatTable([series.label, tot]))
      })
    },

    /**
     * This function does the actual plotting
     */
    plotDatasets: function(data) {
      function trimDataset(data) {
        dataset = {}
        var i = 0;
        $.each(data, function(key, val) {
          if(i < 10) {
            dataset[key]= val
            ++i;
          }
        });
        return dataset
      }

      datasets = trimDataset(data)
      var i = 0;
      $.each(datasets, function(key, val) {
        val.color = i;
        ++i;
      });

      // insert checkboxes 
      var choiceContainer = $("#choices");
      $.each(datasets, function(key, val) {
        choiceContainer.append("<input type='checkbox' name='" + key +
          "' checked='checked' id='id" + key + "'>" +
          "<label for='id" + key + "'>"
          + val.label + "</label></input>");
      });

      choiceContainer.find("input").click(plotAccordingToChoices);

      function plotAccordingToChoices() {
        function weekendAreas(axes) {

          var markings = [],
              d = new Date(axes.xaxis.min);

          // go to the first Saturday

          d.setUTCDate(d.getUTCDate() - ((d.getUTCDay() + 1) % 7))
            d.setUTCSeconds(0);
          d.setUTCMinutes(0);
          d.setUTCHours(0);

          var i = d.getTime();

          // when we don't set yaxis, the rectangle automatically
          // extends to infinity upwards and downwards

          do {
            markings.push({ xaxis: { from: i, to: i + 2 * 24 * 60 * 60 * 1000 } });
            i += 7 * 24 * 60 * 60 * 1000;
          } while (i < axes.xaxis.max);

          return markings;
        }

        var data = [];

        choiceContainer.find("input:checked").each(function () {
          var key = $(this).attr("name");
          if (key && datasets[key]) {
            data.push(datasets[key]);
          }
        });

        if (data.length > 0) {
          options = {
            yaxis: {
              min: 0
            },
            xaxis: {
              mode: "time"
            },
            points: {
              show: true
            },
            lines: {
              show: true
            },
            grid: {
              markings: weekendAreas
            },
            selection: {
              mode: "x"
            }
          }
          $.plot("#placeholder", data, options);

          var overview = $.plot("#overview", data, {
            series: {
              lines: {
                show: true,
                lineWidth: 1
              },
              shadowSize: 0
            },
            xaxis: {
              mode: "time"
            },
            yaxis: {
              min: 0,
              autoscaleMargin: 0.1
            },
            selection: {
              mode: "x"
            },
            legend: {
              show: false
            }
          });

          // now connect the two

          $("#placeholder").bind("plotselected", function (event, ranges) {

            // do the zooming

            plot = $.plot("#placeholder", data, $.extend(true, {}, options, {
              xaxis: {
                min: ranges.xaxis.from,
               max: ranges.xaxis.to
              }
            }));

            // don't fire event on the overview to prevent eternal loop


            $("#overview").bind("plotselected", function (event, ranges) {
              plot.setSelection(ranges);
            });

            overview.setSelection(ranges, true);
          });
        }
      }

      plotAccordingToChoices();
    }
  });
});
