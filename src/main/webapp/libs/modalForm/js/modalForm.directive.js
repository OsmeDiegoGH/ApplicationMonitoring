module.directive('modalForm', function(){
    return {
        restrict: 'E',
        replace: true,
        scope: {
            formName: "@",
            formClass: "@",
            showForm: "=?",
            showSubmitBtn: "=?",
            modalSize: "@",
            onSubmit: "<?",
            onClose: "<?",
            onShow: "<?",
            saveBtnText: "@?",
            closeBtnText: "@?"
        },
        bindToController: true,
        controller: 'modalFormController as modalFormCtrl',
        templateUrl: 'modalForm/templates/modalForm.html',
        transclude: {
            'fields': 'modalFormFields',
            'buttons': '?modalFormButtons'
        },
        link: function($scope, iElement, iattrs, controller, transcludeFn){            
            if(iattrs.formName === undefined){
                controller.formName = "form";
            }
            if($scope.modalFormCtrl.formName !== undefined){
                controller.form = controller[controller.formName];
                $scope.$parent[$scope.modalFormCtrl.formName] = controller.form;
                $scope.$parent[$scope.modalFormCtrl.formName + "Controller"] = controller;
            }
            transcludeFn($scope.$parent, function(transcludeEl) {
                iElement.append(transcludeEl);
            });
            angular.element(document.body).append(iElement);
        }
    };
});