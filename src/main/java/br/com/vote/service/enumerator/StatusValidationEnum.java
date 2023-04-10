package br.com.vote.service.enumerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@ToString
public enum StatusValidationEnum {
    VALID("ABLE_TO_VOTE"),
    INVALID("UNABLE_TO_VOTE");

    final String codigo;

    public static StatusValidationEnum toEnum(String codigo){
        return Arrays.stream(StatusValidationEnum.values())
                .filter(value -> value.codigo.equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(INVALID);
    }
}
