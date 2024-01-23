package icu.etl.util;

import java.util.concurrent.TimeUnit;

/**
 * 计时器
 *
 * @author jeremy8551@qq.com
 */
public class TimeWatch {

    /** 开始计时时间(单位：毫秒) */
    private long startMillis;

    /** 暂停对象 */
    private PauseTime pause;

    /**
     * 初始化
     */
    public TimeWatch() {
        this.pause = new PauseTime();
        this.start();
    }

    /**
     * 开始计时/重新开始计时
     * <p>
     * 这个方法会把计时器所有参数重置为初始状态
     */
    public boolean start() {
        this.pause.reset();
        this.startMillis = System.currentTimeMillis();
        return true;
    }

    /**
     * 暂停/恢复计时
     *
     * @return 返回 true 表示已暂停计时; 返回 false 表示已恢复计时;
     */
    public synchronized boolean pauseOrKeep() {
        if (this.pause.isStart()) {
            this.pause.stop();
            return false;
        } else {
            this.pause.start();
            return true;
        }
    }

    /**
     * 返回计时器用时时间（单位：毫秒）
     * <p>
     * 计时器用时计算公式: 当前时间 - 开始时间 - 暂停时间
     *
     * @return 用时时间，单位：毫秒
     */
    public long useMillis() {
        return System.currentTimeMillis() - this.startMillis - this.pause.getPauseMillis();
    }

    /**
     * 返回计时器用时时间（单位：秒）
     *
     * @return 用时时间，单位：秒
     */
    public long useSeconds() {
        return this.useMillis() / 1000;
    }

    /**
     * 返回计时器用时时间
     *
     * @return 格式详见 {@linkplain Dates#format(long, TimeUnit, boolean)}
     */
    public String useTime() {
        return Dates.format(this.useMillis(), TimeUnit.MILLISECONDS, true).toString();
    }

    /**
     * 返回开始计时的时间戳
     *
     * @return 时间戳
     */
    public long getStartMillis() {
        return this.startMillis;
    }

    /**
     * 计时器暂停类
     */
    private static class PauseTime {

        /** 暂停用时(单位：毫秒) */
        private long pauseMillis;

        /** 暂停开始时间(单位：毫秒) */
        private long beginPauseMills;

        /** 暂停状态返回true */
        private boolean isStart;

        /**
         * 初始化
         */
        public PauseTime() {
            this.reset();
        }

        /**
         * 重置所有参数,恢复到初始状态
         */
        public void reset() {
            this.isStart = false;
            this.beginPauseMills = 0;
            this.pauseMillis = 0;
        }

        /**
         * 暂停状态,返回TRUE
         */
        public boolean isStart() {
            return isStart;
        }

        /**
         * 开始暂停
         */
        public void start() {
            this.isStart = true;
            this.beginPauseMills = System.currentTimeMillis();
        }

        /**
         * 结束暂停
         */
        public void stop() {
            this.isStart = false;
            long mills = System.currentTimeMillis() - this.beginPauseMills;
            this.pauseMillis += mills;
        }

        /**
         * 获取暂停时间,单位毫秒
         */
        public long getPauseMillis() {
            if (this.isStart()) {
                long mills = System.currentTimeMillis() - this.beginPauseMills;
                // 暂停用时 = 暂停用时 + 当前用时
                mills += this.pauseMillis;
                return mills;
            } else {
                return this.pauseMillis;
            }
        }
    }

}
