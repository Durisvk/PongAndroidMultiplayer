package com.main.game.network.packets;

/**
 * Created by juraj on 28.04.16.
 */
public enum PacketType {
    CONNECT {
        public String toString() { return "00"; }
    },
    DISCONNECT {
        public String toString() { return "01"; }
    },
    POSITION_UPDATE {
        public String toString() { return "02"; }
    },
    SCORE_UPDATE {
        public String toString() { return "03"; }
    },
    ACCELEROMETER_UPDATE {
        public String toString() { return "04"; }
    };

    public static PacketType parse(String text) {
        switch(text.substring(0, 2)) {
            case("00"): return CONNECT;
            case("01"): return DISCONNECT;
            case("02"): return POSITION_UPDATE;
            case("03"): return SCORE_UPDATE;
            case("04"): return ACCELEROMETER_UPDATE;
        }
        return null;
    }

    public static String include(String message, PacketType packetType) {
        return packetType.toString() + message;
    }
}
