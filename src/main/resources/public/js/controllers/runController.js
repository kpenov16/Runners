runapp.controller('runController', ['$scope', 'runService',function($scope, runService) {
    //FIXME getRunById id is hardcoded
    runService.getRunById(1).then( function(response){
        $scope.detailedRun = response.data;
        console.log($scope.detailedRun)
        }
    )
}]);

runapp.controller('deleteRunController', ['$scope', 'runService',
    function($scope, runService) {
        $scope.deleteRunById = function(){
            runService.deleteRunById($scope.deleteRun.id).then(
                function(response){
                console.log(response);
            });
        }
}]);


runapp.controller('runsTableController', ['$scope', 'runService', function($scope, runService) {
    runService.getAllURuns().then(function(response) {
        $scope.runs = response.data;
        $scope.plusOne = function(index){
            $scope.runs[index].attendancies++;
        }
    });
}]);


runapp.controller('createRunController', ['$scope', 'runService',
    function($scope, runService){
    $scope.createRun = function() {
        runService.createRun($scope.runCreate).then(function (response) {
            $scope.runs.push(response.data);  // pushes new element to the runs table
        });
    }
}]);


