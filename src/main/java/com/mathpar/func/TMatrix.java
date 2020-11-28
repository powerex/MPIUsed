/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.func;
/**
 *
 * @author Sveta Tararova
 */
/**
 * ����� TMatrix �������� � ���� ������������ ���  ��������� � ����������
 * ������, ����� ����� � �������� ������� �� �������.
 */
public class TMatrix {
  int a[][];
  int b[];
  int[] s;       // index for rows
  int[] d;      //  ibdex for columns
  /** Creates a new instance of TMatrix */
  void Init() {   // initialisation of s and d indexes
    s = new int[a.length];
    for (int c = 0; c < a.length; c++) s[c] = c;
    d = new int[a[0].length];
    for (int c = 0; c < a[0].length; c++) d[c] = c;
  }
  // ����������� ��� ���������� �������
  public TMatrix(int[][] am) {
    a = (int[][]) am.clone();
  }
  // ����������� ��� ����������� �������
  public TMatrix(int[] bm) {
    b = (int[]) bm.clone();
  }
  public TMatrix(int[][] am, int[] bm) {
    a = (int[][]) am.clone();
    b = (int[]) bm.clone();
  }
//  public int[][] ArrayAddNewArray(int[][] mass){
//    int[][] temporary = new int[a.length][];
//    for(int i =0; i<a.length; i++){
//      temporary[i] = new int[a[i].length];
//      System.arraycopy(a[i],0,temporary[i],0,a[i].length);
//    }
//    a = new int[a.length][];
//    for(int i =0; i<stroka.length; i++){
//      contextF[i] = new int[cfa[i].length];
//      System.arraycopy(cfa[i],0,contextF[i],0,cfa[i].length);
//    }
//  }
  /**
   * ��������� solve ������ ������� ��������� ������� �����. �� ���� ��������
   * ������� (int[][] a), ��������� �� �������������� ������� �������� ���������,
   * � ������� ��������� ������ (int[] b).
   * @return int[] x - ������� ������� �������� ���������
   */
  public int[] solve() throws Exception {
    int m = b.length;
    int n = a[0].length;
    int x[] = new int[n];
    Init();
    try {
      //forward way  --- to upper triangular form ---
      for (int k = 0; k < n; k++) {
        int sk = s[k], dk = d[k];
        if (a[sk][dk] == 0) {                    //  nonzero  pivote element
          int row = k + 1;
          while ((row < n) && (a[s[row]][dk] == 0)) row++;
          if (row == n) return x;            //OUTPUT 0-vector
          sk = s[row];
          s[row] = s[k];
          s[k] = sk;  //exchange of rows
        }
        for (int row = k + 1; row < n; row++) {
          int sr = s[row];
          double L = -a[sr][dk] / a[sk][dk];
          for (int col = k; col < m; col++) a[sr][d[col]] += a[sk][d[col]] * L;
          b[sr] += b[sk] * L;
        }
      }
      if (a[s[n - 1]][d[m - 1]] == 0)  return x;         //OUTPUT 0-vector
      // backward way  --- to diagonal form   (n=m)
      x[d[n - 1]] = b[s[n - 1]] / a[s[n - 1]][d[n - 1]];
      int sk = 0;
      int dk = 0;
      for (int k = n - 2; k >= 0; k--) {
        sk = s[k];
        dk = d[k];
        double P = 0;
        for (int col = k + 1; col < n; col++) P += x[d[col]] * a[sk][d[col]];
        x[dk] = (int) (b[sk] - P) / a[sk][dk];
      }
    } catch (Exception ex) {}
    return x;
  }
  /**
   * ��������� Verify ��������� �������� �� ������ ����������
   * @param int[] x - ����� ������� �������� ���������
   * @param double eps - ��������
   * @return boolean (true or false)
   */
  public boolean Verify(int[] x, double eps) {
    int len = a.length;
    if ((len != b.length) || (len != x.length)) return false;
    for (int row = 0; row < a.length; row++) {
      if (a[row].length != len) return false;
      double sum = 0;
      for (int col = 0; col < len; col++) sum += a[row][col] * x[col];
      if (sum - b[row] > eps) return false;
    }
    return true;
  }
  }


//  Ax=b;   F=(A,b); S=F.toEchelomForm; S.isSolvable(); -- true, false --- VectorS==S.oneSysSolvForFraction; (Fraction)
