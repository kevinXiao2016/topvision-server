package com.topvision.ems.cm.pnmp.facade.utils;

import com.topvision.ems.cm.cmpoll.facade.domain.Complex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jay on 17-6-22.
 */
public class PreEqualizationParam {
    private Boolean isEmpty = false;
    private Integer mtIndex;
    private Integer sybom;
    private Integer tapCount;
    private List<Complex> taps = new ArrayList<>();

    public void parse(String mibData) {
        if (mibData == null || mibData.equalsIgnoreCase("")) {
            isEmpty = true;
            return;
        }
        mibData = mibData.replaceAll(":", "");
        PreEqualizationParamStringRead preEqualizationParamStringRead = new PreEqualizationParamStringRead(mibData);
        mtIndex = (int)preEqualizationParamStringRead.readNByteToNumber(1);
        sybom = (int)preEqualizationParamStringRead.readNByteToNumber(1);
        tapCount = (int)preEqualizationParamStringRead.readNByteToNumber(1);
        long d = preEqualizationParamStringRead.readNByteToNumber(1);
        //先补8个0的复数
        for (int i = 0 ;i < 8 ;i++) {
            Complex complex = new Complex(0,0);
            taps.add(complex);
        }
        for (int i = 0;i < tapCount;i++) {
            int real = encode((int) preEqualizationParamStringRead.readNByteToNumber(2));
            int imag = encode((int)preEqualizationParamStringRead.readNByteToNumber(2));
            Complex complex = new Complex(real,imag);
            taps.add(complex);
        }
    }

    public int encode(int i) {
        int tmp = i & 0xF000;
        if (tmp == 0x0 || tmp == 0xF000 ) {
            int re = i & 0xFFF;
            int flag = i & 0x800;
            if (flag == 0x800) {
                re = ~i;
                re = re & 0xFFF;
                re = -1 * (re + 1);
            }
            return re;
        } else {
            int re = i;
            int flag = i & 0x8000;
            if (flag == 0x8000) {
                re = ~i;
                re = re & 0xFFFF;
                re = -1 * (re + 1);
            }
            return re;
        }
    }

    public Complex[] toArray() {
        Complex[] x = new Complex[taps.size()];
        for (int i = 0; i < taps.size(); i++) {
            x[i] = taps.get(i);
        }
        return x;
    }

    public Boolean isEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(Boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
}
