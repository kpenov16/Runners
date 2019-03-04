
angular.module('run', [])
    .controller('runController', function($scope, $http) {
        $http.get('http://localhost:8080/run?location=Pleven').
        then(function(response) {
            $scope.run = response.data;
        });
    }).controller('descriptionController', function($scope, $http) {
        $http.get('http://localhost:8080/run?location=Bla').
        then(function(response) {
            $scope.run = response.data;
        })
});

// angular.module('run', [])
//     .controller('descriptionController', function($scope, $http) {
//         $http.get('http://localhost:8080/run?location=Bla').
//         then(function(response) {
//             $scope.run = response.data;
//         });
//


