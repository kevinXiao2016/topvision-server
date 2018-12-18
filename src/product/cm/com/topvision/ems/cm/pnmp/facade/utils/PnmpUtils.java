package com.topvision.ems.cm.pnmp.facade.utils;

import com.topvision.ems.cm.cmpoll.facade.domain.Complex;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jay on 17-8-15.
 */
public class PnmpUtils {
    private static NumberFormat nf = new DecimalFormat("#.###");
    public static Double mte(Complex[] taps) {
        Complex mainTap = taps[15];
        return mainTap.abs();
    }
    public static Double preMTE(Complex[] taps) {
        Double re = 0D;
        for (int i = 8; i < 15; i++) {
            Complex tap = taps[i];
            re += tap.abs();
        }
        return re;
    }
    public static Double postMTE(Complex[] taps) {
        Double re = 0D;
        for (int i = 16; i < 32; i++) {
            Complex tap = taps[i];
            re += tap.abs();
        }
        return re;
    }
    public static Double tte(Complex[] taps) {
        Double re = 0D;
        for (int i = 8; i < 32; i++) {
            Complex tap = taps[i];
            re += tap.abs();
        }
        return re;
    }
    public static Double mtc(Complex[] taps) {
        Double tte = tte(taps);
        Double mte = mte(taps);
        Double re = 10 * Math.log(tte / mte);
        if (Double.isInfinite(re) || Double.isNaN(re)) {
            return null;
        } else {
            return re;
        }
    }
    public static Double mtr(Complex[] taps) {
        Double tte = tte(taps);
        Double mte = mte(taps);
        Double re = 10 * Math.log(mte / (tte - mte));
        if (Double.isInfinite(re) || Double.isNaN(re)) {
            return null;
        } else {
            return re;
        }
    }
    public static Double mrLevel(Complex[] taps) {
        Double tte = tte(taps);
        Double postMTE = postMTE(taps);
        Double re = 10 * Math.log(postMTE / tte);
        if (Double.isInfinite(re) || Double.isNaN(re)) {
            return null;
        } else {
            return re;
        }
    }
    public static Double nmtTER(Complex[] taps) {
        Double tte = tte(taps);
        Double mte = mte(taps);
        Double re = 10 * Math.log((tte - mte) / tte );
        if (Double.isInfinite(re) || Double.isNaN(re)) {
            return null;
        } else {
            return re;
        }
    }
    public static Double preMTTER(Complex[] taps) {
        Double preMTE = preMTE(taps);
        Double tte = tte(taps);
        Double re = 10 * Math.log(preMTE / tte );
        if (Double.isInfinite(re) || Double.isNaN(re)) {
            return null;
        } else {
            return re;
        }
    }
    public static Double postMTTER(Complex[] taps) {
        Double postMTE = postMTE(taps);
        Double tte = tte(taps);
        Double re = 10 * Math.log(postMTE / tte );
        if (Double.isInfinite(re) || Double.isNaN(re)) {
            return null;
        } else {
            return re;
        }
    }
    public static Double ppesr(Complex[] taps) {
        Double preMTE = preMTE(taps);
        Double postMTE = postMTE(taps);
        Double re = 10 * Math.log(preMTE / postMTE );
        if (Double.isInfinite(re) || Double.isNaN(re)) {
            return null;
        } else {
            return re;
        }
    }
    public static Double tdr(Complex[] taps,Long channelWidth) {
        int maxIndex = 0;
        Double max = 0D;
        for (int i = 0; i < taps.length; i++) {
            Complex tap = taps[i];
            Double te = tap.abs();
            if (te > max) {
                max = te;
                maxIndex = i;
            }
        }
        int x1 = maxIndex;
        Double y0 = taps[maxIndex - 1].abs();
        Double y1 = taps[maxIndex].abs();
        Double y2 = taps[maxIndex + 1].abs();
        Double a = (y0 - 2 * y1 + y2) / 2;
        Double xm = (y0 - y2) / (4 * a);
        Double xOut = x1 + xm;
        Double delayPerSymbol;
        if (channelWidth == 1600000) {
            delayPerSymbol = 12.5;
        } else if (channelWidth == 3200000) {
            delayPerSymbol = 6.25;
        } else if (channelWidth == 6400000)  {
            delayPerSymbol = 3.125;
        } else {
            delayPerSymbol = 6.25;
        }
        Double re = (xOut - 8) * delayPerSymbol /16 * 0.000001 * 299792458;
        if (Double.isInfinite(re) || Double.isNaN(re)) {
            return null;
        } else {
            return re;
        }
    }

