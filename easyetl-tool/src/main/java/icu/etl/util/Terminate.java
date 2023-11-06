package icu.etl.util;

/**
 * 可终止任务接口
 */
public interface Terminate {

    /**
     * 返回 true 表示操作已终止
     *
     * @return
     */
    boolean isTerminate();

    /**
     * 终止任务
     */
    void terminate();

}
