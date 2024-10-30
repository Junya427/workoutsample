package com.example.workoutsample.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.workoutsample.model.OperationLog;
import com.example.workoutsample.service.OperationLogService;

@Controller
@RequestMapping("/admin/logs")
public class OperationLogController {
    
    @Autowired
    private OperationLogService operationLogService;

    @GetMapping
    public String showLogs(Model model) {
        List<OperationLog> logs = operationLogService.findAllLogs();
        model.addAttribute("logs", logs);

        return "logs/show-all";
    }
}
