package br.com.erudio.controller

import br.com.erudio.model.Book
import br.com.erudio.model.Cambio
import br.com.erudio.proxy.CambioProxy
import br.com.erudio.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("book-service")
class BookController {

    @Autowired
    private lateinit var environment: Environment

    @Autowired
    private lateinit var repository: BookRepository

    @Autowired
    private lateinit var proxy: CambioProxy

    @GetMapping(value = ["/{id}/{currency}"])
    fun findBook(
        @PathVariable("id") id: Long,
        @PathVariable("currency") currency: String
    ): Book? {
        val book = repository.findById(id)
            .orElseThrow{RuntimeException("Book Not found")}

        val cambio = proxy.getCambio(book.price, "USD", currency)

        val port = environment.getProperty("local.server.port")
        book.environment = "BOOK PORT $port CAMBIO PORT ${cambio!!.environment}"
        book.currency = currency
        book.price = cambio!!.convertedValue

        return book
    }

    @GetMapping(value = ["/v1/{id}/{currency}"])
    fun findBookV1(
        @PathVariable("id") id: Long,
        @PathVariable("currency") currency: String
    ): Book? {
        val book = repository.findById(id)
            .orElseThrow{RuntimeException("Book Not found")}

        val params = HashMap<String, String>()

        params["amount"] = book.price.toString()
        params["from"] = "USD"
        params["to"] = currency


        val response = RestTemplate()
            .getForEntity("http://localhost:8000/cambio-service/{amount}/{from}/{to}",
                Cambio::class.java,
                params
            )

        val cambio = response.body

        val port = environment.getProperty("local.server.port")
        book.environment = port
        book.currency = currency
        book.price = cambio!!.convertedValue

        return book
    }
}