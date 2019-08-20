package no.ssb.klass.subsets.common;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateRangeTest {

    /**
     * <pre>
     *    subject   |------|
     *    other         |------|
     * </pre>
     */
    @Test
    public void overlaps() {
        // given
        DateRange subject = DateRange.create("2012-01-01", "2014-01-01");
        DateRange other = DateRange.create("2013-01-01", "2015-01-01");

        // when
        boolean result = subject.overlaps(other);

        // then
        assertTrue(result);
    }

    /**
     * <pre>
     *    subject   |------|
     *    other            |------|
     * </pre>
     */
    @Test
    public void overlapsSubjectFirst() {
        // given
        DateRange subject = DateRange.create("2012-01-01", "2014-01-01");
        DateRange other = DateRange.create("2014-01-01", "2016-01-01");

        // when
        boolean result = subject.overlaps(other);

        // then
        assertFalse(result);
    }

    /**
     * <pre>
     *    subject          |------|
     *    other     |------|
     * </pre>
     */
    @Test
    public void overlapsSubjectLast() {
        // given
        DateRange subject = DateRange.create("2014-01-01", "2016-01-01");
        DateRange other = DateRange.create("2012-01-01", "2014-01-01");

        // when
        boolean result = subject.overlaps(other);

        // then
        assertFalse(result);
    }


    @Test
    public void contains() {
        // given
        LocalDate start = DateRange.createDate("2014-01-01");
        LocalDate end = start.plusYears(2);
        DateRange subject = DateRange.create(start, end);

        // then
        assertFalse(subject.contains(start.minusYears(1)));
        assertTrue(subject.contains(start));
        assertTrue(subject.contains(start.plusYears(1)));
        assertFalse(subject.contains(end));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromAfterTo() {
        DateRange.create("2018-01-01", "2016-01-01");
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromEqualTo() {
        DateRange.create("2016-01-01", "2016-01-01");
    }
}
