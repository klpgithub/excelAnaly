package com.oracle.localThreadTest;

public class TestLocal {

    // ①通过匿名内部类覆盖ThreadLocal的initialValue()方法，指定初始值
    private static ThreadLocal<Integer> seqNum = new ThreadLocal<Integer>() {

        public Integer initialValue() {
            return 0;
        };
    };

    // ②获取下一个序列值
    public int getNextNum() {
        seqNum.set(seqNum.get() + 1);
        return seqNum.get();
    }

    private static class TestClient extends Thread {
        private TestLocal testLocal;

        public TestClient(TestLocal testLocal) {
            this.testLocal = testLocal;
        }

        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                // 每个线程打出三个值
                System.out.println("thread [ " + Thread.currentThread().getName() + " ] ---> testLocal [ "
                        + testLocal.getNextNum() + " ] ");
            }
        }
    }

    public static void main(String[] args) {
        TestLocal testLocal = new TestLocal();
        // ③ 3个线程共享sn，各自产生序列号
        TestClient t1 = new TestClient(testLocal);
        TestClient t2 = new TestClient(testLocal);
        TestClient t3 = new TestClient(testLocal);
        t1.start();
        t2.start();
        t3.start();
    }

}
