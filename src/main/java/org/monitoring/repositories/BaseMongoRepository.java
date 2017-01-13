package org.monitoring.repositories;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.util.List;
import org.bson.types.ObjectId;
import org.monitoring.entities.BaseMongoEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public abstract class BaseMongoRepository<T extends BaseMongoEntity> extends BaseRepository<T>{
    
    private final Class<T> classOfT;
    private final MongoClient mongo;
    private final MongoTemplate mongoTemplate;
    
    public BaseMongoRepository(Class<T> classOfT){
        System.out.println("Init mongo client with collection " + classOfT.getName());
        this.classOfT = classOfT;
        this.mongo = new MongoClient();
	mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(), "applicationmonitoring"));
    }
    
    public T insert(T record){
        mongoTemplate.insert(record);
        return record;
    }
    
    public T findById(String Id){
        return findById(new ObjectId(Id));
    }
    
    public T findById(ObjectId Id){
        return mongoTemplate.findById(Id, classOfT);
    }
    
    public List<T> findAll(){
        return mongoTemplate.findAll(classOfT);
    }
    
    public List<T> find(Query query){
        return mongoTemplate.find(query, classOfT);
    }
    
    public List<T> find(CompareFn compareFn){
        return asFunctionalList(mongoTemplate.findAll(classOfT)).find(compareFn);
    }
    
    public T updateById(String uniqueId, T record){
        return updateById(new ObjectId(uniqueId), record);
    }
    
    public T updateById(ObjectId _id, T record){
        Query query = new Query(where("_id").is(_id));       
        
        DBObject dbObject = new BasicDBObject();
        mongoTemplate.getConverter().write(record, dbObject);
        dbObject.removeField("_id");
        
        mongoTemplate.updateFirst(query, Update.fromDBObject(dbObject), classOfT);
        return record;
    }
    
    public int deleteById(String id){
        return deleteById(new ObjectId(id));
    }
    
    public int deleteById(ObjectId id){
        Query query = new Query(where("_id").is(id));
        return delete(query);
    }
    
    public int deleteAll(){
        return mongoTemplate.remove(new Query(), classOfT).getN();
    }
    
    public int delete(Query query){
        return mongoTemplate.remove(query, classOfT).getN();
    }
    
    @Override
    protected void finalize(){
        System.out.println("closing mongo connection");
        this.mongo.close();
    }
    
}
