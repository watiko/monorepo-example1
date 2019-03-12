package monorepo.repository

import monorepo.annotation.DomaRepository
import org.seasar.doma.Dao
import org.seasar.doma.Entity
import org.seasar.doma.GeneratedValue
import org.seasar.doma.GenerationType
import org.seasar.doma.Id
import org.seasar.doma.Insert
import org.seasar.doma.Select
import org.seasar.doma.Table
import org.seasar.doma.experimental.Sql
import org.seasar.doma.jdbc.Result
import org.springframework.transaction.annotation.Transactional

@Entity(immutable = true)
@Table(name = "todos")
data class Todo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    val title: String,
    val done: Boolean
)

@DomaRepository
@Dao
interface TodoRepository {
    @Transactional
    @Insert
    fun create(todo: Todo): Result<Todo>

    @Sql("select * from todos")
    @Select
    fun findAll(): List<Todo>
}
