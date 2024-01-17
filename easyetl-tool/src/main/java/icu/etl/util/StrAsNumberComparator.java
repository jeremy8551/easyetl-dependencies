package icu.etl.util;

import java.math.BigDecimal;
import java.util.Comparator;

public class StrAsNumberComparator implements Comparator<String> {

    public int compare(String o1, String o2) {
        return new BigDecimal(StringUtils.trimBlank(o1)).compareTo(new BigDecimal(StringUtils.trimBlank(o2)));
    }

}
