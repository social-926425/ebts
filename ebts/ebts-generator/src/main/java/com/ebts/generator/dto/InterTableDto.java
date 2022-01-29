package com.ebts.generator.dto;

import com.ebts.generator.entity.ApiClass;
import com.ebts.generator.entity.InterTable;
import com.ebts.generator.entity.Module;

import java.util.List;

public class InterTableDto {

    private ApiClass apiclass;

    private Module module;

    private List<InterTable> interTables;

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public ApiClass getApiclass() {
        return apiclass;
    }

    public void setApiclass(ApiClass apiclass) {
        this.apiclass = apiclass;
    }

    public List<InterTable> getInterTables() {
        return interTables;
    }

    public void setInterTables(List<InterTable> interTables) {
        this.interTables = interTables;
    }
}
