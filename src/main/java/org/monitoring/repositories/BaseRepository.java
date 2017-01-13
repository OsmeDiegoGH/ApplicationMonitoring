package org.monitoring.repositories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class BaseRepository<T> implements IBaseRepository<T>{
    
    public static abstract class CompareFn<T>{
        public abstract boolean compare(T record);
    }
    
    public static class FunctionalList<T> extends ArrayList{
        public FunctionalList<T> find(CompareFn<T> compareFn){
            FunctionalList<T> result = new FunctionalList();
            Iterator<T> iterator = this.iterator();
            while (iterator.hasNext()){
                T item = iterator.next();
                if(compareFn.compare(item)){
                    result.add(item);
                }
            }        
            return result;
        }
    }
            
    public <T> FunctionalList<T> asFunctionalList(List<T> originalList){
        FunctionalList<T> result = new FunctionalList();
        for(T item: (ArrayList<T>)originalList){
            result.add(item);
        }
        return result;
    }
}
