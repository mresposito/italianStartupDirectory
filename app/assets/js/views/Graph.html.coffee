define ["underscore"], (_) ->
  _.template '''
    <div class="graph-container">
      <div id="placeholder" class="demo-placeholder" style="float:left; width:90%; height: 500px;"></div>
      <p id="choices" style="float:right; width:10%;"></p>
    </div>

    <div id="overview" class="demo-placeholder" style="height:150px; margin-top: 550px;"></div>

    <div class="graph-description">
      <p></p>
    </div>

    <table class="table table-striped display-table sortable">
    </table>
  '''
  # <div class="span9">
  #   <hr style="left:0px;">
  # </div>
