package com.generate.start;

import com.generate.build.Create;
import com.generate.build.Remove;
import com.generate.build.Update;
import com.generate.utils.FileUtil;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GenerateStart {

    private final Logger logger = Logger.getLogger(GenerateStart.class);
    private final static Map<Object, Object> params = FileUtil.getConfig();

    public void table() {
        String type = params.get("table.type").toString();
        String[] split = type.split(",");
        if ("".equals(type) || split.length < 1) return;
        int size = Integer.parseInt(params.get("table.thread.size").toString());
        ExecutorService executorService = Executors.newFixedThreadPool(size);
        for (String s : split) {
            if ("create".equals(s)) {
                new Create().create(executorService);
            } else if ("update".equals(s)) {
                new Update().update(executorService);
            } else if ("remove".equals(s)) {
                new Remove().remove(executorService);
            }
        }
        executorService.shutdown();
    }
}
