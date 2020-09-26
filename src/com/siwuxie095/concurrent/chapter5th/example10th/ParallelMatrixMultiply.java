package com.siwuxie095.concurrent.chapter5th.example10th;

import org.jmatrices.dbl.Matrix;
import org.jmatrices.dbl.MatrixFactory;
import org.jmatrices.dbl.operator.MatrixOperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author Jiajing Li
 * @date 2020-09-26 15:32:01
 */
@SuppressWarnings("all")
class ParallelMatrixMultiply {

    private static final int GRANULARITY = 3;

    static class MatrixMultiplyTask extends RecursiveTask<Matrix> {

        private Matrix matrix1;

        private Matrix matrix2;

        private String position;

        public MatrixMultiplyTask(Matrix matrix1, Matrix matrix2, String position) {
            this.matrix1 = matrix1;
            this.matrix2 = matrix2;
            this.position = position;
        }

        @Override
        protected Matrix compute() {
            System.out.println(Thread.currentThread().getId() + ":" + Thread.currentThread().getName() + " start");
            // 小于粒度限制
            if (matrix1.rows() <= ParallelMatrixMultiply.GRANULARITY || matrix2.cols() <= ParallelMatrixMultiply.GRANULARITY) {
                Matrix matrix = MatrixOperator.multiply(matrix1, matrix2);
                return matrix;
                // 如果不是，那么继续分割矩阵
            } else {
                // 左乘的矩阵横向分割
                int rows1 = matrix1.rows();
                int cols1 = matrix1.cols();
                Matrix matrix11 = matrix1.getSubMatrix(1, 1, rows1 / 2, cols1);
                Matrix matrix12 = matrix1.getSubMatrix(rows1 / 2 + 1, 1, rows1, cols1);
                // 右乘的矩阵纵向分割
                int rows2 = matrix2.rows();
                int cols2 = matrix2.cols();
                Matrix matrix21 = matrix2.getSubMatrix(1, 1, rows2, cols2 / 2);
                Matrix matrix22 = matrix2.getSubMatrix(1, cols2 / 2 + 1, rows2, cols2);

                List<MatrixMultiplyTask> subTasks = new ArrayList<>();
                MatrixMultiplyTask tmp = null;
                tmp = new MatrixMultiplyTask(matrix11, matrix21, "matrix1");
                subTasks.add(tmp);
                tmp = new MatrixMultiplyTask(matrix11, matrix22, "matrix2");
                subTasks.add(tmp);
                tmp = new MatrixMultiplyTask(matrix12, matrix21, "matrix3");
                subTasks.add(tmp);
                tmp = new MatrixMultiplyTask(matrix12, matrix22, "matrix4");
                subTasks.add(tmp);
                for (MatrixMultiplyTask subTask : subTasks) {
                    subTask.fork();
                }
                Map<String, Matrix> matrixMap = new HashMap<>();
                for (MatrixMultiplyTask subTask : subTasks) {
                    matrixMap.put(subTask.position, subTask.join());
                }
                Matrix tmp1 = MatrixOperator.horizontalConcatenation(matrixMap.get("matrix1"), matrixMap.get("matrix2"));
                Matrix tmp2 = MatrixOperator.horizontalConcatenation(matrixMap.get("matrix3"), matrixMap.get("matrix4"));
                Matrix res = MatrixOperator.verticalConcatenation(tmp1, tmp2);
                return res;
            }
        }

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Matrix matrix1 = MatrixFactory.getRandomMatrix(300, 300, null);
        Matrix matrix2 = MatrixFactory.getRandomMatrix(300, 300, null);
        ForkJoinTask<Matrix> forkJoinTask = forkJoinPool.submit(new MatrixMultiplyTask(matrix1, matrix2, null));
        Matrix res = forkJoinTask.get();
        System.out.println(res);

    }

}
