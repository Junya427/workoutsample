package com.example.workoutsample.controller.user;

import java.time.LocalDate;
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

import com.example.workoutsample.dto.BodyPartDTO;
import com.example.workoutsample.dto.ExerciseDTO;
import com.example.workoutsample.dto.SessionDTO;
import com.example.workoutsample.model.Session;
import com.example.workoutsample.model.User;
import com.example.workoutsample.service.BodyPartService;
import com.example.workoutsample.service.CustomUserDetailsService;
import com.example.workoutsample.service.ExerciseService;
import com.example.workoutsample.service.SessionService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

/**
 * ユーザー向けのトレーニングセッションを管理するコントローラークラスです。
 * セッションの表示、追加、編集、削除、検索、詳細表示、結果表示の機能を提供します。
 */
@Controller
@RequestMapping("/user/sessions")
public class SessionController {

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private BodyPartService bodyPartService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * アプリのメニュー画面を表示します。
     *
     * @return アプリメニューのビュー名
     */
    @GetMapping
    public String selectMenu() {
        return "sessions/menu";
    }

    /**
     * トレーニングセッション表示メニュー画面を表示します。
     *
     * @return トレーニングセッション表示メニューのビュー名
     */
    @GetMapping("showSessions")
    public String showSessions() {
        return "sessions/show-menu";
    }

    /**
     * ログイン中のユーザーに関連するすべてのトレーニングセッションを取得し、表示します。
     *
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return トレーニングセッション一覧表示のビュー名
     */
    @GetMapping("showAll")
    public String showAll(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loginUser = customUserDetailsService.findUserByUsername(username);

        List<SessionDTO> sessionDTOs = sessionService.findByUser(loginUser);
        model.addAttribute("sessions", sessionDTOs);
        return "sessions/show-all";
    }

