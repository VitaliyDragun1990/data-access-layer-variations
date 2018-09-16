package com.revenat.serviceLayer.dataAPI_JdbcImpl.connectionManagerImpl;

public class ConnectionProperties {
    private final String databaseUrl;
    private final String databaseName;
    private final String login;
    private final String password;

    public ConnectionProperties(String databaseUrl, String databaseName, String login, String password) {
        this.databaseUrl = databaseUrl;
        this.databaseName = databaseName;
        this.login = login;
        this.password = password;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
