package com.example.workoutsample;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * パスワードをBCryptでエンコードするためのユーティリティクラスです。
 * このクラスは、平文のパスワードをエンコードしてコンソールに出力します。
 * <p>
 * 主に開発・デバッグ時に使用され、エンコードされたパスワードをアプリケーション設定やデータベースに保存する際に利用されます。
 */
public class BCryptGenerator {

    /**
     * エントリーポイント。2つの平文パスワードをBCryptでエンコードし、結果をコンソールに出力します。
     *
     * @param args コマンドライン引数（未使用）
     */
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 最初の平文パスワード
        String rawPassword = "userpass1";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Encoded password: " + encodedPassword);

        // 2つ目の平文パスワード
        String rawPassword2 = "adminpass1";
        String encodedPassword2 = encoder.encode(rawPassword2);
        System.out.println("Encoded password: " + encodedPassword2);
    }
}
