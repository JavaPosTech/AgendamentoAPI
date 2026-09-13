package br.com.fiap.agendamentoapi.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        in = ParameterIn.QUERY,
        name = "page",
        description = "Número da página. A numeração começa em 1.",
        schema = @Schema(type = "integer", minimum = "1", defaultValue = "1"))
@Parameter(
        in = ParameterIn.QUERY,
        name = "size",
        description = "Quantidade de registros por página.",
        schema = @Schema(type = "integer", minimum = "1", defaultValue = "100"))
@Parameter(
        in = ParameterIn.QUERY,
        name = "sort",
        description = "Ordenação no formato campo,(asc|desc). Aceita mais de um critério. Quando omitida, ordena por id em ordem crescente.",
        array = @ArraySchema(schema = @Schema(type = "string", example = "id,asc")))
public @interface ParametrosPaginacao {
}