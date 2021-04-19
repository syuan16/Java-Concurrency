import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class BlockingQueue<E> {
    private int capacity;
    private Queue<E> queue;
    private ReentrantLock lock = new ReentrantLock(true);
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public BlockingQueue(int size) {
        capacity = size;
        queue = new LinkedList();
    }

    public void put(E e) {
        lock.lock();
        try {

            // while instead of if here because when the thread is waken up
            // by the condition, it might not be full anymore because another
            // waken thread puts into the queue
            while (queue.size() == capacity) {
                notFull.await();
            }
            queue.add(e);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public E take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            E e = queue.poll();
            notFull.signalAll();
            return e;
        } finally {
            lock.unlock();
        }
    }
}