package com.tubeplus.board_service.global;

import com.tubeplus.board_service.adapter.web.error.ErrorCode;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

public class ExceptionableTest extends TestCase {


    @Test
    public void testExceptionableAct() {

//        Boolean asldkjf = (Boolean) Exceptionable.act(() -> testFunction(1))
//                .ifExceptioned.thenThrow(ErrorCode.SAVE_ENTITY_FAILED);
//
//        Assert.assertEquals("alskdjfa", Boolean.TRUE, asldkjf);
    }

    public boolean testFunction(Integer i) {

        if (i == 0) {
            throw new RuntimeException("i is zero");
        }
        return true;
    }

}