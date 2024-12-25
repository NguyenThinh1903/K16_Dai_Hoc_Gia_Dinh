package com.l221402;

public interface OutputBoundary {

    void outError(ResponseError error);
    void outResult(ResponseResult result);

}
