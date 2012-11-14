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

package org.alfresco.module.org_alfresco_module_rm.record;

import java.util.Set;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;

/**
 * Record Service Interface.
 * 
 * @author Roy Wetherall
 * @since 2.1
 */
public interface RecordService
{
   /**
    * Gets a list of all the record meta-data aspects
    * 
    * @return {@link Set}<{@link QName}>   list of record meta-data aspects
    */
   Set<QName> getRecordMetaDataAspects();

   /**
    * Indicates whether the record is declared
    * 
    * @param nodeRef   node reference of the record for which the check would be performed
    * @return boolean  true if record is declared, false otherwise
    */
   boolean isDeclared(NodeRef nodeRef);  

   /**
    * Creates a new record from an existing document.
    * 
    * @param filePlan   The filePlan in which the record should be placed
    * @param document   The document from which the record will be created
    */
   void createRecordFromDocument(NodeRef filePlan, NodeRef document);

   /**
    * Gets the unfiled record container for the given file plan
    * 
    * @param filePlan   The filePlan for which the unfiled record container should be retrieved
    * @return NodeRef   The nodeRef of the container object
    */
   public NodeRef getUnfiledRecordContainer(NodeRef filePlan);

   // TODO boolean isRecordFiled(NodeRef record);

   // TODO boolean isRecordClassified(NodeRef record);

   // TODO NodeRef getNewRecordContainer(NodeRef filePlan);

   // TODO NodeRef createRecord(NodeRef filePlan, NodeRef document);

   // TODO NodeRef createAndFileRecord(NodeRef recordFolder, NodeRef document);

   // TODO void fileRecord(NodeRef recordFolder, NodeRef record);
}
