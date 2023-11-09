package com.tubeplus.board_service.global;

import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class ExceptionableTest extends TestCase {


    @Test
    public void testExceptionableAct() {

        Boolean asldkjf = Exceptionable.act(() -> tempFunction(1))
                .ifExceptioned.thenThrow(ErrorCode.SAVE_ENTITY_FAILED);

        Boolean kkkk = Exceptionable.act(this::tempFunction, 0)
                .ifExceptioned.thenThrow(ErrorCode.SAVE_ENTITY_FAILED);

        Assert.assertEquals("alskdjfa", Boolean.TRUE, asldkjf);
    }

    public boolean tempFunction(Integer i) {

        if (i == 0) {
            throw new RuntimeException("i is zero");
        }
        return true;
    }

}