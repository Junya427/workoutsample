package com.example.workoutsample.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.workoutsample.dto.AuthorityDTO;
import com.example.workoutsample.dto.UserDTO;
import com.example.workoutsample.service.AuthorityService;
import com.example.workoutsample.service.UserService;

import jakarta.validation.Valid;

/**
 * 管理者向けのユーザー管理を行うコントローラークラスです。
 * ユーザーの追加、編集、削除、検索、表示などの機能を提供します。
 */
@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * ユーザー管理メニュー画面を表示します。
     *
     * @return ユーザー管理メニューのビュー名
     */
    @GetMapping
    public String selectMenu() {
        return "users/menu";
    }

    /**
     * ユーザー表示メニュー画面を表示します。
     *
     * @return ユーザー表示メニューのビュー名
     */
    @GetMapping("show")
    public String showUsersMenu() {
        return "users/show-menu";
    }

    /**
     * すべてのユーザー情報を取得して表示します。
     *
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return すべてのユーザー情報を表示するビュー名
     */
    @GetMapping("showAll")
    public String showAll(Model model) {
        List<UserDTO> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users/show-all";
    }

    /**
     * 新しいユーザーを追加するフォームを表示します。
     *
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return 新しいユーザー追加フォームのビュー名
     */
    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("authorities", authorityService.findAllAuthorities());
        return "users/add";
    }

    /**
     * 新しいユーザーを追加します。
     *
     * @param userDTO ユーザー情報を格納するデータ転送オブジェクト
     * @param result 入力データのバリデーション結果
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return 処理結果のビュー名
     */
    @PostMapping("/add")
    public String addUser(@Valid UserDTO userDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("authorities", authorityService.findAllAuthorities());
            return "users/add";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        String rawPassword = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        userDTO.setPassword(encodedPassword);
        userService.saveUser(userDTO, username);
        model.addAttribute("userDTO", userDTO);
        return "users/result";
    }

    /**
     * ユーザー情報の編集フォームを表示します。
     *
     * @param id 編集対象のユーザーID
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return ユーザー編集フォームのビュー名
     */
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        UserDTO userDTO = userService.findUserDTOById(id);
        List<AuthorityDTO> authorities = authorityService.findAllAuthorities();
        model.addAttribute("user", userDTO);
        model.addAttribute("authorities", authorities);
        return "/users/edit";
    }

    /**
     * ユーザー情報を更新します。
     *
     * @param userDTO ユーザー情報を格納するデータ転送オブジェクト
     * @param result 入力データのバリデーション結果
     * @return ユーザー一覧画面へのリダイレクトURL
     */
    @PostMapping("/edit/{id}")
    public String editUser(@Valid UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println("catch has Errors");
            return "/users/edit";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userService.updateUser(userDTO, username);
        return "redirect:/admin/users/showAll";
    }

    /**
     * 指定されたユーザーを削除します。
     *
     * @param id 削除対象のユーザーID
     * @return ユーザー一覧画面へのリダイレクトURL
     */
    @GetMapping("/delete/{id}")
    public String deleteUserForm(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userService.deleteUserById(id, username);
        return "redirect:/admin/users/showAll";
    }

    /**
     * 指定された条件でユーザーを検索し、結果を表示します。
     *
     * @param username ユーザー名でのフィルタリング条件（任意）
     * @param authorityId 権限IDでのフィルタリング条件（任意）
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return 検索結果を表示するビュー名
     */
    @GetMapping("/search")
    public String searchUsers(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "authorityId", required = false) Long authorityId,
            Model model) {
        List<UserDTO> users = userService.searchUsers(username, authorityId);
        model.addAttribute("users", users);

        List<AuthorityDTO> authorities = authorityService.findAllAuthorities();
        model.addAttribute("authorities", authorities);

        if (users.isEmpty()) {
            model.addAttribute("message", "該当するユーザーが見つかりませんでした。");
        }

        return "users/search";
    }
}
