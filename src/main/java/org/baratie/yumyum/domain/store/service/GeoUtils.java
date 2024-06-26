package org.baratie.yumyum.domain.store.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;
import java.math.BigDecimal;

public class GeoUtils {
    public static BigDecimal[] findGeoPoint(String address) throws IOException, InterruptedException, ApiException {

        if (address == null) {
            return null;
        }

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyAnmhP2KcLhHWXlNvXYI_wOAsxFYTo6XC0")
                .build();

        GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
        context.shutdown();

        // Extract latitude and longitude from the first result
        if (results != null && results.length > 0) {
            BigDecimal lat = BigDecimal.valueOf(results[0].geometry.location.lat);
            BigDecimal lng = BigDecimal.valueOf(results[0].geometry.location.lng);
            return new BigDecimal[]{lat, lng};
        }

        return null;

    }
}
