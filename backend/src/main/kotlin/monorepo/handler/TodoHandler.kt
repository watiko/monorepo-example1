package monorepo.handler

import am.ik.yavi.core.Validator
import arrow.core.Left
import arrow.core.Option
import arrow.core.Right
import monorepo.repository.Todo
import monorepo.repository.TodoId
import monorepo.repository.TodoRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

data class CreateTodoReq(val title: String)
data class DeleteTodoReq(val id: TodoId)
data class UpdateTodoStatusReq(val id: TodoId, val done: Boolean)
data class FindTodoByIdReq(val id: TodoId)

@Component
class TodoHandler(private val todoRepository: TodoRepository) {

    private val createReqValidator = Validator.builder<CreateTodoReq>()
        .constraint(CreateTodoReq::title, "title") { it.notEmpty() }
        .build()

    fun badRes(error: Map<String, Any>) = ServerResponse.badRequest().syncBody(error)
    fun notFound() = ServerResponse.notFound().build()
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

    fun updateTodoStatus(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono(UpdateTodoStatusReq::class.java)
            .publishOn(Schedulers.elastic())
            .map { updateReq ->
                val count = todoRepository.updateTodoStatus(updateReq.id, updateReq.done)
                when (count) {
                    1 -> Right("update success")
                    else -> Left("update target not found")
                }
                    .mapLeft { mapOf("details" to it) }
            }
            .flatMap { either ->
                either.fold(::badRes, ::ok)
            }
    }

    fun deleteTodo(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono(DeleteTodoReq::class.java)
            .publishOn(Schedulers.elastic())
            .map { deleteReq ->
                // 後で考える
                val todo = todoRepository.findById(deleteReq.id)
                Option.fromNullable(todo)
                    .toEither { "todoId(${deleteReq.id}) not found" }
                    .mapLeft { mapOf("details" to it) }
                    .map(todoRepository::delete)
            }
            .flatMap { either ->
                either.fold({ notFound() }, ::ok)
            }
    }

    fun findTodoById(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono(FindTodoByIdReq::class.java)
            .publishOn(Schedulers.elastic())
            .map { findByIdReq ->
                // 後で考える
                val todo = todoRepository.findById(findByIdReq.id)
                Option.fromNullable(todo)
                    .toEither { "todoId(${findByIdReq.id}) not found" }
                    .mapLeft { mapOf("details" to it) }
            }
            .flatMap { either ->
                either.fold({ notFound() }, ::ok)
            }
    }

    fun listTodos(req: ServerRequest) = Mono
        .fromCallable {
            todoRepository.findAll()
        }
        .subscribeOn(Schedulers.elastic())
        .flatMap(::ok)
}
