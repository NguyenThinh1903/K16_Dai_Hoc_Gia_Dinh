package com.l221402;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class AddUIConsoleInput {

    //fields
    private PrintWriter stdOut = null;
    private BufferedReader stdIn = null;
    //private AddUseCaseControl addUseCaseControl = null;
    private InputBoundary addUseCaseControl;

    //methods

    public AddUIConsoleInput(){
        stdOut = new PrintWriter(new BufferedWriter(
            new OutputStreamWriter(System.out)), true);
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        addUseCaseControl = new AddUseCaseControl();
    }

    public void input() throws IOException {
        stdOut.print("Input number1:");
        stdOut.flush();
        String strNumber1 = stdIn.readLine();
        stdOut.print("Input number2:");
        stdOut.flush();
        String strNumber2 = stdIn.readLine();

        RequestData requestData = new RequestData();
        requestData.strNumber1 = strNumber1;
        requestData.strNumber2 = strNumber2;

        addUseCaseControl.execute(requestData);

        
    }

}
