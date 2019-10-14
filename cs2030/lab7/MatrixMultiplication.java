import java.util.concurrent.RecursiveTask;

class MatrixMultiplication extends RecursiveTask<Matrix> {

  /** The fork threshold. */
  private static final int FORK_THRESHOLD = 128;

  /** The first matrix to multiply with. */
  private Matrix m1;

  /** The second matrix to multiply with. */
  private Matrix m2;

  /** The starting row of m1. */
  private int m1Row;

  /** The starting col of m1. */
  private int m1Col;

  /** The starting row of m2. */
  private int m2Row;

  /** The starting col of m2. */
  private int m2Col;

  /**
   * The dimension of the input (sub)-matrices and the size of the output
   * matrix.
   */
  private int dimension;

  double[][] m;

  private static final int THRESHOLD = 32;

  /**
   * A constructor for the Matrix Multiplication class.
   * @param  m1 The matrix to multiply with.
   * @param  m2 The matrix to multiply with.
   * @param  m1Row The starting row of m1.
   * @param  m1Col The starting col of m1.
   * @param  m2Row The starting row of m2.
   * @param  m2Col The starting col of m2.
   * @param  dimension The dimension of the input (sub)-matrices and the size
   *     of the output matrix.
   */
  MatrixMultiplication(Matrix m1, Matrix m2, int m1Row, int m1Col, int m2Row,
                       int m2Col, int dimension) {
    this.m1 = m1;
    this.m2 = m2;
    this.m1Row = m1Row;
    this.m1Col = m1Col;
    this.m2Row = m2Row;
    this.m2Col = m2Col;
    this.dimension = dimension;
  }


  @Override
  public Matrix compute() {
    // If the matrix is small enough, just multiple non-recursively.
    if (dimension <= THRESHOLD) {
      return Matrix.nonRecursiveMultiply(m1, m2, m1Row, m1Col, m2Row, m2Col, dimension);
    }

    // Else, cut the matrix into four blocks of equal size, recursively
    // multiply then sum the multiplication result.
    int size = dimension / 2;
    MatrixMultiplication a11b11 = new MatrixMultiplication(m1, m2, m1Row, m1Col, m2Row,
        m2Col, size);
    MatrixMultiplication a12b21 = new MatrixMultiplication(m1, m2, m1Row, m1Col + size,
        m2Row + size, m2Col, size);


    MatrixMultiplication a11b12 = new MatrixMultiplication(m1, m2, m1Row, m1Col, m2Row,
        m2Col + size, size);
    MatrixMultiplication a12b22 = new MatrixMultiplication(m1, m2, m1Row, m1Col + size,
        m2Row + size, m2Col + size, size);


    MatrixMultiplication a21b11 = new MatrixMultiplication(m1, m2, m1Row + size, m1Col,
        m2Row, m2Col, size);
    MatrixMultiplication a22b21 = new MatrixMultiplication(m1, m2, m1Row + size, m1Col + size,
        m2Row + size, m2Col, size);


    MatrixMultiplication a21b12 = new MatrixMultiplication(m1, m2, m1Row + size, m1Col,
        m2Row, m2Col + size, size);
    MatrixMultiplication a22b22 = new MatrixMultiplication(m1, m2, m1Row + size, m1Col + size,
        m2Row + size, m2Col + size, size);

    a11b11.fork();
    a12b21.fork();
    a11b12.fork();
    a12b22.fork();
    a21b11.fork();
    a22b21.fork();
    a21b12.fork();
    Matrix a22b22M = a22b22.compute();

    Matrix result = new Matrix(dimension);
    for (int i = 0; i < size; i++) {
      double[] m1m = a11b11.join().m[i];
      double[] m2m = a12b21.join().m[i];
      double[] r1m = result.m[i];
      for (int j = 0; j < size; j++) {
        r1m[j] = m1m[j] + m2m[j];
      }
    }
    for (int i = 0; i < size; i++) {
      double[] m1m = a11b12.join().m[i];
      double[] m2m = a12b22.join().m[i];
      double[] r1m = result.m[i];
      for (int j = 0; j < size; j++) {
        r1m[j + size] = m1m[j] + m2m[j];
      }
    }
    for (int i = 0; i < size; i++) {
      double[] m1m = a21b11.join().m[i];
      double[] m2m = a22b21.join().m[i];
      double[] r1m = result.m[i + size];
      for (int j = 0; j < size; j++) {
        r1m[j] = m1m[j] + m2m[j];
      }
    }
    for (int i = 0; i < size; i++) {
      double[] m1m = a21b12.join().m[i];
      double[] m2m = a22b22M.m[i];
      double[] r1m = result.m[i + size];
      for (int j = 0; j < size; j++) {
        r1m[j + size] = m1m[j] + m2m[j];
      }
    }
    return result;
  }
}
