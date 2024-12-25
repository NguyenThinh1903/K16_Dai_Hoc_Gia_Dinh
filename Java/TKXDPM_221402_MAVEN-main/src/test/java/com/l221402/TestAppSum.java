package com.l221402;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestAppSum {

    @Test
    public void testError() {
        RequestData requestData = new RequestData();
        requestData.strNumber1 = "2";
        requestData.strNumber2 = "a";

        InputBoundary inputBoundary = new AddUseCaseControl();
        AddUIConsoleOutput addUIConsoleOutput = new AddUIConsoleOutput();
        AddUseCaseControl addUseCaseControl = (AddUseCaseControl)inputBoundary;
        addUseCaseControl.setAddUIConsoleOutput(addUIConsoleOutput);

        inputBoundary.execute(requestData);

        //addUIConsoleOutput.getError().content;

        assertEquals("ERROR_INPUT",  addUIConsoleOutput.getError().content);
        
        //addUseCaseControl.getAddUIConsoleOutput();
        
    }

}
