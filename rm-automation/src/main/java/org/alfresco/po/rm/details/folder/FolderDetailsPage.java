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
package org.alfresco.po.rm.details.folder;

import org.alfresco.po.common.annotations.RenderableChild;
import org.alfresco.po.rm.details.DisposableItemDetailsPage;
import org.alfresco.po.share.details.document.PropertyPanel;
import org.alfresco.po.share.details.document.SharePanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Folder Details page
 *
 * @author Tatiana Kalinovskaya
 */
@Component
public class FolderDetailsPage extends DisposableItemDetailsPage
{
    /** folder actions panel */
    @Autowired
    @RenderableChild
    private FolderActionsPanel folderActionsPanel;

    /** share panel */
    @Autowired
    @RenderableChild
    private SharePanel sharePanel;

    /** property panel */
    @Autowired
    @RenderableChild
    private PropertyPanel propertyPanel;

    /**
     * @return Folder Actions panel
     */
    public FolderActionsPanel getFolderActionsPanel()
    {
        return folderActionsPanel;
    }

}
