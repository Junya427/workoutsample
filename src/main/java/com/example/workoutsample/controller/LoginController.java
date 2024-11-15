package com.example.workoutsample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ログイン処理を管理するコントローラークラスです。
 * ユーザーのログインページを表示する役割を持ちます。
 */
@Controller
public class LoginController {

    /**
     * ログイン画面を表示します。
     *
     * @return ログイン画面のビュー名
     */
    @GetMapping("/login")
    public String login() {
        return "login/login";
    }
}
