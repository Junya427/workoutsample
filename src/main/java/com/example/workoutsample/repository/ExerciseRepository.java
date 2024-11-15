package com.example.workoutsample.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.workoutsample.model.Exercise;
import com.example.workoutsample.model.User;

/**
 * {@link Exercise}エンティティに対するデータアクセス操作を提供するリポジトリインターフェースです。
 * Spring Data JPAを使用して、自動的にCRUD操作が実装されます。
 */
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    /**
     * 指定されたユーザーとエクササイズ名に基づいてエクササイズを検索します。
     *
     * @param loginUser ユーザー
     * @param name エクササイズ名
     * @return ユーザーとエクササイズ名に一致するエクササイズのリスト
     */
    List<Exercise> findByUserAndName(User loginUser, String name);

    /**
     * 指定されたユーザーと、名前が部分一致するエクササイズを検索します。
     * 名前の一致は大文字小文字を区別しません。
     *
     * @param loginUser ユーザー
     * @param name 部分一致させたいエクササイズ名
     * @return ユーザーと名前が部分一致するエクササイズのリスト
     */
    List<Exercise> findByUserAndNameContainingIgnoreCase(User loginUser, String name);

    /**
     * 指定された挙上回数に基づいてエクササイズを検索します。
     *
     * @param reps 挙上回数
     * @return 指定された挙上回数に一致するエクササイズのリスト
     */
    List<Exercise> findByReps(Long reps);

    /**
     * 指定された重量に基づいてエクササイズを検索します。
     *
     * @param weight 重量
     * @return 指定された重量に一致するエクササイズのリスト
     */
    List<Exercise> findByWeight(Long weight);

    /**
     * 指定されたユーザーに関連するエクササイズを検索します。
     *
     * @param user ユーザー
     * @return ユーザーに関連するエクササイズのリスト
     */
    List<Exercise> findByUser(User user);
}

