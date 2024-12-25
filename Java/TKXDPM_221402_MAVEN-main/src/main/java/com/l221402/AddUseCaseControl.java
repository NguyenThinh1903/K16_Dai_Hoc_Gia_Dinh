package com.l221402;

public class AddUseCaseControl implements InputBoundary {

    //fields
    private AddEntity addEntity = null;
    //private AddUIConsoleOutput addUIConsoleOutput = null;
    //methods
    private OutputBoundary addUIConsoleOutput = null;
    private ResponseData resError = null;

    public AddUseCaseControl(){
        //addUIConsoleOutput = new AddUIConsoleOutput();
        resError = new ResponseData();
    }

    public void setAddUIConsoleOutput(OutputBoundary addUIConsoleOutput) {
        this.addUIConsoleOutput = addUIConsoleOutput;
    }
    

    public void execute(RequestData requestData) {
        //nhan 2 so
        String strNumber1 = requestData.strNumber1;
        String strNumber2 = requestData.strNumber2;
        //tham dinh
        if(isThamDinh(strNumber1) && isThamDinh(strNumber2)){
            //hợp lệ
        }else{
            //khong hợp lệ
            ResponseError resError = new ResponseError();
            resError.content = "ERROR_INPUT";
            //resError.strNumber1 = strNumber1;
           // resError.strNumber2 = strNumber2;


            addUIConsoleOutput.outError(resError);

        }
            //true: số/ false: ! số
        //ok 
        addEntity = new AddEntity(2, 2);

        int result = addEntity.add();

        ResponseData responseData = new ResponseData();
       // responseData.sumResult = result;
        //
       // addUIConsoleOutput.output(responseData);

    }

    private boolean isThamDinh(String str){

        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str); // cố gắng chuyển chuỗi sang số nguyên
            return true; // nếu thành công thì chuỗi là số nguyên
        } catch (NumberFormatException e) {
            return false; // nếu có lỗi NumberFormatException thì không phải số nguyên
        }

    }

    public OutputBoundary getAddUIConsoleOutput() {
        return addUIConsoleOutput;
    }



   
}
