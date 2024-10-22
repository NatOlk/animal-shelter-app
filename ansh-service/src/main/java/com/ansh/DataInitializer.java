package com.ansh;

import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
@Profile("init-user")
public class DataInitializer implements CommandLineRunner {

  private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public void run(String... args) throws Exception {
    ClassPathResource resource = new ClassPathResource("init.sql");
    String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

    jdbcTemplate.execute(sql);

    LOG.info("SQL script executed: init.sql");
  }
}
