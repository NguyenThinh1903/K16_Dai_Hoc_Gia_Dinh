
package com.l221402;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class AddUIConsoleOutput implements OutputBoundary {
    //fields
    private ResponseError error = null;
    // private PrintWriter stdOut = null;
    // public AddUIConsoleOutput() {
    //     this.stdOut = new PrintWriter
    //     (new OutputStreamWriter (System.out));
    // }


    //intertaces
    //API - interface: methods - public
//    public void outError(ResponseData responseData){
//         error(responseData);
//     }

    // //implementation - support
    // private void error(ResponseData responseData){
    //     stdOut.println(responseData.content);
    //     stdOut.println(responseData.strNumber1);
    //     stdOut.println(responseData.strNumber2);
    //     stdOut.flush();

    // }


    @Override
    public void outError(ResponseError error) {
        this.error = error;
        // TODO Auto-generated method stub
       // throw new UnsupportedOperationException("Unimplemented method 'outError'");
    }


    @Override
    public void outResult(ResponseResult result) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'outResult'");
    }

    public ResponseError getError() {
        return error;
    }

}
