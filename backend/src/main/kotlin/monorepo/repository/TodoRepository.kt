package monorepo.repository

import monorepo.annotation.DomaRepository
import org.seasar.doma.*
import org.seasar.doma.experimental.Sql
import org.seasar.doma.jdbc.Result
import org.springframework.transaction.annotation.Transactional

@Domain(valueType = Int::class)
data class TodoId(val value: Int)

@Entity(immutable = true)
@Table(name = "todos")
data class Todo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: TodoId? = null,
    val title: String,
    val done: Boolean
)

@DomaRepository
@Dao
interface TodoRepository {
    @Transactional
    @Insert
    fun create(todo: Todo): Result<Todo>

    @Transactional
    @Delete
    fun delete(todo: Todo): Result<Todo>

    @Transactional
    @Sql("""
        update todos
        set done = /* done */false
        where id = /* id */0
    """)
    @Update
    fun updateTodoStatus(id: TodoId, done: Boolean): Int

    @Sql("select * from todos")
    @Select
    fun findAll(): List<Todo>

    @Sql("select * from todos where id = /* id */0")
    @Select
    fun findById(id: TodoId): Todo?
}
