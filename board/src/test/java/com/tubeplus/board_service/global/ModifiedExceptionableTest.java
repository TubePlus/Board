package com.tubeplus.board_service.global;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModifiedExceptionableTest {

    @Test
    public void ttt() {

    }

    public interface ee {
        abstract Boolean ka(Long b);
    }


    public class TClass {

        public Boolean fun(Long b) {
            System.out.println("dlackstjq qudtls");
            return b != 0;
        }

        public ee getEe() {
            return this::fun;
        }

    }


}