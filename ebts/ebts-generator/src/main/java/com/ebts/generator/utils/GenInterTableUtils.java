package com.ebts.generator.utils;

import com.ebts.generator.entity.ApiClass;
import com.ebts.generator.entity.InterTable;
import org.apache.velocity.VelocityContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenInterTableUtils {
    public static List<VelocityContext> prepareMoudleContext(List<InterTable> interTableList) {
        List<ApiClass> apiClassList = new ArrayList<ApiClass>();
        for (int i = 0; i < interTableList.size(); i++) {
            InterTable interTable = interTableList.get(i);
            if (apiClassList.size() == 0) {
                ApiClass apiclass = interTable.getApiclass();
                List<InterTable> interTables = new ArrayList<InterTable>();
                interTable.setApiclass(null);
                interTables.add(interTable);
                apiclass.setInterTables(interTables);
                apiClassList.add(apiclass);
            } else {
                boolean start = true;
                for (int j = 0; j < apiClassList.size(); j++) {
                    ApiClass api = apiClassList.get(j);
                    if (interTable.getcId() == api.getId()) {
                        List<InterTable> interTableList1 = api.getInterTables();
                        interTable.setApiclass(null);
                        interTableList1.add(interTable);
                        api.setInterTables(interTableList1);
                        apiClassList.set(j, api);
                        start = false;
                        break;
                    }
                }
                if (start) {
                    ApiClass apiclass = interTable.getApiclass();
                    List<InterTable> interTables = new ArrayList<InterTable>();
                    interTable.setApiclass(null);
                    interTables.add(interTable);
                    apiclass.setInterTables(interTables);
                    apiClassList.add(apiclass);
                }
            }
        }
        List<VelocityContext> contextList = new ArrayList<VelocityContext>();
        for (int i = 0; i < apiClassList.size(); i++) {
            ApiClass apiclass = apiClassList.get(i);
            VelocityContext velocityContext = new VelocityContext();
            String module = apiclass.getInterTables().get(0).getmName();
            String className = apiclass.getcName();
            boolean permission = false;
            for (InterTable interTable : apiclass.getInterTables()) {
                if (interTable.getIsPermission().equals("1")) {
                    permission = true;
                }
            }
            velocityContext.put("createBy", GenSecurityUtils.getUserId());
            velocityContext.put("Permission", permission);
            velocityContext.put("packageName", "com.huhyun." + module);
            velocityContext.put("prefix", module + ":" + getLowerCase(className));
            velocityContext.put("ClassName", getUpperCase(className));
            velocityContext.put("columns", apiclass.getInterTables());
            velocityContext.put("reqMapping", "/" + module + "/" + getLowerCase(className));
            velocityContext.put("functionName", apiclass.getcDescribe());
            velocityContext.put("author", apiclass.getAuthor());
            velocityContext.put("emali", apiclass.getEmail());
            velocityContext.put("time", GenDateUtils.getTime());
            contextList.add(velocityContext);

        }
        return contextList;
    }

    public static VelocityContext prepareClassContext(ApiClass apiclass) {
        VelocityContext velocityContext = new VelocityContext();
        boolean permission = false;
        for (InterTable interTable : apiclass.getInterTables()) {
            if (interTable.getIsPermission().equals("1")) {
                permission = true;
            }
        }
        velocityContext.put("createBy", GenSecurityUtils.getUserId());
        velocityContext.put("Permission", permission);
        velocityContext.put("packageName", apiclass.getPackageName());
        velocityContext.put("ClassName", getUpperCase(apiclass.getcName()));
        velocityContext.put("columns", apiclass.getInterTables());
        velocityContext.put("prefix", apiclass.getPrefix());
        velocityContext.put("reqMapping", getClassUrl(apiclass.getcName(), apiclass.getModule().getmName()));
        velocityContext.put("functionName", apiclass.getcDescribe());
        velocityContext.put("author", apiclass.getAuthor());
        velocityContext.put("emali", apiclass.getEmail());
        velocityContext.put("time", GenDateUtils.getTime());
        return velocityContext;
    }

    public static String getClassUrl(String cname, String mname) {
        return "/" + getLowerCase(mname) + "/" + getLowerCase(cname);
    }

    public static Map<String, String> getTemplateList(Integer type) {
        Map<String, String> templates = new HashMap<String, String>();
        templates.put("java", "vm/java/class.java.vm");
        templates.put("sql", "vm/sql/class.sql.vm");
        return templates;
    }

    public static String getUpperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getLowerCase(String str) {
        return str.toLowerCase();
    }
}
