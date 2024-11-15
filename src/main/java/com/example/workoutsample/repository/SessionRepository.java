package com.example.workoutsample.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.workoutsample.model.BodyPart;
import com.example.workoutsample.model.Session;
import com.example.workoutsample.model.User;

/**
 * {@link Session}エンティティに対するデータアクセス操作を提供するリポジトリインターフェースです。
 * Spring Data JPAを使用して、自動的にCRUD操作が実装されます。
 */
public interface SessionRepository extends JpaRepository<Session, Long> {

    /**
     * 指定されたユーザーと日付に基づいてセッションを検索します。
     *
     * @param loginUser ユーザー
     * @param date セッションの日付
     * @return ユーザーと日付に一致するセッションのリスト
     */
    List<Session> findByUserAndDate(User loginUser, LocalDate date);

    /**
     * 指定されたユーザーとトレーニング部位に基づいてセッションを検索します。
     *
     * @param loginUser ユーザー
     * @param bodyPart トレーニング部位
     * @return ユーザーとトレーニング部位に一致するセッションのリスト
     */
    List<Session> findByUserAndBodyPart(User loginUser, BodyPart bodyPart);

    /**
     * 指定されたユーザー、日付、およびトレーニング部位に基づいてセッションを検索します。
     *
     * @param loginUser ユーザー
     * @param date セッションの日付
     * @param bodyPart トレーニング部位
     * @return ユーザー、日付、およびトレーニング部位に一致するセッションのリスト
     */
    List<Session> findByUserAndDateAndBodyPart(User loginUser, LocalDate date, BodyPart bodyPart);

    /**
     * 指定されたユーザーに関連するすべてのセッションを検索します。
     *
     * @param user ユーザー
     * @return ユーザーに関連するセッションのリスト
     */
    List<Session> findByUser(User user);
}
