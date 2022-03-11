package jp.co.amazon.akanash.sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class JdbcClient {

    public static void main(final String... args) {

        final Logger logger = Logger.getGlobal();

        final String url = args[0];
        final String username = args[1];
        final String password = args[2];
        final String sql = args[3];
        final Integer periodSeconds = Integer.valueOf(args[4]);

        final ThreadLocal<Connection> ref = ThreadLocal.withInitial(() -> {
            try {
                final Connection connection = DriverManager.getConnection(url, username, password);
                logger.info("A new connection is created.");
                return connection;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        final Runnable command = () -> {
            logger.info("---------- Task started.");
            try {
                final Connection connection = ref.get();
                connection.createStatement().execute(sql);
                logger.info("The SQL is executed. statement=\"" + sql + "\"");
            } catch (Exception e) {
                e.printStackTrace();
                ref.remove();
            } finally {
                logger.info("---------- Task finished.");
            }
        };

        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(command, 0, periodSeconds, TimeUnit.SECONDS);
    }
}