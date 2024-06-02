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

package org.eclipse.edc.extension.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.edc.extension.user.entity.User;
import org.eclipse.edc.extension.user.service.UserService;
import org.eclipse.edc.spi.monitor.Monitor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Path("/user")
public class UserApiController {
    private final Monitor monitor;
    private Connection connection;
    private UserService userService;


    public UserApiController(Monitor monitor, Connection connection) {
        this.monitor = monitor;
        this.connection = connection;
        this.userService = new UserService(connection);
    }

    @POST
    public String persistJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<User> users = objectMapper.readValue(json, new TypeReference<List<User>>() {
            });
            return objectMapper.writeValueAsString("Persist Json ok, number of persisted users: " + userService.persistUsersToDb(users));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return "Persist Json fail";
        }
    }

    @GET
    public List<User> getAllUsers() {
        try {
            return userService.getAllUsersFromDb();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