    /**
     * 特定のトレーニングセッションの詳細を表示します。
     *
     * @param id トレーニングセッションID
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return トレーニングセッション詳細表示のビュー名
     */
    @GetMapping("details/{id}")
    public String showExercises(@PathVariable("id") Long id, Model model) {
        SessionDTO sessionDTO = sessionService.findSessionDTOById(id)
            .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + id));

        List<ExerciseDTO> sortedExercisesDTO = sessionService.getSortedExercisesDTOBySessionId(id);
        int totalVolume = sessionService.calculateTotalVolume(id);

        model.addAttribute("trainingSession", sessionDTO);
        model.addAttribute("totalVolume", totalVolume);
        model.addAttribute("sortedExercises", sortedExercisesDTO);
        return "sessions/details";
    }

    /**
     * トレーニングセッションを追加するフォームを表示します。
     *
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return トレーニングセッション追加フォームのビュー名
     */
    @GetMapping("/add")
    public String addSessionForm(Model model) {
        List<BodyPartDTO> bodyPartsDTO = bodyPartService.findAllBodyPartDTOs();
        model.addAttribute("bodyParts", bodyPartsDTO);
        model.addAttribute("sessionDTO", new SessionDTO());
        return "sessions/add-session";
    }

    /**
     * トレーニングセッションを追加します。
     *
     * @param sessionDTO トレーニングセッションのデータ転送オブジェクト
     * @param result 入力データのバリデーション結果
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return トレーニングセッション追加後のリダイレクトURL
     */
    @PostMapping("/add")
    public String addSession(@Valid SessionDTO sessionDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<BodyPartDTO> bodyPartDTOs = bodyPartService.findAllBodyPartDTOs();
            model.addAttribute("bodyParts", bodyPartDTOs);
            model.addAttribute("sessionDTO", sessionDTO);
            return "sessions/add-session";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loginUser = customUserDetailsService.findUserByUsername(username);

        sessionDTO.setUserId(loginUser.getId());
        Session session = sessionService.saveSession(sessionDTO, username);
        return "redirect:/user/exercises/" + session.getId() + "/add";
    }

    /**
     * トレーニングセッションを編集するフォームを表示します。
     *
     * @param id トレーニングセッションID
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return トレーニングセッション編集フォームのビュー名
     */
    @GetMapping("/edit/{id}")
    public String editSessionForm(@PathVariable("id") Long id, Model model) {
        SessionDTO sessionDTO = sessionService.findSessionDTOById(id)
            .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + id));

        List<BodyPartDTO> bodyPartDTOs = bodyPartService.findAllBodyPartDTOs();
        model.addAttribute("bodyParts", bodyPartDTOs);
        model.addAttribute("sessionDTO", sessionDTO);
        return "/sessions/edit";
    }

    /**
     * トレーニングセッションを編集します。
     *
     * @param sessionDTO トレーニングセッションのデータ転送オブジェクト
     * @param result 入力データのバリデーション結果
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return 編集後のリダイレクトURL
     */
    @PostMapping("/edit")
    public String editSession(@Valid SessionDTO sessionDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<BodyPartDTO> bodyPartDTOs = bodyPartService.findAllBodyPartDTOs();
            model.addAttribute("bodyParts", bodyPartDTOs);
            return "sessions/edit";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loginUser = customUserDetailsService.findUserByUsername(username);

        sessionDTO.setUserId(loginUser.getId());
        sessionService.updateSession(sessionDTO, username);
        return "redirect:/user/sessions";
    }

    /**
     * トレーニングセッションを削除します。
     *
     * @param id トレーニングセッションID
     * @return 削除後のリダイレクトURL
     */
    @GetMapping("/delete/{id}")
    public String deletExerciseForm(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        SessionDTO sessionDTO = sessionService.findSessionDTOById(id)
            .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + id));
        exerciseService.deleteExerciseBySession(sessionDTO, username);
        sessionService.deleteSessionById(id, username);

        return "redirect:/user/sessions/showAll";
    }

    /**
     * トレーニングセッションを検索します。
     *
     * @param date 検索条件の日付（任意）
     * @param bodyPartId 検索条件の部位ID（任意）
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return 検索結果のビュー名
     */
    @GetMapping("/search")
    public String searchSessions(
        @RequestParam(required = false) LocalDate date,
        @RequestParam(required = false) Long bodyPartId,
        Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loginUser = customUserDetailsService.findUserByUsername(username);

        if (date != null && bodyPartId != null) {
            List<SessionDTO> sessionDTOs = sessionService.findByUserAndDateAndBodyPart(loginUser, date, bodyPartId);
            model.addAttribute("sessionDTOs", sessionDTOs);
        } else if (date != null) {
            List<SessionDTO> sessionDTOs = sessionService.findByUserAndDate(loginUser, date);
            model.addAttribute("sessionDTOs", sessionDTOs);
        } else if (bodyPartId != null) {
            List<SessionDTO> sessionDTOs = sessionService.findByUserAndBodyPart(loginUser, bodyPartId);
            model.addAttribute("sessionDTOs", sessionDTOs);
        }

        List<BodyPartDTO> bodyPartDTOs = bodyPartService.findAllBodyPartDTOs();
        model.addAttribute("bodyParts", bodyPartDTOs);

        return "sessions/search";
    }

    /**
     * トレーニングセッションの結果を表示します。
     *
     * @param id トレーニングセッションID
     * @param model ビューに渡すデータを格納する {@link Model} オブジェクト
     * @return トレーニング結果のビュー名
     */
    @GetMapping("/{sessionId}/result")
    public String result(@PathVariable("sessionId") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loginUser = customUserDetailsService.findUserByUsername(username);

        SessionDTO sessionDTO = sessionService.findSessionDTOById(id)
            .orElseThrow(() -> new EntityNotFoundException("Session not found with sessionId: " + id));

        if (sessionDTO.getExercises().isEmpty()) {
            ExerciseDTO exerciseDTO = new ExerciseDTO();
            exerciseDTO.setSessionId(id);
            String errorMessage = "エクササイズを追加してください";
            model.addAttribute("exerciseDTO", exerciseDTO);
            model.addAttribute("errorMessage", errorMessage);
            return "exercises/add-exercise";
        }

        int totalVolume = sessionService.calculateTotalVolume(id);
        String message = sessionService.comparisonTrainingVolume(loginUser, sessionDTO, totalVolume);

        model.addAttribute("trainingSession", sessionDTO);
        model.addAttribute("totalVolume", totalVolume);
        model.addAttribute("message", message);
        return "sessions/result";
    }
}
