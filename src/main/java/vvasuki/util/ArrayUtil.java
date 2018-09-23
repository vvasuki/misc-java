package hri.speech.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtil {
  public static String makeString(Object[] arrStrings, String sep) {
    return makeString(Arrays.asList(arrStrings), sep);
  }

  public static String makeString(List arrStrings, String sep) {
    if (arrStrings == null)
      return "null";
    String strOut = "";
    for (Object sWord : arrStrings)
      strOut = strOut + sep + sWord;
    if (arrStrings.size() > 0) {
      strOut = strOut.substring(sep.length());
    }
    return strOut;
  }

  public static <T> List<T> intersect(List<T> lstA, List<T> lstB) {
    List<T> lstTmp = new ArrayList<T>();
    lstTmp.addAll(lstA);
    lstTmp.retainAll(lstB);
    return lstTmp;
  }

  public static int getMaxIndex(Double[] arr) {
    return getMaxIndex(Arrays.asList(arr));
  }

  public static int getMaxIndex(List<Double> arr) {
    Double max = arr.get(0);
    int maxIdx = 0;
    for (int i = 1; i < arr.size(); i++) {
      if (arr.get(i) > max) {
        max = arr.get(i);
        maxIdx = i;
      }
    }
    return maxIdx;
  }

  public static double getMean(List<Double> lst) {
    return getSum(lst) / lst.size();
  }

  private static double getSum(List<Double> lst) {
    double sum = 0;
    for (Double num : lst) {
      sum = sum + num;
    }
    return sum;
  }

}
