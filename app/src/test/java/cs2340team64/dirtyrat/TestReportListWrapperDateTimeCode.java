package cs2340team64.dirtyrat;

import org.junit.Test;
import cs2340team64.dirtyrat.model.ReportListWrapper;
import static org.junit.Assert.*;


public class TestReportListWrapperDateTimeCode {

    @Test
    public void testAdd() {
        String str = "Junit is working fine";
        assertEquals("Junit is working fine",str);
    }

    @Test
    public void testInvalidDate1() {
        String invalid = "02/31/245/121/3";
        assertNull(ReportListWrapper.getInstance().dateTimeCode(invalid, false));
    }

    @Test
    public void testInvalidDate2() {
        String invalid = "asdf";
        assertNull(ReportListWrapper.getInstance().dateTimeCode(invalid, false));
    }

    @Test
    public void testNormalDate() {
        String normal = "03/11/2011";
        assertEquals(ReportListWrapper.getInstance().dateTimeCode(normal, false), Long.valueOf(-20110311000000L));
    }

    @Test
    public void testNextMonthDate() {
        String edge = "11/32/2013";
        assertEquals(ReportListWrapper.getInstance().dateTimeCode(edge, false), Long.valueOf(-20131201000000L));
    }

    @Test
    public void testNextYearDate() {
        String edge = "12/32/2013";
        assertEquals(ReportListWrapper.getInstance().dateTimeCode(edge, false), Long.valueOf(-20140101000000L));
    }

    @Test
    public void testLongYear() {
        String edge = "1/1/201312312";
        assertEquals(ReportListWrapper.getInstance().dateTimeCode(edge, false), Long.valueOf(-20130101000000L));
    }
}