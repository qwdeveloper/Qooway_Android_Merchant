package com.example.merchantv01;

import java.util.Map;



    public class Entry {
        public final String OrderID;
        public final Map info;
        public static String[] inputElements = null;
        
        public Entry(String orderID, Map<String, String> info2) {
            this.OrderID = orderID;
            this.info = info2;
        }

    }

