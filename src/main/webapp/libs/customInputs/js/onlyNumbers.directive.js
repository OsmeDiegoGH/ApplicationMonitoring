module.directive('onlyNumbers', function(){
    return {
        restrict: 'A',
        replace: true,        
        controller: 'onlyNumbersController',
        controllerAs: 'onlyNumbersCtrl',
        bindToController: true,
        link: function($scope, $element, $attr){
            $element.bind("keydown", function($event){
                console.log('keydown link');
                
                var validKeyCodes = [
                  8/* del */,   
                  13/* enter */,   
                  13/* enter */,   
                  116/* F5 */,   
                ];
                
                if($event.keyCode !== 8 && $event.keyCode !== 13 && isNaN($event.key)){
                    $event.preventDefault(); 
                    $event.stopPropagation();
                } 
            });
        }
    };
});