package com.ansh;

import com.ansh.utils.IdentifierMasker;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${app.admin.email}")
  private String defaultEmail;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public void run(String... args) throws Exception {
    ClassPathResource resource = new ClassPathResource("init.sql");
    String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

    sql = sql.replace(":defaultEmail", defaultEmail);

    jdbcTemplate.execute(sql);
    LOG.debug("init default user SQL script executed with default email: {}", IdentifierMasker.maskEmail(defaultEmail));

    ClassPathResource resourceAnimals = new ClassPathResource("init_animals.sql");
    String sqlAnimals = StreamUtils.copyToString(resourceAnimals.getInputStream(), StandardCharsets.UTF_8);

    sqlAnimals = sqlAnimals.replace(":defaultEmail", defaultEmail);

    jdbcTemplate.execute(sqlAnimals);

    LOG.debug("init animals SQL script executed with default email: {}", IdentifierMasker.maskEmail(defaultEmail));
  }
}
