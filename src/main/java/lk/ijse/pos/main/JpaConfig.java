package lk.ijse.pos.main;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
@Configuration
@PropertySource("file:${user.dir}\\resources\\application.properties")
@EnableTransactionManagement
public class JpaConfig {
    @Autowired
    private Environment env;
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource ds, JpaVendorAdapter jv)
    {
        LocalContainerEntityManagerFactoryBean lcemf = new LocalContainerEntityManagerFactoryBean();
        lcemf.setDataSource(ds);
        lcemf.setJpaVendorAdapter(jv);
        lcemf.setPackagesToScan("lk.ijse.pos.entity");
        return lcemf;
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dmds = new DriverManagerDataSource();
        dmds.setDriverClassName(env.getRequiredProperty("javax.persistence.jdbc.driver"));
        dmds.setUrl(env.getRequiredProperty("javax.persistence.jdbc.url"));
        dmds.setUsername(env.getRequiredProperty("javax.persistence.jdbc.user"));
        dmds.setPassword(env.getRequiredProperty("javax.persistence.jdbc.password"));
        return dmds;
    }
    @Bean
    public JpaVendorAdapter jpaVendorAdapter()
    {
        HibernateJpaVendorAdapter hjv = new HibernateJpaVendorAdapter();
        hjv.setDatabase(Database.MYSQL);
        hjv.setDatabasePlatform(env.getRequiredProperty("hibernate.dialect"));
        hjv.setShowSql(Boolean.getBoolean(env.getRequiredProperty("hibernate.show_sql")));
        hjv.setGenerateDdl(env.getRequiredProperty("hibernate.hbm2ddl.auto").equalsIgnoreCase("update")?true:false);
        return hjv;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory em)
    {
        return new JpaTransactionManager(em);
    }
}
