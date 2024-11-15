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
    public String selectMenu() {
        return "users/menu";
    }

    @GetMapping("show")
    public String showUsersMenu() {
        return "users/show-menu";
    }

    @GetMapping("showAll")
    public String showAll(Model model) {
        List<UserDTO> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users/show-all";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("authorities", authorityService.findAllAuthorities());
        return "users/add";
    }

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

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        UserDTO userDTO = userService.findUserDTOById(id);
        List<AuthorityDTO> authorities = authorityService.findAllAuthorities();
        model.addAttribute("user", userDTO);
        model.addAttribute("authorities", authorities);
        return "/users/edit";
    }

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

    @GetMapping("/delete/{id}")
    public String deleteUserForm(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userService.deleteUserById(id, username);
        return "redirect:/admin/users/showAll";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam(value = "username", required = false) String username,
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
