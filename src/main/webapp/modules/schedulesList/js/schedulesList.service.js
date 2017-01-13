module.factory('schedulesListService', schedulesListService);

function schedulesListService(schedulesListRESTService){
    var self = {
        list: []
    };   
    
    schedulesListRESTService.getAll(function(response){
        self.list = response;
    });
    
    return self;
}