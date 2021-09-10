package com.wudaokou.easylearn.bean;

import java.util.List;
import java.util.OptionalDouble;

public class OptionSeries {
    public String type;

    public String layout;

    public int symbolSize;

    public boolean roam;

    public OptionSeriesLabel label;

    public List<String> edgeSymbol;

    public List<Integer> edgeSymbolSize;

    public OptionSeriesEdgeLabel edgeLabel;

    public List<OptionSeriesData> data;


}
