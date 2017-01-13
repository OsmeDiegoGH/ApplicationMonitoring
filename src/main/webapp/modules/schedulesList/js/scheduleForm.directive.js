module.directive('scheduleForm', function(){
    return {
        restrict: 'E',
        replace: true,      
        scope:{
            show: "=",
            model: "=?"
        },
        controller: 'scheduleFormController',
        controllerAs: 'scheduleFormCtrl',
        bindToController: true,
        templateUrl: 'schedulesList/templates/scheduleForm.html'
    };
});