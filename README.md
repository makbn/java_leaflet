## Java Leaflet
Java wrapper for Leaflet, JavaScript library for mobile-friendly interactive maps.

*  Current version: **v1.6.0**

> Leaflet is the leading open-source JavaScript library for mobile-friendly interactive maps. Weighing just about 38 KB of JS, it has all the mapping features most > developers ever need.
> Leaflet is designed with simplicity, performance and usability in mind. It works efficiently across all major desktop and mobile platforms, can be extended with > lots of plugins, has a beautiful, easy to use and well-documented API and a simple, readable source code that is a joy to contribute to.


## Getting start

First, you need to initialize an instance of `JLMapView`:

```java
final JLMapView map = JLMapView
        .builder()
        .mapType(JLProperties.MapType.DARK)
        .accessToken(ACCESS_TOKEN)
        .startCoordinate(JLLatLng.builder()
                .lat(43.54)
                .lng(22.54)
                .build())
        .build();

```