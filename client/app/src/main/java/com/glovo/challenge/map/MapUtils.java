package com.glovo.challenge.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import java.util.ArrayList;
import java.util.List;

public class MapUtils {

    /**
     * Implementation of Convex hull algorithm to prevent Polygon overlapping.
     *
     * https://en.wikipedia.org/wiki/Convex_hull
     */
    public static PolygonOptions convexHull(List<PolygonOptions> polygonOptions) {

        List<LatLng> points = new ArrayList<>();

        for (PolygonOptions polygon : polygonOptions) {
            points.addAll(polygon.getPoints());
        }

        if (points.size() < 3) {
            throw new IllegalStateException();
        }

        List<LatLng> pointsResult = new ArrayList<>();

        int leftPoint = 0;
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).longitude < points.get(leftPoint).longitude) {
                leftPoint = i;
            }
        }

        int p = leftPoint, q;
        do {
            pointsResult.add(points.get(p));

            q = (p + 1) % points.size();

            for (int i = 0; i < points.size(); i++) {
                if (orientation(points.get(p), points.get(i), points.get(q)) == 2) {
                    q = i;
                }
            }
            p = q;
        } while (p != leftPoint);

        final PolygonOptions result = new PolygonOptions();

        for (final LatLng latLng : pointsResult) {
            result.add(latLng);
        }

        for (final PolygonOptions polygon : polygonOptions) {
            for (final List<LatLng> hole : polygon.getHoles()) {
                result.addHole(hole);
            }
        }

        return result;
    }

    private static int orientation(LatLng p, LatLng q, LatLng r) {
        double val = (q.latitude - p.latitude) * (r.longitude - q.longitude) -
            (q.longitude - p.longitude) * (r.latitude - q.latitude);

        if (val == 0) {
            return 0;
        }
        return (val > 0) ? 1 : 2;
    }

    public static LatLngBounds getLatLngBounds(Polygon polygon) {
        final LatLngBounds.Builder centerBuilder = LatLngBounds.builder();
        for (LatLng point : polygon.getPoints()) {
            centerBuilder.include(point);
        }
        return centerBuilder.build();
    }

    public static LatLng getCenter(Polygon polygon) {
        return getLatLngBounds(polygon).getCenter();
    }

}
