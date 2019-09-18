package com.pax.order.enums;

/*		
 * ============================================================================		
 * = COPYRIGHT		
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION		
 *   This software is supplied under the terms of a license agreement or		
 *   nondisclosure agreement with PAX  Technology, Inc. and may not be copied		
 *   or disclosed except in accordance with the terms in that agreement.		
 *      Copyright (C) 2000-2018 PAX Technology, Inc. All rights reserved.		
 * Description: // Detail description about the function of this module,		
 *             // interfaces with the other modules, and dependencies. 		
 * Revision History:		
 * Date	                 Author	                Action
 * 2018/8/21 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================		
 */
public enum SplitType {
    NULL((byte) 0),
    TWO((byte) 1),
    THREE((byte) 2),
    BYITEM((byte) 3);

    private byte mType;

    SplitType(byte acType) {
        this.mType = acType;
    }
    public byte index() {
        return (byte) ordinal();
    }
}
