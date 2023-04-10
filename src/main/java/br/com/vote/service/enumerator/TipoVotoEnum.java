package br.com.vote.service.enumerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@ToString
public enum TipoVotoEnum {

    SIM("sim"),
    NAO("nao");

    final String codigo;

    public static TipoVotoEnum toEnum(String codigo){
        return Arrays.stream(TipoVotoEnum.values())
                .filter(value -> value.codigo.equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(null);
    }
}
