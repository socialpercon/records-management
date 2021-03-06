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
package org.alfresco.module.org_alfresco_module_rm.test.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.model.ContentModel;
import org.alfresco.module.org_alfresco_module_rm.action.RecordsManagementActionService;
import org.alfresco.module.org_alfresco_module_rm.action.impl.CutOffAction;
import org.alfresco.module.org_alfresco_module_rm.action.impl.DestroyAction;
import org.alfresco.module.org_alfresco_module_rm.action.impl.TransferAction;
import org.alfresco.module.org_alfresco_module_rm.capability.Capability;
import org.alfresco.module.org_alfresco_module_rm.capability.CapabilityService;
import org.alfresco.module.org_alfresco_module_rm.disposition.DispositionSchedule;
import org.alfresco.module.org_alfresco_module_rm.disposition.DispositionService;
import org.alfresco.module.org_alfresco_module_rm.model.RecordsManagementModel;
import org.alfresco.module.org_alfresco_module_rm.model.security.ModelSecurityService;
import org.alfresco.module.org_alfresco_module_rm.role.FilePlanRoleService;
import org.alfresco.module.org_alfresco_module_rm.role.Role;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

/**
 * Common RM test utility methods.
 * 
 * @author Roy Wetherall
 */
public class CommonRMTestUtils implements RecordsManagementModel
{
    private DispositionService dispositionService;
    private NodeService nodeService;
    private ContentService contentService;
    private RecordsManagementActionService actionService;
    private ModelSecurityService modelSecurityService;
    private FilePlanRoleService filePlanRoleService;
    private CapabilityService capabilityService;

    /** test values */
    public static final String DEFAULT_DISPOSITION_AUTHORITY = "disposition authority";
    public static final String DEFAULT_DISPOSITION_INSTRUCTIONS = "disposition instructions";
    public static final String DEFAULT_DISPOSITION_DESCRIPTION = "disposition action description";
    public static final String DEFAULT_EVENT_NAME = "case_closed";
    public static final String PERIOD_NONE = "none|0";
    public static final String PERIOD_IMMEDIATELY = "immediately|0";

    /**
     * Constructor
     * 
     * @param applicationContext    application context
     */
    public CommonRMTestUtils(ApplicationContext applicationContext)
    {
        dispositionService = (DispositionService)applicationContext.getBean("DispositionService");
        nodeService = (NodeService)applicationContext.getBean("NodeService");
        contentService = (ContentService)applicationContext.getBean("ContentService");
        actionService = (RecordsManagementActionService)applicationContext.getBean("RecordsManagementActionService");
        modelSecurityService = (ModelSecurityService)applicationContext.getBean("ModelSecurityService");
        filePlanRoleService = (FilePlanRoleService)applicationContext.getBean("FilePlanRoleService");
        capabilityService = (CapabilityService)applicationContext.getBean("CapabilityService");
    }

    /**
     * Create a disposition schedule
     * 
     * @param container record category
     * @return {@link DispositionSchedule}  created disposition schedule node reference
     */
    public DispositionSchedule createBasicDispositionSchedule(NodeRef container)
    {
        return createBasicDispositionSchedule(container, DEFAULT_DISPOSITION_INSTRUCTIONS, DEFAULT_DISPOSITION_AUTHORITY, false, true);
    }

    /**
     * Create test disposition schedule
     */
   public DispositionSchedule createBasicDispositionSchedule(
                                   NodeRef container,
                                   String dispositionInstructions,
                                   String dispositionAuthority,
                                   boolean isRecordLevel,
                                   boolean defaultDispositionActions)
   {
       return createDispositionSchedule(container, dispositionInstructions, dispositionAuthority, isRecordLevel, defaultDispositionActions, false);
   }

    /**
     * Create test disposition schedule
     */
    public DispositionSchedule createDispositionSchedule(
                                    NodeRef container,
                                    String dispositionInstructions,
                                    String dispositionAuthority,
                                    boolean isRecordLevel,
                                    boolean defaultDispositionActions,
                                    boolean extendedDispositionSchedule)
    {
        return createDispositionSchedule(
                container, 
                dispositionInstructions, 
                dispositionAuthority, 
                isRecordLevel, 
                defaultDispositionActions, 
                extendedDispositionSchedule, 
                DEFAULT_EVENT_NAME);
    }
    
