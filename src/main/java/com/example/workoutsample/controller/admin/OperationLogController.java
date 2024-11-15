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

/**
 * 管理者向けの操作ログを管理するコントローラークラスです。
 * 操作ログの一覧表示や検索機能を提供します。
 */
@Controller
@RequestMapping("/admin/logs")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 操作ログ管理メニュー画面を表示します。
     *
     * @return 操作ログ管理メニューのビュー名
     */
    @GetMapping
    public String selectMenu() {
        return "logs/show-menu";
    }

    /**
     * すべての操作ログを取得して表示します。
     *
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return 操作ログ一覧を表示するビュー名
     */
    @GetMapping("/showAll")
    public String showLogs(Model model) {
        List<OperationLogDTO> logs = operationLogService.findAllLogs();
        model.addAttribute("logs", logs);

        return "logs/show-all";
    }

    /**
     * 指定された条件（ユーザー名、アクション、期間）で操作ログを検索し、結果を表示します。
     *
     * @param username ユーザー名でログをフィルタリングするためのパラメータ（任意）
     * @param action 操作内容でログをフィルタリングするためのパラメータ（任意）
     * @param startDate 検索範囲の開始日時（任意、ISO形式）
     * @param endDate 検索範囲の終了日時（任意、ISO形式）
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return 検索結果を表示するビュー名
     */
    @GetMapping("/search")
    public String searchLogs(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "startDate", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Model model) {
        List<OperationLogDTO> logs = operationLogService.searchLogs(username, action, startDate, endDate);
        model.addAttribute("logs", logs);
        return "logs/search";
    }
}
