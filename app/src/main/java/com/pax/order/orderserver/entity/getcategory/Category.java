/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *    This software is supplied under the terms of a license agreement or
 *    nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *    or disclosed except in accordance with the terms in that agreement.
 *        Copyright (C) 2018 -? PAX Technology, Inc. All rights reserved.
 * Revision History:
 * Date	                     Author	                        Action
 * 18-9-13 下午8:25  	     JoeyTan           	    Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.orderserver.entity.getcategory;

/**
 * category detail
 */
public class Category {
    private String ID;
    private String Name;
    private int ImageURL;
    private String ParentID;
    private String ParentName;

    public Category() {
        ID = "";
        Name = "";
        ImageURL = 0;
        ParentID = "";
        ParentName = "";
    }

    public int getPicUrl() {
        return ImageURL;
    }

    public void setPicUrl(int picUrl) {
        ImageURL = picUrl;
    }

    public String getId() {
        return ID;
    }

    public void setId(String id) {
        ID = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getParentId() {
        return ParentID;
    }

    public void setParentId(String parentId) {
        ParentID = parentId;
    }

    public String getParentName() {
        return ParentName;
    }

    public void setParentName(String parentName) {
        ParentName = parentName;
    }
}
