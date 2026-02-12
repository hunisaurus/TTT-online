package com.codecool.tttbackend.configuration;

import com.codecool.tttbackend.dao.GameDAO;
import com.codecool.tttbackend.dao.GameDAOJdbc;
import com.codecool.tttbackend.dao.UserDAO;
import com.codecool.tttbackend.dao.UserDAOJdbc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@SpringBootConfiguration
public class Configuration {

    @Value("${tttonline.database.url}")
    private String dataBaseUrl;

    @Value("${tttonline.database.username}")
    private String dataBaseUsername;

    @Value("${tttonline.database.password}")
    private String dataBasePassword;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(dataBaseUrl);
        dataSource.setUsername(dataBaseUsername);
        dataSource.setPassword(dataBasePassword);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public UserDAO userDAO(JdbcTemplate jdbcTemplate) {
        return new UserDAOJdbc(jdbcTemplate);
    }

    @Bean
    public GameDAO gameDAO(JdbcTemplate jdbcTemplate) {
        return new GameDAOJdbc(jdbcTemplate);
    }
}
