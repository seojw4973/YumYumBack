package org.baratie.yumyum.domain.store.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

@Component
public class GeoUtils {

    @Value("${google.maps.API.key}")
    private String key;

    public BigDecimal[] findGeoPoint(String address) throws IOException, InterruptedException, ApiException {
          if (address == null) {
            return null;
        }

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(key)
                .build();

        GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
        context.shutdown();

        if (results != null && results.length > 0) {
            BigDecimal lat = BigDecimal.valueOf(results[0].geometry.location.lat);
            BigDecimal lng = BigDecimal.valueOf(results[0].geometry.location.lng);
            return new BigDecimal[]{lat, lng};
        }

        return null;

    }
}