    public static Double[] amplitudes(Complex[] x) {
        Double[] re = new Double[x.length - 8];
        Double tte = tte(x);
        for (int i = 0,j = 8; j < x.length; i++ , j++) {
            re[i] = 20 *  Math.log10(x[j].abs()/tte);
            if (Double.isInfinite(re[i]) || Double.isNaN(re[i])) {
                re[i] = -60D;
            }
        }
        return re;
    }

    public static Double[] abs(Complex[] x) {
        Double[] re = new Double[x.length];
        for (int i = 0; i < x.length; i++) {
            re[i] = x[i].abs();
        }
        return re;
    }

    private static Double[] sqrt(Double[] x) {
        Double[] re = new Double[x.length];
        for (int i = 0; i < x.length; i++) {
            re[i] = Math.sqrt(x[i]);
        }
        return re;
    }

    public static Complex[] subtract(Complex[] x1, Complex[] x2) {
        Complex[] re = new Complex[x1.length];
        for (int i = 0; i < x1.length; i++) {
            re[i] = x1[i].minus(x2[i]);
        }
        return re;
    }

    public static Complex[] divide(Complex[] x,Complex[] y) {
        Complex[] re = new Complex[x.length];
        for (int i = 0; i < x.length; i++) {
            re[i] = x[i].divides(y[i]);
        }
        return re;
    }

    private static Double[] normalizing(Double[] x,Double n) {
        Double[] re = new Double[x.length];
        for (int i = 0; i < x.length; i++) {
            re[i] = 10 * Math.log(x[i] / n);
        }
        return re;
    }

    public static Complex[] normalizing(Complex[] x,Complex n) {
        Complex[] re = new Complex[x.length];
        for (int i = 0; i < x.length; i++) {
            re[i] = x[i].divides(n);
        }
        return re;
    }

    public static Double[] freqResponse(Complex[] taps) {
        Complex mainTap = taps[15];
        Complex[] fftResult = FFT.fft(taps);
        Double[] absResult = abs(fftResult);
        Double[] normalizing = normalizing(absResult, mainTap.re());
        Double[]  shiftResult = FFT.fftshift(normalizing,16);
        return shiftResult;
    }

    public static Complex[] freqResponse(String taps) {
        PreEqualizationParam p = new PreEqualizationParam();
        p.parse(taps);
        Complex[] x = p.toArray();
        Complex[] shift = FFT.fftshift(x, 16);
        Complex[] guiyi = PnmpUtils.normalizing(shift, shift[31]);
        Complex[] yesuo = PnmpUtils.normalizing(guiyi, new Complex(5, 0));
        Complex[] freqResponse = FFT.fft(yesuo);
        return freqResponse;
    }

    public static double correlationGroup(Complex[] src, Complex[] dest) {
        Complex[] divide = PnmpUtils.divide(src, dest);
        Complex[] ifft = FFT.ifft(divide);
        Complex[] ishift = FFT.fftshift(ifft, 17);
        double mtr = PnmpUtils.mtr(ishift);
        return mtr;
    }

    public static String arrayToString(Double[] array) {
        if (array == null || array.length <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Double tapAmplitude : array) {
            sb.append(nf.format(tapAmplitude));
            sb.append(",");
        }
        return sb.substring(0,sb.length() - 1);
    }

    public static String arrayToString(Complex[] array) {
        if (array == null || array.length <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Complex tap : array) {
            sb.append(tap.toString());
            sb.append(",");
        }
        return sb.substring(0,sb.length() - 1);
    }

