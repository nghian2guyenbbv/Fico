package vn.com.tpf.microservices.models;

public class SearchingInfo {
    String pageNum;
    String searchKey;

    public SearchingInfo(String pageNum, String searchKey) {
        this.pageNum = pageNum;
        this.searchKey = searchKey;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
