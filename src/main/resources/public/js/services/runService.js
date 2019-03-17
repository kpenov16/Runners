runapp.factory('runService', ['$http', function($http) {

    var factory = {};
    var urlBase = 'http://localhost:8080/runs/';
    factory.getAllURuns = function(){
        return $http.get(urlBase)
            .then(function(response) {
                return response;
            })
    }

    factory.getRunById = function(id){
        return $http.get(urlBase+id)
            .then(function(response) {
                return response;
            })
    }

    factory.deleteRunById = function(id){
        return $http.delete(urlBase+id)
            .then(function(response) {
                return response;
            })
    }

    factory.createRun = function(run){
        return  $http.post(urlBase, run).then(
            function (response) {
                return response;
            },
            function errorCallback(response){
                alert("Error. while created user Try Again!\n" + response.data.message);
            })
    }

    return factory;



}]);
/*
runapp.factory('createRunService', ['$http', function( $http) {
        var factory = {};
        var urlBase = 'http://localhost:8080/runs';
        factory.createRun = function(run){
            return  $http.post(urlBase, run).then(
                function (response) {
                    return response;
                },
                function errorCallback(response){
                    alert("Error. while created user Try Again!\n" + response.data.message);
                })
        }
        return factory;
}]);
*/
