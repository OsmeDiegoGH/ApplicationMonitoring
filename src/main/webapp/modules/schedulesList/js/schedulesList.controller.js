module.controller('schedulesListController', schedulesListController);

function schedulesListController(schedulesListService, schedulesListRESTService, URL_RESOURCES, arrayUtils){    
    var self = this;
    
    self.URL_RESOURCES = URL_RESOURCES;
    self.schedulesListService = schedulesListService;
    self.showForm = false;
    self.showFilteredRecordsText = false;
    self.isRecordsFiltered = false;
    self.showModalConfirm = false;
    self.confirmDeleteMessage = "¿Está seguro de eliminar el elemento?";
        
    self.confirmModalOnSubmit = angular.noop;
    self.showEmptyForm = showEmptyForm;
    self.editSchedule = editSchedule;
    self.switchStatusChanged = switchStatusChanged;
    self.deleteSchedule = deleteSchedule;
    self.isEmpty = isEmpty;
    
    function showEmptyForm(){
        self.showForm = true;
        self.currentSchedule = undefined;
    }
    
    function editSchedule(scheduleId){
        schedulesListRESTService.getByUniqueId(scheduleId, function(response){
            self.currentSchedule = response;
            self.currentSchedule.initDate = new Date(response.initDate);
            self.currentSchedule.endDate = new Date(response.endDate);
            self.currentSchedule.requestHeaders = arrayUtils.objectToKeyValueArray(self.currentSchedule.requestHeaders);
            self.currentSchedule.requestBodyParameters = arrayUtils.objectToKeyValueArray(self.currentSchedule.requestBodyParameters);
            self.showForm = true;
        });
    }
    
    function switchStatusChanged(schedule){
        var newStatus = schedule.isActive;
        schedule.isActive = !newStatus;
        schedulesListRESTService.changeStatus(schedule.uniqueId, newStatus, function onSuccess(){
            schedule.isActive = newStatus;
        });
    }
    
    function deleteSchedule(scheduleId){
        self.showModalConfirm = true;
        self.confirmModalMessage = self.confirmDeleteMessage;
        
        self.confirmModalOnSubmit = function(){
            schedulesListRESTService.deleteByUniqueId(scheduleId, function(response){
                self.schedulesListService.list = self.schedulesListService.list.filter(function(item){
                    return item.uniqueId !== scheduleId;
                });
                self.showModalConfirm = false;
            });
        }
    }
    
    function isEmpty(value){
        return value === undefined || value === "" || value === null;
    }
}