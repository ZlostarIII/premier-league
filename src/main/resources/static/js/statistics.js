var app = angular.module('statistics', []);

app.controller('LiverpoolStatsCtrl', function($scope, $http) {
	$http.get("http://localhost:8080/liverpoolstats").then(function(response) {
		var plArrData = [];
		var clArrData = [];
		
		var winsPl = 0,
			drawsPl = 0,
			lossesPl = 0,
			notPlayedPl = 0;
		
		var winsCl = 0,
			drawsCl = 0,
			lossesCl = 0,
			notPlayedCl = 0;
		
		response.data.forEach(sel => {
			if(sel.competition==='Premier League') {
				if(sel.outcome==='WIN') {
					winsPl++;
				} else if(sel.outcome==='DRAW') {
					drawsPl++;
				} else if(sel.outcome==='LOSS') {
					lossesPl++;
				} else {
					notPlayedPl++;
				}
				plArrData.push(sel);
			} else {
				if(sel.outcome==='WIN') {
					winsCl++;
				} else if(sel.outcome==='DRAW') {
					drawsCl++;
				} else if(sel.outcome==='LOSS') {
					lossesCl++;
				} else {
					notPlayedCl++;
				}
				clArrData.push(sel);
			}
		});
		
		var outcome = 0, color = "";
		
		var plOutcomes = [];
		plOutcomes.push({outcome:'WIN', value:winsPl, color:'#4286f4', noun:'wins'});
		plOutcomes.push({outcome:'DRAW', value:drawsPl, color:'#2faf44', noun:'draws'});
		plOutcomes.push({outcome:'LOSS', value:lossesPl, color:'#f44161', noun:'losses'});
		plOutcomes.push({outcome:'NOT_PLAYED', value:notPlayedPl, color:'#f4cd41', noun:'not played'});
		
		var clOutcomes = [];
		clOutcomes.push({outcome:'WIN', value:winsCl, color:'#4286f4', noun:'wins'});
		clOutcomes.push({outcome:'DRAW', value:drawsCl, color:'#2faf44', noun:'draws'});
		clOutcomes.push({outcome:'LOSS', value:lossesCl, color:'#f44161', noun:'losses'});
		clOutcomes.push({outcome:'NOT_PLAYED', value:notPlayedCl, color:'#f4cd41', noun:'not played'});
		
		$scope.plData = plArrData;
		$scope.clData = clArrData;
		$scope.plOutcomeData = plOutcomes;
		$scope.clOutcomeData = clOutcomes;
	});
});

app.directive('pieChart', function($parse, $timeout, $compile) {
	var tooltip = d3.select("body").append("div").style("position", "absolute")
			.style("z-index", "10").style("visibility", "hidden");

	var directiveDefinitionObject = {
		restrict : "A",
		replace : false,
		scope : {
			data : "="
		},
		link : function(scope, element, attrs) {
			$timeout(function() {
				
				var width,
					height,
					radius,
					pie,
					arc,
					svg,
					path;
				
				width = element[0].clientWidth;
				height = element[0].clientHeight;
				radius = Math.min (width, height) / 2;
				
				pie = d3.layout.pie()
						.value(function(d) {
							return d.value;
						})
						.sort(null);
	 
				arc = d3.svg.arc()
						.outerRadius(radius - 20)
						.innerRadius(radius - 150);
	 
				svg = d3.select(element[0])
						.append("svg")
						.attr({width : width, height : height})
						.append("g")
						.attr("transform", "translate(" + width * 0.5 + "," + height * 0.5 + ")");
	 
				path = svg.datum(scope.data)
						.selectAll("path")
						.data(pie)
						.enter()
						.append("path")
						.on("mouseover", function(d, i) {
							return tooltip.style("visibility","visible")
									.style("font-weight", "bold")
									.style("background", "lightblue")
									.style("border-radius", "5px")
									.style("font-family", "arial")
									.style("font-size", "15px")
									.text(scope.data[i].value + " " + scope.data[i].noun);
						})
						.on("mousemove", function() {
							return tooltip
									.style("top", (event.pageY - 10) + "px")
									.style("left", (event.pageX + 10) + "px");
						})
						.on("mouseout", function() {
							return tooltip.style("visibility", "hidden");
						})
						.attr({
							fill : function (d, i) {
								return scope.data[i].color;
							},
							d : arc
						});
					
				scope.$watch (
					"data",
					function () {
						pie.value (function (d) {return d.value;});
						path = path.data(pie);
						path.attr ("d", arc);
					},
					true
				);
			}, 2000);
		}
	};
	return directiveDefinitionObject;
});