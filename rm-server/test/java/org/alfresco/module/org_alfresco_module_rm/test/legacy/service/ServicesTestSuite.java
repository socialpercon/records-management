/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.module.org_alfresco_module_rm.test.legacy.service;

import org.alfresco.module.org_alfresco_module_rm.test.integration.hold.DeleteHoldTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/**
 * RM test suite
 *
 * @author Roy Wetherall
 */
@RunWith(Suite.class)
@SuiteClasses(
{
    ServiceBaseImplTest.class,
    ExtendedSecurityServiceImplTest.class,
    ModelSecurityServiceImplTest.class,
    RecordsManagementActionServiceImplTest.class,
    ExtendedActionServiceTest.class,
    DispositionServiceImplTest.class,
    RecordsManagementActionServiceImplTest.class,
    RecordsManagementAdminServiceImplTest.class,
    RecordsManagementAuditServiceImplTest.class,
    //RecordsManagementEventServiceImplTest.class,
    RecordsManagementSearchServiceImplTest.class,
    VitalRecordServiceImplTest.class,
    DataSetServiceImplTest.class,
    FreezeServiceImplTest.class,
    RecordServiceImplTest.class,
    CapabilityServiceImplTest.class,
    FilePlanRoleServiceImplTest.class,
    FilePlanServiceImplTest.class,
    FilePlanPermissionServiceImplTest.class,
    ReportServiceImplTest.class,
    RecordsManagementQueryDAOImplTest.class,
    DeleteHoldTest.class
})
public class ServicesTestSuite 
{
}
