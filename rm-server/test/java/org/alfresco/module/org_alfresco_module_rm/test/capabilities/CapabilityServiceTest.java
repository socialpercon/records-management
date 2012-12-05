/*
 * Copyright (C) 2005-2012 Alfresco Software Limited.
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
package org.alfresco.module.org_alfresco_module_rm.test.capabilities;

import java.util.List;

import org.alfresco.module.org_alfresco_module_rm.capability.Capability;
import org.alfresco.module.org_alfresco_module_rm.capability.CapabilityService;
import org.alfresco.module.org_alfresco_module_rm.capability.Group;
import org.alfresco.module.org_alfresco_module_rm.capability.GroupImpl;
import org.alfresco.module.org_alfresco_module_rm.test.util.BaseRMTestCase;

/**
 * Test class for testing the methods in {@link CapabilityService}
 *
 * @author Tuna Aksoy
 * @since 2.1
 */
public class CapabilityServiceTest extends BaseRMTestCase
{
    public void testGetAddRemoveGroups() throws Exception
    {
        doTestInTransaction(new Test<Void>()
        {
            @Override
            public Void run() throws Exception
            {
                Group auditGroup = capabilityService.getGroup("audit");
                assertNotNull(auditGroup);
                assertTrue(auditGroup.getIndex() == 1);
                assertTrue(auditGroup.getTitle().equalsIgnoreCase("Audit"));
                assertTrue(auditGroup.getId().equalsIgnoreCase("audit"));

                return null;
            }
        });

        doTestInTransaction(new Test<Void>()
        {
            @Override
            public Void run() throws Exception
            {
                GroupImpl testGroup = new GroupImpl();
                testGroup.setId("testGroup");
                testGroup.setIndex(14);
                testGroup.setTitle("Test group");
                capabilityService.addGroup(testGroup);

                assertTrue(capabilityService.getGroups().size() == 14);

                Group group = capabilityService.getGroup("testGroup");
                assertNotNull(group);
                assertTrue(group.getId().equalsIgnoreCase("testGroup"));
                assertTrue(group.getTitle().equalsIgnoreCase("Test group"));
                assertTrue(group.getIndex() == 14);

                return null;
            }
        });

        doTestInTransaction(new Test<Void>()
        {
            @Override
            public Void run() throws Exception
            {
                Group testGroup = capabilityService.getGroup("testGroup");
                assertNotNull(testGroup);

                capabilityService.removeGroup(testGroup);
                assertTrue(capabilityService.getGroups().size() == 13);

                return null;
            }
        });

        doTestInTransaction(new Test<Void>()
        {
            @Override
            public Void run() throws Exception
            {
                List<Group> groups = capabilityService.getGroups();
                assertNotNull(groups);

                int size = groups.size();
                assertTrue(size == 13);

                for (int i = 0; i < size; i++)
                {
                    assertNotNull(groups.get(i));
                    assertTrue(groups.get(i).getIndex() == (i + 1));
                }

                Group auditGroup = groups.get(0);
                assertNotNull(auditGroup);
                assertTrue(auditGroup.getIndex() == 1);
                assertTrue(auditGroup.getId().equalsIgnoreCase("audit"));
                assertTrue(auditGroup.getTitle().equalsIgnoreCase("Audit"));

                Group vitalRecords = groups.get(size - 1);
                assertNotNull(vitalRecords);
                assertTrue(vitalRecords.getIndex() == 13);
                assertTrue(vitalRecords.getId().equalsIgnoreCase("vitalRecords"));
                assertTrue(vitalRecords.getTitle().equalsIgnoreCase("Vital Records"));

                return null;
            }
        });
    }

    public void testGetCapabilitiesByGroup() throws Exception
    {
        doTestInTransaction(new Test<Void>()
        {
            @Override
            public Void run() throws Exception
            {
                List<Group> groups = capabilityService.getGroups();
                assertNotNull(groups);

                Group auditGroup = groups.get(0);
                assertNotNull(auditGroup);

                List<Capability> auditCapabilities = capabilityService.getCapabilitiesByGroup(auditGroup);
                assertNotNull(auditCapabilities);

                int vitalRecordCapabilitiesSize = auditCapabilities.size();
                assertTrue(vitalRecordCapabilitiesSize == 6);

                for (int i = 0; i < vitalRecordCapabilitiesSize; i++)
                {
                    Capability capability = auditCapabilities.get(i);
                    assertNotNull(capability);
                    assertTrue(capability.getIndex() == (i + 1));
                }

                Group vitalRecordsGroup = groups.get(groups.size() - 1);
                assertNotNull(vitalRecordsGroup);

                List<Capability> vitalRecordCapabilities = capabilityService.getCapabilitiesByGroupId(vitalRecordsGroup.getId());
                assertNotNull(vitalRecordCapabilities);

                vitalRecordCapabilitiesSize = vitalRecordCapabilities.size();
                assertTrue(vitalRecordCapabilitiesSize == 3);

                for (int i = 0; i < vitalRecordCapabilitiesSize; i++)
                {
                    Capability capability = vitalRecordCapabilities.get(i);
                    assertNotNull(capability);
                    assertTrue(capability.getIndex() == (i + 1));
                }

                return null;
            }
        });
    }
}