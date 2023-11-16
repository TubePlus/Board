package com.tubeplus.board_service.global;

import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;


@Slf4j
public class Exceptionable<RETURN, PARAM> {

    // call 'exceptionable.ifExceptioned' to use terminate methods
    public final Executor ifExceptioned = new Executor();

    private final Function<PARAM, RETURN> exceptionableTask;
    private final PARAM parameter;


    public Exceptionable(Function<PARAM, RETURN> function, PARAM parameter) {
        this.exceptionableTask = function;
        this.parameter = parameter;
    }


    public static <RETURN>
    Exceptionable<RETURN, ?> act(Supplier<RETURN> supplyFunction) {

        return new Exceptionable<>(o -> supplyFunction.get(), null);
    }

    public static <RETURN, PARAM>
    Exceptionable<RETURN, PARAM> act(Function<PARAM, RETURN> function, PARAM parameter) {

        return new Exceptionable<>(function, parameter);
    }


    // class for execute function
    public class Executor {

        public RETURN thenThrow(ErrorCode errorCode) {
            return thenThrow(new BusinessException(errorCode));
        }

        public RETURN thenThrow(RuntimeException runtimeException) {

            try {
                RETURN result = exceptionableTask.apply(parameter);
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                log.info(e.getMessage());
                throw runtimeException;
            }
        }

        Executor() {
        }
    }

}
