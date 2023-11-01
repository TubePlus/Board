package com.tubeplus.board_service.global;

import com.tubeplus.board_service.adapter.web.error.BusinessException;
import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;


//todo  추상메서드 하나만 존재하는거면 람다식으로 쓸 수 있는듯, Function 인터페이스를 굳이 멤버변수 X 걍 exceptionalExecute 추상메서드 하나만 만드는 쪽으로 리팩
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
                e.printStackTrace();
                log.info(e.getMessage());
                throw new BusinessException(errorCode);
            }

        }

        Executor() {
        }//todo extension method 알아보기 default 생성자하면 자꾸 뜸
    }

}
