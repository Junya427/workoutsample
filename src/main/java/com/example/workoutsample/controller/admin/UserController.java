package com.example.workoutsample.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.workoutsample.model.Authority;
import com.example.workoutsample.model.User;
import com.example.workoutsample.service.AuthorityService;
//import com.example.workoutsample.model.Session;
import com.example.workoutsample.service.UserService;

import jakarta.validation.Valid;



@Controller
@RequestMapping("/admin/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping
    public String selectMenu(){
        return "users/menu";
    }

    @GetMapping("show")
    public String showUsersMenu(){
        return "users/show-menu";
    }

    @GetMapping("showAll")
    public String showAll(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users/show-all";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("authorities", authorityService.findAllAuthorities());
        return "users/add";
    }

    @PostMapping("/add")
    public String addUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "users/add";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // ログイン中のユーザー名を取得

        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        userService.saveUser(user, username);
        model.addAttribute("user", user);
        return "users/result";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findUserById(id);//.orElseThrow();//orElseThrow()はnullなら例外を投げてあったらget()してくれる。だから直接Bookで受け取れる
        List<Authority> authorities = authorityService.findAllAuthorities();
            model.addAttribute("user", user);
            model.addAttribute("authorities", authorities);
        return "/users/edit";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println("chatch has Errors");
            return "/users/edit";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // ログイン中のユーザー名を取得
        userService.updateUser(user, username);
        return "redirect:/admin/users/showAll";
    }

    @GetMapping("/delete/{id}")
    public String deletUserForm(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // ログイン中のユーザー名を取得
        userService.deleteUserById(id, username);
        return "redirect:/admin/users/showAll";
    }


    // 検索結果を表示
    @GetMapping("/search")
    public String searchUsers(@RequestParam(value = "username", required = false) String username,
                            @RequestParam(value = "authorityId", required = false) Long authorityId,
                            Model model) {

        List<User> users = userService.searchUsers(username, authorityId);
        // 検索結果をモデルに追加
        model.addAttribute("users", users);

        // 権限のリストを取得し、フォームに表示
        List<Authority> authorities = authorityService.findAllAuthorities();
        model.addAttribute("authorities", authorities);

        if (users.isEmpty()) {
            model.addAttribute("message", "該当するユーザーが見つかりませんでした。");
        }

        return "users/search";  // user_search.html へのパス
    }
}