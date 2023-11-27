package com.tubeplus.board_service.global;

import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.util.function.BiFunction;
import java.util.function.Consumer;
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


    // static methods for create Exceptionable instance
    public static <RETURN>
    Exceptionable<RETURN, Object> act(Supplier<RETURN> supplyFunction) {

        return new Exceptionable<>(o -> supplyFunction.get(), null);
    }

    public static <PARAM>
    Exceptionable<?, PARAM> act(Consumer<PARAM> consumeFunction, PARAM parameter) {

        return new Exceptionable<>(param ->
        {
            consumeFunction.accept(parameter);
            return null;

        }, parameter);
    }

    public static <RETURN, PARAM>
    Exceptionable<RETURN, PARAM> act(Function<PARAM, RETURN> function, PARAM parameter) {

        return new Exceptionable<>(function, parameter);
    }

    public static <RETURN, PARAM1, PARAM2>
    Exceptionable<RETURN, PARAM1> act(BiFunction<PARAM1, PARAM2, RETURN> function, PARAM1 parameter1, PARAM2 parameter2) {

        return new Exceptionable<>(o -> function.apply(parameter1, parameter2), parameter1);
    }


    // class for execute function
    public class Executor {

        public RETURN thenThrow(ErrorCode errorCode) {
            return thenThrow(new BusinessException(errorCode));
        }

        public RETURN thenThrow(RuntimeException runtimeException) {//todo static 메서드로 바꾸기

            try {
                RETURN result = exceptionableTask.apply(parameter);
                return result;

            } catch (Exception e) {

                log.info("\n-----------------\n"
                        + "Exceptionable.act caught: "
                        + e.getMessage()
                        + "\n-----------------\n");

                e.printStackTrace();

                throw runtimeException;
            }
        }

        Executor() {
        }
    }

}
