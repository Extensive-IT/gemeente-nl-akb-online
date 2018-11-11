package gemeente.nlakbonline.repository;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import javax.sql.DataSource;

@Configuration
@EnableJdbcRepositories
public class RepositoryConfig {

    @Bean
    @ConfigurationProperties(prefix = "datasource.akb")
    public DataSource primaryAkbDataSource() {
        return DataSourceBuilder.create().build();
    }
}
