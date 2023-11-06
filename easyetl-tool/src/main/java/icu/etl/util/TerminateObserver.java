package icu.etl.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 可终止任务的观察者
 *
 * @author jeremy8551@qq.com
 * @createtime 2023-09-29
 */
public class TerminateObserver {

    /** 观察者 */
    protected Vector<Terminate> list;

    /**
     * 初始化
     */
    public TerminateObserver() {
        this.list = new Vector<Terminate>();
    }

    /**
     * 添加任务
     *
     * @param e 任务
     * @return true表示成功添加任务 false添加任务失败
     */
    public boolean add(Terminate e) {
        return this.list.add(e);
    }

    /**
     * 删除任务
     *
     * @param o 任务
     * @return true表示删除成功 false表示删除失败
     */
    public boolean remove(Terminate o) {
        return this.list.remove(o);
    }

    /**
     * 终止所有任务
     *
     * @param quiet true表示屏蔽异常错误信息
     * @return 返回终止任务时捕获的异常
     */
    public List<Throwable> terminate(boolean quiet) {
        List<Throwable> es = new ArrayList<Throwable>(this.list.size());
        for (Terminate obj : this.list) {
            try {
                if (obj != null && !obj.isTerminate()) {
                    obj.terminate();
                }
            } catch (Throwable e) {
                es.add(e);
                if (!quiet) {
                    e.printStackTrace();
                }
            }
        }

        if (!quiet && es.size() > 0) {
            throw new RuntimeException(es.get(0));
        } else {
            return es;
        }
    }

}
