# jdbc-client
mvn compile exec:java -Dexec.mainClass=jp.co.amazon.akanash.sample.JdbcClient -Dexec.args="\
        'jdbc:mysql:aws://xxxxxxxxxx.cluster-xxxxxxxxxxxx.ap-northeast-1.rds.amazonaws.com:3306'\
        'username'\
        'password'\
        'SELECT 1'\
        '1'\
    "