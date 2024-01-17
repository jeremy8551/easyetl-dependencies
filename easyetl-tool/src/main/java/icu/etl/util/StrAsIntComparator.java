package icu.etl.util;

import java.util.Comparator;

public class StrAsIntComparator implements Comparator<String> {

    public int compare(String o1, String o2) {
        return Integer.parseInt(StringUtils.trimBlank(o1)) - Integer.parseInt(StringUtils.trimBlank(o2));
    }

}
