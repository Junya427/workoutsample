package com.example.workoutsample.controller.admin;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.workoutsample.dto.OperationLogDTO;
import com.example.workoutsample.service.OperationLogService;

@Controller
@RequestMapping("/admin/logs")
public class OperationLogController {
    
    @Autowired
    private OperationLogService operationLogService;

    @GetMapping
    public String selectMenu() {
        return "logs/show-menu";
    }

    @GetMapping("/showAll")
    public String showLogs(Model model) {
        List<OperationLogDTO> logs = operationLogService.findAllLogs();
        model.addAttribute("logs", logs);

        return "logs/show-all";
    }

    @GetMapping("/search")
    public String searchLogs(@RequestParam(value = "username", required = false) String username,
                        @RequestParam(value = "action", required = false) String action,
                        @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                        @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                        Model model) {
        List<OperationLogDTO> logs = operationLogService.searchLogs(username, action, startDate, endDate);
        model.addAttribute("logs", logs);
        return "logs/search";
    }
}
