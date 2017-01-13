module.factory('scheduleFormRESTService', scheduleFormRESTService);

function scheduleFormRESTService(restService){
    
    return{
        testRequest: testRequest,
        save: save
    };
    
    function testRequest(url, requestMethod, requestHeaders, requestBodyParameters, expectedResult, onSuccess, onError){
        if(typeof(onSuccess) !== 'function'){
            onSuccess = angular.noop;
        }
        if(typeof(onError) !== 'function'){
            onError = angular.noop;
        }
        
        var requestData = {
            url: url,
            requestMethod: requestMethod,
            requestHeaders: requestHeaders,
            requestBodyParameters: requestBodyParameters,       
            expectedResult: expectedResult            
        };
       
        return restService.doPOST('schedules/admin/testRequest', requestData, function successCallback(response){
            onSuccess(response);
        }, function errorCallback(response){
            onError(response.errorMessage || 'Not error specified', response.errorCode || 'No Message specified', response);
        });
    }   
    
    function save(data, onSuccess, onError){
        if(typeof(onSuccess) !== 'function'){
            onSuccess = angular.noop;
        }
        if(typeof(onError) !== 'function'){
            onError = angular.noop;
        }
       
        return restService.doPOST('schedules/admin/save', data, function successCallback(response){
            onSuccess(response);
        }, function errorCallback(response){
            onError(response.errorMessage || 'Not error specified', response.errorCode || 'No Message specified', response);
        });
    }   
}