    //        Complex[] taps = new Complex[32];
//        taps[0] = new Complex(0,0);
//        taps[1] = new Complex(0,0);
//        taps[2] = new Complex(0,0);
//        taps[3] = new Complex(0,0);
//        taps[4] = new Complex(0,0);
//        taps[5] = new Complex(0,0);
//        taps[6] = new Complex(0,0);
//        taps[7] = new Complex(0,0);
//        taps[8] = new Complex(-4,2);
//        taps[9] = new Complex(5,1);
//        taps[10] = new Complex(-11,-1);
//        taps[11] = new Complex(18,-2);
//        taps[12] = new Complex(-27,3);
//        taps[13] = new Complex(49,-6);
//        taps[14] = new Complex(-96,12);
//        taps[15] = new Complex(2041,-2);
//        taps[16] = new Complex(88,-5);
//        taps[17] = new Complex(-32,4);
//        taps[18] = new Complex(22,-1);
//        taps[19] = new Complex(-12,-1);
//        taps[20] = new Complex(8,3);
//        taps[21] = new Complex(-2,8);
//        taps[22] = new Complex(2,5);
//        taps[23] = new Complex(2,-3);
//        taps[24] = new Complex(3,0);
//        taps[25] = new Complex(-1,0);
//        taps[26] = new Complex(5,-1);
//        taps[27] = new Complex(3,3);
//        taps[28] = new Complex(7,0);
//        taps[29] = new Complex(1,-2);
//        taps[30] = new Complex(4,-5);
//        taps[31] = new Complex(-4,1);

    public static void main(String[] args) {
        String mibString = "08:01:18:00:00:3c:00:0c:ff:e7:ff:e6:00:7e:00:18:ff:9e:00:0b:00:bb:00:2d:fe:c8:ff:c3:02:d3:00:c2:7f:e6:00:00:fc:f3:ff:9c:01:85:00:41:ff:18:ff:c5:00:7d:00:30:ff:bc:00:22:00:74:00:3e:00:2d:00:11:00:30:00:05:ff:ce:ff:f1:ff:e1:00:1e:ff:fd:00:3b:00:02:ff:fc:00:3b:00:36:ff:f1:ff:f9:00:3b:ff:f4:ff:bf:00:ab";
        //08-01-18-00
        // 003c-000c
        // ffe7-ffe6
        // 007e-0018
        // ff9e-000b
        // 00bb-002d
        // fec8-ffc3
        // 02d3-00c2
        // 7fe6-0000
        // fcf3-ff9c
        // 0185-0041
        // ff18-ffc5
        // 007d-0030
        // ffbc-0022
        // 0074-003e
        // 002d-0011
        // 0030-0005
        // ffce-fff1
        // ffe1-001e
        // fffd-003b
        // 0002-fffc
        // 003b-0036
        // fff1-fff9
        // 003b-fff4
        // ffbf-00ab
        PreEqualizationParam pField = new PreEqualizationParam();
        pField.parse(mibString);
        Complex[] taps = pField.toArray();
        printArray(taps);
        Complex mainTap = taps[15];
        System.out.println("mainTap = " + mainTap);
        Complex[] fftResult = FFT.fft(taps);
        printArray(fftResult);
        Double[] absResult = abs(fftResult);
        printArray(absResult);
        Double[] normalizing = normalizing(absResult, mainTap.re());
        printArray(normalizing);
        Double[]  shiftResult = FFT.fftshift(normalizing,16);
        printArray(shiftResult);

    }

    private static void printArray(Complex[] tapAmplitudes) {
        System.out.println("");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        for (int i = 1; i <= tapAmplitudes.length; i++) {
            Complex tapAmplitude = tapAmplitudes[i - 1];
            System.out.print(tapAmplitude.toString() + ",");
            if (i % 5 == 0) {
                System.out.println("");
            }
        }
    }

