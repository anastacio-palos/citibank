package com.ibm.openpages.ext.ui.dao;

import java.util.Optional;

import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.COLON_SEPERATED_DELIMITER;
import static com.ibm.openpages.ext.tss.service.constants.CommonConstants.FOLDER_SEPARATROR;

public class DBConfig {

    private String name;

    private String host;

    private int port;

    private String userName;

    private String password;

    public String getConnString() {
        return connString;
    }

    public void setConnString(final String connString) {
        this.connString = connString;
    }

    private String url;

    private String connString;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getUrl(){
        if(this.url == null){
            StringBuilder dburl = new StringBuilder();
            dburl.append(this.connString);
            dburl.append(this.host + COLON_SEPERATED_DELIMITER + this.port);
            dburl.append(FOLDER_SEPARATROR + this.name);
            this.url = dburl.toString();
        }

        return this.url;
    }

    @Override
    public String toString() {

        System.out.println("password====" + this.password);
        return "DBConfig{" +
               "name='" + name + '\'' +
               ", host='" + host + '\'' +
               ", port=" + port +
               ", userName='" + userName + '\'' +
               ", password='" + Optional.ofNullable(password).map(v -> "Password length: "+password.length()).orElse("Password null") + '\'' +
               ", url='" + url + '\'' +
               ", connString='" + connString + '\'' +
               '}';
    }

}
