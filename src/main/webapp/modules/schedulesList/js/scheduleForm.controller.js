module.controller('scheduleFormController', scheduleFormController);

function scheduleFormController(URL_RESOURCES, schedulesListService, scheduleFormRESTService, arrayUtils) {
    var self = this;

    self.URL_RESOURCES = URL_RESOURCES;
    self.isTestInProgress = false;
    self.showSuccessRequestIcon = false;
    self.showBadRequestIcon = false;
    self.requestError = '';
    self.defaultModel = {
        requestMethod: 'GET',
        requestHeaders: [],
        requestBodyParameters: [],
        expectedResult: 'HTTP_200',
        emailAlertList: []
    };

    self.initForm = initForm;
    self.addEmptyRequestParameter = addEmptyRequestParameter;
    self.deleteRequestParameterFromList = deleteRequestParameterFromList;
    self.addEmptyRequestHeader = addEmptyRequestHeader;
    self.deleteRequestHeaderFromList = deleteRequestHeaderFromList;
    self.testRequest = testRequest;
    self.save = save;
    self.isEmpty = isEmpty;

    function initForm() {
        self.showBadRequestIcon = false;
        self.showSuccessRequestIcon = false;
        self.model = self.model !== undefined ? self.model : self.defaultModel;
        self.currentTab = 'headers';
        if(self.model.expectedResult === 'HTTP_200'){
            self.expectedResultOption = self.model.expectedResult;
        }else{
            self.expectedResultOption = 'CUSTOM';
            self.expectedResultCustom = self.model.expectedResult;
        }
        self.requestError = '';
    }

    function addEmptyRequestHeader() {
        if (self.model.requestHeaders.length > 0) {
            var lastParameterAdded = self.model.requestHeaders[self.model.requestHeaders.length - 1];

            if (typeof (lastParameterAdded) !== 'object' || isEmpty(lastParameterAdded.value)) {
                return;
            }
        }
        self.model.requestHeaders.push({key: "CONTENT_TYPE", value: ""});
    }

    function deleteRequestHeaderFromList(index) {
        self.model.requestHeaders.splice(index, 1);
    }

    function addEmptyRequestParameter() {
        if (self.model.requestBodyParameters.length > 0) {
            var lastParameterAdded = self.model.requestBodyParameters[self.model.requestBodyParameters.length - 1];

            if (typeof (lastParameterAdded) !== 'object' || isEmpty(lastParameterAdded.key)) {
                return;
            }
        }
        self.model.requestBodyParameters.push({key: "", value: ""});
    }

    function deleteRequestParameterFromList(index) {
        self.model.requestBodyParameters.splice(index, 1);
    }

    function testRequest() {
        self.isTestInProgress = true;
        self.showSuccessRequestIcon = false;
        self.showBadRequestIcon = false;
        self.requestError = '';

        scheduleFormRESTService.testRequest(
                self.model.requestUrl,
                self.model.requestMethod,
                arrayUtils.keyValueArrayToObject(self.model.requestHeaders),
                arrayUtils.keyValueArrayToObject(self.model.requestBodyParameters),
                getCurrentExpectedResult(),
                function onSuccess(response) {
                    self.isTestInProgress = false;
                    if (response.statusCode === 200) {
                        self.showSuccessRequestIcon = true;
                    }else{
                        self.showBadRequestIcon = true;
                        self.requestError = response.errorMessage;
                    }
                },
                function onError(responseMessage) {
                    self.isTestInProgress = false;
                }
        );
    }
    
    function save(){
        var data = angular.copy(self.model);
        
        data.requestHeaders = arrayUtils.keyValueArrayToObject(self.model.requestHeaders);
        data.requestBodyParameters = arrayUtils.keyValueArrayToObject(self.model.requestBodyParameters);
        data.expectedResult = getCurrentExpectedResult();
        data.initDate = self.model.initDate.toString();
        data.endDate = self.model.endDate === null || self.model.endDate.toString() === 'Invalid Date' ? '' : self.model.endDate.toString();
        
        data.emailAlertList = self.model.emailAlertList.map(function(item){
            return item.text;
        });
        
        scheduleFormRESTService.save(data, function(response){
            self.show = false;
            if(self.model.uniqueId === undefined){
                schedulesListService.list.push(response);
            }else{
                schedulesListService.list = schedulesListService.list.map(function(item){
                    if(item.uniqueId === self.model.uniqueId){
                        return response;
                    }
                    return item;
                });
            }
        });
    }
    
    function getCurrentExpectedResult(){
        return (self.expectedResultOption === 'HTTP_200') ? self.expectedResultOption : self.expectedResultCustom;
    }
     
    function isEmpty(value) {
        return value === undefined || value === "" || value === null;
    }
}