package com.ebts.generator.utils;

import com.ebts.generator.dto.InterTableDto;
import org.apache.velocity.VelocityContext;

public class GenInterApiUtils {


    public static VelocityContext prepareContext(InterTableDto interTableDto) {
        return new VelocityContext();
    }

}
