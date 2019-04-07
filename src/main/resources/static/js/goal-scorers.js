var app = angular.module('goalScorers', []);

app.controller('GoalScorersCtrl', function($scope, $http) {
	$http.get("http://localhost:8080/plgoalscorers").then(function(response) {
		$scope.myData = response.data;
	});
});

app.directive('barsChart', function($parse, $timeout, $compile) {
	var tooltip = d3.select("body").append("div").style("position", "absolute")
			.style("z-index", "10").style("visibility", "hidden");

	var directiveDefinitionObject = {
		restrict : 'E',
		replace : false,
		scope : {
			data : '=chartData'
		},
		link : function(scope, element, attrs) {
			$timeout(function() {
				var chart = d3.select(element[0]);
				chart.append("div").attr("class", "chart").selectAll('div')
						.data(scope.data).enter().append("div").on(
								"mouseover",
								function(d) {
									return tooltip.style("visibility",
											"visible").style("font-weight",
											"bold").style("background",
											"lightblue").style("border-radius",
											"5px")
											.style("font-family", "arial")
											.style("font-size", "12px").text(
													d.goals + " goals");
								}).on(
								"mousemove",
								function() {
									return tooltip.style("top",
											(event.pageY - 10) + "px").style(
											"left", (event.pageX + 10) + "px");
								}).on("mouseout", function() {
							return tooltip.style("visibility", "hidden");
						}).transition().ease("elastic").style("width",
								function(d) {
									return d.goals + "%";
								}).text(function(d) {
							return d.name;
						});
			}, 2000);
		}
	};
	return directiveDefinitionObject;
});