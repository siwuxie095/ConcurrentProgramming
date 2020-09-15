package com.siwuxie095.concurrent.chapter4th.example23th;

/**
 * @author Jiajing Li
 * @date 2020-09-15 07:24:18
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 挑战无锁算法：无锁的 Vector 实现
     *
     * 相对于有锁，使用无锁的方式编程更加考验一个程序员的耐心和智力。但是，无锁带来的好处也是显而易见的。第一，
     * 在高并发的情况下，它比有锁的程序拥有更好的性能；第二，它天生就是死锁免疫的。就凭借这两个优势，就值得冒险
     * 尝试使用无锁的并发。
     *
     * 这里介绍一种使用无锁方式实现的 Vector，名字叫做 LockFreeVector，来自 amino 并发包（第三方），主打的
     * 特色就是 Concurrent Building Blocks(简称 CBBs)。
     *
     * PS：Amino 官网连接：http://amino-cbbs.sourceforge.net/
     *
     * LockFreeVector 的特点是可以根据需求动态扩展其内部空间，使用了一个二维数组来表示其内部存储，如下：
     *
     * private final AtomicReferenceArray<AtomicReferenceArray<E>> buckets;
     *
     * 变量 buckets 存放所有的内部元素。从定义上看，它是一个保存着数组的数组，也就是二维数组。特别之处在于这些
     * 数组使用的都是 CAS 的原子数组。为什么使用二维数组去实现一个一维的 Vector 呢？这是为了将来 Vector 进行
     * 动态扩展时可以更加方便。众所周知，AtomicReferenceArray 内部使用 Object[] 来进行实际的数据存储，这使
     * 得动态空间增加特别的麻烦，因此使用二维数组的好处就是为了将来可以方便地增加新的元素。
     *
     * 此外，为了更有序的读写数组，定义了一个称为 Descriptor 的元素。它的作用是使用 CAS 操作写入新数据。
     *
     * 	static class Descriptor<E> {
     * 		public int size;
     * 		volatile WriteDescriptor<E> writeop;
     *
     * 		public Descriptor(int size, WriteDescriptor<E> writeop) {
     * 			this.size = size;
     * 			this.writeop = writeop;
     *      }
     *
     *
     * 		public void completeWrite() {
     * 			WriteDescriptor<E> tmpOp = writeop;
     * 			if (tmpOp != null) {
     * 				tmpOp.doIt();
     * 				writeop = null; // this is safe since all write to writeop use
     * 				// null as r_value.
     *          }
     *      }
     *   }
     *
     * 	static class WriteDescriptor<E> {
     * 		public E oldV;
     * 		public E newV;
     * 		public AtomicReferenceArray<E> addr;
     * 		public int addr_ind;
     *
     * 		public WriteDescriptor(AtomicReferenceArray<E> addr, int addr_ind,
     * 				E oldV, E newV) {
     * 			this.addr = addr;
     * 			this.addr_ind = addr_ind;
     * 			this.oldV = oldV;
     * 			this.newV = newV;
     *      }
     *
     * 		public void doIt() {
     * 			addr.compareAndSet(addr_ind, oldV, newV);
     *      }
     *  }
     *
     * 其中 Descriptor 构造方法接收两个参数，第一个为整个 Vector 的长度，第二个为一个 writeop。最终写入数据
     * 是通过 writeop 进行的，即 在 comleteWrite() 方法中调用 writeop 的 doIt() 方法
     *
     * PS：writeop 可看作 writer。
     *
     * 另外 WriteDescriptor 构造方法接收四个参数。第一个 addr 表示要修改的原子数组，第二个 addr_ind 表示要
     * 写入的数组索引位置，第三个 oldV 表示期望值，第四个 newV 表示需要写入的值。
     *
     * 在构造 LockFreeVector 时，显然需要将 buckets 和 descriptor 进行初始化。
     *
     *  private static final int FIRST_BUCKET_SIZE = 8;
     *
     *  private static final int N_BUCKET = 30;
     *
     * 	public LockFreeVector() {
     * 		buckets = new AtomicReferenceArray<AtomicReferenceArray<E>>(N_BUCKET);
     * 		buckets.set(0, new AtomicReferenceArray<E>(FIRST_BUCKET_SIZE));
     * 		descriptor = new AtomicReference<Descriptor<E>>(new Descriptor<E>(0, null));
     *  }
     *
     * 其中 N_BUCKET 是说这个 buckets 里面一共可以存放 30 个数组（由于数组无法动态增长，因此数组总数也就是不
     * 能超过 30 个）。并且将第一个数组的大小 FIRST_BUCKET_SIZE 设为 8。到这里，不免会有疑问，如果每个数组 8
     * 个元素，一共 30 个数组，岂不是一共只能存放 240 个元素吗？
     *
     * JDK 内的 Vector 在进行空间增长时，默认情况下，每次都会将总容量翻倍。因此，这里也是借鉴类似的思想，每次
     * 空间扩张，新的数组的大小为原来的两倍（即 每次空间扩展都启用一个新的数组），即 第一个数组为 8，第二个数组
     * 为 16，第三个数组就是 32，以此类推，第 30 个数组可以支持的总元素可达到 8 * 2^(30-1)
     *
     * 总的数值已经接近了 2^33，即 在 80 亿以上。因此，可以满足一般的应用。
     *
     * 当有元素需要加入 LockFreeVector 时，使用一个名为 push_back() 的方法，将元素压入 Vector 的最后一个位
     * 置。这个操作显然就是 LockFreeVector 最为核心的方法，也是最能体现 CAS 使用特点的方法，它的实现如下：
     *
     * 	public void push_back(E e) {
     * 		Descriptor<E> desc;
     * 		Descriptor<E> newd;
     * 		do {
     * 			desc = descriptor.get();
     * 			desc.completeWrite();
     *
     * 			int pos = desc.size + FIRST_BUCKET_SIZE;
     * 			int zeroNumPos = Integer.numberOfLeadingZeros(pos);
     * 			int bucketInd = zeroNumFirst - zeroNumPos;
     * 			if (buckets.get(bucketInd) == null) {
     * 				int newLen = 2 * buckets.get(bucketInd - 1).length();
     * 				if (debug)
     * 					System.out.println("New Length is:" + newLen);
     * 				buckets.compareAndSet(bucketInd, null, new AtomicReferenceArray<E>(newLen));
     *          }
     *
     * 			int idx = (0x80000000>>>zeroNumPos) ^ pos;
     * 			newd = new Descriptor<E>(desc.size + 1, new WriteDescriptor<E>(buckets.get(bucketInd), idx, null, e));
     * 		} while (!descriptor.compareAndSet(desc, newd));
     * 		descriptor.get().completeWrite();
     *  }
     *
     * 可以看到，这个方法主体部分是一个 do-while 循环，用来不断尝试对 dscriptor 的设置。也就是通过 CAS 保证了
     * descriptor 的一致性和安全性。当跳出循环后，使用 descriptor 将数据真正地写入数组中。这个 descriptor 写
     * 入的数据由 WriteDescriptor 的构造方法决定。
     *
     * 在循环开始时，使用 descriptor 先将数据写入数组，是为了防止上一个线程设置完 descriptor 后（跳出了循环），
     * 还没来得及执行写入（跳出循环后的写入），因此，做一次预防性的操作。
     *
     * 因为限制要将元素 e 压入 Vector，所以必须首先知道这个 e 应该放在哪个位置。由于目前使用了二维数组，自然就需
     * 要知道 e 所在哪个数组（buckets 中的下标）和数组中的下标。
     *
     * 这里通过当前 Vector 的大小（desc.size），来计算新的元素应该落入哪个数组中，然后使用了位运算计算最终的下标。
     *
     * 可以看到，LockFreeVector 每次都会成倍的扩容，它的第一个数组长度为 8，第二个数组长度为 16，第三个数组长度
     * 为 32，以此类推。它们的二进制表示如下：
     *
     * 00000000 00000000 00000000 00001000      第一个数组大小，28 个前导零
     * 00000000 00000000 00000000 00010000      第二个数组大小，27 个前导零
     * 00000000 00000000 00000000 00100000      第三个数组大小，26 个前导零
     * 00000000 00000000 00000000 01000000      第四个数组大小，25 个前导零
     *
     * ...
     *
     * 它们之和就是整个 LockFreeVector 的总大小，因此，如果每一个数组都恰好填满，那么总大小应该是类似如下数值：
     * （以 4 个数组填满为例）
     *
     * 00000000 00000000 00000000 01111000      4 个数组都恰好填满时的大小
     *
     * 导致这个数字进位的最小条件，就是加上二进制的 1000。而这个数字正好是 8（FIRST_BUCKET_SIZE 就是 8）。这就
     * 是 int pos = desc.size + FIRST_BUCKET_SIZE; 这一行代码的意义。它可以使得数组的大小发生一次二进制的进
     * 位（如果不进位，说明还在当前数组中），进位后前导零的数量就会发生变化。而元素所在的数组，和 pos 的前导零直接
     * 相关。每进行一次数组扩容，它的前导零就会减 1。如果从来没有扩容过，它的前导零就是 28 个。以后，逐级减 1。这
     * 就是 int zeroNumPos = Integer.numberOfLeadingZeros(pos); 获得 pos 前导零的原因。然后通过 pos 的前
     * 导零可以立即定位使用哪个数组（也就是得到了 bucketId 的值）。
     *
     * 通过 buckets.get(bucketInd) == null 判断这个数组是否存在。如果不存在，则创建这个数组，大小为前一个数组
     * 的两倍，并把它设置到 buckets 中。
     *
     * 接着再看一下元素没有恰好填满的情况：
     *
     * 00000000 00000000 00000000 00001000      第一个数组大小，28 个前导零
     * 00000000 00000000 00000000 00010000      第二个数组大小，27 个前导零
     * 00000000 00000000 00000000 00100000      第三个数组大小，26 个前导零
     * 00000000 00000000 00000000 00000001      第四个数组大小，只有一个元素
     *
     * 那么总大小如下：
     *
     * 00000000 00000000 00000000 00111001
     *
     * 总个数加上二进制 1000 后，得到：
     *
     * 00000000 00000000 00000000 01000001
     *
     * 显然，通过前导零可以定位到第四个数组。而剩余位，就表示元素在当前数组内的偏移量（也就是数组下标）。根据这个理论
     * 就可以通过 pos 计算这个元素应该放在给定数组的哪个位置。通过 int idx = (0x80000000>>>zeroNumPos) ^ pos;
     * 获得 pos 的除了第一位数字 1 以外的其他位的数值。因此，pos 的前导零可以表示元素所在的数组，而 pos 后面几位则
     * 表示元素所在这个数组中的位置，也就得到了元素所在位置 idx。
     *
     * 这样，就已经得到了新元素位置的全部信息，剩下的就是将这些信息传递给 Descriptor，让它在给定的位置把元素 e 安置
     * 上去即可。通过 CAS 操作，来保证写入正确性。
     *
     * 如下是 get() 操作的实现：
     *
     *  @Override
     * 	public E get(int index) {
     * 		int pos = index + FIRST_BUCKET_SIZE;
     * 		int zeroNumPos = Integer.numberOfLeadingZeros(pos);
     * 		int bucketInd = zeroNumFirst - zeroNumPos;
     * 		int idx = (0x80000000>>>zeroNumPos) ^ pos;
     * 		return buckets.get(bucketInd).get(idx);
     * 	}
     *
     * 在 get() 的实现中，使用了相同的算法获得所需元素的数组以及数组中的索引下标。这里简单地通过 buckets 定位到对应
     * 的元素即可。
     *
     * 对于 Vector 来说，两个重要的方法 push_back() 和 get() 就已经实现了。其他方法也是类似，这里不再赘述。
     */
    public static void main(String[] args) {

    }

}
