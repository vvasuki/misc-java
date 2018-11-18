/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvasuki.linearAlgebra;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.function.DoubleFunction;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.jet.math.Functions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vvasuki
 */
public class MatrixTransformer {

    public static void transformRows(DoubleMatrix2D A, DoubleMatrix1D v, String functionName) {
        int numRows = A.rows();
        Class[] argList = {double.class};
        try {
            Method f = Functions.class.getMethod(functionName, argList);
            for (int row = numRows; --row >= 0;) {
                MatrixTransformer.transformRow(A, row, (DoubleFunction) f.invoke(null, v.getQuick(row)));
            }
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(MatrixTransformer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(MatrixTransformer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MatrixTransformer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MatrixTransformer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(MatrixTransformer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void transformRow(DoubleMatrix2D A, int rowId, DoubleFunction f) {
        int numColumns = A.columns();
        for (int col = numColumns; --col >= 0;) {
            A.setQuick(rowId, col, f.apply(A.getQuick(rowId, col)));
        }
    }

    public static void transformRow(DoubleMatrix2D A, int rowId, DoubleMatrix1D x, DoubleDoubleFunction f) {
        int numColumns = A.columns();
        for (int col = numColumns; --col >= 0;) {
            A.setQuick(rowId, col, f.apply(A.getQuick(rowId, col), x.getQuick(col)));
        }
        
    }
}
