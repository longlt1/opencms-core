/*
 * File   : $Source: /alkacon/cvs/opencms/modules/com.alkacon.opencms.v8.list/resources/system/modules/com.alkacon.opencms.v8.list/java_src/CmsListEntry.java,v $
 * Date   : $Date: 2011/05/06 15:43:51 $
 * Version: $Revision: 1.1 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) 2002 - 2011 Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.alkacon.opencms.v8.list;

import java.util.Date;

/**
 * Java Bean which describes a single entry in a list.<p>
 * 
 * @author Peter Bonrad
 * @author Michael Moossen
 * 
 * @since 7.6
 * 
 * @version $Revision: 1.1 $ 
 */
public class CmsListEntry {

    /** The author of the list entry. */
    private String m_author;

    /** The date of the list entry. */
    private Date m_date;

    /** The text of the list entry. */
    private String m_text;

    /** The path to the image of the list entry. */
    private String m_image;

    /** The link to the list entry. */
    private String m_link;

    /** The title of the list entry. */
    private String m_title;

    /**
     * Returns the author of the list entry.<p>
     *
     * @return the author of the list entry
     */
    public String getAuthor() {

        return m_author;
    }

    /**
     * Returns the date of the list entry.<p>
     *
     * @return the date of the list entry
     */
    public Date getDate() {

        return m_date;
    }

    /**
     * Returns the text of the list entry.<p>
     *
     * @return the text of the list entry
     */
    public String getText() {

        return m_text;
    }

    /**
     * Returns the path to the image of the list entry.<p>
     *
     * @return the path to the image of the list entry
     */
    public String getImage() {

        return m_image;
    }

    /**
     * Returns the link to the list entry.<p>
     *
     * @return the link to the list entry
     */
    public String getLink() {

        return m_link;
    }

    /**
     * Returns the title of the list entry.<p>
     *
     * @return the title of the list entry
     */
    public String getTitle() {

        return m_title;
    }

    /**
     * Sets the author of the list entry.<p>
     *
     * @param author the author of the list entry to set
     */
    public void setAuthor(String author) {

        m_author = author;
    }

    /**
     * Sets the date of the list entry.<p>
     *
     * @param date the date of the list entry to set
     */
    public void setDate(Date date) {

        m_date = date;
    }

    /**
     * Sets the text of the list entry.<p>
     *
     * @param text the text of the list entry to set
     */
    public void setDescription(String text) {

        m_text = text;
    }

    /**
     * Sets the path to the image of the list entry.<p>
     *
     * @param image the path to the image of the list entry to set
     */
    public void setImage(String image) {

        m_image = image;
    }

    /**
     * Sets the link to the list entry.<p>
     *
     * @param link the link to the list entry to set
     */
    public void setLink(String link) {

        m_link = link;
    }

    /**
     * Sets the title of the list entry.<p>
     *
     * @param title the title of the list entry to set
     */
    public void setTitle(String title) {

        m_title = title;
    }

}