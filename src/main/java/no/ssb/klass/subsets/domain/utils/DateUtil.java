package no.ssb.klass.subsets.domain.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private DateUtil() {
    }

    public static String createDatePostfix(LocalDate validFrom, LocalDate validTo) {

        String pattern = "yyyy-MM";
        if (isFromDateStartOfYear(validFrom) && !sameYear(validFrom, validTo)) {
            pattern = "yyyy";
        }
        DateTimeFormatter formatedDate = DateTimeFormatter.ofPattern(pattern);
        return validFrom.format(formatedDate);
    }


    private static boolean isFromDateStartOfYear(LocalDate fromDate) {
        return fromDate.getDayOfYear() == 1;
    }

    private static boolean sameYear(LocalDate fromDate, LocalDate toDate) {
        return fromDate.getYear() == toDate.getYear();
    }
}
