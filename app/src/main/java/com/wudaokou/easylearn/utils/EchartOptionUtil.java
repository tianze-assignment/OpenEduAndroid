package com.wudaokou.easylearn.utils;

import android.util.Log;

import com.github.abel533.echarts.code.Easing;
import com.github.abel533.echarts.code.Layout;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.Symbol;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Effect;
import com.github.abel533.echarts.series.Graph;
import com.github.abel533.echarts.series.MarkLine;
import com.github.abel533.echarts.series.force.Link;
import com.github.abel533.echarts.series.force.Node;
import com.github.abel533.echarts.series.other.Force;
import com.wudaokou.easylearn.MainActivity;
import com.wudaokou.easylearn.data.Content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EchartOptionUtil {

    public static GsonOption getGraphChartOptions(String centerLabel, List<Content> contentList) {
        GsonOption option = new GsonOption();
        option.title(String.format("%s关联图谱", centerLabel));
        option.animationDurationUpdate(1500);
        option.animationEasingUpdate(Easing.quinticInOut);


        Graph graph = new Graph();  //关系图

        graph.symbolSize(50);
        graph.roam(true);
        graph.preventOverlap(true);
        graph.coolDown(2);
        graph.ratioScaling(true);
        graph.linkSymbol(Symbol.arrow);
        graph.linkSymbolSize(20);

        graph.categories("中心", "主语", "宾语");
        Map<String, Integer> map = new HashMap<>();

        // category: 0 for center, 1 for subject, 2 for object
        List<Node> nodeList = new ArrayList<>();
        List<Link> linkList = new ArrayList<>();
        Node centerNode = new Node(0, centerLabel, 10);
        nodeList.add(centerNode);
        map.put(centerLabel, 0);

        for (Content content : contentList) {
            if (content.subject != null) {
                String label = content.subject_label;
                if (!map.containsKey(label)) {
                    map.put(label, 0);
                } else {
                    while (map.containsKey(label)) {
                        int count = map.get(label) + 1;
                        label = label + count;
                    }
                    map.put(label, 0);
                }
                Node subjectNode = new Node(1, label, 10);
                nodeList.add(subjectNode);
                linkList.add(new Link(subjectNode, centerNode, 10));
            } else {
                String label = content.object_label;
                if (!map.containsKey(label)) {
                    map.put(label, 0);
                } else {
                    while (map.containsKey(label)) {
                        int count = map.get(label) + 1;
                        label = label + count;
                    }
                    map.put(label, 0);
                }
                Node objectNode = new Node(2, label, 10);
                nodeList.add(objectNode);
                linkList.add(new Link(centerNode, objectNode, 10));
            }
        }
        graph.nodes(nodeList);
        graph.links(linkList);
        graph.showAllSymbol(true);
        graph.setLegendHoverLink(true);
        MarkLine markLine = new MarkLine();
        Effect effect = new Effect();
        effect.color("#616161");
        markLine.effect(effect);
        graph.setMarkLine(markLine);
        Log.e("graph", String.format("links size: %d", linkList.size()));

        graph.layout(Layout.force);
        Force force = new Force();
        force.edgeLength(20);
        force.repulsion(100);
        force.layoutAnimation(true);
        graph.force(force);

        option.series(graph);


        return option;
    }

}
