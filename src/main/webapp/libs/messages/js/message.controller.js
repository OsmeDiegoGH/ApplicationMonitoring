module.controller('messageController', messageController);

function messageController($element){    
    var self = this;
    
    self.close = close;
    
    function close(){
        $element.remove();
    }
}