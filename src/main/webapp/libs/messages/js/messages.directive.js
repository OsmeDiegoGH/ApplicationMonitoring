module.directive('messageItem', function(){
    return {
        restrict: 'E',
        replace: true,        
        scope: {
          message: "@",  
          messageType: "@",
          showCloseBtn: "@"
        },
        controller: 'messageController',
        controllerAs: 'messageCtrl',
        bindToController: true,
        templateUrl: 'messages/templates/messageItem.html'
    };
});