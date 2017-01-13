module.factory('restService', restService);

function restService($http, messagesService){
    var self = this;
    
    self.urlContext = "/ApplicationMonitoring/rest/";
    
    return {
        doPOST: doPOST,
        doGET: doGET
    };
    
    function doPOST(url, params, onSuccess, onError){
        var requestData = {
            method: 'POST',
            url: self.urlContext + url,
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'Content-Type': 'application/json'
            },
            data: params
        };
        $http(requestData).then(function successCallback(response){
            if(typeof(onSuccess) === 'function'){
                onSuccess(response.data, response);
            }
        }, function errorCallback(response){
            messagesService.showError("Error '" + response.statusText + "' al consumir el servico");
            if(typeof(onError) === 'function'){
                onError(response.data, response);
            }
        });
    }
    
    function doGET(url, params, onSuccess, onError){
        var requestData = {
            method: 'GET',
            url: self.urlContext + url,
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'Content-Type': 'application/json'
            },
            params: params
        };
        $http(requestData).then(function successCallback(response){
            if(typeof(onSuccess) === 'function'){
                onSuccess(response.data, response);
            }
        }, function errorCallback(response){
            messagesService.showError("Error '" + response.statusText + "' al consumir el servico");
            if(typeof(onError) === 'function'){
                onError(response.data, response);
            }
        });
    }
}
