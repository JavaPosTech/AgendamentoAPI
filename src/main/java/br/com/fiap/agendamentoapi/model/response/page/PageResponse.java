package br.com.fiap.agendamentoapi.model.response.page;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.function.Function;

@Schema(description = "Modelo de resposta paginada.")
public record PageResponse<T>(

        @Schema(description = "Lista de itens retornados na página.")
        List<T> content,

        @Schema(description = "Número da página atual. A numeração começa em 1.", example = "1")
        int page,

        @Schema(description = "Quantidade de itens por página.", example = "100")
        int size,

        @Schema(description = "Total de páginas disponíveis.", example = "1")
        int totalPages,

        @Schema(description = "Total de elementos encontrados.", example = "3")
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