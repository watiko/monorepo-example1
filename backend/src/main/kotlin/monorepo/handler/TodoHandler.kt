package monorepo.handler

import am.ik.yavi.core.Validator
import monorepo.repository.Todo
import monorepo.repository.TodoRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

data class CreateTodoReq(val title: String)

@Component
class TodoHandler(private val todoRepository: TodoRepository) {

    private val createReqValidator = Validator.builder<CreateTodoReq>()
        .constraint(CreateTodoReq::title, "title") { it.notEmpty() }
        .build()

    fun badRes(error: Map<String, Any>) = ServerResponse.badRequest().syncBody(error)
    fun <T : Any> ok(data: T) = ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .syncBody(data)

    fun addTodo(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono(CreateTodoReq::class.java)
            .map { createReq ->
                createReqValidator.validateToEither(createReq)
                    .leftMap { violations ->
                        mapOf(
                            "details" to violations.details()
                        )
                    }
            }
            .publishOn(Schedulers.elastic())
            .flatMap { either ->
                either.fold(
                    { badRes(it) },
                    { createReq ->
                        val (todo) = todoRepository.create(
                            Todo(
                                title = createReq.title,
                                done = false
                            )
                        )
                        ok(todo)
                    })
            }
    }

    fun listTodos(req: ServerRequest) = Mono
        .fromCallable {
            todoRepository.findAll()
        }
        .subscribeOn(Schedulers.elastic())
        .flatMap(::ok)
}
