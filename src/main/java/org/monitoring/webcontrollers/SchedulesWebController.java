package org.monitoring.webcontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/schedules")
public class SchedulesWebController {
    
    @RequestMapping(value="/admin", method = RequestMethod.GET)
    public ModelAndView adminView() {
        ModelAndView view = new ModelAndView("/modules/schedulesList/view");
        return view;
    }
    
}
