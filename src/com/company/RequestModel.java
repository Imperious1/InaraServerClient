package com.company;

/**
 * Created by blaze on 5/23/2016.
 */
class RequestModel {

    private int requestId;
    private String searchedName;

    int getRequestId() {
        return requestId;
    }

    RequestModel setRequestId(int requestId) {
        this.requestId = requestId;
        return this;
    }

    String getSearchedName() {
        return searchedName;
    }

    RequestModel setSearchedName(String searchedName) {
        this.searchedName = searchedName;
        return this;
    }
}
