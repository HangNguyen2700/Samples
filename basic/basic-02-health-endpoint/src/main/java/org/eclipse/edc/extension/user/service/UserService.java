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

package org.eclipse.edc.extension.user.service;

import org.eclipse.edc.extension.user.entity.Address;
import org.eclipse.edc.extension.user.entity.Company;
import org.eclipse.edc.extension.user.entity.Geo;
import org.eclipse.edc.extension.user.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private Connection connection;

    public UserService(Connection connection) {
        this.connection = connection;
    }

    public int persistUsersToDb(List<User> users) throws SQLException {
        int count = 0;
        for (User user : users) {
            count += persistUserToDb(user);
        }
        return count;
    }

    public int persistUserToDb(User user) throws SQLException {
        String sql = "INSERT INTO user (" +
                "id, full_name, username, email, street, suite, city, zipcode, lat, lng, phone, website, company_name, company_catch_phrase, company_bs" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, user.getId());
        statement.setString(2, user.getName());
        statement.setString(3, user.getUsername());
        statement.setString(4, user.getEmail());
        statement.setString(5, user.getAddress().getStreet());
        statement.setString(6, user.getAddress().getSuite());
        statement.setString(7, user.getAddress().getCity());
        statement.setString(8, user.getAddress().getZipcode());
        statement.setString(9, user.getAddress().getGeo().getLat());
        statement.setString(10, user.getAddress().getGeo().getLng());
        statement.setString(11, user.getPhone());
        statement.setString(12, user.getWebsite());
        statement.setString(13, user.getCompany().getName());
        statement.setString(14, user.getCompany().getCatchPhrase());
        statement.setString(15, user.getCompany().getBs());

        return statement.executeUpdate();
    }

    public List<User> getAllUsersFromDb() throws SQLException {
        String sql = "SELECT * FROM user";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet users = statement.executeQuery();

        List<User> result = new ArrayList<>();

        while (users.next()) {
            int id = users.getInt(1);
            String name = users.getString(2);
            String username = users.getString(3);
            String email = users.getString(4);
            String street = users.getString(5);
            String suite = users.getString(6);
            String city = users.getString(7);
            String zipcode = users.getString(8);
            String lat = users.getString(9);
            String lng = users.getString(10);
            String phone = users.getString(11);
            String website = users.getString(12);
            String companyName = users.getString(13);
            String companyCatchPhrase = users.getString(14);
            String companyBs = users.getString(15);

            Geo geo = new Geo(lat, lng);
            Address address = new Address(street, suite, city, zipcode, geo);
            Company company = new Company(companyName, companyCatchPhrase, companyBs);
            User user = new User(id, name, username, email, address, phone, website, company);

            result.add(user);
        }
        return result;
    }

}
