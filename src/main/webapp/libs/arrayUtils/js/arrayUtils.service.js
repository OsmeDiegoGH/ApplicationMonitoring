module.factory('arrayUtils', arrayUtils);

function arrayUtils(){
    
    return {
        keyValueArrayToObject: keyValueArrayToObject,
        objectToKeyValueArray: objectToKeyValueArray
    };
    
    function keyValueArrayToObject(keyValueArray){
        var obj = {};
        for(var i = 0; i < keyValueArray.length; i++){
            obj[keyValueArray[i].key] = keyValueArray[i].value;
        }
        return obj;
    }
    
    function objectToKeyValueArray(obj){
        var keyValueArray = [];
        for (var key in obj) {
            if (obj.hasOwnProperty(key)) {
              keyValueArray.push({'key': key, value: obj[key]});
            }
        }
        return keyValueArray;
    }
}