package org.monitoring.restcontrollers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import org.fakebuilder.api.FakeBuilder;
import org.monitoring.entities.Schedule;
import org.monitoring.helpers.RESTRequestHelper;
import org.monitoring.helpers.RESTRequestHelper.HTTP_REQUEST_HEADER_NAME;
import org.monitoring.helpers.RESTRequestHelper.HTTP_REQUEST_HEADER_VALUE;
import org.monitoring.helpers.RESTRequestHelper.HTTP_REQUEST_METHOD;
import org.monitoring.mappers.SchedulesMapper;
import org.monitoring.models.AdminSchedulesListItemModel;
import org.monitoring.repositories.ScheduleRepository;
import org.monitoring.structures.ResponseError;
import org.monitoring.utils.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules/")
public class SchedulesRESTController {

    private final FakeBuilder fakeBuilder = new FakeBuilder();
    private final ScheduleRepository repository;
    private final int INTERNAL_ERROR_CODE = 500;

    public SchedulesRESTController() throws NamingException {
        this.repository = new ScheduleRepository();
    }

    @RequestMapping(value = "/admin/getAll", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        try {
            //List<Schedule> originalList = fakeBuilder.createList(Schedule.class).ofSize(10).all().build();
            List<Schedule> originalList = this.repository.findAll();
            List<AdminSchedulesListItemModel> list = SchedulesMapper.INSTANCE.map_ListSchedule_To_ListAdminSchedulesListItemModel(originalList);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseError(this.INTERNAL_ERROR_CODE, ex.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/admin/get", method = RequestMethod.GET)
    public ResponseEntity<?> getByUniqueId(
            @RequestParam(value = "uniqueId") String uniqueId
    ) {
        try {
            return new ResponseEntity<>(this.repository.findById(uniqueId), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseError(this.INTERNAL_ERROR_CODE, ex.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public class testRequestDTO {
        public String url;
        public HTTP_REQUEST_METHOD requestMethod;
        public HashMap<HTTP_REQUEST_HEADER_NAME, HTTP_REQUEST_HEADER_VALUE> requestHeaders;
        public String requestBodyType;
        public HashMap<String, String> requestBodyParameters;
        public String expectedResult;
    }

    public class testRequestModel {
        public int statusCode;
        public String errorMessage;
    }

    @RequestMapping(value = "/admin/testRequest", method = RequestMethod.POST)
    public ResponseEntity<?> testRequest(
            @RequestBody testRequestDTO dto
    ) {
        testRequestModel result = new testRequestModel();
        if (dto.expectedResult == null) {
            dto.expectedResult = "";
        }

        try {
            HashMap<String, String> headers = new HashMap<>();
            for(Map.Entry<HTTP_REQUEST_HEADER_NAME, HTTP_REQUEST_HEADER_VALUE> entry: dto.requestHeaders.entrySet()){
                headers.put(entry.getKey().getName(), entry.getValue().getName());
            }
            String resultRawRequest = new RESTRequestHelper().doRequest(dto.url, dto.requestMethod, headers, dto.requestBodyParameters);

            if (dto.expectedResult.toLowerCase().equals("http_200") || resultRawRequest.toLowerCase().equals(dto.expectedResult.toLowerCase())) {
                result.statusCode = 200;
            } else {
                result.errorMessage = "Expected result is not equals to '" + resultRawRequest + "'";
                result.statusCode = 500;
            }
        } catch (Exception ex) {
            //TODO: log exception
            result.errorMessage = ex.toString();
            result.statusCode = 500;
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

        
    public class SaveDTO {
        public String uniqueId;
        public String name;
        public boolean isActive;
        public String initDate;
        public String endDate;
        public int frecuency;//minutes
        public String requestUrl;
        public String requestMethod;
        public HashMap<HTTP_REQUEST_HEADER_NAME, HTTP_REQUEST_HEADER_VALUE> requestHeaders;
        public HashMap<String, String> requestBodyParameters;
        public String expectedResult;
        public List<String> emailAlertList;
    }
    
    @RequestMapping(value = "/admin/save", method = RequestMethod.POST)
    public ResponseEntity<?> save(
            @RequestBody SaveDTO dto
    ) {
        try {
            Schedule mappedObj;
            
            if (StringUtils.INSTANCE.isNullOrEmpty(dto.uniqueId)) {
                dto.isActive = true;
                mappedObj = SchedulesMapper.INSTANCE.map_SaveDTO_To_Schedule(dto);
                this.repository.insert(mappedObj);
            } else {
                mappedObj = SchedulesMapper.INSTANCE.map_SaveDTO_To_Schedule(dto, this.repository.findById(dto.uniqueId));
                this.repository.updateById(dto.uniqueId, mappedObj);
            }
            
            return new ResponseEntity<>(SchedulesMapper.INSTANCE.map_Schedule_To_AdminSchedulesListItemModel(mappedObj), HttpStatus.OK);
        } catch (Exception ex) {
            //TODO:log full exception
            return new ResponseEntity<>(new ResponseError(this.INTERNAL_ERROR_CODE, ex.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public class ChangeStatusDTO{
        public String uniqueId;
        public boolean newStatus;
    }
    
    @RequestMapping(value = "/admin/changeStatus", method = RequestMethod.POST)
    public ResponseEntity<?> changeStatus(
            @RequestBody ChangeStatusDTO dto
    ) {
        try {
            Schedule schedule = this.repository.findById(dto.uniqueId);
            if(schedule == null){
                throw new Exception("Registro especificado no existe");
            }
            schedule.setIsActive(dto.newStatus);
            this.repository.updateById(dto.uniqueId, schedule);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseError(this.INTERNAL_ERROR_CODE, ex.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public class DeleteDTO{
        public String uniqueId;
    }
    
    @RequestMapping(value = "/admin/delete", method = RequestMethod.POST)
    public ResponseEntity<?> deleteByUniqueId(
            @RequestBody DeleteDTO dto
    ) {
        try {
            if(this.repository.deleteById(dto.uniqueId) < 1){
                throw new Exception("Ocurrio un error interno al intentar eliminar el registro");
            }
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ResponseError(this.INTERNAL_ERROR_CODE, ex.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
