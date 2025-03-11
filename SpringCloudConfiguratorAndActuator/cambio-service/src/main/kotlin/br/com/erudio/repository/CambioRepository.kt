package br.com.erudio.repository

import br.com.erudio.model.Cambio
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CambioRepository : JpaRepository<Cambio, Long?>{

    fun findByFromAndTo(from: String, to: String): Cambio?
}