/*
 *  Copyright (c) 2021 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - Initial implementation
 *
 */

package org.eclipse.edc.extension.user.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class H2Connection {
    private Connection connection;

    public H2Connection() throws SQLException {
        this.connection = getNewConnection();
    }

    public void initDatabase() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS user (" +
                "id INT PRIMARY KEY," +
                "full_name VARCHAR(255)," +
                "username VARCHAR(255)," +
                "email VARCHAR(255)," +
                "street VARCHAR(255)," +
                "suite VARCHAR(255)," +
                "city VARCHAR(255)," +
                "zipcode VARCHAR(255)," +
                "lat VARCHAR(255)," +
                "lng VARCHAR(255)," +
                "phone VARCHAR(255)," +
                "website VARCHAR(255)," +
                "company_name VARCHAR(255), " +
                "company_catch_phrase VARCHAR(255)," +
                "company_bs VARCHAR(255))";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.executeUpdate();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private Connection getNewConnection() throws SQLException {
        String url = "jdbc:h2:mem:test";
        String user = "user";
        String password = "password";
        return DriverManager.getConnection(url, user, password);
    }
}
