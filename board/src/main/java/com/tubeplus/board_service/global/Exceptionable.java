package com.tubeplus.board_service.global;

import com.tubeplus.board_service.external.web.error.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
public class Exceptionable<RETURN_TYPE, PARAM_TYPE> {

    private final Function<PARAM_TYPE, RETURN_TYPE> exceptionableFunction;
    private final PARAM_TYPE parameter;


    public RETURN_TYPE ifFailedThrow(BusinessException be) {

        try {
            RETURN_TYPE result = exceptionableFunction.apply(parameter);
            return result;

        } catch (Exception e) {
//            e.printStackTrace();
            log.info(e.getMessage());
            throw be;
        }
    }


    public Exceptionable
            (
                    Function<PARAM_TYPE, RETURN_TYPE> function,
                    PARAM_TYPE parameter
            ) {

        this.exceptionableFunction = function;
        this.parameter = parameter;
    }


}
