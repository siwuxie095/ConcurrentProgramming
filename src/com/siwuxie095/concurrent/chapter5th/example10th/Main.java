package com.siwuxie095.concurrent.chapter5th.example10th;

/**
 * @author Jiajing Li
 * @date 2020-09-26 14:27:46
 */
public class Main {

    /**
     * 并行算法：矩阵乘法
     *
     * 众所周知，Linus 认为并行程序目前只有在服务端程序和图像处理领域有发展的空间。且不论这种说法是否正确，但从中
     * 也可以看出并发对于这两个应用领域的重要性。而对于图像处理来说，矩阵运行是其中必不可少的重要数学方法。当然，
     * 除了图像处理，矩阵运算在神经网络、模式识别等领域也有着广泛的用途。下面将介绍矩阵运算的典型代表 -- 矩阵乘法
     * 的并行化实现。
     *
     * 在矩阵乘法中，第一个矩阵的列数和第二个矩阵的行数必须是相同的。比如，矩阵 A 和矩阵 B 相乘，其中矩阵 A 为 4
     * 行 2 列，矩阵 B 为 2 行 4 列，它们相乘后，得到的是 4 行 4 列的矩阵，并且新矩阵中每一个元素为矩阵 A 和矩
     * 阵 B 对应行列的乘积求和。
     *
     * 如果需要进行并行计算，一种简单的策略是可以将 A 矩阵进行水平分割，得到子矩阵 A1 和 A2，B 矩阵进行垂直分割，
     * 得到子矩阵 B1 和 B2。此时，只要分别计算这些子矩阵的乘积，将结果进行拼接，就能得到原始矩阵 A 和 B 的乘积。
     *
     * 当然，这个过程是可以反复进行的。为了计算 A1*B1，还可以进一步将 A1 和 B1 进行分解，直到认为子矩阵的大小已
     * 经在可接受范围内。
     *
     * 以 ParallelMatrixMultiply 为例，是并行矩阵相乘的实现。这里使用 Fork/Join 框架来进行任务分解，使用
     * jMatrices 来进行矩阵计算。其中使用 jMatrices 的主要 API 如下：
     *
     * （1）Matrix：代笔一个矩阵。
     * （2）MatrixOperator.multiply(Matrix, Matrix)：矩阵相乘。
     * （3）Matrix.row()：获得矩阵的行数。
     * （4）Matrix.getSubMatrix()：获得矩阵的子矩阵。
     * （5）MatrixOperator.horizontalConcatenation(Matrix, Matrix)：将两个矩阵进行水平连接。
     * （6）MatrixOperator.verticalConcatenation(Matrix, Matrix)：将两个矩阵进行垂直连接。
     *
     * （jMatrices 官网：http://jmatrices.sourceforge.net/）
     *
     * 其中的 MatrixMultiplyTask 任务类由三个参数构成，分别是需要计算的矩阵双方，以及计算结果位于父矩阵相乘结果
     * 中的位置。
     *
     * （1）成员变量 matrix1 和 matrix2 表示要相乘的两个矩阵。
     * （2）position 表示这个乘积结果在父矩阵相乘结果中所处的位置，有 matrix1、matrix2、matrix3 和 matrix4
     * 四种。
     *
     * 代码中先对矩阵进行分割，分割后得到 matrix11、matrix12、matrix21 和 matrix22 等四个矩阵，并将它们按照
     * 规则进行子任务的创建，然后计算这些子任务，在子任务返回后，将返回的四个矩阵 matrix1、matrix2、matrix3 和
     * matrix4 拼接成新的矩阵作为最终结果。
     *
     * 如果矩阵的粒度足够小就直接进行运算而不进行分解。
     *
     * 最后随机创建两个 300*300 的随机矩阵，构造矩阵计算任务并将其提交给 ForkJoinPool 线程池，等待获取结果即可。
     */
    public static void main(String[] args) {

    }

}
