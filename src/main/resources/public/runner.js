
angular.module('run', [])
    .controller('runController', function($scope, $http) {
        $http.get('http://localhost:8080/runs/1').
        then(function(response) {
            $scope.run = response.data;
        });
    }).controller('runsController', function($scope, $http) {
        $http.get('http://localhost:8080/runs').
        then(function(response) {
            $scope.runs = response.data;
        })
});

// angular.module('run', [])
//     .controller('descriptionController', function($scope, $http) {
//         $http.get('http://localhost:8080/run?location=Bla').
//         then(function(response) {
//             $scope.run = response.data;
//         });
//


