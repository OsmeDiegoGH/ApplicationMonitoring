package org.monitoring.repositories;

import java.util.Date;
import java.util.List;
import org.monitoring.entities.Schedule;

public class ScheduleRepository extends BaseMongoRepository<Schedule>{
    
    public ScheduleRepository() {
        super(Schedule.class);
    }
    
    public List<Schedule> findActiveSchedules(){
        final Date currentDate = new Date();
        return find(new CompareFn<Schedule>(){
            @Override
            public boolean compare(Schedule record) {
                boolean isValidInitDate = record.getInitDate().compareTo(currentDate) >= 0;
                boolean isValidEndDate;
                if(record.getEndDate() == null){
                    isValidEndDate = true;
                }else{
                    isValidEndDate = record.getEndDate().compareTo(currentDate) <= 0;
                }
                return record.isActive() && isValidInitDate && isValidEndDate;
            }
        });
    }
    
    /*public List<Schedule> getAll() throws SQLException{
        HashMap<String, Integer> outputParameters = new HashMap<>();
        outputParameters.put("recordList", OracleTypes.CURSOR);
        
        HashMap<String, Object> result = callProcedure(Config.obtenerParametro("SP_GET_ALL_SCHEDULES"), null, outputParameters);
        
        List<HashMap<String, Object>> recordList = (List<HashMap<String, Object>>) result.get("recordList");
        
        List<Schedule> scheduleList = new ArrayList<>();
        
        for(HashMap<String, Object> map : recordList){
            scheduleList.add( new Schedule(map) );
        }
        
        return scheduleList;
    }
    
    public Schedule getById(int id) throws SQLException, RecordNotFoundException, MultiplesRecordsWithSameIDFoundException{
        HashMap<String, Object> inputParameters = new HashMap<>();
        inputParameters.put("id", id);
        
        HashMap<String, Integer> outputParameters = new HashMap<>();
        outputParameters.put("recordList", OracleTypes.CURSOR);
        
        HashMap<String, Object> result = callProcedure("SP_GET_SCHEDULE_BY_ID", inputParameters, outputParameters);
        
        List<HashMap<String, Object>> recordList = (List<HashMap<String, Object>>) result.get("recordList");
        
        if(recordList.isEmpty()){
            throw new RecordNotFoundException();
        }
        if(recordList.size() > 1){
            throw new  MultiplesRecordsWithSameIDFoundException();
        }
        
        return new Schedule(recordList.get(0));
    }*/
    
}
