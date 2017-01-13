module.directive('schedulesList', function(){
    return {
        restrict: 'E',
        replace: true,        
        controller: 'schedulesListController',
        controllerAs: 'schedulesListCtrl',
        bindToController: true,
        templateUrl: 'schedulesList/templates/schedulesList.html'
    };
});