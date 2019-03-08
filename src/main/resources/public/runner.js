var runapp = angular.module('run', []);

runapp.controller('runController', function($scope, $http) {
    $http.get('http://localhost:8080/runs/1').then(function (response) {
        $scope.run = response.data;
        console.log($scope.run);
    })
});

runapp.controller('runsController', function($scope, $http) {
        $http.get('http://localhost:8080/runs').
        then(function(response) {
            $scope.runs = response.data;
            $scope.plusOne = function(index){
                $scope.runs[index].attendancies++;
            }
            //todo add function to join button. Updates number of participants.
        })
});

runapp.controller('createRunController', function($scope, $http) {
    $http.post('http://localhost:8080/createRun', {
        id: 12, location : 'Aarhus'
    }).
    then(function(response) {
    })
});


