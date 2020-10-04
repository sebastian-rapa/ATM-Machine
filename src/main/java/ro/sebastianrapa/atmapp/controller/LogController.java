package ro.sebastianrapa.atmapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ro.sebastianrapa.atmapp.service.LogService;

@Controller
@RequestMapping("admin/log")
public class LogController {

    private transient final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/index")
    public ModelAndView index() {

        ModelAndView modelAndView = new ModelAndView("admin/log/index");
        modelAndView.addObject("logs", logService.fetchAllLogs());

        return modelAndView;
    }
}
