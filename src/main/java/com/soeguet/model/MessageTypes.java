package com.soeguet.model;

public interface MessageTypes {

    byte DELETED = 127;

   byte NORMAL = 0;
   byte LINK = 16;
   byte INTERRUPT = 25;

   byte INTERACTED = 2;

   byte EDITED = 7;
}