package br.com.fiap.agendamentoapi.model.response.page;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.function.Function;

@Schema(description = "Modelo de resposta paginada.")
public record PageResponse<T>(

        @Schema(description = "Lista de itens retornados na página")
        List<T> content,

        @Schema(description = "Número da página atual", example = "1")
        int page,

        @Schema(description = "Quantidade de itens por página")
        int size,

        @Schema(description = "Total de páginas disponíveis")
        int totalPages,

        @Schema(description = "Total de elementos encontrados")
        long totalElements

) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements());
    }

    public static <T, R> PageResponse<R> from(Page<T> page, Function<T, R> mapper) {
        return from(page.map(mapper));
    }
}