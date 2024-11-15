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
import com.example.workoutsample.dto.SessionDTO;
import com.example.workoutsample.model.Exercise;
import com.example.workoutsample.model.User;
import com.example.workoutsample.service.CustomUserDetailsService;
import com.example.workoutsample.service.ExerciseService;
import com.example.workoutsample.service.SessionService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

/**
 * ユーザー向けのエクササイズ管理を行うコントローラークラスです。
 * ユーザーがエクササイズを追加、編集、削除、検索、表示するための機能を提供します。
 */
@Controller
@RequestMapping("/user/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * エクササイズメニュー画面を表示します。
     *
     * @return エクササイズメニューのビュー名
     */
    @GetMapping
    public String selectMenu() {
        return "exercises/menu";
    }

    /**
     * エクササイズの表示メニュー画面を表示します。
     *
     * @return エクササイズ表示メニューのビュー名
     */
    @GetMapping("showExercises")
    public String showExercises() {
        return "exercises/show-menu";
    }

    /**
     * ログイン中のユーザーに関連するすべてのエクササイズを取得し、表示します。
     *
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return エクササイズ一覧表示のビュー名
     */
    @GetMapping("showAll")
    public String showAll(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // ログイン中のユーザー名を取得
    
        User loginUser = customUserDetailsService.findUserByUsername(username);
        List<Exercise> exercises = exerciseService.findByUser(loginUser);
        model.addAttribute("exercises", exercises);
        return "exercises/show-all";
    }

    /**
     * 特定のセッションに関連付けられたエクササイズを追加するフォームを表示します。
     *
     * @param sessionId セッションID
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return エクササイズ追加フォームのビュー名
     */
    @GetMapping("/{sessionId}/add")
    public String addExerciseForm(@PathVariable("sessionId") Long sessionId, Model model) {
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        exerciseDTO.setSessionId(sessionId);
        model.addAttribute("exerciseDTO", exerciseDTO);
        return "exercises/add-exercise";
    }

    /**
     * エクササイズを追加します。
     *
     * @param sessionId セッションID
     * @param exerciseDTO エクササイズ情報を格納するデータ転送オブジェクト
     * @param result 入力データのバリデーション結果
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return エクササイズ追加後のリダイレクトURL
     */
    @PostMapping("/{sessionId}/add")
    public String addExercise(@PathVariable("sessionId") Long sessionId, @Valid ExerciseDTO exerciseDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            exerciseDTO.setSessionId(sessionId);
            model.addAttribute("exerciseDTO", exerciseDTO);
            return "exercises/add-exercise";
        }
        SessionDTO session = sessionService.findSessionDTOById(sessionId)
            .orElseThrow(() -> new EntityNotFoundException("Exercise not found with id: " + sessionId));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loginUser = customUserDetailsService.findUserByUsername(username);
        
        exerciseDTO.setUserId(loginUser.getId());
        exerciseDTO.setSessionId(sessionId);
        exerciseService.comparePRs(loginUser, exerciseDTO);
        exerciseService.saveExercise(exerciseDTO, username, session);
        return "redirect:/user/exercises/" + sessionId + "/add";
    }

    /**
     * 特定のエクササイズを編集するフォームを表示します。
     *
     * @param id エクササイズID
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return エクササイズ編集フォームのビュー名
     */
    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable("id") Long id, Model model) {
        Exercise exercise = exerciseService.findExerciseById(id)
            .orElseThrow(() -> new EntityNotFoundException("Exercise not found with id: " + id));
        model.addAttribute("exercise", exercise);
        return "/exercises/edit";
    }

    /**
     * エクササイズを編集します。
     *
     * @param exercise エクササイズのエンティティ
     * @param result 入力データのバリデーション結果
     * @return 編集後のリダイレクトURL
     */
    @PostMapping("/edit/{id}")
    public String editExercise(@Valid Exercise exercise, BindingResult result) {
        if (result.hasErrors()) {
            return "/exercises/edit";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loginUser = customUserDetailsService.findUserByUsername(username);

        exercise.setUser(loginUser);
        exerciseService.updateExercise(exercise, username);
        return "redirect:/user/sessions/details/" + exercise.getSession().getId();
    }

    /**
     * 特定のエクササイズを削除します。
     *
     * @param id エクササイズID
     * @return 削除後のリダイレクトURL
     */
    @GetMapping("/delete/{id}")
    public String deletExerciseForm(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Exercise exercise = exerciseService.findExerciseById(id)
            .orElseThrow(() -> new EntityNotFoundException("Exercise not found with id: " + id));
        exerciseService.deleteExerciseById(id, username);
        return "redirect:/user/sessions/details/" + exercise.getSession().getId();
    }

    /**
     * ユーザー名やエクササイズ名を基にエクササイズを検索します。
     *
     * @param name 検索条件としてのエクササイズ名（任意）
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return 検索結果のビュー名
     */
    @GetMapping("/search")
    public String search(
            @RequestParam(name = "name", required = false) String name,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loginUser = customUserDetailsService.findUserByUsername(username);

        List<Exercise> exercises = exerciseService.findByUserAndName(loginUser, name).stream()
            .filter(exercise -> exercise.getUser().getId() == loginUser.getId())
            .toList();

        model.addAttribute("exercises", exercises);
        return "exercises/search";
    }
}
