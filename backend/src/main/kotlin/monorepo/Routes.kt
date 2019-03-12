package monorepo

import monorepo.handler.TodoHandler
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.router

@Component
class Routes(private val todoHandler: TodoHandler) {
    @Bean
    fun router() = router {
        "/api".nest {
            "/todos".nest {
                accept(MediaType.APPLICATION_JSON_UTF8).nest {
                    GET("", todoHandler::listTodos)
                    POST("", todoHandler::addTodo)
                }
            }
        }
    }
}
