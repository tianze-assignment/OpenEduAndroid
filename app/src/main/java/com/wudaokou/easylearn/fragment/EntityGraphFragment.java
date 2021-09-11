package com.wudaokou.easylearn.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.data.Content;
import com.wudaokou.easylearn.data.EntityInfo;
import com.wudaokou.easylearn.data.MyDatabase;
import com.wudaokou.easylearn.databinding.FragmentEntityGraphBinding;
import com.wudaokou.easylearn.retrofit.EduKGService;
import com.wudaokou.easylearn.retrofit.JSONObject;
import com.wudaokou.easylearn.widget.EchartView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EntityGraphFragment extends Fragment {

    FragmentEntityGraphBinding binding;
    private final String course;
    private final String label;
    public List<Content> data;
//    GraphSurfaceView graphSurfaceView;
    EchartView echartView;

    public EntityGraphFragment(final String course, final String label) {
        this.course = course;
        this.label = label;
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEntityGraphBinding.inflate(inflater, container, false);
//        graphSurfaceView = binding.mySurfaceView;
        echartView = binding.echartView;
        checkDatabase();
        return binding.getRoot();
    }

//    public void initGraph() {
//        NetworkGraph graph = new NetworkGraph();
//
//        Node centerNode = new SimpleNode(label);
//        graph.getVertex().add(new Vertex(centerNode, ContextCompat.getDrawable(requireContext(),
//                R.drawable.node_center)));
//
//        for (Content content : data) {
//            if (content.subject != null) {
//                Node subjectNode = new SimpleNode(content.subject_label);
//                graph.getVertex().add(new Vertex(subjectNode, ContextCompat.getDrawable(requireContext(),
//                        R.drawable.node_subject)));
//                graph.addEdge(new SimpleEdge(subjectNode, centerNode, "0"));
//            } else {
//                Node objectNode = new SimpleNode(content.object_label);
//                graph.getVertex().add(new Vertex(objectNode, ContextCompat.getDrawable(requireContext(),
//                        R.drawable.node_object)));
//                graph.addEdge(new SimpleEdge(centerNode, objectNode, "1"));
//            }
//        }
//
//        graph.setDefaultColor(ContextCompat.getColor(requireContext(), android.R.color.black));
//        graph.setEdgeColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_light));
//        graph.setNodeColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_light));
//        graph.setNodeBgColor(ContextCompat.getColor(requireContext(), android.R.color.white));
//
//        graphSurfaceView.init(graph);
//        Log.e("graph", "finish init graph");
//    }

    public void initEchartView() {
        echartView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //最好在h5页面加载完毕后再加载数据，防止html的标签还未加载完成，不能正常显示
                echartView.refreshEchartsWithOption(contentListToJsonString(), label);
            }
        });
    }

    String contentListToJsonString() {
        // 去重
        data = data.stream().collect(Collectors.groupingBy(c -> c.object_label==null ? c.subject_label : c.object_label))
                .values().stream().map(l -> l.get(0)).collect(Collectors.toList());

        // 分类
        List<String> categories = data.stream().map(c -> c.predicate_label).distinct().collect(Collectors.toList());
        categories.add(0, "-1");

        Map<?, ?> option = Map.of(
                "series", List.of(Map.ofEntries(
                        Map.entry("type", "graph"),
                        Map.entry("layout", "force"),
                        Map.entry("force", Map.of(
                                "repulsion", 3500,
                                "edgeLength", 5
                        )),
                        Map.entry("roam", true),
                        Map.entry("nodeScaleRatio", 0.2),
                        Map.entry("symbolSize", 50),
                        Map.entry("label", Map.of("show", true)),
                        Map.entry("edgeSymbol", List.of("circle", "arrow")),
                        Map.entry("edgeSymbolSize", List.of(4, 10)),
                        Map.entry("data", IntStream.range(0, data.size()).mapToObj(i -> Map.of(
                                            "name", data.get(i).object_label==null ? data.get(i).subject_label : data.get(i).object_label,
                                            "id", String.valueOf(i),
                                            "category", categories.indexOf(data.get(i).predicate_label)
                                        )).collect(Collectors.toList())),
                        Map.entry("links", IntStream.range(0, data.size()).mapToObj(i -> {
                                            Content c = data.get(i);
                                            if(c.object_label == null)
                                                return Map.of("source", String.valueOf(i),
                                                        "target", "-1",
                                                        "label", Map.of(
                                                                "show", true,
                                                                "formatter", c.predicate_label,
                                                                "position", data.size() < 10 ? "middle" : "start"
                                                        ));
                                            else
                                                return Map.of("source", "-1",
                                                        "target", String.valueOf(i),
                                                        "label", Map.of(
                                                                "show", true,
                                                                "formatter", c.predicate_label,
                                                                "position", data.size() < 10 ? "middle" : "end"
                                                        ));
                                        }).collect(Collectors.toList())),
                        Map.entry("categories", categories)
                ))
        );
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(option);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        json = json.replace("\\\"", "");
//        Log.d("json", json);
        return json;
    }

    public void checkDatabase() {
        Future<List<Content>> listFuture = MyDatabase.databaseWriteExecutor.submit(new Callable<List<Content>>() {
            @Override
            public List<Content> call() throws Exception {
                return MyDatabase.getDatabase(getContext())
                        .contentDAO().loadContentByCourseAndLabel(course, label);
            }
        });
        try {
            List<Content> localList = listFuture.get();
            if (localList != null && localList.size() != 0) {
                data = localList;
//                initGraph();

                // 从数据库拿到关系数据
                initEchartView();
            } else {
                getEntityInfo();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void getEntityInfo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.eduKGBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EduKGService service = retrofit.create(EduKGService.class);
        Call<JSONObject<EntityInfo>> call = service.infoByInstanceName(Constant.eduKGId, course, label);
        call.enqueue(new Callback<JSONObject<EntityInfo>>() {
            @Override
            public void onResponse(@NotNull Call<JSONObject<EntityInfo>> call,
                                   @NotNull Response<JSONObject<EntityInfo>> response) {
                JSONObject<EntityInfo> jsonObject = response.body();
                if (jsonObject != null) {
                    if (jsonObject.data.content != null) {

                        data = jsonObject.data.content;
                        for (Content content : data) {
                            content.course = course;
                            content.label = label;
                            content.hasRead = false;
                            content.hasStar = false;
                        }
//                        initGraph();
                        initEchartView();

                        // 本地缓存
                        for (Content content : data) {
                            MyDatabase.databaseWriteExecutor.submit(new Runnable() {
                                @Override
                                public void run() {
                                    MyDatabase.getDatabase(getContext()).contentDAO()
                                            .insertContent(content);
                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onFailure(@NotNull Call<JSONObject<EntityInfo>> call,
                                  @NotNull Throwable t) {
                Log.e("retrofit", "http error");
            }
        });
    }
}