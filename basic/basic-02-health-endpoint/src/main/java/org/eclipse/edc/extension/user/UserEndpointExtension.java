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

import org.eclipse.edc.extension.user.util.H2Connection;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.web.spi.WebService;

import java.sql.SQLException;

public class UserEndpointExtension implements ServiceExtension {
    @Inject
    WebService webService;
    private H2Connection h2Connection;

    @Override
    public void initialize(ServiceExtensionContext context) {
        try {
            h2Connection = new H2Connection();
            h2Connection.initDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            webService.registerResource(new UserApiController(context.getMonitor(), h2Connection.getConnection()));
        }
    }

    @Override
    public void shutdown() {
        try {
            h2Connection.getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
