package br.com.erudio.controller

import io.github.resilience4j.bulkhead.annotation.Bulkhead
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("book-service")
class FooBarController {

    private val logger: Logger = LoggerFactory.getLogger(FooBarController::class.java)

    @GetMapping("/foo-bar")
    //@Retry(name = "foo-bar", fallbackMethod = "fallbackMethod")
    //@CircuitBreaker(name = "default", fallbackMethod = "fallbackMethod")
    //@RateLimiter(name = "default")
    @Bulkhead(name = "default")
    fun fooBar(): String? {

        logger.info("Request foo-bar is received")

//        var response = RestTemplate()
//            .getForEntity("http://localhost:8080/foo-bar",
//            String::class.java)
        return "foo-bar!!!"
        //return response.body
    }

    fun fallbackMethod(ex: Exception) : String {
        return "fallbackMethod Foo-bar!!!"
    }
}