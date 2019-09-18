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
package com.pax.order.orderserver.entity;

/**
 * login information
 */
public class LoginInfo {
    private String mUserName;
    private String mPassword;
    private String mToken;
    private String mTerminalID;
    private String mTerminalSN;
    private String mTableID;

    public LoginInfo() {

    }

    public LoginInfo(
            String userName, String password, String token, String terminalID, String terminalSN, String tableID) {
        mUserName = userName;
        mPassword = password;
        mToken = token;
        mTerminalID = terminalID;
        mTerminalSN = terminalSN;
        mTableID = tableID;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public String getTerminalID() {
        return mTerminalID;
    }

    public void setTerminalID(String terminalID) {
        mTerminalID = terminalID;
    }

    public String getTerminalSN() {
        return mTerminalSN;
    }

    public void setTerminalSN(String terminalSN) {
        mTerminalSN = terminalSN;
    }

    public String getTableID() {
        return mTableID;
    }

    public void setTableID(String tableID) {
        mTableID = tableID;
    }
}
