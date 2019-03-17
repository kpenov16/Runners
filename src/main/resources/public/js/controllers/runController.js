runapp.controller('runController', ['$scope', 'getRunService',function($scope, getRunService) {
    //FIXME getRunById id is hardcoded
    getRunService.getRunById(1).then( function(response){
        $scope.detailedRun = response.data;
        console.log($scope.detailedRun)
        }
    )
}]);


runapp.controller('runsTableController', ['$scope', 'getRunService', function($scope, getRunService) {
    getRunService.getAllURuns().then(function(response) {
        $scope.runs = response.data;
        $scope.plusOne = function(index){
            $scope.runs[index].attendancies++;
        }
    });
}]);


runapp.controller('createRunController', ['$scope', 'createRunService',
    function($scope, createRunService){
    $scope.runCreate = {};
    $scope.createRun = function() {
        createRunService.createRun($scope.runCreate).then(function (response) {
            $scope.runs.push(response.data);  // pushes new element to the runs table
        });
    }
}]);


