package com.tubeplus.board_service.global;

import com.tubeplus.board_service.external.web.error.BusinessException;
import com.tubeplus.board_service.external.web.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

//todo catch exception 할때 말고 custom으로 onCase(Supplier exceptionalCondition)으로 받아서 실행하는것도 짜기
//todo 동적할당 아끼는 것도 좋지만, exception throw하는 상황은 극히 드문데 항상 exception 인스턴스 메모리를 들고있는게 맞는지 고민해보기
@Slf4j
public class Exceptionable<RETURN_TYPE, PARAM_TYPE> {

    // call 'exceptional.ifExceptioned' to use terminate methods
    public final Executor ifExceptioned;

//todo Exceptional onNormalCondition(Predicate isNormalCondition) 이후 종단메서드는 ifExceptioned로만 호출하게

    //todo 고민좀 해보기, static 안쓰고 spring의 빈 프록시 이용가능한 방법으로
    private static final HashMap<ErrorCode, BusinessException> bizExceptionMap = new HashMap<>();


    private final Function<PARAM_TYPE, RETURN_TYPE> exceptionableFunction;
    private final PARAM_TYPE parameter;


    //todo of로 고치기
    public Exceptionable
    (
            Function<PARAM_TYPE, RETURN_TYPE> function,
            PARAM_TYPE parameter
    ) {

        this.exceptionableFunction = function;
        this.parameter = parameter;
        this.ifExceptioned = new Executor();
    }


    public class Executor {
        public RETURN_TYPE thenThrow(BusinessException toThrow) {

            try {
                RETURN_TYPE result = exceptionableFunction.apply(parameter);
                return result;

            } catch (BusinessException be) {
                throw be;

            } catch (Exception e) {
//            e.printStackTrace();
                log.info(e.getMessage());
                throw toThrow;
            }
        }

        public RETURN_TYPE throwBizExceptionOf(ErrorCode errorCode) {

            if (!bizExceptionMap.containsKey(errorCode))
                bizExceptionMap.put(errorCode, new BusinessException(errorCode));

            BusinessException toThrow = bizExceptionMap.get(errorCode);

            return thenThrow(toThrow);
        }

        Executor() {
        }//todo extension method 알아보기 default 생성자하면 자꾸 뜸
    }

}
