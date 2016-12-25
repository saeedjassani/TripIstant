package com.technovanzahackathon.tripistant.TripChecklists;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Pooja S on 12/24/2016.
 */
public class ColorGenerator {
    public static ColorGenerator DEFAULT;

    public static ColorGenerator MATERIAL;

    static {
        DEFAULT = create(Arrays.asList(
                0xfff16364,
                0xfff58559,
                0xfff9a43e,
                0xffe4c62e,
                0xff67bf74,
                0xff59a2be,
                0xff2093cd,
                0xffad62a7,
                0xff805781
        ));
        MATERIAL = create(Arrays.asList(
                0xff00bcd4,
                0xff009688,
                0xff795548,
                0xff455a64,
                0xffe57b72,
                0xff8d2345,
                0xff607d8b,
                0xffff7043,
                0xffff4081,
                0xffe91e63,
                0xffffeb3b,
                0xfff5be25,
                0xff359ff2,
                0xff5677fc,
                0xd44e6cef,
                0xff263238,
                0xff009688,
                0xff029ae4,
                0xff009e29,
                0xff33b679
        ));
    }

    private final List<Integer> mColors;
    private final Random mRandom;

    public static ColorGenerator create(List<Integer> colorList) {
        return new ColorGenerator(colorList);
    }

    private ColorGenerator(List<Integer> colorList) {
        mColors = colorList;
        mRandom = new Random(System.currentTimeMillis());
    }

    public int getRandomColor() {
        return mColors.get(mRandom.nextInt(mColors.size()));
    }

    public int getColor(Object key) {
        return mColors.get(Math.abs(key.hashCode()) % mColors.size());
    }
}
