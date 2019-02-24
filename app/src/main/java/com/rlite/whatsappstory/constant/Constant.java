package com.rlite.whatsappstory.constant;

import android.os.Environment;

/**
 * Created by le no vo on 01-06-2017.
 */

public abstract class Constant {

    public static final String WHATSAPP_URI = Environment.getExternalStorageDirectory().getPath()
            +"/WhatsApp/Media/.Statuses/"; // get the images/videos from whatsapp directory
    public static final String MY_URI = Environment.getExternalStorageDirectory().getPath()+"/WhatsAppStories/"; // manage our directory

}
