package com.aloautoworks.alo.remote;

public class Common {

    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";
    public static IGoogleApiService getGoogleAPIService()
    {
        return GooglemapsApiclient.getClient(GOOGLE_API_URL).create(IGoogleApiService.class);
    }
}