    private static void printArray(Double[] tapAmplitudes) {
        System.out.println("");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        for (int i = 1; i <= tapAmplitudes.length; i++) {
            Double tapAmplitude = tapAmplitudes[i - 1];
            System.out.print(tapAmplitude + ",");
            if (i % 5 == 0) {
                System.out.println("");
            }
        }
    }
    
    private static Object syObject = new Object();

    public static Double mtrVariance(Double mtr, List<PnmpCmData> cmDataList) {
        /**
         * avg = (x1 + x2 + ...... + xn)/n
         * variance = ((pow(x1,2) + .... + pow(xn,2)) / n - pow(avg,2)
         */
        Double sum = mtr;
        Double lastPow = Math.pow(mtr,2);
        Double sumPow = lastPow;
        Integer count = 1;
        synchronized (syObject) {
            for (PnmpCmData pnmpCmData : cmDataList) {
                if (pnmpCmData.getMtr() != null) {
                    sum += pnmpCmData.getMtr();
                    Double pow = Math.pow(pnmpCmData.getMtr(),2);
                    sumPow += pow;
                    count++;
                }
            }
        }
        Double avg = sum / count;
        Double avgPow = sumPow / count;
        return avgPow - Math.pow(avg,2);
    }

    public static Double upSnrVariance(Double upSnr, List<PnmpCmData> cmDataList) {
        Double sum = upSnr;
        Double lastPow = Math.pow(upSnr,2);
        Double sumPow = lastPow;
        Integer count = 1;
        synchronized (syObject) {
            for (PnmpCmData pnmpCmData : cmDataList) {
                if (pnmpCmData.getUpSnr() != null) {
                    sum += pnmpCmData.getUpSnr();
                    Double pow = Math.pow(pnmpCmData.getUpSnr(),2);
                    sumPow += pow;
                    count++;
                }
            }
        }
        Double avg = sum / count;
        Double avgPow = sumPow / count;
        return avgPow - Math.pow(avg,2);
    }

    public static Double mtrToUpSnrSimilarity(List<PnmpCmData> cmDataList) {
        /**
         * 由于mtr与snr的变化是反的 没办法算关联度，所以关联度用mtc来计算
         * 1、构造一个同时存在mtc和snr的列表A
         * 2、使用A分别进行mtc和snr的归一运算 归一算法为   （x - min）/（max - min）
         * 3、计算相似度，算法为  sum（abs(mtcx - snrx)）/ size（A）  越小相似度越高
         */
        synchronized (syObject) {
            Double mtrMin = 0D;
            Double mtrMax = 0D;
            Double upSnrMin = 0D;
            Double upSnrMax = 0D;
            List<PnmpCmData> aList = new ArrayList<>();
            for (PnmpCmData pnmpCmData : cmDataList) {
                if (pnmpCmData.getMtr() != null && pnmpCmData.getUpSnr() != null) {
                    aList.add(pnmpCmData);
                    if (pnmpCmData.getMtc() < mtrMin) {
                        mtrMin = pnmpCmData.getMtc();
                    }
                    if (pnmpCmData.getMtc() > mtrMax) {
                        mtrMax = pnmpCmData.getMtc();
                    }
                    if (pnmpCmData.getUpSnr() < upSnrMin) {
                        upSnrMin = pnmpCmData.getUpSnr();
                    }
                    if (pnmpCmData.getUpSnr() > upSnrMax) {
                        upSnrMax = pnmpCmData.getUpSnr();
                    }
                }
            }
            if (aList.size() > 0) {
                Double sum = 0D;
                for (PnmpCmData pnmpCmData : aList) {
                    Double mtrNormalizing = (pnmpCmData.getMtr() - mtrMin) / (mtrMax - mtrMin);
                    Double upSnrNormalizing = (pnmpCmData.getUpSnr() - upSnrMin) / (upSnrMax - upSnrMin);
                    sum += Math.abs(mtrNormalizing - upSnrNormalizing);
                }
                return sum / aList.size();
            }
        }
        return null;
    }
}
