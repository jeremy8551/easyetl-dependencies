package icu.etl.collection;

import icu.etl.util.StringComparator;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MatrixTest {

    @Test
    public void test() {
        Matrix<String> m = new Matrix<String>(5, 5);
        for (int r = 0; r < m.getRow(); r++) {
            for (int c = 0; c < m.getColumn(); c++) {
                m.set(r, c, r + "," + c);
            }
        }
        m.expandCapacity(6, 6);
        // for (int r = 0; r < 5; r++) {
        // for (int c = 0; c < 5; c++) {
        // m.set(r, c, r + "," + c);
        // }
        // }
        m.set(5, 5, "5,5");
        m.expandCapacity(10, 10);
        m.set(9, 9, "9,9");
        m.set(9, 0, "9,0");

        assertTrue(m.get(9, 0).equals("9,0"));
        assertTrue(m.get(9, 9).equals("9,9"));
        assertTrue(m.get(0, 0).equals("0,0"));
        assertTrue(m.get(0, 4).equals("0,4"));
        System.out.println(m);
        Matrix<String> n = m.clone();

        // System.out.println(m.get(0, 0, 0, 0));
        // System.out.println(m.get(0, 0, 0, 1));
        // System.out.println(m.get(0, 0, 0, 2));
        //
        // System.out.println(m.get(0, 0, 0, 0));
        // System.out.println(m.get(1, 0, 0, 0));
        //
        // System.out.println(m.get(0, 0, 2, 4));
        // System.out.println(m.get(2, 4, 0, 0));

        Matrix<String> s = m.clone();
        s.sortRow(false, new int[]{0}, new StringComparator());
        System.out.println(s);

        s = m.clone();
        s.sortCol(false, new int[]{9}, new StringComparator());
        System.out.println(s);

        /**
         * 顺时针旋转 再 逆时针旋转
         */
        m.rotate();
        m.reverseRotate();
        System.out.println(m.equals(n));

        m.rotate();
        m.rotate();
        m.rotate();
        m.rotate();
        System.out.println(m.equals(n));

        m.reverseRotate();
        m.reverseRotate();
        m.reverseRotate();
        m.reverseRotate();
        System.out.println(m.equals(n));
    }

}
