package com.topvision.ems.cm.pnmp.facade.utils;



import com.topvision.ems.cm.cmpoll.facade.domain.Complex;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by jay on 17-6-21.
 */
public class TestFFT {
    public static void main(String[] args) {
        TestFFT testFFT = new TestFFT();
//        testFFT.go3();
        testFFT.go4();
//        PreEqualizationParam preEqualizationParam = new PreEqualizationParam();
//        int i = preEqualizationParam.encode(65533);
//        System.out.println("i = " + i);



//        calc(-15.9,-16.3,-25.9, -9.94);
//        calc(-11.3,-20.7,-12.2, -9.94);

//        calc(-17.3,-17.5,-30.1);
//        calc(-18.9,-19.1,-32.2);
//        calc(-21.1,-21.4,-32.2);
//        calc(-24.9,-25.1,-38);
//        calc(-29,-29.4,-39.4);
//        System.out.println("huawei******************************");
//        calc(-10.72,-37.99,-10.72,-9.94);
//        calc(-10.76,-35.74,-10.77, -10.27);
//        calc(-11.1,-32.46,-11.13, -10.88);
//        calc(-25.58,-37.65,-28.03, -24.56);
//        calc(-28.79,-35.49,-29.83, -24.95);




    }

    private void go4() {
        String data1 = "08:01:18:00:00:1f:00:00:ff:fe:ff:fe:00:45:ff:c4:ff:e1:00:01:00:24:ff:df:ff:ba:ff:e3:00:a1:ff:e0:7f:fe:00:00:ff:5a:ff:be:00:46:ff:e5:ff:df:ff:c4:ff:fc:ff:e8:ff:e3:ff:fd:00:28:00:02:00:02:00:05:00:19:00:01:00:24:ff:b9:00:42:ff:e8:00:21:00:3d:00:1c:ff:eb:ff:c0:ff:f8:00:48:00:26:00:20:00:02:00:0a:ff:f2";
        System.out.println(data1);
        PreEqualizationParam pField = new PreEqualizationParam();
        pField.parse(data1);
        Complex[] x1 = pField.toArray();
//        FFT.show(x1,"x1");
//        Double[] taps = PnmpUtils.amplitudes(x1);
//        for (Double tap : taps) {
//            System.out.println(tap + ",");
//        }

        Double mtc = PnmpUtils.mtc(x1);
        System.out.println("mtc = " + mtc);
        Double mtr = PnmpUtils.mtr(x1);
        System.out.println("mtr = " + mtr);

        String data2 = "08:01:18:00:FF:C8:FF:F0:FF:F8:FF:E8:FF:C0:00:20:FF:F8:00:38:FF:C8:FF:D8:00:18:00:18:00:38:FF:F0:3F:20:00:00:00:08:00:40:FF:D8:FF:E8:00:38:FF:D0:FF:D8:FF:B8:00:08:FF:D8:00:40:00:40:FF:E8:00:00:00:00:FF:C0:00:48:00:20:00:20:00:58:00:10:FF:F0:00:00:FF:E8:FF:F0:FF:D8:00:50:00:00:FF:D8:FF:F0:00:18:FF:E8";
        System.out.println(data2);
        PreEqualizationParam pField2 = new PreEqualizationParam();
        pField2.parse(data2);
        Complex[] x2 = pField2.toArray();
//        FFT.show(x2,"x2");
//        Double[] taps2 = PnmpUtils.amplitudes(x2);
//        for (Double tap2 : taps2) {
//            System.out.println(tap2 + ",");
//        }

        Double mtc2 = PnmpUtils.mtc(x2);
        System.out.println("mtc2 = " + mtc2);
        Double mtr2 = PnmpUtils.mtr(x2);
        System.out.println("mtr2 = " + mtr2);
    }

