package vvasuki.probabilisticModels;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.math.Functions;
import java.util.Arrays;
import vvasuki.linearAlgebra.MatrixTransformer;

public class LogisticMultiClass {

    public static Logger logger = Logger.getLogger(LogisticMultiClass.class.getName());

    public static List getNegLogLikelihoodWithDerivatives(DoubleMatrix1D x, double y_x, double[][] B, DoubleMatrix1D Bx, DoubleMatrix1D exp_Bx, int nargout) {
        //      Process inputs
        //   Bx, exp_Bx is passed for faster execution.
        int numStatesLess1 = B.length;
        int dim = B[0].length;
        // assert(length(y_x)==1, 'Bad input!');
        double term1 = 0;
        double normalizationFactor = 0;
        List outputList = new ArrayList();

        normalizationFactor = (1 + exp_Bx.aggregate(Functions.plus, Functions.identity));
        if (y_x <= numStatesLess1) {
            term1 = Bx.get((int) y_x - 1);
        }
        double negLogLikelihood = -term1 + Math.log(normalizationFactor);
        outputList.add(new Double(negLogLikelihood));

        if (nargout > 1) {
            double[][] tmpMatrix = {x.toArray()};
            DoubleMatrix2D GradientNegLogLikelihood = new DenseDoubleMatrix2D(tmpMatrix);
            GradientNegLogLikelihood = DoubleFactory2D.dense.repeat(GradientNegLogLikelihood, numStatesLess1, 1);

            DoubleMatrix2D HessianDiagNegLogLikelihood = GradientNegLogLikelihood.copy();
            HessianDiagNegLogLikelihood.assign(Functions.pow(2));

            DoubleMatrix1D tmpNormalizationVector = exp_Bx.assign(Functions.div(normalizationFactor));
            MatrixTransformer.transformRows(GradientNegLogLikelihood, tmpNormalizationVector, "mult");
            if (y_x <= numStatesLess1) {
                MatrixTransformer.transformRow(GradientNegLogLikelihood, (int) y_x - 1, x, Functions.minus);
            }
            DoubleMatrix1D tmpNormalizationVector2 = tmpNormalizationVector.copy();
            tmpNormalizationVector2.assign(Functions.pow(2));
            tmpNormalizationVector.assign(tmpNormalizationVector2, Functions.minus);
            MatrixTransformer.transformRows(HessianDiagNegLogLikelihood, tmpNormalizationVector, "mult");

            outputList.add(GradientNegLogLikelihood);
            outputList.add(HessianDiagNegLogLikelihood);

            if (nargout > 3) {
                throw new UnsupportedOperationException("Not yet implemented");
                // Compute HessianNegLogLikelihood.
                // H = -exp_Bx*exp_Bx'/(normalizationFactor^2) + diag(exp_Bx)/normalizationFactor;
                // varargout(3) = kron(H, x*x');
            }
        }
        return outputList;
    }

    public static List getAvgNegLogLikelihoodWithDerivatives(double[][] X_in, double[] y, double[][] B, double[][] BX_in, double[][] exp_BX_in, int nargout) {
        // Efficiency: Making the getNegLogLikelihoodWithDerivatives call inline actually decreases the speed!!

        //      Process inputs
        DoubleMatrix2D X = new DenseDoubleMatrix2D(X_in);
        DoubleMatrix2D BX = new DenseDoubleMatrix2D(BX_in);
        DoubleMatrix2D exp_BX = new DenseDoubleMatrix2D(exp_BX_in);
        int dim = X.rows();
        int numSamples = X.columns();
        int numStatesLess1 = B.length;

        double avgNegLogLikelihood = 0;
        DoubleMatrix2D avgGradientNegLogLikelihood = new DenseDoubleMatrix2D(numStatesLess1, dim);
        avgGradientNegLogLikelihood.assign(0);
        DoubleMatrix2D avgHessianDiagNegLogLikelihood = new DenseDoubleMatrix2D(numStatesLess1, dim);
        avgHessianDiagNegLogLikelihood.assign(0);
        if (nargout > 3) {
            DoubleMatrix2D avgHessianNegLogLikelihood = new DenseDoubleMatrix2D(numStatesLess1 * dim, numStatesLess1 * dim);
            avgHessianNegLogLikelihood.assign(0);
        }

        for (int i = 1; i <= numSamples; i++) {
            DoubleMatrix1D x = X.viewColumn(i - 1);
            int y_x = (int) y[i - 1];
            DoubleMatrix1D exp_Bx = exp_BX.viewColumn(i - 1);
            DoubleMatrix1D Bx = BX.viewColumn(i - 1);
            List outputListTmp = LogisticMultiClass.getNegLogLikelihoodWithDerivatives(x, y_x, B, Bx, exp_Bx, nargout);
            Double NegLogLikelihood = (Double) outputListTmp.get(0);
            if (nargout > 1) {
                // if(nargout >3)
                //     avgHessianNegLogLikelihood = avgHessianNegLogLikelihood + outputListTmp.get(3);
                // }
                DoubleMatrix2D GradientNegLogLikelihood = (DoubleMatrix2D) outputListTmp.get(1);
                DoubleMatrix2D HessianDiagNegLogLikelihood = (DoubleMatrix2D) outputListTmp.get(2);

                avgGradientNegLogLikelihood.assign(GradientNegLogLikelihood, Functions.plus);
                avgHessianDiagNegLogLikelihood.assign(HessianDiagNegLogLikelihood, Functions.plus);
            }
            avgNegLogLikelihood += NegLogLikelihood.doubleValue();
        }
        avgNegLogLikelihood = avgNegLogLikelihood / (numSamples);
        List outputList = new ArrayList();
        outputList.add(new Double(avgNegLogLikelihood));

        if (nargout > 1) {
            avgGradientNegLogLikelihood.assign(Functions.div(numSamples));
            avgHessianDiagNegLogLikelihood.assign(Functions.div(numSamples));
            outputList.add(avgGradientNegLogLikelihood.toArray());
            outputList.add(avgHessianDiagNegLogLikelihood.toArray());
            // if(nargout>3) {
            //     outputList.add(avgHessianNegLogLikelihood.assign(Functions.div(numSamples)));
            // }
        }
        return outputList;
        //      B
        //      reshape(avgGradientNegLogLikelihood, size(B))
    }

    public static void main(String[] agrs) {
        logger.info("Beginning test");
    }
}
