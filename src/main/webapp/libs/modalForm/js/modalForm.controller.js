module.controller('modalFormController', modalFormController);

function modalFormController($scope){
    var self = this;

    if(self.modalSize !== "lg" && self.modalSize !== "md" && self.modalSize !== "sm"){
        throw "Invalid modal size:" + self.modalSize + " for " + self.formName;   
    }
    self.submit = submit;
    self.close = close;  
    self.showSubmitBtn = (self.showSubmitBtn === undefined) ? true : self.showSubmitBtn;
    self.onShow = (typeof(self.onShow) === 'function') ? self.onShow : angular.noop;
    self.colsBtns = self.modalSize === "lg" ? '2' : '3';
    self.colsOffsetSaveBtn = self.modalSize === "lg" ? '8' : '6';
    self.closeBtnText = self.closeBtnText !== undefined ? self.closeBtnText : "Cancelar";
    self.saveBtnText = self.saveBtnText !== undefined ? self.saveBtnText : "Guardar";
    
    if(self.showForm === undefined){
        self.showForm = true;
        self.onShow();
    }else{
        $scope.$watch(function(){
            return self.showForm;
        }, function(newValue){
            if(newValue === true){
                self.onShow();
                resetForm();
                angular.element(document).on("keydown", modalFormKeyPressListener);
                return;
            }    
            angular.element(document).off("keydown", modalFormKeyPressListener);
        });
    }
    
    function modalFormKeyPressListener(event) {
        switch (event.which) {
            case 13:
                self.submit();
                break;
            case 27:
                self.close();
                break;
        }       
        $scope.$apply();
    }
    function resetForm(){
        self.form.$setPristine();
        self.form.$setUntouched();
    }
        
    function submit(){
        self.form.$setSubmitted();
        if(self.form.$invalid){
            return;
        }
        if(typeof(self.onSubmit) === 'function'){
            self.onSubmit(self.form);
        }
    }
    
    function close(){
        if(typeof(self.onClose) === 'function'){
            self.onClose(self.form);
            return;
        }
        self.showForm = false;
    }    
}