    /**
     * Create test disposition schedule
     */
    public DispositionSchedule createDispositionSchedule(
                                    NodeRef container,
                                    String dispositionInstructions,
                                    String dispositionAuthority,
                                    boolean isRecordLevel,
                                    boolean defaultDispositionActions,
                                    boolean extendedDispositionSchedule,
                                    String defaultEvent)
    {
        Map<QName, Serializable> dsProps = new HashMap<QName, Serializable>(3);
        dsProps.put(PROP_DISPOSITION_AUTHORITY, dispositionAuthority);
        dsProps.put(PROP_DISPOSITION_INSTRUCTIONS, dispositionInstructions);
        dsProps.put(PROP_RECORD_LEVEL_DISPOSITION, isRecordLevel);
        DispositionSchedule dispositionSchedule = dispositionService.createDispositionSchedule(container, dsProps);

        if (defaultDispositionActions)
        {
            Map<QName, Serializable> adParams = new HashMap<QName, Serializable>(3);
            adParams.put(PROP_DISPOSITION_ACTION_NAME, CutOffAction.NAME);
            adParams.put(PROP_DISPOSITION_DESCRIPTION, DEFAULT_DISPOSITION_DESCRIPTION);

            List<String> events = new ArrayList<String>(1);
            events.add(defaultEvent);
            adParams.put(PROP_DISPOSITION_EVENT, (Serializable)events);

            dispositionService.addDispositionActionDefinition(dispositionSchedule, adParams);

            if (extendedDispositionSchedule)
            {
                adParams = new HashMap<QName, Serializable>(4);
                adParams.put(PROP_DISPOSITION_ACTION_NAME, TransferAction.NAME);
                adParams.put(PROP_DISPOSITION_DESCRIPTION, DEFAULT_DISPOSITION_DESCRIPTION);
                adParams.put(PROP_DISPOSITION_PERIOD, PERIOD_IMMEDIATELY);
                adParams.put(PROP_DISPOSITION_LOCATION, StringUtils.EMPTY);

                dispositionService.addDispositionActionDefinition(dispositionSchedule, adParams);
            }

            adParams = new HashMap<QName, Serializable>(3);
            adParams.put(PROP_DISPOSITION_ACTION_NAME, DestroyAction.NAME);
            adParams.put(PROP_DISPOSITION_DESCRIPTION, DEFAULT_DISPOSITION_DESCRIPTION);
            adParams.put(PROP_DISPOSITION_PERIOD, PERIOD_IMMEDIATELY);

            dispositionService.addDispositionActionDefinition(dispositionSchedule, adParams);
        }

        return dispositionSchedule;
    }

    public NodeRef createRecord(NodeRef recordFolder, String name)
    {
        return createRecord(recordFolder, name, null, "Some test content");
    }

    public NodeRef createRecord(NodeRef recordFolder, String name, String title)
    {
        Map<QName, Serializable> props = new HashMap<QName, Serializable>(1);
        props.put(ContentModel.PROP_TITLE, title);
        return createRecord(recordFolder, name, props, "Some test content");
    }

    public NodeRef createRecord(NodeRef recordFolder, String name, Map<QName, Serializable> properties, String content)
    {
        // Create the document
        if (properties == null)
        {
            properties = new HashMap<QName, Serializable>(1);
        }
        if (!properties.containsKey(ContentModel.PROP_NAME))
        {
            properties.put(ContentModel.PROP_NAME, name);
        }
        NodeRef recordOne = nodeService.createNode(recordFolder,
                                                        ContentModel.ASSOC_CONTAINS,
                                                        QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, name),
                                                        ContentModel.TYPE_CONTENT,
                                                        properties).getChildRef();

        // Set the content
        ContentWriter writer = contentService.getWriter(recordOne, ContentModel.PROP_CONTENT, true);
        writer.setMimetype(MimetypeMap.MIMETYPE_TEXT_PLAIN);
        writer.setEncoding("UTF-8");
        writer.putContent(content);

        return recordOne;
    }

    /**
     * Helper method to complete record.
     */
    public void completeRecord(final NodeRef record)
    {
        AuthenticationUtil.runAs(new RunAsWork<Void>()
        {
            @Override
            public Void doWork() throws Exception
            {
                modelSecurityService.setEnabled(false);
                try
                {
                    nodeService.setProperty(record, RecordsManagementModel.PROP_DATE_FILED, new Date());                    
                    nodeService.setProperty(record, ContentModel.PROP_TITLE, "titleValue");                    
                    actionService.executeRecordsManagementAction(record, "declareRecord");
                }
                finally
                {
                    modelSecurityService.setEnabled(true);
                }

                return null;
            }

        }, AuthenticationUtil.getAdminUserName());

	}

    public void closeFolder(final NodeRef recordFolder)
    {
        AuthenticationUtil.runAs(new RunAsWork<Void>()
        {
            @Override
            public Void doWork() throws Exception
            {
                modelSecurityService.setEnabled(false);
                try
                {
                    actionService.executeRecordsManagementAction(recordFolder, "closeRecordFolder");
                }
                finally
                {
                    modelSecurityService.setEnabled(true);
                }
                return null;
            }
        }, AuthenticationUtil.getAdminUserName());
    }

    public Role createRole(NodeRef filePlan, String roleName, String ... capabilityNames)
    {
        Set<Capability> capabilities = new HashSet<Capability>(capabilityNames.length);
        for (String name : capabilityNames)
        {
            Capability capability = capabilityService.getCapability(name);
            if (capability == null)
            {
                throw new AlfrescoRuntimeException("capability " + name + " not found.");
            }
            capabilities.add(capability);
        }

        return filePlanRoleService.createRole(filePlan, roleName, roleName, capabilities);
    }
}
