package com.example.workoutsample.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.workoutsample.dto.ExerciseDTO;
import com.example.workoutsample.model.Exercise;
import com.example.workoutsample.model.Session;
import com.example.workoutsample.model.User;
import com.example.workoutsample.service.CustomUserDetailsService;
import com.example.workoutsample.service.ExerciseService;
import com.example.workoutsample.service.SessionService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/user/exercises")
public class ExerciseController {
    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @GetMapping
    public String selectMenu(){
        return "exercises/menu";
    }

    @GetMapping("showExercises")
    public String showExercises(){
        return "exercises/show-menu";
    }

    @GetMapping("showAll")
    public String showAll(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // ログイン中のユーザー名を取得
    
        // カスタムUserDetailsServiceからユーザー情報を取得
        User loginUser = customUserDetailsService.findUserByUsername(username);

        // ログインユーザーに関連するエクササイズを取得
        List<Exercise> exercises = exerciseService.findByUser(loginUser);
        System.out.println(exercises.size());
        model.addAttribute("exercises", exercises);
        return "exercises/show-all";
    }

    @GetMapping("/{sessionId}/add")
    public String addExerciseForm(@PathVariable("sessionId") Long sessionId, Model model) {
        //SessionDTO sessionDTO = sessionService.findSessionDTOById(sessionId).orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + sessionId));
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        exerciseDTO.setSessionId(sessionId);
        System.out.println("--------");
        System.out.println(sessionId);
        System.out.println(exerciseDTO.getSessionId());
        System.out.println("--------");
        model.addAttribute("exerciseDTO", exerciseDTO);
        return "exercises/add-exercise";
    }

    @PostMapping("/{sessionId}/add")
    public String addExercise(@PathVariable("sessionId") Long sessionId, @Valid ExerciseDTO exerciseDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // セッション情報を再設定
            exerciseDTO.setSessionId(sessionId);
            model.addAttribute("exerciseDTO", exerciseDTO);
            return "exercises/add-exercise";
        }
        Session session = sessionService.findSessionById(sessionId)
            .orElseThrow(() -> new EntityNotFoundException("Exercise not found with id: " + sessionId));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // ログイン中のユーザー名を取得
    
        // カスタムUserDetailsServiceからユーザー情報を取得
        User loginUser = customUserDetailsService.findUserByUsername(username);
        
        // ユーザーをエクササイズに設定
        exerciseDTO.setUserId(loginUser.getId());
        exerciseDTO.setSessionId(sessionId);
        exerciseService.comparePRs(loginUser, exerciseDTO);
        exerciseService.saveExercise(exerciseDTO, username, session);
        return "redirect:/user/exercises/" + sessionId + "/add";
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable("id") Long id, Model model) {
        Exercise exercise = exerciseService.findExerciseById(id)
            .orElseThrow(() -> new EntityNotFoundException("Exercise not found with id: " + id));//.orElseThrow();//orElseThrow()はnullなら例外を投げてあったらget()してくれる。だから直接Bookで受け取れる
        model.addAttribute("exercise", exercise);
        return "/exercises/edit";
    }

    @PostMapping("/edit/{id}")
    public String editExercise(@Valid Exercise exercise, BindingResult result) {
        if (result.hasErrors()) {
            return "/exercises/edit";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // ログイン中のユーザー名を取得
    
        // カスタムUserDetailsServiceからユーザー情報を取得
        User loginUser = customUserDetailsService.findUserByUsername(username);

        exercise.setUser(loginUser);

        exerciseService.updateExercise(exercise, username);
        return "redirect:/user/sessions/details/" + exercise.getSession().getId();
    }

    @GetMapping("/delete/{id}")
    public String deletExerciseForm(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // ログイン中のユーザー名を取得

        Exercise exercise = exerciseService.findExerciseById(id)
        .orElseThrow(() -> new EntityNotFoundException("Exercise not found with id: " + id));;
        exerciseService.deleteExerciseById(id, username);
        return "redirect:/user/sessions/details/" + exercise.getSession().getId();
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(name = "name", required = false) String name,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // ログイン中のユーザー名を取得
            
        // カスタムUserDetailsServiceからユーザー情報を取得
        User loginUser = customUserDetailsService.findUserByUsername(username);

        List<Exercise> exercises = exerciseService.findByUserAndName(loginUser, name).stream()
            .filter(exercise -> exercise.getUser().getId() == loginUser.getId())
            .toList();

        model.addAttribute("exercises", exercises);
        return "exercises/search";
    }
}
