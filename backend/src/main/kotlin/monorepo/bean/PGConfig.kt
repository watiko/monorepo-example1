package monorepo.bean

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.dialect.Dialect
import org.seasar.doma.jdbc.dialect.PostgresDialect
import org.springframework.stereotype.Component
import org.springframework.context.annotation.Primary
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@Component
@ConfigurationProperties(prefix = "spring.datasource.postgres")
class PGConfig {

    lateinit var driverClassName: String
    lateinit var url: String
    lateinit var username: String
    lateinit var password: String

    private val lazyDataSource: DataSource by lazy {
        DataSourceBuilder
            .create()
            .driverClassName(driverClassName)
            .url(url)
            .username(username)
            .password(password)
            .build()
    }

    @Bean
    @Primary
    fun getDataSource(): DataSource = lazyDataSource

    @Bean
    fun pgDomaConfig() = object : Config {
        override fun getDataSource(): DataSource = TransactionAwareDataSourceProxy(lazyDataSource)
        override fun getDialect(): Dialect = PostgresDialect()
    }
}
