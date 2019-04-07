var app = angular.module('statistics', []);
app.controller('LiverpoolStatsCtrl', function($scope, $http) {
	$http.get("http://localhost:8080/liverpoolstats").then(function(response) {
		$scope.myData = response.data;
		var plArrData = [];
		var clArrData = [];
		
		response.data.forEach(sel => {
			if(sel.competition==='Premier League') {
				plArrData.push(sel);
			} else {
				clArrData.push(sel);
			}
		});
		console.log('plArrData: ', plArrData);
		console.log('clArrData: ', clArrData);
		
		$scope.plData = plArrData;
		$scope.clData = clArrData;
	});
});