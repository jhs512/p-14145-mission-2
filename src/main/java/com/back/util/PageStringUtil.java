package com.back.util;

public class PageStringUtil {
    private static final int range = 1;
    public static String getPageList(int pageNo, int lastPageNo) {
        if (lastPageNo <= 2 * range + 1) { // 슬라이딩 윈도우 불필요
            return pageListString(pageNo, 1, lastPageNo) +"\n";
        }
        if (pageNo <= range+2) {
            return pageListString(pageNo, 1, pageNo+range) + " ... " + lastPageNo;
        } else if (pageNo > lastPageNo - range) {
            return "1 ... " + pageListString(pageNo, lastPageNo - range, lastPageNo) +"\n";
        } else {
            return "1 ... " + pageListString(pageNo, pageNo - range, pageNo + range) + " ... " + lastPageNo +"\n";
        }
    }
    public static String pageListString(int pageNo, int startPageNo, int lastPageNo) {
        StringBuilder sb = new StringBuilder();
        for (int i = startPageNo; i <= lastPageNo; i++) {
            if (i == pageNo) {
                sb.append("[").append(i).append("] / ");
            } else {
                sb.append(i).append(" / ");
            }
        }
        sb.delete(sb.length() - 3, sb.length());
        return sb.toString();
    }
}
