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
    $scope.runCreate = {};
    $scope.createRun = function () {
    $http.post('http://localhost:8080/createRun', $scope.runCreate).then(function (response) {
        console.log(response.data);
        $scope.runs.push(response.data);
    }, function errorCallback(response) {
        alert("Error. while created user Try Again!\n" + response.data.message);
    })
    }
});


