package com.example.workoutsample.mapper;

import com.example.workoutsample.dto.BodyPartDTO;
import com.example.workoutsample.model.BodyPart;

/**
 * BodyPartエンティティとBodyPartDTOの間の変換を行うマッパークラスです。
 * エンティティとDTO間のデータ変換ロジックを提供します。
 */
public class BodyPartMapper {

    /**
     * BodyPartエンティティをBodyPartDTOに変換します。
     *
     * @param bodyPart BodyPartエンティティ
     * @return BodyPartDTOオブジェクト
     */
    public static BodyPartDTO toDTO(BodyPart bodyPart) {
        if (bodyPart == null) {
            return null;
        }

        return new BodyPartDTO(
            bodyPart.getId(),
            bodyPart.getName()
        );
    }

    /**
     * BodyPartDTOをBodyPartエンティティに変換します。
     *
     * @param bodyPartDTO BodyPartDTOオブジェクト
     * @return BodyPartエンティティ
     */
    public static BodyPart toEntity(BodyPartDTO bodyPartDTO) {
        if (bodyPartDTO == null) {
            return null;
        }

        BodyPart bodyPart = new BodyPart();
        bodyPart.setId(bodyPartDTO.getId());
        bodyPart.setName(bodyPartDTO.getName());

        return bodyPart;
    }
}
