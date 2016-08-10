package vn.datsan.datsan.search;

import java.util.ArrayList;
import java.util.List;

import vn.datsan.datsan.utils.Constants;

/**
 * Created by yennguyen on 7/29/16.
 */
public class SearchOption {
    private String keyword;
    private List<String> indices = new ArrayList<>();
    private List<String> types;
    // Geo distance for filtered search results
    // It is a string like "1km", "100m", "200cm", so on
    private String filteredDistance;
    private String lat;
    private String lon;
    private String distanceUnit = "km";
    // Pagination paramters
    private int from = 0;
    private int size = Constants.ELASTICSEARCH_PAGINATION_SIZE_DEFAULT;

    public SearchOption() {

    }

    public SearchOption(String keyword) {
        this.keyword = keyword;
        // Defaulted search index
        this.indices.add(Constants.ELASTICSEARCH_INDEX);
    }

    public SearchOption(String keyword, List<String> types) {
        this.keyword = keyword;
        this.types = types;
        // Defaulted search index
        this.indices.add(Constants.ELASTICSEARCH_INDEX);
    }

    public SearchOption(String keyword, List<String> indices, List<String> types) {
        this.keyword = keyword;
        this.indices = indices;
        this.types = types;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<String> getIndices() {
        return indices;
    }

    public void setIndices(List<String> indices) {
        this.indices = indices;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getFilteredDistance() {
        return filteredDistance;
    }

    public void setFilteredDistance(String filteredDistance) {
        this.filteredDistance = filteredDistance;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setLatLon(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
