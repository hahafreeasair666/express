package com.ch999.express.common;

public class Distance {

    // 地球平均半径
    private static final double EARTH_RADIUS = 6378137;

    // 把经纬度转为度（°）
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     *
     * @param lng1 A点的经度
     * @param lat1 A点的纬度
     * @param lng2 B点的经度
     * @param lat2 B点的纬度
     * @return 距离值（单位：米）
     */
    public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 计算两点间的距离，该函数是一个重载，用于处理参数为String类型，以,逗号分隔的坐标值
     *
     * @param coordinateA A点的坐标
     * @param coordinateB B点的坐标
     * @return 距离值（单位：米）
     */
    public static double getDistance(String coordinateA, String coordinateB) {
        String[] positionA = coordinateA.trim().split(",");
        double longitudeA = Double.parseDouble(positionA[1].trim());
        double latitudeA = Double.parseDouble(positionA[0].trim());

        String[] positionB = coordinateB.trim().split(",");
        double longitudeB = Double.parseDouble(positionB[1].trim());
        double latitudeB = Double.parseDouble(positionB[0].trim());

        return getDistance(longitudeA, latitudeA, longitudeB, latitudeB);
    }
}
