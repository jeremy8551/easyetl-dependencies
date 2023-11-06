package icu.etl.util;

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
     * 开始计时/重新开始计时 <br>
     * <br>
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
     * 返回计时器用时时间（单位：毫秒） <br>
     * <br>
     * 计时器用时计算公式: 当前时间 - 开始时间 - 暂停时间 <br>
     *
     * @return
     */
    public long useMillis() {
        return System.currentTimeMillis() - this.startMillis - this.pause.getPauseMillis();
    }

    /**
     * 返回计时器用时时间（单位：秒）
     *
     * @return
     */
    public long useSeconds() {
        return this.useMillis() / 1000;
    }

    /**
     * 返回计时器用时时间
     *
     * @return 格式详见 {@linkplain Dates#format(long, boolean)}
     */
    public String useTime() {
        return Dates.format(this.useSeconds(), true).toString();
    }

    /**
     * 返回计时器用时时间(单位：毫秒)
     */
    public long getStartMillis() {
        return this.startMillis;
    }

    /**
     * 计时器暂停类
     */
    private class PauseTime {

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
        public PauseTime reset() {
            this.isStart = false;
            this.beginPauseMills = 0;
            this.pauseMillis = 0;
            return this;
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
        public long start() {
            this.isStart = true;
            this.beginPauseMills = System.currentTimeMillis();
            return this.beginPauseMills;
        }

        /**
         * 结束暂停
         */
        public long stop() {
            this.isStart = false;
            long mills = System.currentTimeMillis() - this.beginPauseMills;
            this.pauseMillis += mills;
            return this.pauseMillis;
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