    private static void calc(double v, double v1, double v2, double v3) {
        double tte = 10000;
        NumberFormat numberFormat = new DecimalFormat("#.########");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        double nmt = tte * Math.pow(10,v/10);
        System.out.println("nmt = " + numberFormat.format(nmt));
        double pre = tte * Math.pow(10, v1/10);
        System.out.println("pre = " + numberFormat.format(pre));
        double post = tte * Math.pow(10, v2/10);
        System.out.println("post = " + numberFormat.format(post));
        double mrlevel = tte * Math.pow(10, v3/10);
        System.out.println("mrlevel = " + numberFormat.format(mrlevel));
        double mte = tte - pre - post;
        System.out.println("mte = " + numberFormat.format(mte));
        double x4 = 10 * Math.log10(mte / tte);
        System.out.println("10 * Math.log10(post / mte) = " + numberFormat.format(x4));
        System.out.println("v = " + v);
        System.out.println("v1 = " + v1);
        System.out.println("v2 = " + v2);
        System.out.println("v3 = " + v3);

    }
    private static void calc2(double v, double v1, double v2) {
        NumberFormat numberFormat = new DecimalFormat("#.########");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        double x1 = Math.exp(v/10);
        System.out.println("x1 = " + numberFormat.format(x1));
        double x2 = Math.exp( v1/10);
        System.out.println("x2 = " + numberFormat.format(x2));
        double x3 = Math.exp( v2/10);
        System.out.println("x3 = " + numberFormat.format(x3));

    }


    public void go3 () {
        String data1 = "08:01:18:00:ff:ff:ff:ff:00:00:00:02:ff:fd:00:00:00:09:00:05:ff:f7:ff:fb:00:12:00:0a:ff:d8:ff:ee:07:fe:00:00:00:1d:00:10:ff:f2:ff:f9:00:07:00:06:ff:f9:ff:fc:00:04:00:02:ff:fb:ff:fd:ff:fd:ff:ff:00:02:ff:ff:00:03:00:01:00:01:00:01:00:01:00:01:00:03:00:00:ff:ff:00:01:ff:fd:00:00:00:05:00:06:ff:ff:00:01";
        System.out.println(data1);
        PreEqualizationParam pField = new PreEqualizationParam();
        pField.parse(data1);
        Complex[] x1 = pField.toArray();
        FFT.show(x1,"x1");
        Double[] taps = PnmpUtils.amplitudes(x1);
        for (Double tap : taps) {
            System.out.println(tap + ",");
        }

        String data2 = "08:01:18:00:00:04:ff:fd:ff:fb:ff:fa:ff:fd:ff:fd:00:07:00:04:ff:f8:00:00:00:17:ff:ff:ff:d6:ff:e8:07:f7:ff:f9:ff:8a:ff:94:ff:f7:00:28:00:11:ff:ec:ff:f7:00:19:00:06:ff:f5:ff:fc:ff:ff:00:0d:ff:fb:00:01:00:01:00:04:00:04:ff:f6:00:07:00:07:ff:fb:00:00:00:08:ff:ff:ff:fe:00:00:00:04:ff:fc:ff:ff:00:08:00:00";
        System.out.println(data2);
        PreEqualizationParam pField2 = new PreEqualizationParam();
        pField2.parse(data1);
        Complex[] x2 = pField.toArray();
        FFT.show(x2,"x2");
        Double[] taps2 = PnmpUtils.amplitudes(x1);
        for (Double tap : taps2) {
            System.out.println(tap + ",");
        }


    }


    public void go () {
        String data1 = "08:01:18:00:00:18:00:08:ff:be:00:20:00:4a:00:02:ff:98:ff:e5:00:bf:00:3c:fe:9c:ff:d5:02:ec:00:ba:7f:e4:00:00:fc:f1:ff:a6:01:ab:00:1b:ff:19:ff:b9:00:67:00:3c:ff:8a:00:18:00:4a:00:42:00:04:00:34:00:3e:00:47:00:21:ff:e9:ff:a1:00:04:00:31:00:09:00:07:00:2a:00:5b:00:74:00:45:00:7f:00:fe:ff:c6:ff:b8:ff:e6";
        System.out.println(data1);
        PreEqualizationParam pField = new PreEqualizationParam();
        pField.parse(data1);
        Complex[] x1 = pField.toArray();
        FFT.show(x1, "x");
        Complex[] re1 = FFT.fft(x1);
        FFT.show(re1, "re");
        double[] results = new double[32];
        for (int i = 0 ; i < re1.length;i++) {
            results[i] = Math.sqrt(Math.pow(re1[i].re(),2) + Math.pow(re1[i].im(),2));
            System.out.println("re["+ i +"]:" + results[i]);
        }


    }


