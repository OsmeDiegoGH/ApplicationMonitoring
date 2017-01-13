package org.monitoring.entities;

import org.bson.types.ObjectId;

public abstract class BaseMongoEntity {
    protected ObjectId _id;
    protected String uniqueId; 
    
    public BaseMongoEntity(){
        this(new ObjectId());
    }
    
    public BaseMongoEntity(ObjectId _id){
        this._id = _id;
        this.uniqueId = _id.toString();
    }

    public ObjectId getId() {
        return _id;
    }
    
    public String getUniqueId() {
        return uniqueId;
    }
}
