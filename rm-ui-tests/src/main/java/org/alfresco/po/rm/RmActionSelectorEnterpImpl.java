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
package org.alfresco.po.rm;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.po.share.site.contentrule.createrules.selectors.impl.ActionSelectorEnterpImpl;
import org.alfresco.webdrone.WebDrone;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * Extends the {@link ActionSelectorEnterpImpl} in order to add the RM specific actions
 *
 * @author Tuna Aksoy
 * @since 2.2
 */
public class RmActionSelectorEnterpImpl extends ActionSelectorEnterpImpl
{
    // FIXME: Description
    private static final By ACTION_OPTIONS_SELECT = By.cssSelector("ul[id$=ruleConfigAction-configs]>li select[class$='config-name']");

    // FIXME: Description
    private final static long MAX_WAIT_TIME = 60000;

    // FIXME: Description
    public static enum PerformActions
    {
        COMPLETE_RECORD("declareRecord"),
        REOPEN_RECORD("undeclareRecord"),
        OPEN_RECORD_FOLDER("openRecordFolder"),
        CLOSE_RECORD_FOLDER("closeRecordFolder"),
        FILE_TO("fileTo"),
        REJECT("reject"),
        REQUEST_INFORMATION("requestInfo"),
        COMPLETE_EVENT("completeEvent"),
        ADD_RECORD_TYPES("addRecordTypes"),
        EXECUTE_SCRIPT("executeScript"),
        SEND_EMAIL("sendEmail"),
        SET_PROPERTY_VALUE("setPropertyValue");

        private final String value;

        PerformActions(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    // FIXME: Description
    public RmActionSelectorEnterpImpl(WebDrone drone)
    {
        super(drone);
    }

    // FIXME: Description
    public void selectAction(PerformActions action)
    {
        // FIXME: Check parameter for public methods

        List<WebElement> actionOptions = getDrone().findAndWaitForElements(ACTION_OPTIONS_SELECT);
        List<Select> actionSelects = new ArrayList<Select>();
        for (WebElement actionOption : actionOptions)
        {
            actionSelects.add(new Select(actionOption));
        }
        actionSelects.get(actionSelects.size() - 1).selectByValue(action.getValue());
    }

    // FIXME: Description
    public void selectFileTo(String path, boolean createRecordPath)
    {
        // FIXME: Check parameter for public methods

        selectAction(PerformActions.FILE_TO);
        setFileToPath(path, MAX_WAIT_TIME);
        if (createRecordPath)
        {
            toggleCreateRecordPath(MAX_WAIT_TIME);
        }
    }

    // FIXME: Description
    private void setFileToPath(String path, long timeout)
    {
        WebElement input = getDrone().findAndWait(By.className("yui-ac-input"), timeout);
        input.clear();
        input.sendKeys(path);
    }

    // FIXME: Description
    private void toggleCreateRecordPath(long timeout)
    {
        WebElement checkBox = getDrone().findAndWait(By.xpath("//span[@class='menutype_action menuname_fileTo paramtype_d_boolean paramname_createRecordPath']/child::input"), timeout);
        checkBox.click();
    }
}