    public void go2 () {
        String d_30_1 = "08:01:18:00:00:1e:00:1b:00:25:00:01:00:39:00:02:ff:c2:ff:dd:00:7c:00:43:ff:7e:ff:fc:01:61:00:aa:7f:f7:00:00:fe:40:ff:98:00:e2:00:87:ff:7a:ff:bc:00:45:00:45:ff:e1:00:3f:00:20:00:03:ff:e0:00:3f:00:21:00:24:00:1f:00:25:00:1e:00:1d:00:3e:ff:e2:ff:e0:00:3e:00:02:ff:df:00:45:00:21:00:6a:ff:fe:ff:fc:00:62";
        String d_30_2 = "08:01:18:00:00:08:00:3f:ff:c9:00:10:00:4e:ff:fc:ff:9e:ff:e2:00:d3:00:28:fe:a0:ff:c5:02:bf:00:c1:7f:e8:00:00:fd:1d:ff:80:01:5e:00:8d:ff:5c:ff:f0:00:78:00:4c:ff:69:00:18:00:3d:00:2b:00:0e:ff:ef:00:03:00:1a:ff:f7:00:42:00:07:ff:95:ff:e3:00:58:00:00:ff:c6:00:02:00:03:00:2b:00:1c:00:70:00:20:ff:cd:00:1f";
        String d_20_1 = "08:01:18:00:00:00:00:03:ff:fe:00:00:ff:fe:00:03:ff:fe:00:00:00:05:00:00:ff:fd:ff:ff:00:0b:00:08:07:ff:00:00:ff:ee:ff:f9:00:0b:00:03:ff:fb:ff:fe:00:03:00:02:ff:fc:00:00:00:02:00:02:00:03:ff:fe:00:04:00:00:00:04:00:01:ff:fb:ff:fc:00:01:00:02:00:01:ff:fd:ff:ff:00:02:00:00:ff:fe:00:04:ff:f9:00:01:00:03";
        String d_20_2 = "08:01:18:00:ff:f9:00:03:00:00:ff:fc:ff:fb:ff:ff:00:0b:ff:f8:ff:ef:00:05:00:1f:ff:f9:ff:c2:00:0f:07:fc:ff:fc:00:36:ff:f1:ff:e5:00:06:00:0f:ff:fb:ff:fa:00:06:00:05:00:04:ff:fb:ff:fe:00:00:00:01:00:00:ff:fe:ff:ff:00:06:00:00:ff:f9:00:04:00:05:ff:ff:ff:fd:00:05:ff:fe:ff:fe:00:01:00:05:ff:f7:ff:ff:00:07";
        PreEqualizationParam p1 = new PreEqualizationParam();
        p1.parse(d_30_1);
        Complex[] x_30_1 = p1.toArray();
        FFT.show(x_30_1,"x_30_1");
        x_30_1[11] = new Complex(0,0);
        x_30_1[12] = new Complex(0,0);
        x_30_1[13] = new Complex(0,0);
        x_30_1[14] = new Complex(0,0);
        x_30_1[16] = new Complex(0,0);
        x_30_1[17] = new Complex(0,0);
        x_30_1[18] = new Complex(0,0);
        x_30_1[19] = new Complex(0,0);

        PreEqualizationParam p4 = new PreEqualizationParam();
        p4.parse(d_30_2);
        Complex[] x_30_2 = p4.toArray();
//        FFT.show(x_30_2,"x_30_2");
        x_30_2[11] = new Complex(0,0);
        x_30_2[12] = new Complex(0,0);
        x_30_2[13] = new Complex(0,0);
        x_30_2[14] = new Complex(0,0);
        x_30_2[16] = new Complex(0,0);
        x_30_2[17] = new Complex(0,0);
        x_30_2[18] = new Complex(0,0);
        x_30_2[19] = new Complex(0,0);

        PreEqualizationParam p2 = new PreEqualizationParam();
        p2.parse(d_20_1);
        Complex[] x_20_1 = p2.toArray();
//        FFT.show(x_20_1,"x_20_1");
        x_20_1[11] = new Complex(0,0);
        x_20_1[12] = new Complex(0,0);
        x_20_1[13] = new Complex(0,0);
        x_20_1[14] = new Complex(0,0);
        x_20_1[16] = new Complex(0,0);
        x_20_1[17] = new Complex(0,0);
        x_20_1[18] = new Complex(0,0);
        x_20_1[19] = new Complex(0,0);

        PreEqualizationParam p3 = new PreEqualizationParam();
        p3.parse(d_20_2);
        Complex[] x_20_2 = p3.toArray();
//        FFT.show(x_20_2,"x_20_2");
        x_20_2[11] = new Complex(0,0);
        x_20_2[12] = new Complex(0,0);
        x_20_2[13] = new Complex(0,0);
        x_20_2[14] = new Complex(0,0);
        x_20_2[16] = new Complex(0,0);
        x_20_2[17] = new Complex(0,0);
        x_20_2[18] = new Complex(0,0);
        x_20_2[19] = new Complex(0,0);

        Complex[] shift_30_1 = FFT.fftshift(x_30_1, 16);
        FFT.show(shift_30_1,"shift_30_1");
        Complex[] shift_30_2 = FFT.fftshift(x_30_2, 16);
        Complex[] shift_20_1 = FFT.fftshift(x_20_1, 16);
        Complex[] shift_20_2 = FFT.fftshift(x_20_2, 16);

        Complex[] guiyi_30_1 = PnmpUtils.normalizing(shift_30_1, shift_30_1[31]);
        FFT.show(guiyi_30_1,"guiyi_30_1");
        Complex[] guiyi3_30_1 = PnmpUtils.normalizing(guiyi_30_1, new Complex(5, 0));
        FFT.show(guiyi3_30_1,"guiyi3_30_1");
        Complex[] guiyi_30_2 = PnmpUtils.normalizing(shift_30_2, shift_30_2[31]);
        Complex[] guiyi3_30_2 = PnmpUtils.normalizing(guiyi_30_2, new Complex(5, 0));
        Complex[] guiyi_20_1 = PnmpUtils.normalizing(shift_20_1, shift_20_1[31]);
        Complex[] guiyi3_20_1 = PnmpUtils.normalizing(guiyi_20_1, new Complex(5, 0));
        Complex[] guiyi_20_2 = PnmpUtils.normalizing(shift_20_2, shift_20_2[31]);
        Complex[] guiyi3_20_2 = PnmpUtils.normalizing(guiyi_20_2, new Complex(5, 0));

        Complex[] fr_30_1 = FFT.fft(guiyi3_30_1);
        FFT.show(fr_30_1,"fr_30_1");
        Complex[] fr_30_2 = FFT.fft(guiyi3_30_2);
        Complex[] fr_20_1 = FFT.fft(guiyi3_20_1);
        Complex[] fr_20_2 = FFT.fft(guiyi3_20_2);


        Complex[] xz_30_20 = PnmpUtils.divide(fr_30_1, fr_20_1);
        FFT.show(xz_30_20,"xz_30_20");
        Complex[] xz_20_30 = PnmpUtils.divide(fr_20_2, fr_30_2);
        FFT.show(xz_30_20,"xz_20_30",true);
        Complex[] xz_30_30 = PnmpUtils.divide(fr_30_1, fr_30_2);
        FFT.show(xz_30_20,"xz_30_30",true);
        Complex[] xz_20_20 = PnmpUtils.divide(fr_20_1, fr_20_2);
        FFT.show(xz_30_20,"xz_20_20",true);
        Complex[] xz2_20_20 = PnmpUtils.divide(fr_20_2, fr_20_1);
        FFT.show(xz2_20_20,"xz2_20_20",true);

        Complex[] td_30_20 = FFT.ifft(xz_30_20);
        FFT.show(td_30_20,"td_30_20",true);
        Complex[] td_20_30 = FFT.ifft(xz_20_30);
//        FFT.show(td_20_30,"td_20_30");
        Complex[] td_30_30 = FFT.ifft(xz_30_30);
//        FFT.show(td_30_30,"td_30_30");
        Complex[] td_20_20 = FFT.ifft(xz_20_20);
//        FFT.show(td_20_20,"td_20_20");
        Complex[] td2_20_20 = FFT.ifft(xz2_20_20);
//        FFT.show(td_20_20,"td_20_20");

        Complex[] ishift_30_20 = FFT.fftshift(td_30_20, 17);
        FFT.show(ishift_30_20,"ishift_30_20",true);
        Complex[] ishift_20_30 = FFT.fftshift(td_20_30, 17);
        Complex[] ishift_30_30 = FFT.fftshift(td_30_30, 17);
        Complex[] ishift_20_20 = FFT.fftshift(td_20_20, 17);
        Complex[] ishift2_20_20 = FFT.fftshift(td2_20_20, 17);


        double mtr_30_20 = PnmpUtils.mtr(ishift_30_20);
        System.out.println("mtr_30_20 = " + mtr_30_20);
        double mtr_20_30 = PnmpUtils.mtr(ishift_20_30);
        System.out.println("mtr_20_30 = " + mtr_20_30);
        double mtr_30_30 = PnmpUtils.mtr(ishift_30_30);
        System.out.println("mtr_30_30 = " + mtr_30_30);
        double mtr_20_20 = PnmpUtils.mtr(ishift_20_20);
        System.out.println("mtr_20_20 = " + mtr_20_20);
        double mtr2_20_20 = PnmpUtils.mtr(ishift2_20_20);
        System.out.println("mtr2_20_20 = " + mtr2_20_20);


//        FieldMatrix<Complex> fm_30_1 = new Array2DRowFieldMatrix<Complex>(fr_30_1);
//        FieldMatrix<Complex> fm_30_2 = new Array2DRowFieldMatrix<Complex>(fr_30_2);
//        FieldMatrix<Complex> fm_20_1 = new Array2DRowFieldMatrix<Complex>(fr_20_1);
//        FieldMatrix<Complex> fm_20_2 = new Array2DRowFieldMatrix<Complex>(fr_20_2);
//
//        FieldMatrix<Complex> lud_30_1 = new FieldLUDecomposition<Complex>(fm_30_1).getSolver().getInverse();
//        FieldMatrix<Complex> lud_30_2 = new FieldLUDecomposition<Complex>(fm_30_2).getSolver().getInverse();
//        FieldMatrix<Complex> lud_20_1 = new FieldLUDecomposition<Complex>(fm_20_1).getSolver().getInverse();
//        FieldMatrix<Complex> lud_20_2 = new FieldLUDecomposition<Complex>(fm_20_2).getSolver().getInverse();
//
//        FieldMatrix<Complex> xz_30_20 = fm_30_1.multiply(lud_20_1);
//        FieldMatrix<Complex> xz_20_30 = fm_20_1.multiply(lud_30_1);
//        FieldMatrix<Complex> xz_30_30 = fm_30_1.multiply(lud_30_2);
//        FieldMatrix<Complex> xz_20_20 = fm_20_1.multiply(lud_20_2);
//
//        System.out.println("xz_30_20 = " + xz_30_20);
//        System.out.println("xz_20_30 = " + xz_20_30);
//        System.out.println("xz_30_30 = " + xz_30_30);
//        System.out.println("xz_20_20 = " + xz_20_20);


    }
    public void go1 () {
        String d_30_1 = "08:01:18:00:00:1e:00:1b:00:25:00:01:00:39:00:02:ff:c2:ff:dd:00:7c:00:43:ff:7e:ff:fc:01:61:00:aa:7f:f7:00:00:fe:40:ff:98:00:e2:00:87:ff:7a:ff:bc:00:45:00:45:ff:e1:00:3f:00:20:00:03:ff:e0:00:3f:00:21:00:24:00:1f:00:25:00:1e:00:1d:00:3e:ff:e2:ff:e0:00:3e:00:02:ff:df:00:45:00:21:00:6a:ff:fe:ff:fc:00:62";
        String d_30_2 = "08:01:18:00:00:08:00:3f:ff:c9:00:10:00:4e:ff:fc:ff:9e:ff:e2:00:d3:00:28:fe:a0:ff:c5:02:bf:00:c1:7f:e8:00:00:fd:1d:ff:80:01:5e:00:8d:ff:5c:ff:f0:00:78:00:4c:ff:69:00:18:00:3d:00:2b:00:0e:ff:ef:00:03:00:1a:ff:f7:00:42:00:07:ff:95:ff:e3:00:58:00:00:ff:c6:00:02:00:03:00:2b:00:1c:00:70:00:20:ff:cd:00:1f";
        String d_20_1 = "08:01:18:00:00:00:00:03:ff:fe:00:00:ff:fe:00:03:ff:fe:00:00:00:05:00:00:ff:fd:ff:ff:00:0b:00:08:07:ff:00:00:ff:ee:ff:f9:00:0b:00:03:ff:fb:ff:fe:00:03:00:02:ff:fc:00:00:00:02:00:02:00:03:ff:fe:00:04:00:00:00:04:00:01:ff:fb:ff:fc:00:01:00:02:00:01:ff:fd:ff:ff:00:02:00:00:ff:fe:00:04:ff:f9:00:01:00:03";
        String d_20_2 = "08:01:18:00:ff:f9:00:03:00:00:ff:fc:ff:fb:ff:ff:00:0b:ff:f8:ff:ef:00:05:00:1f:ff:f9:ff:c2:00:0f:07:fc:ff:fc:00:36:ff:f1:ff:e5:00:06:00:0f:ff:fb:ff:fa:00:06:00:05:00:04:ff:fb:ff:fe:00:00:00:01:00:00:ff:fe:ff:ff:00:06:00:00:ff:f9:00:04:00:05:ff:ff:ff:fd:00:05:ff:fe:ff:fe:00:01:00:05:ff:f7:ff:ff:00:07";
        PreEqualizationParam p1 = new PreEqualizationParam();
        p1.parse(d_30_1);
        Complex[] x1 = p1.toArray();

        PreEqualizationParam p2 = new PreEqualizationParam();
        p2.parse(d_20_1);
        Complex[] x2 = p2.toArray();

        PreEqualizationParam p3 = new PreEqualizationParam();
        p3.parse(d_20_2);
        Complex[] x3 = p3.toArray();

        PreEqualizationParam p4 = new PreEqualizationParam();
        p4.parse(d_30_2);
        Complex[] x4 = p4.toArray();

//        FFT.show(x1,"x1");
//        FFT.show(x4,"x4");
//        FFT.show(x2,"x2");
//        FFT.show(x3,"x3");

        Complex[] xx1 = PnmpUtils.normalizing(x1, x1[15]);
//        FFT.show(xx1,"xx1");

        Complex[] xx2 = PnmpUtils.normalizing(x2, x2[15]);
//        FFT.show(xx2,"xx2");

        Complex[] xx3 = PnmpUtils.normalizing(x3, x3[15]);
//        FFT.show(xx3,"xx3");

        Complex[] xx4 = PnmpUtils.normalizing(x4, x4[15]);
//        FFT.show(xx4,"xx4");

        Complex[] xz1 = PnmpUtils.subtract(xx2, xx1);
//        FFT.show(xz1,"xz1");
        xz1[15] = new Complex(1D,0D);
        Double mtr1 = PnmpUtils.mtr(xz1);
        System.out.println("******************************************************");
        System.out.println("mtr20_30 = " + mtr1);

        Complex[] xz2 = PnmpUtils.subtract(xx4, xx3);
//        FFT.show(xz2,"xz2");
        xz2[15] = new Complex(1D,0D);
        Double mtr2 = PnmpUtils.mtr(xz2);
        System.out.println("******************************************************");
        System.out.println("mtr30_20 = " + mtr2);

        Complex[] xz3 = PnmpUtils.subtract(xx2, xx3);
//        FFT.show(xz3,"xz3");
        xz3[15] = new Complex(1D,0D);
        Double mtr3 = PnmpUtils.mtr(xz3);
        System.out.println("******************************************************");
        System.out.println("mtr20_20 = " + mtr3);


        Complex[] xz4 = PnmpUtils.subtract(xx4, xx1);
//        FFT.show(xz3,"xz3");
        xz4[15] = new Complex(1D,0D);
        Double mtr4 = PnmpUtils.mtr(xz4);
        System.out.println("******************************************************");
        System.out.println("mtr30_30 = " + mtr4);

//        Complex[] xy1 = PnmpUtils.normalizing2(x1,x1[15]);
//        FFT.show(xy1,"xy1");
//
//        Complex[] xy2 = PnmpUtils.normalizing2(x2,x2[15]);
//        FFT.show(xy2,"xy2");


    }

}
