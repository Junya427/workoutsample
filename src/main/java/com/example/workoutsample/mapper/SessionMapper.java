package com.example.workoutsample.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.workoutsample.dto.BodyPartDTO;
import com.example.workoutsample.dto.ExerciseDTO;
import com.example.workoutsample.dto.SessionDTO;
import com.example.workoutsample.model.BodyPart;
import com.example.workoutsample.model.Exercise;
import com.example.workoutsample.model.Session;
import com.example.workoutsample.model.User;
import com.example.workoutsample.service.UserService;

@Component
public class SessionMapper {
    @Autowired
    private ExerciseMapper exerciseMapper;

    private final UserService userService;

    @Autowired
    public SessionMapper(UserService userService) {
        this.userService = userService;
    }

    
    public SessionDTO toDTO(Session session) {
        if (session == null) {
            return null;
        }
        BodyPartDTO bodyPartDTO = BodyPartMapper.toDTO(session.getBodyPart());
        List<ExerciseDTO> exercisesDTO = exerciseMapper.toDTOList(session.getExercises());

        return new SessionDTO(
            session.getId(),
            session.getDate(),
            exercisesDTO,
            session.getUser().getId(),
            bodyPartDTO,
            false);
    }

    public List<SessionDTO> toDTOList(List<Session> sessions) {
        return sessions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Session toEntity(SessionDTO sessionDTO) {
        if (sessionDTO == null) {
            return null;
        }
        BodyPart bodyPart = BodyPartMapper.toEntity(sessionDTO.getBodyPart());
        System.out.println("------------");
        System.out.println("ユーザーID：" + sessionDTO.getUserId());
        System.out.println("------------");
        // ユーザーを取得
        User user = userService.findUserById(sessionDTO.getUserId());
    
        // エクササイズリストをセッションを渡して生成
        List<Exercise> exercises = exerciseMapper.toEntityList(sessionDTO.getExercises(), null);
    
        return new Session(
            sessionDTO.getId(),
            sessionDTO.getDate(),
            bodyPart,
            user,
            exercises
        );
    }
    
}
