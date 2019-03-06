
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
            //todo add function to join button. Updates number of participants.
        })
    }).controller('createRunController', function($scope, $http) {
    $http.post('http://localhost:8080/createRun', {
        id: 10
    }).
    then(function(response) {
    })
});


