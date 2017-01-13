module.factory('schedulesListRESTService', schedulesListRESTService);

function schedulesListRESTService(restService){
    
    return{
        getAll: getAll,
        getByUniqueId: getByUniqueId,
        changeStatus: changeStatus,
        deleteByUniqueId: deleteByUniqueId
    };
    
    function getAll(onSuccess, onError){
        if(typeof(onSuccess) !== 'function'){
            onSuccess = angular.noop;
        }
        if(typeof(onError) !== 'function'){
            onError = angular.noop;
        }
        return restService.doGET("schedules/admin/getAll", {}, function successCallback(response){
            onSuccess(response);
        }, function errorCallback(response){
            onError(response.errorMessage || "Not error specified", response.errorCode || "No Message specified", response);
        });
    }   
    
    function getByUniqueId(uniqueId, onSuccess, onError){
        if(typeof(onSuccess) !== 'function'){
            onSuccess = angular.noop;
        }
        if(typeof(onError) !== 'function'){
            onError = angular.noop;
        }
        return restService.doGET("schedules/admin/get", {"uniqueId": uniqueId}, function successCallback(response){
            onSuccess(response);
        }, function errorCallback(response){
            onError(response.errorMessage || "Not error specified", response.errorCode || "No Message specified", response);
        });
    }   
    
    function changeStatus(uniqueId, newStatus, onSuccess, onError){
        if(typeof(onSuccess) !== 'function'){
            onSuccess = angular.noop;
        }
        if(typeof(onError) !== 'function'){
            onError = angular.noop;
        }
        return restService.doPOST("schedules/admin/changeStatus", {"uniqueId": uniqueId, "newStatus": newStatus}, function successCallback(response){
            onSuccess(response);
        }, function errorCallback(response){
            onError(response.errorMessage || "Not error specified", response.errorCode || "No Message specified", response);
        });
    }   
    
    function deleteByUniqueId(uniqueId, onSuccess, onError){
        if(typeof(onSuccess) !== 'function'){
            onSuccess = angular.noop;
        }
        if(typeof(onError) !== 'function'){
            onError = angular.noop;
        }
        return restService.doPOST("schedules/admin/delete", {"uniqueId": uniqueId}, function successCallback(response){
            onSuccess(response);
        }, function errorCallback(response){
            onError(response.errorMessage || "Not error specified", response.errorCode || "No Message specified", response);
        });
    }   
}