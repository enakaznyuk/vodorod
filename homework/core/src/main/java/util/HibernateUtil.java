package util;

import entity.Order;
import entity.Product;
import entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                    .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                    .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/store_db")
                    .setProperty("hibernate.connection.username", "postgres")
                    .setProperty("hibernate.connection.password", "root")
                    .setProperty("hibernate.hbm2ddl.auto", "update")
                    .setProperty("hibernate.show_sql", "true")
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Product.class)
                    .addAnnotatedClass(Order.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}