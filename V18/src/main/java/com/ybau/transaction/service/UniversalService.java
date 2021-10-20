package com.ybau.transaction.service;

import com.ybau.transaction.util.ResponseData;

public interface UniversalService {
    ResponseData updateWarning(int red, int yellow);

    ResponseData findAll();


}
