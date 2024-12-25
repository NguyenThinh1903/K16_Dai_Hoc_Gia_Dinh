package org.example.useCase.deleteInvoice;

public class DeleteInvoiceOutputDTO {
    private String status;
    private String msg;

    public DeleteInvoiceOutputDTO() {

    }

    public String getMsg() {
        return msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
