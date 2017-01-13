package org.monitoring.mappers;

import java.util.ArrayList;
import java.util.List;
import org.monitoring.entities.Schedule;
import org.monitoring.helpers.RESTRequestHelper.HTTP_REQUEST_METHOD;
import org.monitoring.models.AdminSchedulesListItemModel;
import org.monitoring.restcontrollers.SchedulesRESTController.SaveDTO;
import org.monitoring.utils.DateUtils;
import org.monitoring.utils.StringUtils;

public class SchedulesMapper {

    public static final SchedulesMapper INSTANCE = new SchedulesMapper();

    protected SchedulesMapper() {
    }

    public List<AdminSchedulesListItemModel> map_ListSchedule_To_ListAdminSchedulesListItemModel(List<Schedule> originalList) {
        List<AdminSchedulesListItemModel> generatedList = new ArrayList<>();
        for (Schedule item : originalList) {
            generatedList.add(map_Schedule_To_AdminSchedulesListItemModel(item));
        }
        return generatedList;
    }
    
    public AdminSchedulesListItemModel map_Schedule_To_AdminSchedulesListItemModel(Schedule originalItem) {
        AdminSchedulesListItemModel model = new AdminSchedulesListItemModel();
        model.uniqueId = originalItem.getUniqueId();
        model.name = originalItem.getName();
        model.isActive = originalItem.isActive();

        String applyDate = DateUtils.INSTANCE.toString(originalItem.getInitDate());
        if (originalItem.getEndDate() != null) {
            applyDate += " - " + DateUtils.INSTANCE.toString(originalItem.getEndDate());
        }
        model.applyDate = applyDate;

        model.frecuency = formatFrecuency(originalItem.getFrecuency());
        model.requestUrl = originalItem.getRequestUrl();
        model.requestMethod = originalItem.getRequestMethod().name();

        return model;
    }

    private String formatFrecuency(int frecuencyInMinutes) {
        int hours = (int) Math.floor(frecuencyInMinutes / 60);
        int minutes = frecuencyInMinutes - (hours * 60);

        String frecuency = "";
        if (hours > 0 && minutes < 1) {
            frecuency = hours + " hr";
        } else if (hours > 0 && minutes > 0) {
            frecuency = hours + " hr, " + minutes + " min";
        } else if (hours < 1 && minutes > 0) {
            frecuency = minutes + " min";
        }

        return frecuency;
    }

    public Schedule map_SaveDTO_To_Schedule(SaveDTO originalItem) {
        return map_SaveDTO_To_Schedule(originalItem, new Schedule());
    }

    public Schedule map_SaveDTO_To_Schedule(SaveDTO originalItem, Schedule schedule) {
        schedule.setName(originalItem.name);
        schedule.setIsActive(originalItem.isActive);
        schedule.setInitDate(DateUtils.INSTANCE.toDate(originalItem.initDate));

        if (!StringUtils.INSTANCE.isNullOrEmpty(originalItem.endDate)) {
            schedule.setEndDate(DateUtils.INSTANCE.toDate(originalItem.endDate));
        }

        schedule.setFrecuency(originalItem.frecuency);
        schedule.setRequestUrl(originalItem.requestUrl);
        schedule.setRequestMethod(HTTP_REQUEST_METHOD.valueOf(originalItem.requestMethod));
        schedule.setRequestHeaders(originalItem.requestHeaders);
        schedule.setRequestBodyParameters(originalItem.requestBodyParameters);
        schedule.setExpectedResult(originalItem.expectedResult);
        schedule.setEmailAlertList(originalItem.emailAlertList);

        return schedule;
    }
}
