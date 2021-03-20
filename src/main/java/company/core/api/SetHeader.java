package company.core.api;

import java.util.*;

import io.restassured.http.Header;
import io.restassured.http.Headers;

public class SetHeader {

//    static Headers headerList = null;
    private static Header contentType = null;
    private static Header version = null;
    private static Header tenant = null;
    private static Header accept = null;
    private static Header accessToken = null;


    public static Headers SetHeaderDefault() {
        contentType = new Header("Content-Type", "application/json");
        version = new Header("X-API-VERSION", "v1");
        tenant = new Header("X-API-TENANT", "wl-st");
        accept = new Header("Accept", "application/json");
        List<Header> listHeader = Arrays.asList(contentType, version, tenant, accept);
        return new Headers(listHeader);
    }

    public static Headers SetHeaderWithAccessToken(String token) {
        contentType = new Header("Content-Type", "application/json");
        version = new Header("X-API-VERSION", "v1");
        tenant = new Header("X-API-TENANT", "cewl-st");
        accept = new Header("Accept", "application/json");
        accessToken = new Header("Authorization", token);
        List<Header> listHeader = Arrays.asList(contentType, version, tenant, accept, accessToken);
        return new Headers(listHeader);
    }


}
