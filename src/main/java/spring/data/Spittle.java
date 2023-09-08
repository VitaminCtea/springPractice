package spring.data;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class Spittle {
    private final Long id;
    private final String message;
    private final Date time;
    private final Double latitude;
    private final Double longitude;

    public Spittle(String message, Date time, Double latitude, Double longitude) {
        this((long) 0, message, time, latitude, longitude);
    }

    public Spittle(Long id, String message, Date time, Double latitude, Double longitude) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Spittle(String message, Date time) {
        this(message, time, null, null);
    }

    public long getId() { return ObjectUtils.defaultIfNull(id, (long) 0); }
    public double getLatitude() { return ObjectUtils.defaultIfNull(latitude, (double) 0); }
    public double getLongitude() { return ObjectUtils.defaultIfNull(longitude, (double) 0); }
    public String getMessage() {
        return message;
    }
    public Date getTime() {
        return time;
    }

    @Override public boolean equals(Object that) { return EqualsBuilder.reflectionEquals(this, that, "id", "time"); }
    @Override public int hashCode() { return HashCodeBuilder.reflectionHashCode(this, "id", "time"); }
}
