package no.ssb.klass.subsets.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a date range. From is inclusive and to is exclusive |-->.
 */
public final class DateRange {
    private final LocalDate from;
    private final LocalDate to;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static boolean isMaxDate(LocalDate date) {
        return LocalDate.MAX.equals(date);
    }

    public static boolean isMinDate(LocalDate date) {
        return LocalDate.MIN.equals(date);
    }

    public DateRange(LocalDate from, LocalDate to) {
        if (from.equals(to) || from.isAfter(to)) {
            throw new IllegalArgumentException("From is equal or after to. From: " + from + " To: " + to);
        }
        this.from = from;
        this.to = to;
    }

    public Boolean overlapsCurrentDate() {
        return (LocalDate.now().isEqual(from) || LocalDate.now().isAfter(from)) && (LocalDate.now().isBefore(to));
    }

    public boolean overlaps(DateRange other) {
        if (other.to.isAfter(from)) {
            return other.from.isBefore(to);
        }
        return false;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }


    public boolean contains(LocalDate date) {
        if (from.isBefore(date) || from.equals(date)) {
            return to.isAfter(date);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        DateRange other = (DateRange) obj;
        return Objects.equals(this.from, other.from) && Objects.equals(this.to, other.to);
    }

    @Override
    public String toString() {
        String fromString = isMinDate(from) ? "min" : from.format(DATE_FORMATTER);
        String toString = isMaxDate(to) ? "max" : to.format(DATE_FORMATTER);
        return "[from=" + fromString + ", to=" + toString + "]";
    }

    public static DateRange create(LocalDate from, LocalDate to) {
        if (from == null) {
            from = LocalDate.MIN;
        }
        if (to == null) {
            to = LocalDate.MAX;
        }

        return new DateRange(from, to);
    }

    /**
     * From and to are specified in format yyyy-MM-dd
     */
    public static DateRange create(String from, String to) {
        return create(createDate(from), createDate(to));
    }

    public static LocalDate createDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
