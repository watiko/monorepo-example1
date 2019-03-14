package monorepo.repository

import monorepo.annotation.DomaRepository
import org.seasar.doma.Dao
import org.seasar.doma.Delete
import org.seasar.doma.Domain
import org.seasar.doma.Entity
import org.seasar.doma.GeneratedValue
import org.seasar.doma.GenerationType
import org.seasar.doma.Id
import org.seasar.doma.Insert
import org.seasar.doma.Select
import org.seasar.doma.Table
import org.seasar.doma.Update
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
