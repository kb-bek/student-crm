package com.kaitech.student_crm.exceptions;

import jakarta.persistence.spi.ClassTransformer;

public class ActivityNotCreatedException extends RuntimeException{
    public ActivityNotCreatedException(String msg){
        super(msg);
    }
    public ActivityNotCreatedException(){}

}
