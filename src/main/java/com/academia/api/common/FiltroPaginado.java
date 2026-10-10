package com.academia.api.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Contrato compartilhado por todos os DTOs de filtro paginado.
 * Centraliza a lógica de montagem do {@link Pageable} e os valores-padrão
 * de paginação, eliminando ternários duplicados nos services e controllers.
 */
public interface FiltroPaginado {

    Integer paginaAtual();

    Integer tamanhoPagina();

    default Pageable toPageable() {
        int pagina = paginaAtual() != null && paginaAtual() >= 0 ? paginaAtual() : 0;
        int tamanho = tamanhoPagina() != null && tamanhoPagina() > 0 ? tamanhoPagina() : 10;
        return PageRequest.of(pagina, tamanho);
    }
}
