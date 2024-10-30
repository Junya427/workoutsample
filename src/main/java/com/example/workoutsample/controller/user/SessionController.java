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
import com.example.workoutsample.mapper.SessionMapper;
import com.example.workoutsample.model.Session;
import com.example.workoutsample.model.User;
import com.example.workoutsample.service.BodyPartService;
import com.example.workoutsample.service.CustomUserDetailsService;
import com.example.workoutsample.service.ExerciseService;
import com.example.workoutsample.service.SessionService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;



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

    @Autowired
    private SessionMapper sessionMapper;

    @GetMapping
    public String selectMenu(){
        return "sessions/menu";
    }

    @GetMapping("showSessions")
    public String showSessions(){
        return "sessions/show-menu";
    }

    @GetMapping("showAll")
    public String showAll(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // ログイン中のユーザー名を取得
    
        // カスタムUserDetailsServiceからユーザー情報を取得
        User loginUser = customUserDetailsService.findUserByUsername(username);

        // ログインユーザーに関連するトレーニングセッションを取得
        List<Session> sessions = sessionService.findByUser(loginUser);

        List<SessionDTO> sessionsDTO = sessionMapper.toDTOList(sessions);
        model.addAttribute("sessions", sessionsDTO);
        return "sessions/show-all";
    }

    @GetMapping("details/{id}")
    public String showExercises(@PathVariable("id") Long id, Model model) {
        SessionDTO sessionDTO = sessionService.findSessionDTOById(id)
            .orElseThrow(() -> new EntityNotFoundException("Exercise not found with id: " + id));

        List<ExerciseDTO> sortedExercisesDTO = sessionService.getSortedExercisesDTOBySessionId(id);
        
        int totalVolume = sessionService.calculateTotalVolume(id);
    
        model.addAttribute("trainingSession", sessionDTO);
        model.addAttribute("totalVolume", totalVolume);
        model.addAttribute("sortedExercises", sortedExercisesDTO);
        return "sessions/details";
    }

    @GetMapping("/add")
    public String addSessionForm(Model model) {
        List<BodyPartDTO> bodyPartsDTO = bodyPartService.findAllBodyPartDTOs();
        model.addAttribute("bodyParts", bodyPartsDTO);
        model.addAttribute("sessionDTO", new SessionDTO());
        return "sessions/add-session";
    }

    @PostMapping("/add")
    public String addSession(@Valid SessionDTO sessionDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            List<BodyPartDTO> bodyPartDTOs = bodyPartService.findAllBodyPartDTOs();
            model.addAttribute("bodyParts", bodyPartDTOs);
            model.addAttribute("sessionDTO", sessionDTO);
            return "sessions/add-session";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // ログイン中のユーザー名を取得
    
        // カスタムUserDetailsServiceからユーザー情報を取得
        User loginUser = customUserDetailsService.findUserByUsername(username);

        sessionDTO.setUserId(loginUser.getId());
        System.out.println("------------");
        System.out.println("ユーザーID：" + sessionDTO.getUserId());
        System.out.println("------------");
        Session session = sessionService.saveSession(sessionDTO, username);
        return "redirect:/user/exercises/" + session.getId() + "/add"; // 成功時にリダイレクト
    }

    @GetMapping("/edit/{id}")
    public String editSessionForm(@PathVariable("id") Long id, Model model) {
        SessionDTO sessionDTO = sessionService.findSessionDTOById(id)
            .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + id));//.orElseThrow();//orElseThrow()はnullなら例外を投げてあったらget()してくれる。
        List<BodyPartDTO> bodyPartDTOs = bodyPartService.findAllBodyPartDTOs();
        model.addAttribute("bodyParts", bodyPartDTOs);
        model.addAttribute("sessionDTO", sessionDTO);
        return "/sessions/edit";
    }

    @PostMapping("/edit")
    public String editSession(@Valid SessionDTO sessionDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<BodyPartDTO> bodyPartDTOs = bodyPartService.findAllBodyPartDTOs();
            model.addAttribute("bodyParts", bodyPartDTOs);
            return "sessions/edit";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // ログイン中のユーザー名を取得
    
        // カスタムUserDetailsServiceからユーザー情報を取得
        User loginUser = customUserDetailsService.findUserByUsername(username);

        sessionDTO.setUserId(loginUser.getId());

        sessionService.updateSession(sessionDTO, username);
        return "redirect:/user/sessions";
    }

    @GetMapping("/delete/{id}")
    public String deletExerciseForm(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // ログイン中のユーザー名を取得

        Session session = sessionService.findSessionById(id)
            .orElseThrow(() -> new EntityNotFoundException("Exercise not found with id: " + id));
        exerciseService.deleteExerciseBySession(session, username);
        sessionService.deleteSessionById(id, username);

        return "redirect:/user/sessions/showAll";
    }

    @GetMapping("/search")
    public String searchSessions(
        @RequestParam(required = false) LocalDate date,
        @RequestParam(required = false) Long bodyPartId,
        Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // ログイン中のユーザー名を取得
        
        // カスタムUserDetailsServiceからユーザー情報を取得
        User loginUser = customUserDetailsService.findUserByUsername(username);


        // 検索条件に応じてセッションをフィルタリング
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
        // ジャンルリストをモデルに追加
        List<BodyPartDTO> bodyPartDTOs = bodyPartService.findAllBodyPartDTOs();
        model.addAttribute("bodyParts", bodyPartDTOs);

        return "sessions/search";
    }

    @GetMapping("/{sessionId}/result")
    public String result(@PathVariable("sessionId") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // ログイン中のユーザー名を取得
        
        // カスタムUserDetailsServiceからユーザー情報を取得
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
