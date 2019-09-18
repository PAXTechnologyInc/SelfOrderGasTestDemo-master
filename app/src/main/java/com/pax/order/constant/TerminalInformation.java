package com.pax.order.constant;

/**
 * Created by lixc
 * terminal information
 */
public class TerminalInformation {

    private TerminalInformation() {

    }

    public static final String[][] INFORMATION = {
            {"SN:","Serial Number"},
            {"Term OS Ver:","Extended Serial Number"},
            {"APP Name:","Application Name"},
            {"Term Info:","Terminal Information"},
    };

    public static final byte[][] TERMINAL_TAG = {
            {(byte) 0x01, 0x01},
            {(byte) 0x01, 0x27},
            {(byte) 0x01, 0x18},
            {(byte) 0x01, 0x02}
    };
}
