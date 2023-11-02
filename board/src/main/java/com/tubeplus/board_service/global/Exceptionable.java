package com.tubeplus.board_service.global;

import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;


@Slf4j
public class Exceptionable<RETURN_TYPE, PARAM_TYPE> {

    // call 'exceptionable.ifExceptioned' to use terminate methods
    public final Executor ifExceptioned = new Executor();

    private final Function<PARAM_TYPE, RETURN_TYPE> exceptionableTask;
    private final PARAM_TYPE parameter;


    public Exceptionable
            (
                    Function<PARAM_TYPE, RETURN_TYPE> function,
                    PARAM_TYPE parameter
            ) {
        this.exceptionableTask = function;
        this.parameter = parameter;
    }

//    public static Exceptionable<?, ?> of(Function<?, ?> task, Object param) {//컴파일러가 위험한짓 하지말라고 경고함
//        return new Exceptionable(task, param);
//    }

    public class Executor {

        public RETURN_TYPE throwOf(ErrorCode errorCode) {

            try {
                RETURN_TYPE result = exceptionableTask.apply(parameter);
                return result;

            } catch (BusinessException be) {
                throw be;

            } catch (Exception e) {
//                e.printStackTrace();
                log.info(e.getMessage());
                throw new BusinessException(errorCode);
            }

        }

        Executor() {
        }
    }

}
