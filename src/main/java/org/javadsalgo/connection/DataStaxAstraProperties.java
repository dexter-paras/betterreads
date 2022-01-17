package org.javadsalgo.connection;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.File;

/*Bind file location denoted by prefix location to below secureConnectBundle object*/

//@ConfigurationProperties(prefix = "datastax.astra")
public class DataStaxAstraProperties {

    public File secureConnectBundle = new File("secure-connect.zip");

/*
    public File getSecureConnectBundle() {
        System.out.println("secureConnectBundle looks like "+ secureConnectBundle);
        return secureConnectBundle;
    }

    public void setSecureConnectBundle(File secureConnectBundle) {
        this.secureConnectBundle = secureConnectBundle;
    }
*/

}